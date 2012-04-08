package org.still.parse.ast.stmt;

import java.lang.reflect.Method;

import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;
import org.still.runtime.fun.STGenericFunction;
import org.still.runtime.fun.STReflectiveMethodFunction;

public class STImport implements STStatement {
    public final String path;
    public final String target;

    public STImport(String path) {
        this(path, null);
    }
    
    public STImport(String path, String target) {
        this.path = path;
        this.target = target;
    }

    @Override
    public Object eval(STEnviroment env) {
        if(target != null) {
            if(env.check(target)) {
                Object o = env.val(target);
                if(o instanceof STEnviroment) {
                    return javaModule((STEnviroment) o);
                } else {
                    throw new RuntimeException("Name already in use: " + target);
                }
            } else {
                STEnviroment nenv = new STEnviroment();
                env.defVal(target, nenv);
                return javaModule(nenv);
            }
        } else {
            return javaModule(env);
        }
    }
    
    private Object stillModule(STEnviroment env) {
        return null;
    }
    
    private Object javaModule(STEnviroment env) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(path);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading class: " + path, e);
        }
        
        for(Method method : clazz.getMethods()) {
            defineMethod(env, clazz, method);
        }
        
        return env;
    }
    
    private void defineMethod(STEnviroment env, Class<?> clazz, Method method) {
        if(env.check(method.getName())) {
            Object o = env.val(method.getName());
            if(o instanceof STGenericFunction) {
                defineMethodInGenericFunction((STGenericFunction) o, clazz, method);
            } else {
                throw new RuntimeException("Not supported");
            }
        } else {
            STGenericFunction gfun = new STGenericFunction();
            env.defVal(method.getName(), gfun);
            
            defineMethodInGenericFunction(gfun, clazz, method);
        }
    }
    
    private void defineMethodInGenericFunction(STGenericFunction gfun, Class<?> clazz, Method method) {
        Class<?>[] parameters = method.getParameterTypes(); // TODO maybe on types?
        Class<?>[] funParams = new Class<?>[parameters.length + 1];
        
        funParams[0] = clazz;
        System.arraycopy(parameters, 0, funParams, 1, parameters.length);
        
        gfun.register(new STReflectiveMethodFunction(clazz, method), funParams);
    }
    
    @Override
    public String toString() {
        return "import " + path;
    }
}
