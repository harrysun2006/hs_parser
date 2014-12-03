/*
 * Created on 2005-11-14
 *
 */
package com.sophia.parser;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-14
 *
 */
public class Constant {

	public final static Class ERROR = Error.class;

	public final static Object PENDING = new Object() {
		public String toString() {
			return "pending";
		}
	};

	public final static Token NULL = new Token();

	public static boolean isError(Class clazz) {
		return clazz == Constant.ERROR;
	}

	public static boolean hasError(Class[] classes) {
		if(classes == null) return false;
		for(int i = 0; i < classes.length; i++) {
			if(classes[i] == Constant.ERROR)return true;
		}
		return false;
	}

	public static boolean isPending(Object o) {
		return o == Constant.PENDING;
	}

	public static boolean hasPending(Object[] objs) {
		if(objs == null) return false;
		for(int i = 0; i < objs.length; i++) {
			if(objs[i] == Constant.PENDING)return true;
		}
		return false;
	}

	private final static class Error {
		public String toString() {
			return "error";
		}
	}
}
