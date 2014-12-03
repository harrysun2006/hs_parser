/*
 * Created on 2005-11-4
 *
 */
package com.sophia.parser.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.sophia.parser.AbstractParser;
import com.sophia.parser.ParseContext;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-4
 *
 */
public class ParserAdapter extends AbstractParser {

	private static final String[] _operators = new String[] {
		"+", "-", "*", "/", "%", "^",
		"<", ">", "<=", ">=", "==", "!=", "~=", "&&", "||", "!",
		"(", ")", "[", "]", "{", "}", ".",
	};
	private static final String[] _keywords = new String[] {
		"null", "true", "false",
		"new", "import", "return",
		"if", "else", "while", "do", "for", "switch", "case", "default", "break", "continue",
	};

	private final static Set keywords = new HashSet();
	private final static Set operators = new HashSet();

	static {
		operators.addAll(Arrays.asList(_operators));
		keywords.addAll(Arrays.asList(_keywords));
	};

	public Object evaluate(String s, ParseContext context) throws Exception {
		Binding binding = new Binding();
		if(context != null) {
			Set keys = context.keySet();
			String key;
			for(Iterator it = keys.iterator(); it.hasNext(); ) {
				key = (String)it.next();
				binding.setVariable(key, context.get(key));
			}
		}
		GroovyShell shell = new GroovyShell(binding);
		return shell.evaluate(s);
	}

	public Set getReservedWords() {
		return keywords;
	}

	public Set getOperators() {
		return operators;
	}

	public boolean isReserved(String word) {
		return keywords.contains(word);
	}

}
