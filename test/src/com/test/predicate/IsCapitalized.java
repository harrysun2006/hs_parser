/*
 * Created on 2005-10-19
 *
 */
package com.test.predicate;


/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-19
 *
 */
public class IsCapitalized implements Predicate {

	public boolean is(String s, PredicateContext ctx) {
		char c = 0;
		if(s != null && s.length() > 0) c = s.charAt(0);
		return (c >= 'A' && c <= 'Z');
	}

}
