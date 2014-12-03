/*
 * Created on 2005-10-19
 *
 */
package com.sophia.parser.jparsec;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jfun.parsec.Expressions;
import jfun.parsec.FromString;
import jfun.parsec.Lexers;
import jfun.parsec.Map;
import jfun.parsec.Map2;
import jfun.parsec.OperatorTable;
import jfun.parsec.Parser;
import jfun.parsec.ParserEval;
import jfun.parsec.Parsers;
import jfun.parsec.Scanners;
import jfun.parsec.Terms;

import com.sophia.parser.ParseContext;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-10-19
 *
 */
public class Calculator extends com.sophia.parser.AbstractParser {

	private final Parser parser;
	private ParseContext context;

	private static final String[] _empty = new String[]{};
	private static final String[] _operators = new String[] {
		"+", "-", "*", "/", "%", "^",
		"(", ")",
	};
	private static final String[] _keywords = new String[]{
		"true", "false", "null",
	};

	private static final Terms operators = Terms.getOperators(_operators);
	private static final Terms keywords = Terms.getCaseInsensitive(_empty, _keywords);

	private static final Parser lazy_expr =
		Parsers.lazy("lazy_expr", new ParserEval() {
		public Parser eval() {
			return expr();
		}
	});

	private static final FromString lexer1 = new FromString() {
		public Object fromString(int from, int len, String s) {
			return new Double(Double.parseDouble(s));
		}
	};

	private static final Parser number = Terms.decimalParser(lexer1);

	private static final Parser terms = Parsers.plus(
		Parsers.between(
			operators.getParser("("),
			operators.getParser(")"),
			lazy_expr
		),
		number
	);

	public Calculator() {
		this.parser = expr().seq(Parsers.eof("\n"));
	}

	private static Parser op(final String n, final Map2 m2) {
		return operators.getParser(n).seq(Parsers.retn(m2));
	}

	private static Parser op(final String n, final Map m) {
		return operators.getParser(n).seq(Parsers.retn(m));
	}

	private static Parser expr_lexer() {
		return Scanners.lexeme(
			Scanners.javaDelimiter(),
			Parsers.plus(Lexers.decimal(), operators.getLexer()));
	}

	private static Parser expr() {
		final OperatorTable ops = new OperatorTable();
		ops
			.infixr(op("^", Maps.power), 110)
			.prefix(op("+", Maps.id), 100)
			.prefix(op("-", Maps.negate), 100)
			.infixl(op("*", Maps.mul), 90)
			.infixl(op("/", Maps.div), 90)
			.infixl(op("%", Maps.mod), 90)
			.infixl(op("+", Maps.add), 80)
			.infixl(op("-", Maps.sub), 80);
		return Expressions.buildExpressionParser("expr", terms, ops);
	}

	private final static Set operatorsSet = new HashSet();
	private final static Set reservedWords = new HashSet();
	static {
		operatorsSet.addAll(Arrays.asList(_operators));
		reservedWords.addAll(Arrays.asList(_keywords));
	}

	private static Object parse(final String src, final Parser lexer, final Parser p){
	  return Parsers.runParser(src, Parsers.parseTokens(lexer, p, "CalcParser"), "CalcParser");
	}

	public Object evaluate(String s, ParseContext context) throws Exception {
		return parse(s, expr_lexer(), this.parser);
	}

	public Set getReservedWords() {
		return reservedWords;
	}

	public Set getOperators() {
		return operatorsSet;
	}

	public boolean isReserved(String word) {
		return reservedWords.contains(word);
	}
}
