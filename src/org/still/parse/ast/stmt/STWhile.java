package org.still.parse.ast.stmt;

import java.util.List;

import org.still.parse.ast.STBlockUtils;
import org.still.parse.ast.STExpression;
import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;
import org.still.runtime.STInterrupt;

public class STWhile implements STStatement {
    public final STExpression cond;
    public final List<STStatement> body;
    
    public STWhile(STExpression cond, List<STStatement> body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public Object eval(STEnviroment env) {
        Object lastValue = null;
        while(STExpression.Util.evalAsBool(env, cond)) {
            Object tmpValue = STBlockUtils.evalInChildInterrupt(env, body);
            if(tmpValue instanceof STInterrupt.STResult) {
                return tmpValue;
            } else if(tmpValue instanceof STInterrupt.STBreak) {
                break;
            } else if(tmpValue instanceof STInterrupt.STContinue) {
                continue;
            } else {
                lastValue = tmpValue;
            }
        }
        return lastValue;
    }
}
