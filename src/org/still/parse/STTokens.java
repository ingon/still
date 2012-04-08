package org.still.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.still.parse.ast.STExpression;
import org.still.parse.ast.STStatement;
import org.still.parse.ast.expr.STApply;
import org.still.parse.ast.expr.STApplyOp;
import org.still.parse.ast.expr.STIf;
import org.still.parse.ast.expr.STLambda;
import org.still.parse.ast.expr.STSymbol;
import org.still.parse.ast.expr.literal.STLiteralBoolean;
import org.still.parse.ast.expr.literal.STLiteralInteger;
import org.still.parse.ast.expr.literal.STLiteralList;
import org.still.parse.ast.expr.literal.STLiteralMap;
import org.still.parse.ast.expr.literal.STLiteralNull;
import org.still.parse.ast.expr.literal.STLiteralString;
import org.still.parse.ast.stmt.STBreak;
import org.still.parse.ast.stmt.STContinue;
import org.still.parse.ast.stmt.STFor;
import org.still.parse.ast.stmt.STFunction;
import org.still.parse.ast.stmt.STImport;
import org.still.parse.ast.stmt.STOperator;
import org.still.parse.ast.stmt.STReturn;
import org.still.parse.ast.stmt.STVal;
import org.still.parse.ast.stmt.STVar;
import org.still.parse.ast.stmt.STWhile;
import org.still.runtime.type.STFunctionType;
import org.still.runtime.type.STParameterizedType;
import org.still.runtime.type.STPlainType;
import org.still.runtime.type.STType;
import org.still.util.Pair;
import org.tdp.Parser;
import org.tdp.Token;

public class STTokens {
    public static interface StatementToken<STM, RES> {
        public STM std(Parser<RES> parser);
    };
    
	public static class BlockToken extends Token<STExpression> {
		public static final String TYPE = "(block)";
		
		public final int level;

		public BlockToken(int level) {
			super(TYPE, "" + level);
			this.level = level;
		}
		
		@Override
		public String toString() {
		    return TYPE;
		}
	}
	
	public static class KeywordToken extends Token<STExpression> {
		private static final Map<String, KeywordToken> keywords = new HashMap<String, STTokens.KeywordToken>();
		public static KeywordToken get(String key) {
			return keywords.get(key);
		}
		
		public static final KeywordToken FN = new KeywordFnStatementToken("fn") {
		    public STStatement std(org.tdp.Parser<STExpression> parser) {
		        parser.expect(STTokens.SymbolToken.TYPE);
		        String name = parser.val();
		        List<String> params = parseFunctionParameters(parser);
		        STFunctionType type = parseFunctionType(parser);
		        List<STStatement> stmts = parseNewBlock(parser);
		        return new STFunction(name, params, type, stmts);
		    };
		    
		    public STExpression nud(org.tdp.Parser<STExpression> parser) {
                List<String> params = new ArrayList<String>();
                
                if(parser.check(SeparatorToken.LPR)) {
                    parser.advance(SeparatorToken.LPR);
                    
                    if(! parser.check(STTokens.SeparatorToken.RPR)) {
                        while(true) {
                            parser.expect(STTokens.SymbolToken.TYPE);
                            params.add(parser.val());
                            parser.advance();
                            
                            if(parser.check(STTokens.SeparatorToken.CMA)) {
                                parser.advance();
                            } else {
                                break;
                            }
                        }
                    }
                    parser.advance(")");
                }
                return new STLambda(params, parser.expression(0));
		    };
		};
		public static final KeywordToken OP = new KeywordFnStatementToken("op") {
		    public STStatement std(org.tdp.Parser<STExpression> parser) {
		        String op = parser.val();
		        parser.advance();
		        
		        parser.advance(STTokens.SeparatorToken.LPR);
		        String position = parser.val();
		        if("infix".equals(position)) {
		            parser.advance();
		            parser.advance(STTokens.SeparatorToken.CMA);
		            
		            int lbp = Integer.parseInt(parser.val());
		            parser.advance();
		            
		            int rbp = lbp;
		            if(parser.check(STTokens.SeparatorToken.CMA)) {
		                parser.advance();
		                rbp = Integer.parseInt(parser.val());
		                parser.advance();
		            }
		            if(! parser.check(STTokens.SeparatorToken.RPR)) {
		                throw new RuntimeException("Expected )");
		            }
		            
		            ((STParser) parser).tokenizer.registerOperator(op, lbp, rbp);
		        } else if("prefix".equals(position)) {
		            parser.advance();
		            if(! parser.check(STTokens.SeparatorToken.RPR)) {
		                throw new RuntimeException("Expected )");
		            }
		        } else {
		            throw new RuntimeException("Expected either prefix or infix");
		        }
		        
		        List<String> params = parseFunctionParameters(parser);
		        STFunctionType type = parseFunctionType(parser);
		        List<STStatement> stmts = parseNewBlock(parser);
		        
		        return new STOperator(op, params, type, stmts);
		    };
		};
		public static final KeywordToken RETURN = new KeywordStatementToken("return") {
		    public STStatement std(org.tdp.Parser<STExpression> parser) {
		        return new STReturn(parser.expression(0));
		    };
		};
        public static final KeywordToken BREAK = new KeywordStatementToken("break") {
            public STStatement std(org.tdp.Parser<STExpression> parser) {
                return STBreak.INSTANCE;
            };
        };
        public static final KeywordToken CONTINUE = new KeywordStatementToken("continue") {
            public STStatement std(org.tdp.Parser<STExpression> parser) {
                return STContinue.INSTANCE;
            };
        };
		public static final KeywordToken IF = new KeywordStatementToken("if") {
		    public STStatement std(org.tdp.Parser<STExpression> _parser) {
		        STParser parser = (STParser) _parser;
		        STExpression condition = parser.expression(0);
		        List<STStatement> posStatements = parseNewBlock(parser);
		        
		        Token<STExpression> nextToken = parser.lookAhead(0);
		        if(! (nextToken instanceof STTokens.BlockToken)) {
		            return new org.still.parse.ast.stmt.STIf(condition, posStatements);
		        }
		        BlockToken blockToken = (BlockToken) nextToken;
		        if(blockToken.level != parser.currentBlockLevel()) {
		            return new org.still.parse.ast.stmt.STIf(condition, posStatements);
		        }

		        if(parser.check(STTokens.KeywordToken.ELSE, 1)) {
		            parser.advance(STTokens.BlockToken.TYPE);
		            parser.advance(STTokens.KeywordToken.ELSE);
		            List<STStatement> negStatements = parseNewBlock(parser);
		            return new org.still.parse.ast.stmt.STIf(condition, posStatements, negStatements);
		        } else {
		            return new org.still.parse.ast.stmt.STIf(condition, posStatements);
		        }
		    };
		    
		    public STExpression nud(org.tdp.Parser<STExpression> parser) {
		        STExpression cond = parser.expression(0);
		        parser.advance(SeparatorToken.COL);
		        STExpression succ = parser.expression(0);
		        parser.advance(KeywordToken.ELSE);
		        STExpression fail = parser.expression(0);
		        return new STIf(cond, succ, fail);
		    };
		};
        public static final KeywordToken IMPORT = new KeywordStatementToken("import") {
            public STStatement std(org.tdp.Parser<STExpression> parser) {
                if(! parser.check(STTokens.SymbolToken.TYPE)) {
                    throw new RuntimeException("Expected symbol");
                }
                
                StringBuilder sb = new StringBuilder();
                while(true) {
                    sb.append(parser.val());
                    parser.advance();
                    
                    if(parser.check(".")) {
                        sb.append(".");
                        parser.advance();
                    } else {
                        break;
                    }
                }
                
                if(parser.check(STTokens.KeywordToken.AS)) {
                    parser.advanceAndExpect(STTokens.SymbolToken.TYPE);
                    String target = parser.val();
                    parser.advance();
                    return new STImport(sb.toString(), target);
                } else {
                    return new STImport(sb.toString());
                }
            };
        };
        public static final KeywordToken NULL = new KeywordToken("null") {
            public STExpression nud(org.tdp.Parser<STExpression> parser) {
                return STLiteralNull.INSTANCE;
            };
        };
        public static final KeywordToken TRUE = new KeywordToken("true") {
            public STExpression nud(org.tdp.Parser<STExpression> parser) {
                return STLiteralBoolean.TRUE;
            };
        };
        public static final KeywordToken FALSE = new KeywordToken("false") {
            public STExpression nud(org.tdp.Parser<STExpression> parser) {
                return STLiteralBoolean.FALSE;
            };
        };
        public static final KeywordToken VAL = new KeywordDefStatementToken("val") {
            public STStatement std(org.tdp.Parser<STExpression> parser) {
                Pair<List<String>, List<STExpression>> defs = parseDefining(parser);
                return new STVal(defs.f, defs.s);
            };
        };
        public static final KeywordToken VAR = new KeywordDefStatementToken("var") {
            public STStatement std(org.tdp.Parser<STExpression> parser) {
                Pair<List<String>, List<STExpression>> defs = parseDefining(parser);
                return new STVar(defs.f, defs.s);
            };
        };
        public static final KeywordToken FOR = new KeywordStatementToken("for"){
            public STStatement std(org.tdp.Parser<STExpression> parser) {
                parser.expect(STTokens.SymbolToken.TYPE);
                String sym = parser.val();
                parser.advance();
                parser.advance(STTokens.KeywordToken.IN);
                STExpression expr = parser.expression(0);
                List<STStatement> body = parseNewBlock(parser);
                
                return new STFor(sym, expr, body);
            };
        };
        public static final KeywordToken WHILE = new KeywordStatementToken("while") {
            public STStatement std(org.tdp.Parser<STExpression> parser) {
                STExpression condition = parser.expression(0);
                List<STStatement> body = parseNewBlock(parser);
                
                return new STWhile(condition, body);
            };
        };

        public static final KeywordToken AS = new KeywordToken("as");
        public static final KeywordToken ELSE = new KeywordToken("else");
        public static final KeywordToken IN = new KeywordToken("in");

		public KeywordToken(String key) {
			super(key, key);
			keywords.put(key, this);
		}
        
        @Override
        public String toString() {
            return "KEY(" + value + ")";
        }
        
        protected List<STStatement> parseNewBlock(org.tdp.Parser<STExpression> _parser) {
            STParser parser = (STParser) _parser;
            return parser.parseNewBlock();
        }
	}
	
	public static class KeywordStatementToken extends KeywordToken implements StatementToken<STStatement, STExpression> {
        public KeywordStatementToken(String key) {
            super(key);
        }

        @Override
        public STStatement std(Parser<STExpression> parser) {
            throw new RuntimeException("Not implemented: " + type);
        }
	}
	
	public static class KeywordDefStatementToken extends KeywordStatementToken {
	    public KeywordDefStatementToken(String key) {
            super(key);
        }

        protected Pair<List<String>, List<STExpression>> parseDefining(Parser<STExpression> parser) {
	        List<String> names = new ArrayList<String>();
	        List<STExpression> values = new ArrayList<STExpression>();
	        while(true) {
	            STExpression expr = parser.expression(0);
	            if(! (expr instanceof STApplyOp)) {
	                throw new RuntimeException("Expected assignment");
	            }
	            STApplyOp applyOp = (STApplyOp) expr;
	            
	            if(! (applyOp.left() instanceof STSymbol)) {
	                throw new RuntimeException("Expected symbol");
	            }
	            String name = ((STSymbol) applyOp.left()).symbol;
	            
	            if(! (applyOp.symbol instanceof STSymbol)) {
	                throw new RuntimeException("Expected assignment");
	            }
	            if(! "=".equals(((STSymbol) applyOp.symbol).symbol)) {
	                throw new RuntimeException("Expected assignment");
	            }
	            
	            names.add(name);
	            values.add(applyOp.right());
	            
	            if(parser.check(STTokens.SeparatorToken.CMA)) {
	                parser.advance();
	            } else {
	                break;
	            }
	        }
	        
	        return Pair.of(names, values);
	    }
	}
	
	public static class KeywordFnStatementToken extends KeywordStatementToken {
        public KeywordFnStatementToken(String key) {
            super(key);
        }

        protected List<String> parseFunctionParameters(Parser<STExpression> parser) {
            List<String> params = new ArrayList<String>();
            parser.advanceAndExpect(STTokens.SeparatorToken.LPR);
            parser.advance();
            if (!parser.check(STTokens.SeparatorToken.RPR)) {
                while (true) {
                    parser.expect(STTokens.SymbolToken.TYPE);
                    params.add(parser.val());
                    parser.advance();

                    if (parser.check(STTokens.SeparatorToken.CMA)) {
                        parser.advance();
                    } else {
                        break;
                    }
                }
            }
            parser.advance(STTokens.SeparatorToken.RPR);
            return params;
        }
        
        protected STFunctionType parseFunctionType(Parser<STExpression> parser) {
            STFunctionType type = null;
            if (parser.check(STTokens.SeparatorToken.COL)) {
                parser.advance();
                parser.advance(STTokens.SeparatorToken.COL);
                type = parseFunctionTypeDelegate(parser);
            }
            return type;
        }

        private STFunctionType parseFunctionTypeDelegate(Parser<STExpression> parser) {
            List<STType> types = new ArrayList<STType>();
            while (true) {
                if (parser.check(STTokens.SeparatorToken.LPR)) {
                    parser.advance();
                    types.add(parseFunctionTypeDelegate(parser));
                    parser.advance(STTokens.SeparatorToken.RPR);
                } else if (parser.check(STTokens.SymbolToken.TYPE)) {
                    types.add(parseBasicType(parser));
                } else if (parser.check("->")) {
                    parser.advance();
                } else {
                    break;
                }
            }
            return new STFunctionType(types);
        }

        private STType parseBasicType(Parser<STExpression> parser) {
            if (!parser.check(STTokens.SymbolToken.TYPE)) {
                throw new RuntimeException("Expected type");
            }
            String mainType = parser.val();
            parser.advance();

            List<String> typeParams = new ArrayList<String>();
            while (parser.check(STTokens.SymbolToken.TYPE)) {
                typeParams.add(parser.val());
                parser.advance();
            }

            return typeParams.isEmpty() ? new STPlainType(mainType) : new STParameterizedType(mainType, typeParams);
        }
	}
	
	public static class SymbolToken extends Token<STExpression> {
		public static final String TYPE = "(symbol)";
		
		public SymbolToken(String symbol) {
			super(TYPE, symbol);
		}
		
		@Override
		public STExpression nud(Parser<STExpression> parser) {
			return new STSymbol(value);
		}
        
        @Override
        public String toString() {
            return "SYM(" + value + ")";
        }
	}
	
	public static class SeparatorToken extends Token<STExpression> {
		private static final Map<String, SeparatorToken> separators = new HashMap<String, STTokens.SeparatorToken>();
		public static SeparatorToken get(String sep) {
			return separators.get(sep);
		}
        public static SeparatorToken getStrict(String op) {
            SeparatorToken opt = get(op);
            if(opt == null)
                throw new RuntimeException("Unknown separator: " + op);
            return opt;
        }
		
		public static final SeparatorToken LPR = new SeparatorToken("(") {
		    public STExpression nud(org.tdp.Parser<STExpression> parser) {
		        STExpression expr = parser.expression(0);
		        parser.advance(RPR);
		        return expr;
		    };
		    
			public STExpression led(org.tdp.Parser<STExpression> parser, STExpression left) {
				List<STExpression> params = new ArrayList<STExpression>();
				if(! parser.check(RPR)) {
					while(true) {
						params.add(parser.expression(0));
						
						if(parser.check(CMA)) {
							parser.advance();
						} else {
							break;
						}
					}
				}
				parser.advance(RPR);
				return new STApply(left, params);
			};
			
			public int lbp() {
				return 80;
			};
		};
		public static final SeparatorToken RPR = new SeparatorToken(")");
        public static final SeparatorToken LSPR = new SeparatorToken("[") {
            public STExpression nud(org.tdp.Parser<STExpression> parser) {
                List<STExpression> elements = new ArrayList<STExpression>();
                if(! parser.check(RSPR)) {
                    while(true) {
                        elements.add(parser.expression(0));
                        if(parser.check(STTokens.SeparatorToken.CMA)) {
                            parser.advance();
                        } else {
                            break;
                        }
                    }
                }
                parser.advance(RSPR);
                return new STLiteralList(elements);
            };
        };
        public static final SeparatorToken RSPR = new SeparatorToken("]");
        public static final SeparatorToken LCPR = new SeparatorToken("{") {
            public STExpression nud(org.tdp.Parser<STExpression> parser) {
                List<STExpression> keys = new ArrayList<STExpression>();
                List<STExpression> vals = new ArrayList<STExpression>();
                
                if(! parser.check(RCPR)) {
                    while(true) {
                        STExpression asse = parser.expression(0);
                        if(! (asse instanceof STApplyOp)) {
                            throw new RuntimeException("Expected = expression");
                        }
                        STApplyOp ass = (STApplyOp) asse;
                        if(! (ass.symbol instanceof STSymbol)) {
                            throw new RuntimeException("Expected = expression");
                        }
                        STSymbol eq = (STSymbol) ass.symbol;
                        if(! "=".equals(eq.symbol)) {
                            throw new RuntimeException("Expected = expression");
                        }
                        
                        keys.add(ass.arguments.get(0));
                        vals.add(ass.arguments.get(1));
                        
                        if(parser.check(STTokens.SeparatorToken.CMA)) {
                            parser.advance();
                        } else {
                            break;
                        }
                    }
                }
                parser.advance(RCPR);
                return new STLiteralMap(keys, vals);
            };
        };
        public static final SeparatorToken RCPR = new SeparatorToken("}");
		public static final SeparatorToken CMA = new SeparatorToken(",");
        public static final SeparatorToken COL = new SeparatorToken(":");
		
		public SeparatorToken(String sep) {
			super(sep, sep);
			separators.put(sep, this);
		}
		
		@Override
		public String toString() {
		    return "SEP(" + value + ")";
		}
	}
	
	public static class OperatorToken extends Token<STExpression> {
        private static final Map<String, OperatorToken> operators = new HashMap<String, STTokens.OperatorToken>();
        public static OperatorToken get(STTokenizer tokenizer, String op) {
            OperatorToken token = operators.get(op);
            if(token == null)
                operators.put(op, token = new OperatorToken(tokenizer, op));
            return token;
        }
        
        private final STTokenizer tokenizer;
        public OperatorToken(STTokenizer tokenizer, String op) {
            super(op, op);
            this.tokenizer = tokenizer;
        }
        
        @Override
        public STExpression nud(Parser<STExpression> parser) {
            STExpression right = parser.expression(0);
            return new STApplyOp(value, right);
        }
        
        @Override
        public STExpression led(Parser<STExpression> parser, STExpression left) {
            STExpression right = parser.expression(tokenizer.rbp(value));
            return new STApplyOp(value, left, right);
        }
        
        @Override
        public int lbp() {
            return tokenizer.lbp(value);
        }
        
        @Override
        public String toString() {
            return "OP(" + value + ")";
        }
	}
	
	public static class LiteralToken extends Token<STExpression> {
		public static final String TYPE = "(literal)";
		
		public LiteralToken(String value) {
			super(TYPE, value);
		}
		
		@Override
		public STExpression nud(Parser<STExpression> parser) {
			return new STLiteralInteger(value);
		}
		
		@Override
		public String toString() {
		    return "(" + value + ")";
		}
	}

    public static class StringToken extends Token<STExpression> {
        public static final String TYPE = "(str)";

        public StringToken(String value) {
            super(TYPE, value);
        }

        @Override
        public STExpression nud(Parser<STExpression> parser) {
            return new STLiteralString(value);
        }

        @Override
        public String toString() {
            return "(" + value + ")";
        }
    }

	public static class SpaceToken extends Token<STExpression> {
		public static final SpaceToken INSTANCE = new SpaceToken();
		
		public static final String TYPE = "( )";
		
		public SpaceToken() {
			super(TYPE);
		}
		
		@Override
		public String toString() {
		    return type;
		}
	}

   public static class EOLToken extends Token<STExpression> {
        public static final EOLToken INSTANCE = new EOLToken();
        
        public static final String TYPE = "(eol)";
        
        public EOLToken() {
            super(TYPE);
        }

        @Override
        public String toString() {
            return type;
        }
   }

	public static class EOFToken extends Token<STExpression> {
		public static final EOFToken INSTANCE = new EOFToken();
		
		public static final String TYPE = "(eof)";
		
		public EOFToken() {
			super(TYPE);
		}
		
        @Override
        public String toString() {
            return type;
        }
	}
}
