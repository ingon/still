package org.still.runtime;

import java.util.List;

import org.still.parse.ast.STExpression;

public interface STLazyFunction {
    public Object apply(STEnviroment env, List<STExpression> args);
}
