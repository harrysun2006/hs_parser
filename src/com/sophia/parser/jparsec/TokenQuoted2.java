/*****************************************************************************
 * Copyright (C) Zephyr Business Solutions Corp. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
/*
 * Created on 2004-11-15
 *
 * Author Ben Yu
 */
package com.sophia.parser.jparsec;

import jfun.parsec.Tokenizer;

/**
 * represents a string that is quoted by a open and close string.
 * Use this token if the value of open quote and close quote matters to the syntax.
 * @author Ben Yu
 *
 * 2004-11-15
 */
public class TokenQuoted2 implements java.io.Serializable{

	private final static char[] specials = new char[]{
		'\\', '^', '$', '*', '+', '?', '|', '.', 
		'(', ')', '[', ']', '{', '}',
	};

  private final String open;
  private final String close;
  private final String escape;
  private final String oe;
  private final String ce;
  private final String quoted;

	/**
	 * @deprecated use TokenQuoted2(final String open, final String close, final String escape, final String quoted) instead.
	 * @param open the open quote
	 * @param close the close quote
	 * @param quoted the quoted string
	 */
  TokenQuoted2(final String open, final String close, final String quoted) {
  	this(open, close, "", quoted);
  }

  /**
   * @param open the open quote
   * @param close the close quote
   * @param escape the escape string
   * @param quoted the quoted string
   */
  TokenQuoted2(final String open, final String close, final String escape, final String quoted) {
    this.open = open;
    this.close = close;
    this.escape = (escape == null) ? "" : escape;
    this.oe = getRegex(this.escape + this.open);
    this.ce = getRegex(this.escape + this.close);
    this.quoted = quoted;
  }

  public boolean equals(Object obj) {
    if(obj instanceof TokenQuoted2){
      final TokenQuoted2 tq2 = (TokenQuoted2)obj;
      return open.equals(tq2.open) && close.equals(tq2.close)
      && escape.equals(tq2.escape) && quoted.equals(tq2.quoted);
    }
    else return false;
  }

  public int hashCode() {
    return open.hashCode() + escape.hashCode() + quoted.hashCode() + close.hashCode();
  }

	private int indexOf(char ch, char[] cs) {
		if(cs == null) return -1;
		for(int i = 0; i < cs.length; i++) {
			if(ch == cs[i]) return i;
		}
		return -1;
	}

	private String getRegex(String s) {
		if(s == null) return "";
		StringBuffer r = new StringBuffer();
		char ch;
		for(int i = 0; i < s.length(); i++) {
			ch = s.charAt(i);
			if(indexOf(ch, specials) >= 0)r.append('\\');
			r.append(ch);
		}
		return r.toString();
	}

	private String escape() {
		if(oe.equals(ce)) return quoted.replaceAll(oe, open);
		else return quoted.replaceAll(oe, open).replaceAll(ce, close);
	}

  public String toString(){
    return open + escape() + close;
  }

	/**
	 * @deprecated
	 * @param open
	 * @param close
	 * @return
	 */
  public static Tokenizer getTokenizer(final char open, final char close){
		return getTokenizer(open, close, (char)0);
  }

  /**
   * creates a Tokenizer instance that can parse a string quoted by open and close.
   * @param open the open quote
   * @param close the close quote
   * @return the tokenizer.
   */
  public static Tokenizer getTokenizer(final char open, final char close, final char escape){
    return getTokenizer("" + open, "" + close, "" + escape);
  }

  /**
   * creates a Tokenizer instance that can parse a string quoted by open and close.
   * @param open the opening quote
   * @param close the closeing quote
   * @return the tokenizer.
   */

  public static Tokenizer getTokenizer(final String open, final String close, final String escape){
    return new Tokenizer(){
      public Object toToken(final CharSequence cs, final int from, final int len){
        final int start = from + open.length();
        final int end = from + len - close.length();
        return new TokenQuoted2(open, close, escape, cs.subSequence(start, end).toString());
      }
    };
  }

  /**
   * Returns the closing quote.
   * @return the closing quote
   */
  public final String getClose() {
    return close;
  }

  /**
   * Returns the quoted text(escaped).
   * @return the quoted text(escaped)
   */
  public final String getQuoted() {
    return escape();
  }

  /**
   * Returns the opening quote.
   * @return the opening quote
   */
  public final String getOpen() {
    return open;
  }

	/**
	 * Returns the escaping string.
	 * @return the escaping string
	 */
	public final String getEscape() {
		return escape;
	}
}
