package org.still.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.still.parse.ast.STExpression;
import org.still.util.Pair;
import org.tdp.Token;
import org.tdp.Tokenizer;

public class STTokenizer implements Tokenizer<STExpression> {
	private enum CharClass {
		SYMBOL,
		OPERATOR,
		SEPARATOR,
		NUMBER,
		STRING,
		WS,
		EOL;
	}
	
	private final BufferedReader reader;
	private final List<Token<STExpression>> nextTokens = new ArrayList<Token<STExpression>>();
	private final StringBuilder sb = new StringBuilder();
	private final Map<String, Pair<Integer, Integer>> operatorPrecedence = new HashMap<String, Pair<Integer,Integer>>();
	
	public STTokenizer(Reader reader) {
		this.reader = new BufferedReader(reader);
	}
	
	@Override
	public Token<STExpression> next() {
		while(nextTokens.isEmpty()) {
			String line = readLine();
			if(line == null)
				return STTokens.EOFToken.INSTANCE;
			
			parseLine(line);
		}
		
		return nextTokens.remove(0);
	}

    public void registerOperator(String op, int bp) {
        registerOperator(op, bp, bp);
    }

	public void registerOperator(String op, int lbp, int rbp) {
	    Pair<Integer, Integer> precedence = operatorPrecedence.get(op);
	    if(precedence != null) {
    	    if(precedence.f != lbp || precedence.s != rbp) {
    	        throw new RuntimeException("Operator already registered at different precedence levels");
    	    }
	    } else {
	        operatorPrecedence.put(op, Pair.of(lbp, rbp));
	    }
	}
	
	protected int lbp(String op) {
	    return getPrecedence(op).f;
	}
	
	protected int rbp(String op) {
	    return getPrecedence(op).s;
	}
	
	private Pair<Integer, Integer> getPrecedence(String op) {
	    Pair<Integer, Integer> precedence = operatorPrecedence.get(op);
	    if(precedence == null)
	        throw new RuntimeException("Unknown operator: " + op);
	    return precedence;
	}
	
	private void parseLine(String line) {
		if(line.length() == 0)
			return;
		
		int li = 0;
		int blockLevel = 0;
		while(line.charAt(li) == ' ') {
			li++;
			blockLevel++;
			if(li >= line.length()) {
			    return;
			}
		}
		nextTokens.add(new STTokens.BlockToken(blockLevel));
		
		while(li < line.length()) {
			sb.setLength(0);
			
			char ch = line.charAt(li);
			CharClass cl = startClass(ch);
			if(cl == CharClass.EOL) {
			    nextTokens.add(STTokens.EOLToken.INSTANCE);
				break;
			} else if(cl == CharClass.SEPARATOR) {
				sb.append(ch);
				nextTokens.add(resolveToken(cl));
				li += 1;
				continue;
			} else if(cl == CharClass.STRING) {
			    li = readString(line, li);
			    nextTokens.add(resolveToken(cl));
			    continue;
			}
			
			sb.append(ch);
			while(true) {
				li += 1;
				if(li >= line.length())
					break;
				
				ch = line.charAt(li);
				CharClass nc = midClass(cl, ch);
				if(cl != nc) {
					break;
				}
				
				sb.append(ch);
			}

			if(cl != CharClass.WS)
				nextTokens.add(resolveToken(cl));
		}
	}
	
	private Token<STExpression> resolveToken(CharClass cl) {
		String val = sb.toString();
		if(cl == CharClass.SYMBOL) {
			STTokens.KeywordToken ktoken = STTokens.KeywordToken.get(val);
			if(ktoken != null) return ktoken;
			
			return new STTokens.SymbolToken(val);
		} else if(cl == CharClass.OPERATOR) {
			return STTokens.OperatorToken.get(this, val);
		} else if(cl == CharClass.SEPARATOR) {
			return STTokens.SeparatorToken.getStrict(val);
		} else if(cl == CharClass.NUMBER) {
			return new STTokens.LiteralToken(val);
		} else if(cl == CharClass.STRING) {
		    return new STTokens.StringToken(val);
		} else {
			throw new RuntimeException("Unknown char class: " + cl);
		}
	}

	private String readLine() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private int readString(String line, int li) {
	    int nli = li + 1;
	    while(nli < line.length()) {
	        char ch = line.charAt(nli);
	        if(ch == '\\') {
	            nli++;
	            if(nli >= line.length())
	                throw new RuntimeException("Unexpected eol");
	            char nch = line.charAt(nli);
	            switch (nch) {
                case '\\':
                    sb.append('\\');
                    break;
                case 't':
                    sb.append('\t');
                    break;
                case 'b':
                    sb.append('\b');
                    break;
                case 'n':
                    sb.append('\n');
                    break;
                case 'r':
                    sb.append('\r');
                    break;
                case 'f':
                    sb.append('\f');
                    break;
                case '"':
                    sb.append('"');
                    break;
                case '\'':
                    sb.append('\'');
                    break;
                default:
                    sb.append(nch);
                    break;
                }
	        } else if(ch == '"') {
	            return nli + 1;
	        } else {
	            sb.append(ch);
	        }
	        
	        nli++;
	    }
	    
        throw new RuntimeException("String literals only till the end of line");
	}

	private CharClass startClass(char ch) {
		if(isLetter(ch))
			return CharClass.SYMBOL;
		else if(isOperator(ch))
			return CharClass.OPERATOR;
		else if(isSeparator(ch))
			return CharClass.SEPARATOR;
		else if(isWhitespace(ch))
			return CharClass.WS;
		else if(isNumber(ch))
			return CharClass.NUMBER;
		else if(isString(ch))
		    return CharClass.STRING;
		else if(isEol(ch))
			return CharClass.EOL;
		else
			throw new RuntimeException("Unknown start char: " + ch);
	}
	
	private CharClass midClass(CharClass beginClass, char ch) {
		CharClass midClass = startClass(ch);
		if(beginClass == CharClass.SYMBOL)
			if(midClass == CharClass.OPERATOR || midClass == CharClass.NUMBER)
				return CharClass.SYMBOL;
		
		return midClass;
	}
	
	private static boolean isLetter(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
	}
	
	private static boolean isNumber(char ch) {
		return ch >= '0' && ch <= '9';
	}

	private static boolean isOperator(char ch) {
	    return isArithmetic(ch) || isComparsion(ch) || isLogical(ch) || isOtherOperator(ch);
	}
	
	private static boolean isArithmetic(char ch) {
		return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == '±';
	}
	
	private static boolean isComparsion(char ch) {
	    return ch == '=' || ch == '<' || ch == '>' || ch == '!';
	}
	
	private static boolean isLogical(char ch) {
	    return ch == '&' || ch == '|' || ch == '?';
	}

	private static boolean isOtherOperator(char ch) {
	    return ch == '@' || ch == '#' || ch == '$' || ch == '^' || ch == '\\' || ch == '.';
    }

	private static boolean isSeparator(char ch) {
		return ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}' || ch == ',' || ch == ':';
	}
	
	private static boolean isString(char ch) {
	    return ch == '"';
	}
	
	private static boolean isWhitespace(char ch) {
		return ch == ' ';
	}
	
	private static boolean isEol(char ch) {
		return ch == '\r' || ch == '\n';
	}
}
