package org.still.runtime;

public interface STInterrupt {
    public static class STResult implements STInterrupt {
        public final Object val;

        public STResult(Object val) {
            this.val = val;
        }
    }

    public static class STContinue implements STInterrupt {
        public static final STContinue INSTANCE = new STContinue();
    }

    public static class STBreak implements STInterrupt {
        public static final STBreak INSTANCE = new STBreak();
    }
}
