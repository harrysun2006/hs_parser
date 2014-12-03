/*
 * Created on 2005-11-8
 *
 */
package com.sophia.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-8
 *
 */
public abstract class AbstractParser implements Parser {

	private List tokenVisitors = new ArrayList();
	private List exprVisitors = new ArrayList();

	public AbstractParser() {
		this(true);
	}

	public AbstractParser(boolean catchable) {
		if(catchable)this.addVisitor(new Catcher());
	}

	public void addVisitor(Visitor visitor) {
		if(TokenVisitor.class.isInstance(visitor)) tokenVisitors.add(visitor);
		if(ExprVisitor.class.isInstance(visitor)) exprVisitors.add(visitor);
	}

	public void removeVisitor(Visitor visitor) {
		tokenVisitors.remove(visitor);
		exprVisitors.remove(visitor);
	}

	public void clearVisitors(Class clazz) {
		if(TokenVisitor.class.equals(clazz)) tokenVisitors.clear();
		if(ExprVisitor.class.equals(clazz)) exprVisitors.clear();
	}

	protected void notifyTokenVisitors(Token token) {
		TokenVisitor tv;
		for(int i = 0; i < tokenVisitors.size(); i++) {
			tv = (TokenVisitor)tokenVisitors.get(i);
			try {
				tv.visit(token);
			} catch(ParseException pe) {
				throw pe;
			} catch(Exception e) {
			}
		}
	}

	protected void notifyExprVisitors(Token a, Token b, Token c) {
		ExprVisitor ev;
		for(int i = 0; i < exprVisitors.size(); i++) {
			ev = (ExprVisitor)exprVisitors.get(i);
			try {
				ev.visit(a, b, c);
			} catch(Exception e) {
			}
		}
	}
}
