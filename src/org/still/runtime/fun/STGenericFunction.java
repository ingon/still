package org.still.runtime.fun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.still.runtime.STFunction;
import org.still.util.ListUtils;
import org.still.util.ListUtils.Callback;
import org.still.util.Pair;

public class STGenericFunction implements STFunction {
    private static final Callback<Object, Class<?>> OBJECT_CLASS_CALLBACK =  new Callback<Object, Class<?>>() {
        @Override
        public Class<?> invoke(Object s) {
            return s != null ? s.getClass() : null;
        }
    };
    
    public final List<Pair<Class<?>[], STFunction>> functions = new ArrayList<Pair<Class<?>[],STFunction>>();
    public final Map<Class<?>, List<Pair<Class<?>[], STFunction>>> mappedFunctions = new HashMap<Class<?>, List<Pair<Class<?>[],STFunction>>>();
    
    public STGenericFunction() {
    }
    
    @Override
    public Object apply(List<Object> args) {
        Object mainObject = args.get(0);
        Class<?> mainClass = mainObject.getClass();
        
        List<Object> realArgsList = args.subList(1, args.size());
        List<Class<?>> realArgsTypesList = ListUtils.map(realArgsList, OBJECT_CLASS_CALLBACK);
        Class<?>[] realArgsTypes = realArgsTypesList.toArray(new Class<?>[realArgsTypesList.size()]);

        List<Class<?>> classes = new LinkedList<Class<?>>();
        classes.add(mainClass);
        while(! classes.isEmpty()) {
            Class<?> tmpClass = classes.remove(0);
            List<Pair<Class<?>[], STFunction>> methods = mappedFunctions.get(tmpClass);
            if(methods != null) {
                STFunction fun = findFunction(realArgsTypes, methods);
                if(fun != null) {
                    return fun.apply(args);
                }
            }
            
            if(tmpClass.getSuperclass() != null)
                classes.add(tmpClass.getSuperclass());
            classes.addAll(Arrays.asList(tmpClass.getInterfaces()));
        }
        
        throw new RuntimeException("Unable to find method");
    }
    
    private STFunction findFunction(Class<?>[] realArgsTypes, List<Pair<Class<?>[], STFunction>> methods) {
        Pair<Class<?>[], STFunction> bestFun = null;
        Integer bestMatchValue = null;
        for(Pair<Class<?>[], STFunction> fun : methods) {
            Integer matchValue = matchValue(fun.f, realArgsTypes);
            if(matchValue == null) {
                continue;
            }
            
            if(bestMatchValue == null) {
                bestFun = fun;
                bestMatchValue = matchValue;
                continue;
            }
            
            if(matchValue < bestMatchValue) {
                continue;
            }
            
            bestFun = fun;
            bestMatchValue = matchValue;
        }
        
        if(bestFun == null) {
            throw new RuntimeException("Unable to find method");
        }
        
        return bestFun.s;
    }
    
    private static Integer matchValue(Class<?>[] to, Class<?>[] types) {
        if (to.length != types.length) {
            return null;
        }

        int value = 0;
        for (int i = 0, n = to.length; i < n; i++) {
            if (to[i] == types[i]) {
                value += 3;
                continue;
            }

            if (types[i] == null) {
                continue;
            }

            if (to[i].isPrimitive() && !types[i].isPrimitive()) {
                Class<?> toWrap = getWrapperClass(to[i]);
                if (toWrap.equals(types[i])) {
                    value += 2;
                    continue;
                }

                return null;
            }

            if (!to[i].isPrimitive() && types[i].isPrimitive()) {
                Class<?> typeWrap = getWrapperClass(types[i]);
                if (!to[i].isAssignableFrom(typeWrap)) {
                    return null;
                }

                value += 1;
                continue;
            }

            if (!to[i].isAssignableFrom(types[i])) {
                return null;
            }

            value += 1;
        }

        return value;
    }

    private static Class<?> getWrapperClass(Class<?> primitive) {
        if (primitive == byte.class) {
            return Byte.class;
        } else if (primitive == short.class) {
            return Short.class;
        } else if (primitive == int.class) {
            return Integer.class;
        } else if (primitive == long.class) {
            return Long.class;
        } else if (primitive == float.class) {
            return Float.class;
        } else if (primitive == double.class) {
            return Double.class;
        } else if (primitive == boolean.class) {
            return Boolean.class;
        } else if (primitive == char.class) {
            return Character.class;
        }

        return primitive;
    }

    public void register(STFunction function, Class<?>[] funParams) {
        functions.add(new Pair<Class<?>[], STFunction>(funParams, function));
        
        Class<?>[] subParams = Arrays.copyOfRange(funParams, 1, funParams.length);
        Pair<Class<?>[], STFunction> p = new Pair<Class<?>[], STFunction>(subParams, function);
        
        List<Pair<Class<?>[], STFunction>> l = mappedFunctions.get(funParams[0]);
        if(l == null) {
            l = new ArrayList<Pair<Class<?>[],STFunction>>();
            mappedFunctions.put(funParams[0], l);
        }
        
        for(Pair<Class<?>[], STFunction> op : l) {
            if(Arrays.equals(p.f, op.f)) {
                throw new RuntimeException("Always should be different?");
            }
        }
        l.add(p);
    }
}
