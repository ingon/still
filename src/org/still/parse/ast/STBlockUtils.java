package org.still.parse.ast;

import java.util.List;

import org.still.runtime.STEnviroment;
import org.still.runtime.STInterrupt;
import org.still.runtime.STInterrupt.STResult;

public class STBlockUtils {
    public static Object eval(STEnviroment env, List<STStatement> statements) {
        Object lastVal = null;
        for(STStatement statement : statements) {
            lastVal = statement.eval(env);
            if(lastVal instanceof STResult) {
                return ((STResult) lastVal).val;
            }
        }
        return lastVal;
    }

    public static Object evalRet(STEnviroment env, List<STStatement> statements) {
        Object lastVal = null;
        for(STStatement statement : statements) {
            lastVal = statement.eval(env);
            if(lastVal instanceof STResult) {
                return lastVal;
            }
        }
        return lastVal;
    }

    public static Object evalInterrupt(STEnviroment env, List<STStatement> statements) {
        Object lastVal = null;
        for(STStatement statement : statements) {
            lastVal = statement.eval(env);
            if(lastVal instanceof STInterrupt) {
                return lastVal;
            }
        }
        return lastVal;
    }

    public static Object evalInChild(STEnviroment env, List<STStatement> statements) {
        return eval(env.child(), statements);
    }

    public static Object evalInChildRet(STEnviroment env, List<STStatement> statements) {
        return evalRet(env.child(), statements);
    }

    public static Object evalInChildInterrupt(STEnviroment env, List<STStatement> statements) {
        return evalInterrupt(env.child(), statements);
    }
}
