package org.still.parse.ast.expr.literal;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;

public class STLiteralNull implements STExpression {
    public static final STLiteralNull INSTANCE = new STLiteralNull();
    
    private STLiteralNull() {
    }
    
    @Override
    public Object eval(STEnviroment env) {
        return null;
    }
    
    @Override
    public STCell evalToCell(STEnviroment env) {
        throw new RuntimeException("Unsupported lvalue");
    }
    
    @Override
    public String toString() {
        return "null";
    }
}
