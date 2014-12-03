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
public class Collector implements TokenVisitor {

	private List list = new ArrayList();

	public Collector() {
	}

	public void visit(Token token) {
		list.add(token);
	}

	public Object getResult() {
		return this.list;
	}

}
