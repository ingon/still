package org.still.parse.ast.expr;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;

public class STModuleAccess implements STExpression {
    public final STExpression parent;
    public final STExpression child;
    
    public STModuleAccess(STExpression parent, STExpression child) {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public Object eval(STEnviroment env) {
        Object o = parent.eval(env);
        if(o instanceof STEnviroment) {
            return child.eval((STEnviroment) o);
        } else {
            throw new RuntimeException("Unknown module");
        }
    }

    @Override
    public STCell evalToCell(STEnviroment env) {
        throw new RuntimeException("Unsupported lvalue");
    }
}
