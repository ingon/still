package org.still.parse.ast.stmt;

import org.still.parse.ast.STStatement;
import org.still.runtime.STEnviroment;
import org.still.runtime.STInterrupt;

public class STContinue implements STStatement {
    public static final STContinue INSTANCE = new STContinue();
    
    private STContinue() {
    }
    
    @Override
    public Object eval(STEnviroment env) {
        return STInterrupt.STContinue.INSTANCE;
    }
}
