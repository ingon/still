package org.still.parse.ast;

import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;

public interface STExpression extends STSourceElement {
    public STCell evalToCell(STEnviroment env);
    
    public class Util {
        public static boolean evalAsBool(STEnviroment env, STExpression expr) {
            Object cval = expr.eval(env);
            if(! (cval instanceof Boolean))
                throw new RuntimeException("Expected boolean value");
            
            return ((Boolean) cval);
        }
    }
}
