package org.still.runtime.fun;

import java.lang.reflect.Method;
import java.util.List;

import org.still.runtime.STFunction;

public class STReflectiveMethodFunction implements STFunction {
    public final Class<?> clazz;
    public final Method method;
    
    public STReflectiveMethodFunction(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public Object apply(List<Object> args) {
        List<Object> methodArgs = args.subList(1, args.size());
        try {
            return method.invoke(args.get(0), methodArgs.toArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
