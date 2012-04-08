package org.still.parse.ast.expr.literal;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;

public class STLiteralInteger implements STExpression {
	public final String value;
	public final int pval;
	
	public STLiteralInteger(String value) {
		this.value = value;
		this.pval = Integer.parseInt(value);
	}
	
	@Override
	public Object eval(STEnviroment env) {
		return pval;
	}

    @Override
    public STCell evalToCell(STEnviroment env) {
        throw new RuntimeException("Unsupported lvalue");
    }
    
	@Override
	public String toString() {
		return value;
	}
}
