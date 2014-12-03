/*
 * Created on 2005-11-8
 *
 */
package com.sophia.parser.yacc;

import com.sophia.parser.Constant;
import com.sophia.parser.Member;
import com.sophia.parser.ParseContext;
import com.sophia.parser.Parser;
import com.sophia.parser.Token;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-8
 *
 */
class TokenHelper {

	final static Token clone(Token a) {
		Token token = new Token();
		token.setClazz(a.getClazz());
		token.setValue(a.getValue());
		token.setLine(a.getLine());
		token.setColumn(a.getColumn());
		token.setText(a.getText());
		return token;
	}

	final static Token uminus(Token a) {
		Token token = new Token();
		Class clazz = ClassHelper.uminus(a.getClazz());
		Object value = ValueHelper.uminus(a.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token uplus(Token a) {
		Token token = new Token();
		Class clazz = ClassHelper.uplus(a.getClazz());
		Object value = ValueHelper.uplus(a.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token add(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.add(a.getClazz(), b.getClazz());
		Object value = ValueHelper.add(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token sub(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.sub(a.getClazz(), b.getClazz());
		Object value = ValueHelper.sub(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token; 
	}

	final static Token mul(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.mul(a.getClazz(), b.getClazz());
		Object value = ValueHelper.mul(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token div(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.div(a.getClazz(), b.getClazz());
		Object value = ValueHelper.div(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token mod(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.mod(a.getClazz(), b.getClazz());
		Object value = ValueHelper.mod(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token power(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.power(a.getClazz(), b.getClazz());
		Object value = ValueHelper.power(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token lt(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.lt(a.getClazz(), b.getClazz());
		Object value = ValueHelper.lt(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token gt(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.gt(a.getClazz(), b.getClazz());
		Object value = ValueHelper.gt(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token le(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.le(a.getClazz(), b.getClazz());
		Object value = ValueHelper.le(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token ge(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.ge(a.getClazz(), b.getClazz());
		Object value = ValueHelper.ge(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token eq(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.eq(a.getClazz(), b.getClazz());
		Object value = ValueHelper.eq(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token ne(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.ne(a.getClazz(), b.getClazz());
		Object value = ValueHelper.ne(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token ie(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.ie(a.getClazz(), b.getClazz());
		Object value = ValueHelper.ie(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token and(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.and(a.getClazz(), b.getClazz());
		Object value = ValueHelper.and(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token or(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.or(a.getClazz(), b.getClazz());
		Object value = ValueHelper.or(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token not(Token a) {
		Token token = new Token();
		Class clazz = ClassHelper.not(a.getClazz());
		Object value = ValueHelper.not(a.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token exprs(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.exprs(a.getClazz(), b.getClazz());
		Object value = ValueHelper.exprs(a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		token.setSubClasses(a.getSubClasses());
		token.addSubClass(b.getClazz());
		return token;
	}

	final static Token array(Token a) {
		Token token = new Token();
		Class clazz = ClassHelper.array(a.getClazz());
		Object value = ValueHelper.array(a.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		token.setSubClasses(a.getSubClasses());
		return token;
	}

	final static Token list(Token a) {
		Token token = new Token();
		Class clazz = ClassHelper.list(a.getClazz());
		Object value = ValueHelper.list(a.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		token.setSubClasses(a.getSubClasses());
		return token;
	}

	/**
	 * a: name
	 * b: null, Object[0] or Token
	 * @param a
	 * @param b
	 * @return
	 */
	final static Token member(Token a, Object b) {
		Token token = new Token();
		Object bv = null;
		Class bc = null;
		if(b == null) bc = null;
		else if(Token.class.isInstance(b)) bc = ((Token)b).getClazz();
		else bc = b.getClass();
		if(b == null) bv = null;
		else if(Token.class.isInstance(b)) bv = ((Token)b).getValue();
		else bv = b;
		Class clazz = ClassHelper.member(a.getClazz(), bc);
		Object value = ValueHelper.member(a.getValue(), bv);
		token.setClazz(clazz);
		token.setValue(value);
		if(Member.class.isInstance(value) && Token.class.isInstance(b)) {
			Member m = (Member)value;
			Token t = (Token)b;
			if(t.getClazz() == null && t.getValue() == null) m.setParamTypes(null);
			else m.setParamTypes(t.getSubClasses());
		}
		return token;
	}

	/**
	 * a: expr
	 * b: token(value is member)
	 * @param a
	 * @param b
	 * @return
	 */
	final static Token call(Token a, Token b) {
		Token token = new Token();
		Class clazz = ClassHelper.call(a.getClazz(), b.getValue());
		Object value = ValueHelper.call(a.getClazz(), a.getValue(), b.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token construct(Token a) {
		Token token = new Token();
		Class clazz = ClassHelper.construct(a.getValue());
		Object value = ValueHelper.construct(a.getValue());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	final static Token name(Token a, ParseContext context, Parser parser) {
		Token token = new Token();
		Class clazz = ClassHelper.name(a.getValue(), context);
		Object value = ValueHelper.name(a.getValue(), context, parser);
		if(Class.class.isInstance(value)) value = Constant.PENDING;
		token.setLine(a.getLine());
		token.setColumn(a.getColumn());
		token.setText(a.getText());
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

	/**
	 * Date b = new Date();
	 * d = new Double(9.9);
	 * int i = 3;
	 * @param a the class name
	 * @param b the object name
	 * @param c the object value
	 */
	final static Token set(Token a, Token b, Token c, ParseContext context) {
		Token token = new Token();
		Class clazz = ClassHelper.set(a.getValue(), c.getValue());
		Object value = ValueHelper.set(a.getValue(), b.getValue(), c.getValue(), context);
		token.setClazz(clazz);
		token.setValue(value);
		return token;
	}

}
