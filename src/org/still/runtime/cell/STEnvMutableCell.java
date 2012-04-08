package org.still.runtime.cell;

import org.still.runtime.STMutableCell;

public class STEnvMutableCell implements STMutableCell {
    private Object o;
    
    public STEnvMutableCell(Object o) {
        this.o = o;
    }

    @Override
    public Object val() {
        return o;
    }

    @Override
    public void setVal(Object o) {
        this.o = o;
    }
}
