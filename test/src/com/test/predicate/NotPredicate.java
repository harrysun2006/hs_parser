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
public class NotPredicate implements Predicate {

	private final Predicate p;

	public NotPredicate(Predicate p) {
		this.p = p;
	}

	public boolean is(String s, PredicateContext ctx) {
	  return !p.is(s, ctx);
	}

}
