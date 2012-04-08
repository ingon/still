package org.still.parse.ast.expr.literal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.still.parse.ast.STExpression;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;

public class STLiteralMap implements STExpression {
    public final List<STExpression> keys;
    public final List<STExpression> vals;

    public STLiteralMap(List<STExpression> keys, List<STExpression> vals) {
        this.keys = keys;
        this.vals = vals;
    }

    @Override
    public Object eval(STEnviroment env) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        for(int i = 0, n = keys.size(); i < n; i++) {
            STExpression key = keys.get(i);
            STExpression val = vals.get(i);
            map.put(key.eval(env), val.eval(env));
        }
        return map;
    }
    
    @Override
    public STCell evalToCell(STEnviroment env) {
        throw new RuntimeException("Unsupported lvalue");
    }
}
