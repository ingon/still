package org.still.parse.ast.stmt;

import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;
import org.still.runtime.STInterrupt;

public class STBreak implements STStatement {
    public static final STBreak INSTANCE = new STBreak();
    
    private STBreak() {
    }
    
    @Override
    public Object eval(STEnviroment env) {
        return STInterrupt.STBreak.INSTANCE;
    }
}
