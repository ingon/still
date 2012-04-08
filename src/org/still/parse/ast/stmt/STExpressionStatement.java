package org.still.parse.ast.stmt;

import org.still.parse.ast.STExpression;
import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;

public class STExpressionStatement implements STStatement {
	public final STExpression expression;

	public STExpressionStatement(STExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public Object eval(STEnviroment env) {
		return expression.eval(env);
	}
	
	@Override
	public String toString() {
	    return expression.toString();
	}
}
