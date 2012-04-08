package org.still.runtime.fun;

import java.util.List;

import org.still.parse.ast.STBlockUtils;
import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;
import org.still.runtime.STFunction;

public class STCompoundFunction implements STFunction {
	public final STEnviroment env;
	public final List<String> params;
	public final List<STStatement> statements;
	
	public STCompoundFunction(STEnviroment env, List<String> params, List<STStatement> statements) {
		this.env = env;
		this.params = params;
		this.statements = statements;
	}

	@Override
	public Object apply(List<Object> args) {
	    if(args.size() != params.size())
	        throw new RuntimeException("Wrong arity: " + args.size() + " expected: " + params.size());
	    
        STEnviroment currentEnv = env.child();
        for(int i = 0, n = params.size(); i < n; i++) {
            currentEnv.defVal(params.get(i), args.get(i));
        }
        
        return STBlockUtils.eval(currentEnv, statements);
	}
}
