/*
 * Created on 2005-11-2
 *
 */
package com.sophia.parser.yacc;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-2
 *
 */
public class Context {

	public final static int MAX_NESTLEVEL = 50;
	private IfClause[] ifs = new IfClause[MAX_NESTLEVEL];
	private int level = 0;

	public void inif(int index) {
		inif(index, true);
	}

	public void inif(int index, boolean result) {
		IfClause clause = new IfClause(index, result); 
		ifs[level++] = clause;
	}

	public void outif() {
		level--;
	}

	public int getIfLevel() {
		return level;
	}

	public int getIfIndex() {
		return ifs[level].index;
	}

	public boolean getIfResult() {
		return ifs[level].result;
	}

	private final static class IfClause {
		private int index;
		private boolean result;

		public IfClause(int index, boolean result) {
			this.index = index;
			this.result = result;
		}

	}
}
