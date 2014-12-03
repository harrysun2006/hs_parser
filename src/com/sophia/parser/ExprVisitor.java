/*
 * Created on 2005-11-24
 *
 */
package com.sophia.parser;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-24
 *
 */
public interface ExprVisitor extends Visitor {

	public void visit(Token a, Token b, Token c);

}
