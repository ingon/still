package org.still.parse.ast.expr;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;

public class STSymbol implements STExpression {
	public final String symbol;

	public STSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public Object eval(STEnviroment env) {
		return env.val(symbol);
	}
	
    @Override
    public STCell evalToCell(STEnviroment env) {
        return env.var(symbol);
    }
    
	@Override
	public String toString() {
		return symbol;
	}
}
