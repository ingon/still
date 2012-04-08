package org.still.parse.ast.stmt;

import java.util.List;

import org.still.parse.ast.STBlockUtils;
import org.still.parse.ast.STExpression;
import org.still.parse.ast.STStatement;
import org.still.parse.ast.STExpression.Util;
import org.still.runtime.STEnviroment;
import org.still.util.ListUtils;

public class STIf implements STStatement {
    public final STExpression condition;
    public final List<STStatement> posStatements;
    public final List<STStatement> negStatements;
    
    public STIf(STExpression condition, List<STStatement> posStatements) {
        this(condition, posStatements, null);
    }
    
    public STIf(STExpression condition, List<STStatement> posStatements, List<STStatement> negStatements) {
        this.condition = condition;
        this.posStatements = posStatements;
        this.negStatements = negStatements;
    }

    @Override
    public Object eval(STEnviroment env) {
        if(Util.evalAsBool(env, condition)) {
            return STBlockUtils.evalInChildRet(env, posStatements);
        } else if(negStatements != null) {
            return STBlockUtils.evalInChildRet(env, negStatements);
        } else {
            return null;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("if ").append(condition);
        sb.append("\r\n").append(ListUtils.join(posStatements, "\r\n"));
        if(negStatements != null) {
            sb.append("\r\nelse\r\n").append(ListUtils.join(negStatements, "\r\n"));
        }
        return sb.toString();
    }
}
