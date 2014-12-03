/*
 * Created on 2005-10-31
 *
 */
package com.sophia.parser.jparsec;

import jfun.parsec.Parser;
import jfun.parsec.Parsers;
import jfun.parsec.FromString3;
import jfun.parsec.FromToken;
import jfun.parsec.PositionedToken;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-31
 *
 */
public class Terms2 {

	/**
	 * gets a Parser object to parse TokenQuoted.
	 * @param fc the mapping to map the quoted string to an object returned by the parser.
	 * @return the parser
	 */
	public static Parser quotedWordParser(final FromString3 fc){
	  return quotedWordParser("quotedName", fc);
	}

	/**
	 * gets a Parser object to parse TokenQuoted.
	 * @param name the parser name.
	 * @param fc the mapping to map the quoted string to an object returned by the parser.
	 * @return the parser
	 */
	public static Parser quotedWordParser(final String name, final FromString3 fc){
	  return Parsers.token(name, new FromToken(){
		public Object fromToken(PositionedToken ptok){
		  final Object t = ptok.getToken();
		  if(t instanceof TokenQuoted2){
			final TokenQuoted2 c = (TokenQuoted2)t;
			return fc.fromString3(ptok.getIndex(), ptok.getLength(),
				c.getOpen(), c.getQuoted(), c.getClose());
		  }
		  else return null;
		}
	  });
	}
}
