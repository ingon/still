package org.still.parse.ast.stmt;

import java.util.Iterator;
import java.util.List;

import org.still.parse.ast.STBlockUtils;
import org.still.parse.ast.STExpression;
import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;
import org.still.runtime.STInterrupt;
import org.still.util.ListUtils;

public class STFor implements STStatement {
    public final String val;
    public final STExpression expr;
    public final List<STStatement> body;

    public STFor(String val, STExpression expr, List<STStatement> body) {
        this.val = val;
        this.expr = expr;
        this.body = body;
    }

    @Override
    public Object eval(STEnviroment env) {
        Object obj = expr.eval(env);
        if(! (obj instanceof Iterator))
            throw new RuntimeException("Expected an iterator as parameter");
        
        Object lastValue = null;
        Iterator<?> ite = (Iterator<?>) obj;
        while(ite.hasNext()) {
            Object o = ite.next();
            STEnviroment fenv = env.child();
            fenv.defVal(val, o);
            
            Object tmpValue = STBlockUtils.evalInterrupt(fenv, body);
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
    
    @Override
    public String toString() {
        return "for " + val + " in " + expr + "\r\n" + ListUtils.join(body, "\r\n");
    }
}
