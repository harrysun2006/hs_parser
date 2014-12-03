/*
 * Created on 2005-10-28
 *
 */
package com.sophia.parser;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-28
 *
 */
public class Member {
	private String name;
	private Object[] params = null;
	private Class[] paramTypes = null;
		
	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] objects) {
		params = objects;
	}

	public Class[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(Class[] types) {
		paramTypes = types;
	}

}