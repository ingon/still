package org.still.parse.ast;

import org.still.runtime.STEnviroment;

public interface STSourceElement {
	public Object eval(STEnviroment env);
}
