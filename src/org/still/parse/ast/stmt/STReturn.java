package org.still.parse.ast.stmt;

import org.still.parse.ast.STExpression;
import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;
import org.still.runtime.STInterrupt.STResult;

public class STReturn implements STStatement {
	public final STExpression expr;

	public STReturn(STExpression expr) {
		this.expr = expr;
	}
	
	@Override
	public Object eval(STEnviroment env) {
	    Object res = expr.eval(env);
	    if(res instanceof STResult)
	        return res;
		return new STResult(res);
	}
	
	@Override
	public String toString() {
		return "return " + expr;
	}
}
