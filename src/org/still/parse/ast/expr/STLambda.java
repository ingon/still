package org.still.parse.ast.expr;

import java.util.List;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;
import org.still.runtime.fun.STLambdaFunction;
import org.still.util.ListUtils;

public class STLambda implements STExpression {
    public final List<String> params;
    public final STExpression expression;

    public STLambda(List<String> params, STExpression expression) {
        this.params = params;
        this.expression = expression;
    }

    @Override
    public Object eval(STEnviroment env) {
        return new STLambdaFunction(env, params, expression);
    }
    
    @Override
    public STCell evalToCell(STEnviroment env) {
        throw new RuntimeException("Unsupported lvalue");
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("fun").append("(");
        sb.append(ListUtils.join(params, ", "));
        sb.append(") ");
        sb.append(expression);
        return sb.toString();
    }
}
