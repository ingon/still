package org.still.parse.ast.expr.literal;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;

public class STLiteralString implements STExpression {
    public final String val;
    
    public STLiteralString(String val) {
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
}
