package org.still.parse.ast.stmt;

import java.util.List;

import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;
import org.still.runtime.fun.STCompoundFunction;
import org.still.runtime.type.STFunctionType;
import org.still.util.ListUtils;

public class STFunction implements STStatement {
	public final String symbol;
	public final List<String> params;
    public final STFunctionType type;
	public final List<STStatement> statements;

	public STFunction(String symbol, List<String> params, STFunctionType type, List<STStatement> body) {
		this.symbol = symbol;
		this.params = params;
		this.type = type;
		this.statements = body;
	}
	
	@Override
	public Object eval(STEnviroment env) {
	    STCompoundFunction fun = new STCompoundFunction(env, params, statements);
	    env.defVal(symbol, fun);
	    return fun;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("fun ").append(symbol).append("(");
		sb.append(ListUtils.join(params, ", "));
		sb.append(")\r\n");
		sb.append(ListUtils.join(statements, "\r\n"));
		return sb.toString();
	}
}
