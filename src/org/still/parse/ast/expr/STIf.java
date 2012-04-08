package org.still.parse.ast.expr;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;

public class STIf implements STExpression {
    public final STExpression condition;
    public final STExpression succ;
    public final STExpression fail;

    public STIf(STExpression condition, STExpression succ, STExpression fail) {
        this.condition = condition;
        this.succ = succ;
        this.fail = fail;
    }

    @Override
    public Object eval(STEnviroment env) {
        if(Util.evalAsBool(env, condition)) {
            return succ.eval(env);
        } else {
            return fail.eval(env);
        }
    }
    
    @Override
    public STCell evalToCell(STEnviroment env) {
        if(Util.evalAsBool(env, condition)) {
            return succ.evalToCell(env);
        } else {
            return fail.evalToCell(env);
        }
    }

    @Override
    public String toString() {
        return "if " + condition + ": " + succ + " else " + fail;
    }
}
