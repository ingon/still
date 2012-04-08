package org.still.runtime.type;

import java.util.List;

import org.still.util.ListUtils;

public class STFunctionType implements STType {
    public final List<STType> type;
    public final List<STType> parameterTypes;
    public final STType resultType;
    
    public STFunctionType(List<STType> type) {
        this.type = type;
        this.parameterTypes = type.subList(0, type.size() - 1);
        this.resultType = type.get(type.size() - 1);
    }
    
    @Override
    public String toString() {
        return "(" + ListUtils.join(type, " -> ") + ")";
    }
}
