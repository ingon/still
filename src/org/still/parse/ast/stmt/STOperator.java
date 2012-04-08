package org.still.parse.ast.stmt;

import java.util.List;

import org.still.parse.ast.STStatement;
import org.still.runtime.type.STFunctionType;

public class STOperator extends STFunction {
    public STOperator(String symbol, List<String> params, STFunctionType type, List<STStatement> body) {
        super(symbol, params, type, body);
    }
}
