package org.still.util;

public class Pair<F, S> {
    public final F f;
    public final S s;
    
    public Pair(F f, S s) {
        this.f = f;
        this.s = s;
    }
    
    @Override
    public int hashCode() {
        int val = 37 * (f != null ? f.hashCode() : 0);
        val += 37 * (s != null ? s.hashCode() : 0);
        return val;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Pair))
            return false;
        
        Pair<?, ?> p = (Pair<?, ?>) obj;
        return ObjectUtils.safeEquals(f, p.f) && ObjectUtils.safeEquals(s, p.s);
    }
    
    public static <F, S> Pair<F, S> of(F f, S s) {
        return new Pair<F, S>(f, s);
    }
}
