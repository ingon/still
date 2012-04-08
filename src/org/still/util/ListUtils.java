package org.still.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
	public static interface Callback<S, R> {
		public R invoke(S s);
	}
	
	public static <X> String join(List<X> l, String s) {
		StringBuilder sb = new StringBuilder();
		for(X a : l) {
			if(sb.length() > 0) {
				sb.append(s);
			}
			sb.append(a);
		}
		return sb.toString();
	}
	
	public static <S, R> List<R> map(List<S> l, Callback<S, R> c) {
		List<R> r = new ArrayList<R>();
		for(S s : l) {
			r.add(c.invoke(s));
		}
		return r;
	}
}
