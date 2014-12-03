/*
 * Created on 2005-11-8
 *
 */
package com.sophia.parser;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-8
 *
 */
public interface TokenVisitor extends Visitor {

	public void visit(Token token);

}
