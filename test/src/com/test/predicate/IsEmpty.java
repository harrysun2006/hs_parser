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
class IsEmpty implements Predicate {

  public boolean is(String s, PredicateContext ctx) {
  	return s.length() == 0;
  }

}
