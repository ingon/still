package org.still.runtime.fun;

import java.util.List;

import org.still.parse.ast.STExpression;
import org.still.runtime.STEnviroment;
import org.still.runtime.STFunction;

public class STLambdaFunction implements STFunction {
    public final STEnviroment env;
    public final List<String> params;
    public final STExpression expression;

    public STLambdaFunction(STEnviroment env, List<String> params, STExpression expression) {
        this.env = env;
        this.params = params;
        this.expression = expression;
    }

    @Override
    public Object apply(List<Object> args) {
        if(args.size() != params.size())
            throw new RuntimeException("Wrong arity: " + args.size() + " expected: " + params.size());
        
        STEnviroment currentEnv = env.child();
        for(int i = 0, n = params.size(); i < n; i++) {
            currentEnv.defVal(params.get(i), args.get(i));
        }
        
        return expression.eval(currentEnv);
    }
}
