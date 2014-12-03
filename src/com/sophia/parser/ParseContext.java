/*
 * Created on 2005-10-20
 *
 */
package com.sophia.parser;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-20
 *
 */
public class ParseContext {

	private Map context = new Hashtable();
	private final static Map internalCtxt = new Hashtable();
	
	static {
		internalCtxt.put("Boolean", Boolean.class);
		internalCtxt.put("Byte", Byte.class);
		internalCtxt.put("Character", Character.class);
		internalCtxt.put("Double", Double.class);
		internalCtxt.put("Float", Float.class);
		internalCtxt.put("Integer", Integer.class);
		internalCtxt.put("Long", Long.class);
		internalCtxt.put("Short", Short.class);
		internalCtxt.put("Void", Void.class);
		internalCtxt.put("String", String.class);
		internalCtxt.put("Class", Class.class);
		internalCtxt.put("Runtime", Runtime.class);
		internalCtxt.put("System", System.class);
		internalCtxt.put("Math", Math.class);
	}

	public ParseContext() {
	}

	public void put(String name, Object value) throws IllegalArgumentException {
		if(!isValidName(name)) throw new IllegalArgumentException("value name is NOT valid, should be in pattern: [a-zA-Z_]\\w*!");
		//if(parser != null && parser.isReserved(name)) throw new IllegalArgumentException("can NOT use reserved word as value name!");
		Variable var;
		if(!Variable.class.isInstance(value)) {
			var = new Variable(name, value);
		} else {
			var = (Variable)value;
		}
		context.put(name, var);
	}

	public Object get(String name) {
		Object value = context.get(name);
		Variable var;
		if(Variable.class.isInstance(value)) {
			var = (Variable)value;
			value = var.getValue();
		}
		return value;
	}

	public void clear() {
		context.clear();
		context.putAll(internalCtxt);
	}

	public boolean containsKey(String name) {
		return context.containsKey(name);
	}

	public boolean containsValue(Object value) {
		return context.containsValue(value);
	}

	public boolean isEmpty() {
		return context.isEmpty();
	}

	public Object remove(String name) {
		Object r = context.remove(name);
		if(internalCtxt.containsKey(name)) context.put(name, internalCtxt.get(name));
		return r;
	}

	public int size(){
		return context.size();
	}

	public Collection values() {
		return context.values();
	}

	public Set keySet() {
		return context.keySet();
	}

	public void putAll(ParseContext other) {
		if(other == null) return;
		Set keys = other.keySet();
		String name;
		if(keys != null) {
			for(Iterator it = keys.iterator(); it.hasNext(); ) {
				name = it.next().toString();
				context.put(name, other.get(name));
			}
		}
	}

	public static boolean isValidName(String name) {
		String regex = "[a-zA-Z_]\\w*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(name);
		return m.matches();
	}

	public static ParseContext valueOf(Map map) {
		ParseContext context = new ParseContext();
		Object key;
		for(Iterator it = map.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			context.put(key.toString(), map.get(key));
		}
		return context;
	}
}