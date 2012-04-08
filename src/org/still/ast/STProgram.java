package org.still.ast;

import java.util.Iterator;
import java.util.List;

import org.still.parse.ast.STBlockUtils;
import org.still.parse.ast.STExpression;
import org.still.parse.ast.STStatement;
import org.still.runtime.STCell;
import org.still.runtime.STEnviroment;
import org.still.runtime.STMutableCell;
import org.still.runtime.fun.STPrimitiveFunction;
import org.still.runtime.fun.STPrimitiveLazyFunction;
import org.still.util.ListUtils;

public class STProgram {
	public final List<STStatement> statements;

	public STProgram(List<STStatement> statements) {
		this.statements = statements;
	}
	
    public Object eval() {
        STEnviroment env = new STEnviroment();
        importSys(env);
        return STBlockUtils.eval(env, statements);
    }
    
    public void importSys(STEnviroment env) {
        env.defVal("=", new STPrimitiveLazyFunction(2) {
            @Override
            protected Object invoke(STEnviroment env, STExpression e1, STExpression e2) {
                STCell target = e1.evalToCell(env);
                Object value = e2.eval(env);
                if(! (target instanceof STMutableCell))
                    throw new RuntimeException("Wrong lvalue");
                ((STMutableCell) target).setVal(value);
                return value;
            }
        });
        env.defVal("==", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                if(o1 != null && o2 != null)
                    return o1.equals(o2);
                else if(o1 == null && o2 == null)
                    return true;
                else
                    return false;
            }
        });
        env.defVal("!=", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 != i2;
            }
        });
        env.defVal("<", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 < i2;
            }
        });
        env.defVal("<=", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 <= i2;
            }
        });
        env.defVal(">", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 > i2;
            }
        });
        env.defVal(">=", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 >= i2;
            }
        });
        env.defVal("+", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 + i2;
            }
        });
        env.defVal("-", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 - i2;
            }
        });
        env.defVal("*", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 * i2;
            }
        });
        env.defVal("/", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 / i2;
            }
        });
        env.defVal("%", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                return i1 % i2;
            }
        });
        env.defVal("||", new STPrimitiveLazyFunction(2) {
            @Override
            protected Object invoke(STEnviroment env, STExpression o1, STExpression o2) {
                boolean b1 = (Boolean) o1.eval(env);
                return b1 ? true : (Boolean) o2.eval(env);
            }
        });
        env.defVal("&&", new STPrimitiveLazyFunction(2) {
            @Override
            protected Object invoke(STEnviroment env, STExpression o1, STExpression o2) {
                boolean b1 = (Boolean) o1.eval(env);
                return b1 ? (Boolean) o2.eval(env) : false;
            }
        });
        env.defVal("..", new STPrimitiveFunction(2) {
            @Override
            protected Object invoke(Object o1, Object o2) {
                final int i1 = ((Integer) o1).intValue();
                final int i2 = ((Integer) o2).intValue();
                return new Iterator<Integer>() {
                    private int pos = i1;
                    @Override
                    public boolean hasNext() {
                        return pos < i2;
                    }

                    @Override
                    public Integer next() {
                        return pos++;
                    }

                    @Override
                    public void remove() {
                    }
                };
            }
        });
        env.defVal("print", new STPrimitiveFunction(1) {
            @Override
            protected Object invoke(Object o) {
                System.out.println(o);
                return o;
            }
        });
    }
    
	@Override
	public String toString() {
		if(statements == null)
			return "(empty)";
        return ListUtils.join(statements, "\r\n\r\n");
	}
}
