package org.still.parse.ast.expr;

import java.util.Collections;
import java.util.List;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;
import org.still.runtime.STFunction;
import org.still.runtime.STLazyFunction;
import org.still.util.ListUtils;

public class STApply implements STExpression {
	public static class EvalCallback implements ListUtils.Callback<STExpression, Object> {
		public final STEnviroment env;
		
		public EvalCallback(STEnviroment env) {
			this.env = env;
		}

		@Override
		public Object invoke(STExpression s) {
			return s.eval(env);
		}
	}
	
	public final STExpression symbol;
	public final List<STExpression> arguments;
	
	public STApply(STExpression symbol) {
		this(symbol, Collections.<STExpression>emptyList());
	}

	public STApply(STExpression symbol, List<STExpression> params) {
		this.symbol = symbol;
		this.arguments = params;
	}
	
	@Override
	public Object eval(STEnviroment env) {
		Object funo = symbol.eval(env);
		if(funo instanceof STFunction) {
	        STFunction fun = (STFunction) funo;
	        List<Object> params = ListUtils.map(this.arguments, new EvalCallback(env));
	        return fun.apply(params);
		} else if(funo instanceof STLazyFunction) {
		    STLazyFunction fun = (STLazyFunction) funo;
		    return fun.apply(env, arguments);
		} else {
			throw new RuntimeException("Expected function");
		}
	}
	
	@Override
	public STCell evalToCell(STEnviroment env) {
	    throw new RuntimeException("Unsupported lvalue");
	}
	
	@Override
	public String toString() {
		return symbol.toString() + "(" + ListUtils.join(arguments, ", ") + ")";
	}
}
