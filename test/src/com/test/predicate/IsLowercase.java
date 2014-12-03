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
public class IsLowercase implements Predicate {

	public boolean is(String s, PredicateContext ctx) {
		if(s == null) return false;
		return s.toLowerCase().equals(s);
	}

}