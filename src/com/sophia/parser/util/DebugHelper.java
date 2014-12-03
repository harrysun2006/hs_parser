/*
 * Created on 2005-10-27
 *
 */
package com.sophia.parser.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-27
 *
 */
public class DebugHelper {

	private DebugHelper() {
	}

	public static String debug(Object r) {
		StringBuffer sb = new StringBuffer();
		if(r == null) {
			sb.append("null");
		} else if(Object[].class.isInstance(r)) {
			Object[] objs = (Object[])r;
			sb.append(debugObjects(objs));
		} else if(Collection.class.isInstance(r)) {
			Collection c = (Collection)r;
			sb.append(debugCollection(c));
		} else if(Enumeration.class.isInstance(r)) {
			Enumeration e = (Enumeration)r;
			sb.append(debugEnumeration(e));
		} else if(Map.class.isInstance(r)) {
			Map map = (Map)r;
			sb.append(debugMap(map));
		} else if(Set.class.isInstance(r)) {
			Set set = (Set)r;
			sb.append(debugSet(set));
		} else {
			sb.append(r);
		}
		return sb.toString();
	}

	private static String debugObjects(Object[] objs) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i = 0; i < objs.length; i++) 
			sb.append(debug(objs[i])).append(", ");
		if(objs.length > 0) sb.delete(sb.length() - 2, sb.length());
		sb.append("]");
		return sb.toString();
	}

	private static String debugCollection(Collection c) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for(Iterator it = c.iterator(); it.hasNext();) 
			sb.append(debug(it.next())).append(", ");
		if(c.size() > 0) sb.delete(sb.length() - 2, sb.length());
		sb.append("}");
		return sb.toString();
	}

	private static String debugEnumeration(Enumeration e) {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		while(e.hasMoreElements()) {
			sb.append(debug(e.nextElement())).append(", ");
		}
		if(sb.length() > 1) sb.delete(sb.length() - 2, sb.length());
		sb.append(")");
		return sb.toString();
	}

	private static String debugMap(Map map) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		Object key;
		Object value;
		for(Iterator it = map.keySet().iterator(); it.hasNext(); ) {
			key = it.next();
			value = map.get(key);
			sb.append(debug(key)).append(" = ").append(debug(value)).append(", ");
		}
		if(map.size() > 0) sb.delete(sb.length() - 2, sb.length());
		sb.append("}");
		return sb.toString();
	}

	private static String debugSet(Set set) {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		for(Iterator it = set.iterator(); it.hasNext();) 
			sb.append(debug(it.next())).append(", ");
		if(set.size() > 0) sb.delete(sb.length() - 2, sb.length());
		sb.append(">");
		return sb.toString();
	}

}
