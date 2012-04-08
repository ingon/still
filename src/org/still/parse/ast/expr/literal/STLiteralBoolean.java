package org.still.parse.ast.expr.literal;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;

public class STLiteralBoolean implements STExpression {
    public static final STLiteralBoolean TRUE = new STLiteralBoolean(true);
    public static final STLiteralBoolean FALSE = new STLiteralBoolean(false);
    
    public final boolean val;
    
    private STLiteralBoolean(boolean val) {
        this.val = val;
    }
    
    @Override
    public Object eval(STEnviroment env) {
        return val;
    }
    
    @Override
    public STCell evalToCell(STEnviroment env) {
        throw new RuntimeException("Unsupported lvalue");
    }
    
    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
