package org.still.runtime;

import org.still.runtime.fun.STGenericFunction;
import org.still.runtime.fun.STPrimitiveFunction;

public class STPrimitiveOperators {
    private static Class<?>[] NUMERIC_TYPES = new Class<?>[] {Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class};
    private static Class<?>[] INTEGRAL_TYPES = new Class<?>[] {Byte.class, Short.class, Character.class, Integer.class};

    private static Number promoteToNumber(Object o) {
        if(o instanceof Character) {
            return (int) ((Character) o).charValue();
        } else {
            return (Number) o;
        }
    }
    
    private static abstract class ArithmeticPrefixFunction extends STPrimitiveFunction {
        public ArithmeticPrefixFunction() {
            super(1);
        }
        
        @Override
        protected Object invoke(Object o) {
            Number v = promoteToNumber(o);
            
            if(v instanceof Double) {
                return op(v.doubleValue());
            } else if(v instanceof Float) {
                return op(v.floatValue());
            } else if(v instanceof Long) {
                return op(v.longValue());
            } else {
                return op(v.intValue());
            }
        }
        
        public abstract double op(double v);
        public abstract float op(float v);
        public abstract long op(long v);
        public abstract int op(int v);
    }
    
    private static abstract class IntegralPrefixFunction extends STPrimitiveFunction {
        public IntegralPrefixFunction() {
            super(1);
        }
        
        @Override
        protected Object invoke(Object o) {
            Number v = promoteToNumber(o);
            return op(v.intValue());
        }
        
        public abstract int op(int v);
    }
    
    private static abstract class ArithmeticInfixFunction extends STPrimitiveFunction {
        public ArithmeticInfixFunction() {
            super(2);
        }
        
        @Override
        protected Object invoke(Object lo, Object ro) {
            Number l = promoteToNumber(lo);
            Number r = promoteToNumber(ro);
            
            if(l instanceof Double || r instanceof Double) {
                double ld = l.doubleValue();
                double rd = r.doubleValue();
                return op(ld, rd);
            } else if(l instanceof Float || r instanceof Float) {
                float lf = l.floatValue();
                float rf = r.floatValue();
                return op(lf, rf);
            } else if(l instanceof Long || r instanceof Long) {
                long lf = ((Number) l).longValue();
                long rf = ((Number) r).longValue();
                return op(lf, rf);
            } else {
                int lf = ((Number) l).intValue();
                int rf = ((Number) r).intValue();
                return op(lf, rf);
            }
        }
        
        public abstract double op(double l, double r);
        public abstract float op(float l, float r);
        public abstract long op(long l, long r);
        public abstract int op(int l, int r);
    }
    
    private static void genArithmeticPrefixTable(STGenericFunction fun, ArithmeticPrefixFunction delegate) {
        for(Class<?> leftClass : NUMERIC_TYPES) {
            fun.register(delegate, new Class<?>[] {leftClass});
        }
    }

    private static void genIntegralPrefixTable(STGenericFunction fun, IntegralPrefixFunction delegate) {
        for(Class<?> leftClass : INTEGRAL_TYPES) {
            fun.register(delegate, new Class<?>[] {leftClass});
        }
    }

    private static void genArithmeticInfixTable(STGenericFunction fun, ArithmeticInfixFunction delegate) {
        for(Class<?> leftClass : NUMERIC_TYPES) {
            for(Class<?> rightClass : NUMERIC_TYPES) {
                fun.register(delegate, new Class<?>[] {leftClass, rightClass});
            }
        }
    }
    
    public static void importAll(STEnviroment env) {
        importAddOperator(env);
        importSubOperator(env);
        importMulOperator(env);
        importDivOperator(env);
        importModOperator(env);
        importIncDecOperators(env);
        
        importBitComplement(env);
    }
    
    private static void importAddOperator(STEnviroment env) {
        STGenericFunction method = new STGenericFunction();
        genArithmeticInfixTable(method, new ArithmeticInfixFunction() {
            @Override
            public int op(int l, int r) {
                return l + r;
            }
            
            @Override
            public long op(long l, long r) {
                return l + r;
            }
            
            @Override
            public float op(float l, float r) {
                return l + r;
            }
            
            @Override
            public double op(double l, double r) {
                return l + r;
            }
        });
        
        genArithmeticPrefixTable(method, new ArithmeticPrefixFunction() {
            @Override
            public int op(int v) {
                return +v;
            }
            
            @Override
            public long op(long v) {
                return +v;
            }
            
            @Override
            public float op(float v) {
                return +v;
            }
            
            @Override
            public double op(double v) {
                return +v;
            }
        });
        
        method.register(new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object lo, Object ro) {
                String ls = (String) lo;
                String rs = (String) ro;
                return ls + rs;
            }
        }, new Class<?>[]{String.class, String.class});
        
        env.defVal("+", method);
    }
    
    private static void importSubOperator(STEnviroment env) {
        STGenericFunction method = new STGenericFunction();
        genArithmeticInfixTable(method, new ArithmeticInfixFunction() {
            @Override
            public int op(int l, int r) {
                return l - r;
            }
            
            @Override
            public long op(long l, long r) {
                return l - r;
            }
            
            @Override
            public float op(float l, float r) {
                return l - r;
            }
            
            @Override
            public double op(double l, double r) {
                return l - r;
            }
        });
        
        genArithmeticPrefixTable(method, new ArithmeticPrefixFunction() {
            @Override
            public int op(int v) {
                return -v;
            }
            
            @Override
            public long op(long v) {
                return -v;
            }
            
            @Override
            public float op(float v) {
                return -v;
            }
            
            @Override
            public double op(double v) {
                return -v;
            }
        });

        env.defVal("-", method);
    }

    private static void importMulOperator(STEnviroment env) {
        STGenericFunction method = new STGenericFunction();
        genArithmeticInfixTable(method, new ArithmeticInfixFunction() {
            @Override
            public int op(int l, int r) {
                return l * r;
            }
            
            @Override
            public long op(long l, long r) {
                return l * r;
            }
            
            @Override
            public float op(float l, float r) {
                return l * r;
            }
            
            @Override
            public double op(double l, double r) {
                return l * r;
            }
        });
        env.defVal("*", method);
    }

    private static void importDivOperator(STEnviroment env) {
        STGenericFunction method = new STGenericFunction();
        genArithmeticInfixTable(method, new ArithmeticInfixFunction() {
            @Override
            public int op(int l, int r) {
                return l / r;
            }
            
            @Override
            public long op(long l, long r) {
                return l / r;
            }
            
            @Override
            public float op(float l, float r) {
                return l / r;
            }
            
            @Override
            public double op(double l, double r) {
                return l / r;
            }
        });
        env.defVal("/", method);
    }

    private static void importModOperator(STEnviroment env) {
        STGenericFunction method = new STGenericFunction();
        genArithmeticInfixTable(method, new ArithmeticInfixFunction() {
            @Override
            public int op(int l, int r) {
                return l % r;
            }
            
            @Override
            public long op(long l, long r) {
                return l % r;
            }
            
            @Override
            public float op(float l, float r) {
                return l % r;
            }
            
            @Override
            public double op(double l, double r) {
                return l % r;
            }
        });
        env.defVal("%", method);
    }

    private static void importIncDecOperators(STEnviroment env) {
        STGenericFunction incFun = new STGenericFunction();
        genArithmeticPrefixTable(incFun, new ArithmeticPrefixFunction() {
            @Override
            public int op(int v) {
                return ++v;
            }
            
            @Override
            public long op(long v) {
                return ++v;
            }
            
            @Override
            public float op(float v) {
                return ++v;
            }
            
            @Override
            public double op(double v) {
                return ++v;
            }
        });
        env.defVal("++", incFun);
        
        STGenericFunction decFun = new STGenericFunction();
        genArithmeticPrefixTable(decFun, new ArithmeticPrefixFunction() {
            @Override
            public int op(int v) {
                return --v;
            }
            
            @Override
            public long op(long v) {
                return --v;
            }
            
            @Override
            public float op(float v) {
                return --v;
            }
            
            @Override
            public double op(double v) {
                return --v;
            }
        });
        env.defVal("--", decFun);
    }
    
    private static void importBitComplement(STEnviroment env) {
        STGenericFunction method = new STGenericFunction();
        genIntegralPrefixTable(method, new IntegralPrefixFunction() {
            @Override
            public int op(int v) {
                return ~v;
            }});
        env.defVal("~", method);
    }
}
