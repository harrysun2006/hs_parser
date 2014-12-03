/*
 * Created on 2005-11-1
 *
 */
package com.sophia.parser;

import java.util.Set;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-1
 *
 */
public interface Parser {

	public Object evaluate(String s, ParseContext context) throws Exception;

	public Set getReservedWords();

	public Set getOperators();

	public void addVisitor(Visitor visitor);

	public void removeVisitor(Visitor visitor);

	public void clearVisitors(Class clazz);

	public boolean isReserved(String word);

}
