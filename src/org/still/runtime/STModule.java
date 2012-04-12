package org.still.runtime;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.still.parse.STParser;
import org.still.parse.ast.STBlockUtils;
import org.still.parse.ast.STStatement;
import org.still.runtime.fun.STPrimitiveFunction;

public class STModule {
    public final List<STStatement> statements;
    
    private STEnviroment moduleEnv;

    public STModule(List<STStatement> statements) {
        this.statements = statements;
    }
    
    public STModule(STParser parser) {
        this.statements = parser.parseModule();
    }

    public Object eval(Map<String, Object> bindings) {
        if(moduleEnv != null) {
            throw new RuntimeException("Already evaluated");
        }
        
        moduleEnv = new STEnviroment();
        importSystem();
        
        for(Map.Entry<String, Object> e : bindings.entrySet()) {
            moduleEnv.defVal(e.getKey(), e.getValue());
        }
        
        return STBlockUtils.eval(moduleEnv, statements);
    }
    
    public Object call(String funName) {
        return call(funName, Collections.emptyList());
    }
    
    public Object call(String funName, List<Object> params) {
        Object o = moduleEnv.val(funName);
        if(o instanceof STFunction) {
            STFunction fun = (STFunction) o;
            return fun.apply(params);
        } else {
            throw new RuntimeException("Not a function: " + funName);
        }
    }
    
    private void importSystem() {
        STPrimitiveOperators.importAll(moduleEnv);

        moduleEnv.defVal("print", new STPrimitiveFunction(1) {
            @Override
            protected Object invoke(Object o) {
                System.out.println(o);
                return o;
            }
        });
    }
}
