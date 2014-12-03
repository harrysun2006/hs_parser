/*
 * Created on 2005-11-11
 *
 */
package com.sophia.parser;

import java.util.ResourceBundle;
import java.text.MessageFormat;

import com.sophia.parser.util.ResourceHelper;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-11
 *
 */
public class Catcher implements TokenVisitor {

	private final static ResourceBundle rb1 = ResourceHelper.getBundle("resource/langs/parse");
	private static ResourceBundle rb2;

	static {
		try {
			rb2 = ResourceBundle.getBundle("parse");
		} catch(Exception e) {
			rb2 = null;
		}
	}

	private static String getResource(String s, ResourceBundle[] rbs) {
		String r = s;
		int count = (rbs == null) ? 0 : rbs.length;
		for(int i = 0; i < count; i++) {
			if(rbs[i] == null) continue;
			try {
				r = rbs[i].getString(s);
				break;
			} catch(Exception e) {
			}
		}
		return r;
	}

	private static String format(String s) {
		return format(s, null);
	}

	private static String format(String s, Object[] args) {
		String p = getResource(s, new ResourceBundle[]{rb2, rb1});
		try {
			if(args == null) args = new Object[0];
			p = MessageFormat.format(p, args);
		} catch(Exception e) {
			p = s;
		}
		return p;
	}

	public Catcher() {
	}

	public void visit(Token token) {
		ParseException pe = null;
		Object[] info = new Object[]{new Integer(token.getLine()), new Integer(token.getColumn() + 1), token.getText()};
		String message;
		if(token.isError()) {
			message = format("parse.class.error", info);
			pe = new ParseException(message);
		}
		if(ParseException.class.isInstance(token.getValue())) {
			pe = (ParseException)token.getValue();
			message = format(pe.getMessage(), info);
			pe = new ParseException(message, pe.getCause());
		}
		if(pe != null) throw pe;
	}

	public Object getResult() {
		return null;
	}

}
