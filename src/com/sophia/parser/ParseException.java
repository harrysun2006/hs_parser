/*
 * Created on 2005-3-23
 *
 */
package com.sophia.parser;

import com.sophia.parser.exception.NestableRuntimeException;

/**
 * @author harry.sun
 * @mailto: hysun@thorn-bird.com
 * 2005-3-23
 */
public class ParseException extends NestableRuntimeException {

	public ParseException(Throwable root) {
		super(root);
	}

	public ParseException(String s, Throwable root) {
		super(s, root);
	}

	public ParseException(String s) {
		super(s);
	}

}
