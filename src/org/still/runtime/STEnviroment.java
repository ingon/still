package org.still.runtime;

import java.util.HashMap;
import java.util.Map;

import org.still.runtime.cell.STEnvCell;
import org.still.runtime.cell.STEnvMutableCell;

public class STEnviroment {
	private final STEnviroment parent;
	private final Map<String, STCell> defs = new HashMap<String, STCell>();
	
	public STEnviroment() {
		this(null);
	}
	
	private STEnviroment(STEnviroment parent) {
		this.parent = parent;
		
		defVal("_currentEnv", this);
		defVal("_parentEnv", parent);
	}
	
	public boolean check(String symbol) {
	    return defs.containsKey(symbol);
	}
	
	public Object val(String symbol) {
		if(defs.containsKey(symbol))
			return defs.get(symbol).val();
		
		if(parent != null)
			return parent.val(symbol);
		
		throw new RuntimeException("Unknown symbol: " + symbol);
	}
	
	public STCell var(String symbol) {
        if(defs.containsKey(symbol))
            return defs.get(symbol);
        
        if(parent != null)
            return parent.var(symbol);
        
        throw new RuntimeException("Unknown symbol: " + symbol);
	}
	
	public void defVal(String symbol) {
	    defVal(symbol, null);
	}
	
	public void defVal(String symbol, Object value) {
        if(defs.containsKey(symbol))
            throw new RuntimeException("Already defined: " + symbol);
        defs.put(symbol, new STEnvCell(value));
	}

    public void defVar(String symbol) {
        defVar(symbol, null);
    }

    public void defVar(String symbol, Object value) {
        if (defs.containsKey(symbol))
            throw new RuntimeException("Already defined: " + symbol);
        defs.put(symbol, new STEnvMutableCell(value));
    }

	public STEnviroment child() {
		return new STEnviroment(this);
	}
}
