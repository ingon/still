package org.still.runtime.fun;

import java.util.List;

public abstract class STPrimitiveGenericFunction extends STGenericFunction {
    @Override
    public Object apply(List<Object> args) {
        if(args.size() == 2) {
            Object lo = args.get(0);
            Object ro = args.get(1);
            if(isNumeric(lo) && isNumeric(ro)) {
                Number l = promote(lo);
                Number r = promote(ro);
                
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
        }
        
        return super.apply(args);
    }
    
    protected abstract double op(double l, double r);
    protected abstract float op(float l, float r);
    protected abstract long op(long l, long r);
    protected abstract int op(int l, int r);
    
    private boolean isNumeric(Object o) {
        return o instanceof Number || o instanceof Character;
    }
    
    private Number promote(Object o) {
        if(o instanceof Character) {
            return (int) ((Character) o).charValue();
        } else {
            return (Number) o;
        }
    }
}
