/*
 * Created on 2005-11-1
 *
 */
package com.sophia.parser;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-1
 *
 */
public class Variable {

	private String name;
	private Class formalClass;
	private Object value;

	public Variable(String name, Object value) {
		this(name, value, null);
	}

	public Variable(String name, Object value, Class formalClass) {
		this.name = name;
		this.value = value;
		this.formalClass = formalClass;
		if(formalClass == null && value != null) this.formalClass = value.getClass();
	}

	public Class getActualClass() {
		return (value == null) ? this.formalClass : value.getClass();
	}

	public Class getFormalClass() {
		return formalClass;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void castTo(Class clazz) {
		
	}

}
