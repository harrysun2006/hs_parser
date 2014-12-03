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
public interface Predicate {

	public boolean is(String s, PredicateContext ctx);

}
