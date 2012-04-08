package org.still.runtime.type;

public class STPlainType implements STType {
    public final String name;

    public STPlainType(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
