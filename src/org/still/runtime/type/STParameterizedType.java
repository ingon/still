package org.still.runtime.type;

import java.util.List;

import org.still.util.ListUtils;

public class STParameterizedType implements STType {
    public final String name;
    public final List<String> params;
    
    public STParameterizedType(String name, List<String> params) {
        this.name = name;
        this.params = params;
    }
    
    @Override
    public String toString() {
        return name + " " + ListUtils.join(params, " ");
    }
}
