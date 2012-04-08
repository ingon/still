package org.still.runtime.fun;

import java.util.List;

import org.still.runtime.STFunction;

public class STPrimitiveFunction implements STFunction {
    public final int arity;
    
    public STPrimitiveFunction(int arity) {
        this.arity = arity;
    }
    
    @Override
    public Object apply(List<Object> args) {
        if(arity != args.size())
            throw new RuntimeException("Wrong arity: " + args.size() + " expected: " + arity);
        
        if(arity == 0) {
            return invoke();
        } else if(arity == 1) {
            return invoke(args.get(0));
        } else if(arity == 2) {
            return invoke(args.get(0), args.get(1));
        }
        
        throw new RuntimeException("Not implemented: " + arity);
    }

    protected Object invoke() {
        throw new RuntimeException("Not implemented");
    }

    protected Object invoke(Object o) {
        throw new RuntimeException("Not implemented");
    }

    protected Object invoke(Object o1, Object o2) {
        throw new RuntimeException("Not implemented");
    }
}
