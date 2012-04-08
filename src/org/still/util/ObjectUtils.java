package org.still.util;

public class ObjectUtils {
    public static boolean safeEquals(Object o1, Object o2) {
        if(o1 != null && o2 != null)
            return o1.equals(o2);
        else if(o1 == null && o2 == null)
            return true;
        else
            return false;
    }
}
