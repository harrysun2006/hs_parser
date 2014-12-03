%{
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.sophia.parser.Constant;
import com.sophia.parser.ParseContext;
import com.sophia.parser.ParseException;
import com.sophia.parser.Token;
%}

/* YACC Declarations */
%left  OR
%left  AND
%right NOT
%left  LT, GT, EQ, IE, NE, LE, GE
%left  '-' '+'
%left  '*' '/' '%'
%right UMINUS	/* negation--unary minus */
%right UPLUS	/* positiveness--unary minus */
%right '^'		/* exponentiation */
%left  '.'

%token NUMBER STRING NAME RESERVEDWORD

/* Grammar follows */
%%
input: { result = Constant.PENDING; }
 | expr { result = $1; }
 ;

exprs: expr { $$ = ValueHelper.exprs(Constant.NULL, $1); }
 | exprs ',' expr { $$ = ValueHelper.exprs($1, $3); }
 ;

array: '(' ')' { $$ = ValueHelper.array(Constant.NULL); }
 | '[' ']' { $$ = ValueHelper.array(Constant.NULL); }
 | '(' exprs ')' { $$ = ValueHelper.array($2); }
 | '[' exprs ']' { $$ = ValueHelper.array($2); }
 ;

list: '{''}' { $$ = ValueHelper.list(Constant.NULL); }
 | '{' exprs '}' { $$ = ValueHelper.list($2); }
 ;

member: NAME { $$ = ValueHelper.member($1, null); }
 | NAME '(' ')' { $$ = ValueHelper.member($1, new Object[0]); }
 | NAME '(' exprs ')' { $$ = ValueHelper.member($1, $3); }
 ;

expr: NUMBER { $$ = $1; }
 | STRING
 | NAME { $$ = deduceName($1); }
 | expr '+' expr { $$ = ValueHelper.add($1, $3); }
 | expr '-' expr { $$ = ValueHelper.sub($1, $3); }
 | expr '*' expr { $$ = ValueHelper.mul($1, $3); }
 | expr '/' expr { $$ = ValueHelper.div($1, $3); }
 | expr '%' expr { $$ = ValueHelper.mod($1, $3); }
 | '-' expr %prec UMINUS { $$ = ValueHelper.uminus($2); }
 | '+' expr %prec UPLUS { $$ = ValueHelper.uplus($2); }
 | expr '^' expr { $$ = ValueHelper.power($1, $3); }
 | expr LT expr { $$ = ValueHelper.lt($1, $3); }
 | expr GT expr { $$ = ValueHelper.gt($1, $3); }
 | expr LE expr { $$ = ValueHelper.le($1, $3); }
 | expr GE expr { $$ = ValueHelper.ge($1, $3); }
 | expr EQ expr { $$ = ValueHelper.eq($1, $3); }
 | expr NE expr { $$ = ValueHelper.ne($1, $3); }
 | expr IE expr { $$ = ValueHelper.ie($1, $3); }
 | expr AND expr { $$ = ValueHelper.and($1, $3); }
 | expr OR expr { $$ = ValueHelper.or($1, $3); }
 | NOT expr { $$ = ValueHelper.not($2); }
 | '(' expr ')' { $$ = $2; }
 | array
 | list
 | expr '.' member { $$ = ValueHelper.call($1, $3); }
 ;
%%
private static final String[] _operators = new String[]{
	"+", "-", "*", "/", "%", "^",
	"<", ">", "<=", ">=", "==", "!=", "~=", "&&", "||", "!",
	"(", ")", "[", "]", "{", "}", ".",
};
private final static Map _keywords = new Hashtable();
private final static Set operators = new HashSet();

static {
	operators.addAll(Arrays.asList(_operators));
	_keywords.put("null", new Integer(NAME));
	_keywords.put("true", new Integer(NAME));
	_keywords.put("false", new Integer(NAME));
};

private String ins;
private Object result;
private ParseContext context;
private int index;
private int pindex;

public Object evaluate(String s, ParseContext context) throws Exception {
	this.ins = s;
	this.result = null;
	this.index = 0;
	this.pindex = 0;
	this.context = context;
	yyparse();
	return this.result;
}

public Set getReservedWords() {
	return _keywords.keySet();
}

public Set getOperators() {
	return operators;
}

public boolean isReserved(String word) {
	return _keywords.containsKey(word);
}

private void yyerror(String s) {
	Token token = new Token(1, pindex, yytext, new ParseException("parse.syntax.error"));
	notifyTokenVisitors(token);
}

private int yylex() {
	return yylex1();
}

private boolean isQuote(char ch) {
	return (ch == '\'' || ch == '"');
}

private int yylex1() {
	final short NA = 0;
	final short INNUMBER = 1;
	final short INNAME = 2;
	final short INSTRING = 3;
	short state = NA;
	int indot = 0;	// 0: false; 1: true
	int tok = 0;
	char ch, ch2;
	char quote = 0;
	boolean fin = false;
	StringBuffer sb = new StringBuffer();
	String text;
	pindex = index;
	yylval = Constant.PENDING;
	if(yydebug) System.out.print("yylex ");
	while(index < ins.length() && !fin) {
		ch = ins.charAt(index++);
		if(index < ins.length()) ch2 = ins.charAt(index);
		else ch2 = 0;
		sb.append(ch);
		tok = ch;
		switch(state) {
		case NA:
			if(ch == ' ' || ch == '\t' || ch == '\r') {
			} else if(Character.isLetter(ch) || ch == '_') {
				state = INNAME;
			} else if(Character.isDigit(ch)) {
				state = INNUMBER;
			} else if(ch == '.' && indot == 0) {
		    	state = INNUMBER;
		    	indot = 1;
		   	} else if(isQuote(ch)) {
				quote = ch;
				state = INSTRING;
			} else {
				if(ch == '<') {
					if(ch2 == '=') {
						index++;
						sb.append(ch2);
						tok = LE;
					} else {
						tok = LT;
					}
				} else if(ch == '>') {
					if(ch2 == '=') {
						index++;
						sb.append(ch2);
						tok = GE;
					} else {
						tok = GT;
					}
				} else if(ch == '=') {
					if(ch2 == '=') {
						index++;
						sb.append(ch2);
						tok = EQ;
					}
				} else if(ch == '~') {
					if(ch2 == '=') {
						index++;
						sb.append(ch2);
						tok = IE;
					}
				} else if(ch == '!') {
					if(ch2 == '=') {
						index++;
						sb.append(ch2);
						tok = NE;
					} else {
						tok = NOT;
					}
				} else if(ch == '&') {
					if(ch2 == '&') {
						index++;
						sb.append(ch2);
						tok = AND;
					}
				} else if(ch == '|') {
					if(ch2 == '|') {
						index++;
						sb.append(ch2);
						tok = OR;
					}
				}
				fin = true;
			}
			break;
		case INNUMBER:
			if(Character.isDigit(ch)) {
			} else if(ch == '.' && indot == 0) {
				indot = 1;
			} else {
				index--;
				sb.deleteCharAt(sb.length() - 1);
				tok = NUMBER;
				fin = true;
			}
			break;
		case INSTRING: 
			if(ch == quote) {
				if(ch2 == quote) {
					index++;
				} else {
					tok = STRING;
					fin = true;
				}
			} else if(ch == '\n' || ch == '\r') {
				yylval = new ParseException("parse.string.truncation.error");
				pindex = index - 1;
				fin = true;
			} 
			break;
		case INNAME:
			if(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') {
			} else {
				index--;
				sb.deleteCharAt(sb.length() - 1);
				tok = NAME;
				fin = true;
			}
		}
	}
	if(!fin) {
		switch(state){
			case INSTRING:
				yylval = new ParseException("parse.string.uncompleted.error");
				break;
			case INNAME:
				tok = NAME;
				break;
			case INNUMBER:
				tok = NUMBER;
				break;
			case NA:
			default:
				tok = 0;
				break;
		}
	}
	if(yydebug) System.out.println("tok:" + sb.toString());
	yytext = sb.toString();
	text = yytext.trim();
	switch(tok) {
		case NUMBER:
			if(".".equals(yytext)) {
				tok = '.';
				yylval = ".";
			} else {
				if(indot == 1) yylval = Double.valueOf("0" + text);
				else yylval = Long.valueOf(text);
			}
			break;
		case STRING:
			yylval = text.substring(1, text.length() - 1);
			break;
		case NAME:
			yylval = text;
			break;
		default:
			if(!ParseException.class.isInstance(yylval))yylval = text;
			break;
	}
	Token token = new Token(1, pindex, yytext, yylval);
	/*
	if(tok == NAME) {
		token = TokenHelper.name(token, context);
		yylval = token.getValue();
	} 
	*/
	notifyTokenVisitors(token);
	return tok;
}

private Object deduceName(Object name) {
	Object r;
	r = ValueHelper.name(name, context, this);
	Token t = new Token(1, pindex, name.toString(), name);
	notifyTokenVisitors(TokenHelper.name(t, context, this));
	return r;
}