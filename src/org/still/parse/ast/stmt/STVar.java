package org.still.parse.ast.stmt;

import java.util.List;

import org.still.parse.ast.STExpression;
import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;

public class STVar implements STStatement {
    public final List<String> names;
    public final List<STExpression> values;
    
    public STVar(List<String> names, List<STExpression> values) {
        this.names = names;
        this.values = values;
    }

    @Override
    public Object eval(STEnviroment env) {
        Object lastValue = null;
        
        for(int i = 0, n = names.size(); i < n; i++) {
            lastValue = values.get(i).eval(env);
            env.defVar(names.get(i), lastValue);
        }
        
        return lastValue;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("var ");
        for(int i = 0, n = names.size(); i < n; i++) {
            if(i > 0)
                sb.append(", ");
            sb.append(names.get(i)).append(" = ").append(values.get(i));
        }
        return sb.toString();
    }
}
