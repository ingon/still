package org.still;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Map;

import org.still.parse.STParser;
import org.still.parse.STTokenizer;
import org.still.runtime.STModule;

public class Still {
    public static Object eval(String str) {
        return eval(new StringReader(str));
    }
    
    public static Object eval(Reader reader) {
        return eval(reader, Collections.<String, Object>emptyMap());
    }
    
    public static Object eval(String str, Map<String, Object> binings) {
        return eval(new StringReader(str), Collections.<String, Object>emptyMap());
    }
    
    public static Object eval(Reader reader, Map<String, Object> bindings) {
        STModule module = compile(reader, bindings);
        return module.eval(bindings);
    }
    
    public static STModule compile(String str) {
        return compile(new StringReader(str));
    }
    
    public static STModule compile(Reader reader) {
        return compile(reader, Collections.<String, Object>emptyMap());
    }
    
    public static STModule compile(String str, Map<String, Object> binings) {
        return compile(new StringReader(str), Collections.<String, Object>emptyMap());
    }

    public static STModule compile(Reader reader, Map<String, Object> bindings) {
        STTokenizer tokenizer = new STTokenizer(reader);
        STParser parser = new STParser(tokenizer);
        return new STModule(parser);
    }
}
