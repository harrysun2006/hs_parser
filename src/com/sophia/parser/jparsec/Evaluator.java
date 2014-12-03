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
import jfun.parsec.FromString3;
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
public class Evaluator extends com.sophia.parser.AbstractParser {

	private final Parser parser;
	private ParseContext context;

	private static final String[] _empty = new String[]{};
	private static final String[] _operators = new String[]{
		"+", "-", "*", "/", "%", "^",
		"<", ">", "<=", ">=", "==", "!=", "~=", "&&", "||", "!",
		"(", ")", "[", "]", "{", "}", ".", ",",
	};
	private static final String[] _keywords = new String[]{
		"true", "false", "null",
	};

	private static final Terms operators = Terms.getOperators(_operators);
	private static final Terms keywords = Terms.getCaseInsensitive(_empty, _keywords);

	private static final FromString lexer1 = new FromString() {
		public Object fromString(int from, int len, String s) {
			return new Double(Double.parseDouble(s));
		}
	};

	private static final FromString3 lexer2 = new FromString3() {
		public Object fromString3(int from, int len, String s1, String s2, String s3) {
			return s2;
		}
	};

	private final FromString lexer3 = new FromString() {
		public Object fromString(int from, int len, String s){
			  return s;
		}
	};

	private static final Parser number = Terms.decimalParser(lexer1);

	private static final Parser string = Terms2.quotedWordParser(lexer2);

	private final Parser name = Terms.wordParser(lexer3);

	private final Map variable = new Map() {
		public Object map(Object o) {
			Object r = o.toString();
			if(String.class.isInstance(o)) {
				String name = (String)o;
				if("true".equalsIgnoreCase(name)) {
					r = new Boolean(true);
				} else if("false".equalsIgnoreCase(name)) {
					r = new Boolean(false);
				} else if(context != null) {
					r = context.get(name);
					if(r == null) r = name;
				}
			}
			return r;
		}
	};

	private final Parser lazy_expr =
		Parsers.lazy("lazy_expr", new ParserEval() {
			public Parser eval() {
				return expr();
			}
		}
	);

	private static final Parser comma = operators.getParser(",");

	private Parser parameters =
		Parsers.sepBy(Object.class, comma, lazy_expr);

	private Parser array =
		Parsers.plus(
			Parsers.between(operators.getParser("("), operators.getParser(")"), parameters),
			Parsers.between(operators.getParser("["), operators.getParser("]"), parameters)
		);

	private Parser list = Parsers.map3(operators.getParser("{"), parameters, operators.getParser("}"), Maps.list);

	private final Parser terms = Parsers.longest(
		new Parser[] {
			number,
			string,
			Parsers.between(
				operators.getParser("("),
				operators.getParser(")"),
				lazy_expr
			),
			array,
			list,
			Parsers.map("variable", name, variable),
			Parsers.map2(name, array, Maps.member),
		}
	);

	public Evaluator() {
		this.parser = expr().seq(Parsers.eof("\n"));
	}

	private static Parser op(final String n, final Map m) {
		return operators.getParser(n).seq(Parsers.retn(m));
	}

	private static Parser op(final String n, final Map2 m2) {
		return operators.getParser(n).seq(Parsers.retn(m2));
	}

	private static Parser expr_lexer() {
		return Scanners.lexeme(
			Scanners.javaDelimiter(),
			Parsers.sum(
				new Parser[] {
					Lexers.word(), 
					Lexers.decimal(),
					//Lexers.quoted('\'', '\''),
					Lexers2.quoted('\'', '\'', '\''),
					Lexers2.quoted('"', '"', '"'),
					operators.getLexer(), 
					keywords.getLexer(),
				}
			)
		);
	}

	private Parser expr() {
		final OperatorTable ops = new OperatorTable();
		ops
			.infixl(op(".", Maps.call), 140)
			.infixr(op("^", Maps.power), 120)
			.prefix(op("+", Maps.id), 100)
			.prefix(op("-", Maps.negate), 100)
			.infixl(op("*", Maps.mul), 90)
			.infixl(op("/", Maps.div), 90)
			.infixl(op("%", Maps.mod), 90)
			.infixl(op("+", Maps.add), 80)
			.infixl(op("-", Maps.sub), 80)
			.infixl(op("<", Maps.lt), 70)
			.infixl(op(">", Maps.gt), 70)
			.infixl(op("<=", Maps.le), 70)
			.infixl(op(">=", Maps.ge), 70)
			.infixl(op("==", Maps.eq), 70)
			.infixl(op("!=", Maps.ne), 70)
			.infixl(op("~=", Maps.ie), 70)
			.prefix(op("!", Maps.not), 60)
			.infixl(op("&&", Maps.and), 50)
			.infixl(op("||", Maps.or), 40);
		return Expressions.buildExpressionParser("expr", terms, ops);
	}

	private final static Set operatorsSet = new HashSet();
	private final static Set reservedWords = new HashSet();
	static {
		operatorsSet.addAll(Arrays.asList(_operators));
		reservedWords.addAll(Arrays.asList(_keywords));
	}

	private static Object parse(final String src, final Parser lexer, final Parser p){
	  return Parsers.runParser(src, Parsers.parseTokens(lexer, p, "EvalParser"), "EvalParser");
	}

	public Object evaluate(String s, ParseContext context) throws Exception {
		this.context = context;
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
