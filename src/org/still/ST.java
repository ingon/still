package org.still;

import java.io.FileReader;
import java.io.IOException;

public class ST {
	public static void main(String[] args) throws IOException {
	    long startTime = System.currentTimeMillis();
//      STTokenizer tokenizer = new STTokenizer(new FileReader("src-still/main.st"));
//		STTokenizer tokenizer = new STTokenizer(new FileReader("src-still/tmp.st"));
//		STTokenizer tokenizer = new STTokenizer(new FileReader("src-still/ok.st"));
//        STTokenizer tokenizer = new STTokenizer(new FileReader("src-euler/eul1_i.st"));
//        STTokenizer tokenizer = new STTokenizer(new FileReader("src-euler/eul1_f.st"));
//        STTokenizer tokenizer = new STTokenizer(new FileReader("src-euler/eul2_i.st"));
//        STTokenizer tokenizer = new STTokenizer(new FileReader("src-euler/eul2_f.st"));
//		STParser parser = new STParser(tokenizer);
//		STProgram prog = parser.parseModule();
//		
//		System.out.println("SOURCE: \r\n" + prog);
//		System.out.println("OUT: ");
//		Object res = prog.eval();
//		
//		System.out.println("VALUE: \r\n" + res);
		
//	    FileReader reader = new FileReader("src-still/main.st");
	    FileReader reader = new FileReader("src-still/tmp.st");
//	    FileReader reader = new FileReader("src-euler/eul1_i.st");
	    Object res = Still.eval(reader);
	    System.out.println("VALUE: \r\n" + res);
	    
		long endTime = System.currentTimeMillis();
		System.out.println("TIME: " + (endTime - startTime));
	}
}
