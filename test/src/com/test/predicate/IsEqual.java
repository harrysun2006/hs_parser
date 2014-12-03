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
public class IsEqual implements Predicate {

	private final String v;

	public IsEqual(String v) {
		this.v = v;
	}

	public boolean is(String s, PredicateContext ctx) {
		return s != null && s.equals(v);
	}

}