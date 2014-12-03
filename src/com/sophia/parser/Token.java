/*
 * Created on 2005-11-7
 *
 */
package com.sophia.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-7
 *
 */
public class Token {

	private int line;
	private int column;
	private String text;
	private Class clazz;
	private Object value;
	private List subClasses = new ArrayList();
	private boolean atomic = true;

	public Token() {
		this(0, 0, "", null, null);
	}

	public Token(int line, int column, String text, Object value) {
		this(line, column, text, value, null);
	}

	public Token(int line, int column, String text, Object value, Class clazz) {
		this.line = line;
		this.column = column;
		this.text = text;
		this.value = value;
		if(clazz == null) {
			if(value == null || value == Constant.PENDING) this.clazz = null;
			else this.clazz = value.getClass();
		} else {
			this.clazz = clazz;
		}
	}

	public int getLine() {
		return line;
	}

	public void setLine(int i) {
		line = i;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int i) {
		column = i;
	}

	public String getText() {
		return text;
	}

	public void setText(String s) {
		text = s;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class c) {
		clazz = c;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object o) {
		value = o;
	}

	public void addSubClass(Class clazz) {
		subClasses.add(clazz);
	}

	public Class[] getSubClasses() {
		if(subClasses == null) return null;
		else {
			int size = subClasses.size();
			Class[] classes = new Class[size];
			for(int i = 0; i < size; i++) classes[i] = (Class)subClasses.get(i);
			return classes;
		}
	}

	public void setSubClasses(Class[] classes) {
		if(classes != null) {
			subClasses = new ArrayList();
			for(int i = 0; i < classes.length; i++) subClasses.add(classes[i]);
		}
	}

	public boolean isAtomic() {
		return atomic;
	}

	public void setAtomic(boolean b) {
		atomic = b;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(").append(line).append(", ").append(column).append(") ");
		sb.append(text).append(" ");
		sb.append(atomic).append(" ");
		sb.append(clazz).append(" ");
		sb.append(value);
		return sb.toString();
	}

	public boolean isError() {
		return clazz == Constant.ERROR;
	}

	public boolean isPending() {
		return value == Constant.PENDING;
	}

}
