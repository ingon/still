package org.still.parse;

import java.util.ArrayList;
import java.util.List;

import org.still.parse.STTokens.BlockToken;
import org.still.parse.STTokens.StatementToken;
import org.still.parse.ast.STExpression;
import org.still.parse.ast.STStatement;
import org.still.parse.ast.stmt.STExpressionStatement;
import org.tdp.LookAheadParser;

public class STParser extends LookAheadParser<STExpression> {
    public static final int BLOCK_LEVEL_INDENT = 4;
    public final STTokenizer tokenizer;
    private int blockLevel = 0;
    
	public STParser(STTokenizer tokenizer) {
		super(tokenizer);
		this.tokenizer = tokenizer;
		
		registerDefaultOperators();
	}
	
	private void registerDefaultOperators() {
        tokenizer.registerOperator("=", 10, 9);
        tokenizer.registerOperator("||", 20);
        tokenizer.registerOperator("..", 30);
        tokenizer.registerOperator("==", 40);
        tokenizer.registerOperator("!=", 40);
        tokenizer.registerOperator("<", 50);
        tokenizer.registerOperator("<=", 50);
        tokenizer.registerOperator(">", 50);
        tokenizer.registerOperator(">=", 50);
        tokenizer.registerOperator("+", 60);
        tokenizer.registerOperator("-", 60);
        tokenizer.registerOperator("*", 70);
        tokenizer.registerOperator("/", 70);
        tokenizer.registerOperator("%", 70);
    }

    public List<STStatement> parseModule() {
		advance();

		return parseBlock();
	}
	
	public List<STStatement> parseNewBlock() {
	    blockLevel += BLOCK_LEVEL_INDENT;
	    try {
	        return parseBlock();
	    } finally {
	        blockLevel -= BLOCK_LEVEL_INDENT;
	    }
	}
	
	public List<STStatement> parseBlock() {
		List<STStatement> stmts = new ArrayList<STStatement>();
		while(token() instanceof BlockToken) {
			BlockToken bt = (BlockToken) token();
			if(bt.level < blockLevel) {
				break;
			} else if(bt.level > blockLevel) {
				throw new RuntimeException("Unexpected indentation");
			} else {
				advance();
				stmts.add(parseStatement());
			}
		}
		return stmts;
	}
	
	public STStatement parseStatement() {
	    if(token instanceof StatementToken) {
	        StatementToken<STStatement, STExpression> t = (StatementToken<STStatement, STExpression>) token;
	        advance();
	        return t.std(this);
	    } else {
	        return new STExpressionStatement(expression(0));
	    }
	}
	
	public int currentBlockLevel() {
	    return blockLevel;
	}
}
