/*
 * Created on 2005-10-31
 *
 */
package com.sophia.parser.jparsec;

import jfun.parsec.Parser;
import jfun.parsec.Scanners;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-31
 *
 */
public class Lexers2 {

	/**
	 * Create a lexer that parsers a string literal quoted by open and close,
	 * and then converts it to a String token instance.
	 * @param open the opening character.
	 * @param close the closing character.
	 * @param escape the escaping character.
	 * @return the lexer.
	 */
	public static Parser quoted(final char open, final char close) {
	  return quoted("quoted", open, close, (char)0);
	}

	public static Parser quoted(final char open, final char close, final char escape) {
	  return quoted("quoted", open, close, escape);
	}

	/**
	 * Create a lexer that parsers a string literal quoted by open and close,
	 * and then converts it to a String token instance.
	 * @param name the lexer name.
	 * @param open the opening character.
	 * @param close the closing character.
	 * @param escape the escaping character.
	 * @return the lexer.
	 */
	public static Parser quoted(final String name, final char open, final char close) {
	  return quoted(name, open, close, (char)0);
	}

	public static Parser quoted(final String name, final char open, final char close, final char escape) {
	  return Scanners.lexer(name, Scanners2.quoted(name, open, close, escape), TokenQuoted2.getTokenizer(open, close, escape));
	}

}
