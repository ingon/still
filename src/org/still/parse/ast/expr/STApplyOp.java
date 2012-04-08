package org.still.parse.ast.expr;

import java.util.Arrays;

import org.still.parse.ast.STExpression;
import org.still.util.ListUtils;

public class STApplyOp extends STApply {
	public STApplyOp(String symbol, STExpression p) {
		super(new STSymbol(symbol), Arrays.asList(p));
	}

	public STApplyOp(String symbol, STExpression p1, STExpression p2) {
		super(new STSymbol(symbol), Arrays.asList(p1, p2));
	}
	
	public STExpression left() {
	    return arguments.get(0);
	}
	
	public STExpression right() {
	    return arguments.get(1);
	}
	
	@Override
	public String toString() {
	    return ListUtils.join(arguments, symbol.toString());
	}
}
