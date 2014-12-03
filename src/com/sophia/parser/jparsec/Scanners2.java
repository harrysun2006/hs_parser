/*
 * Created on 2005-10-31
 *
 */
package com.sophia.parser.jparsec;

import jfun.parsec.Parser;
import jfun.parsec.Scanners;
import jfun.parsec.pattern.Pattern;
import jfun.parsec.pattern.Patterns;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-31
 *
 */
public class Scanners2 {

	/**
	 * scans a quoted string that is opened by c1 and closed by c2.
	 * @param name the scanner name. 
	 * @param c1 the opening character.
	 * @param c2 the closing character.
	 * @return the scanner.
	 */
	public static Parser quoted(final String name, final char c1, final char c2, final char c3) {
		String regex;
		Pattern pp;
		Parser p;
		String err = "" + c1;
		if(c3 != 0) {
			regex = c1 + "(([^" + c1 + c2 + "]|" + c3 + c1 + "|" + c3 + c2 + ")*)" + c2; 
		} else {
			regex = c1 + "([^" + c1 + c2 + "]*)" + c2;
		}
		pp = Patterns.regex(regex);
		p = Scanners.isPattern(name, pp, err);
		//pp = Patterns.isChar(c1).seq(Patterns.many(CharPredicates.notChar(c2)));
		//p = Scanners.isPattern(name, pp, err).seq(Scanners.isChar(c2));
		return p;
	}

}
