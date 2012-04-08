package org.still.runtime.cell;

import org.still.runtime.STCell;

public class STEnvCell implements STCell {
    public final Object o;

    public STEnvCell(Object o) {
        this.o = o;
    }
    
    @Override
    public Object val() {
        return o;
    }
}
