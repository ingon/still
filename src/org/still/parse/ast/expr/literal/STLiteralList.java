package org.still.parse.ast.expr.literal;

import java.util.List;

import org.still.parse.ast.STExpression;
import org.still.parse.ast.expr.STApply.EvalCallback;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;
import org.still.util.ListUtils;

public class STLiteralList implements STExpression {
    public final List<STExpression> elements;
    
    public STLiteralList(List<STExpression> elements) {
        this.elements = elements;
    }

    @Override
    public Object eval(STEnviroment env) {
        return ListUtils.map(elements, new EvalCallback(env));
    }
    
    @Override
    public STCell evalToCell(STEnviroment env) {
        throw new RuntimeException("Unsupported lvalue");
    }
    
    @Override
    public String toString() {
        return elements.toString();
    }
}
