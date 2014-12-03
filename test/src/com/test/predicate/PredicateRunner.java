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
public class PredicateRunner {

	public boolean run(String s, Predicate p) {
	  final PredicateContext ctxt = new PredicateContext();
	  return p.is(s, ctxt);
	}

}
