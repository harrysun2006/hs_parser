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
public class IsUppercase implements Predicate {

	public boolean is(String s, PredicateContext ctx) {
		if(s == null) return false;
		return s.toUpperCase().equals(s);
	}

}