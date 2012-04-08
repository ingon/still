package org.still.runtime.fun;

import java.util.List;

import org.still.parse.ast.STExpression;
import org.still.runtime.STEnviroment;
import org.still.runtime.STLazyFunction;

public class STPrimitiveLazyFunction implements STLazyFunction {
    public final int arity;
    
    public STPrimitiveLazyFunction(int arity) {
        this.arity = arity;
    }

    @Override
    public Object apply(STEnviroment env, List<STExpression> args) {
        if(arity != args.size())
            throw new RuntimeException("Wrong arity: " + args.size() + " expected: " + arity);
        
        if(arity == 0) {
            return invoke(env);
        } else if(arity == 1) {
            return invoke(env, args.get(0));
        } else if(arity == 2) {
            return invoke(env, args.get(0), args.get(1));
        }
        
        throw new RuntimeException("Not implemented: " + arity);
    }

    protected Object invoke(STEnviroment env) {
        throw new RuntimeException("Not implemented");
    }

    protected Object invoke(STEnviroment env, STExpression e) {
        throw new RuntimeException("Not implemented");
    }

    protected Object invoke(STEnviroment env, STExpression e1, STExpression e2) {
        throw new RuntimeException("Not implemented");
    }
}
