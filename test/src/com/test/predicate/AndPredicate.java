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
public class AndPredicate implements Predicate {

	private final Predicate p1;
	private final Predicate p2;

	public AndPredicate(Predicate p1, Predicate p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public boolean is(String s, PredicateContext ctx) {
	  return p1.is(s, ctx) && p2.is(s, ctx);
	}

}
