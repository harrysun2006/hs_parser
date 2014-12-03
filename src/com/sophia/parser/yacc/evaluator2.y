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
input: { result = Constant.NULL; }
 | expr { result = $1; }
 ;

exprs: expr {
   $$ = TokenHelper.exprs(Constant.NULL, $1); 
   deduce($$, new Token[]{$1});
  }
 | exprs ',' expr {
   $$ = TokenHelper.exprs($1, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 ;

array: '(' ')' {
   $$ = TokenHelper.array(Constant.NULL);
   deduce($$, new Token[]{$1, $2});
  }
 | '[' ']' {
   $$ = TokenHelper.array(Constant.NULL);
   deduce($$, new Token[]{$1, $2});
  }
 | '(' exprs ')' {
   $$ = TokenHelper.array($2);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | '[' exprs ']' {
   $$ = TokenHelper.array($2);
   deduce($$, new Token[]{$1, $2, $3});
  }
 ;

list: '{''}' {
   $$ = TokenHelper.list(Constant.NULL);
   deduce($$, new Token[]{$1, $2});
  }
 | '{' exprs '}' {
   $$ = TokenHelper.list($2);
   deduce($$, new Token[]{$1, $2, $3});
  }
 ;

member: NAME {
   $$ = TokenHelper.member($1, Constant.NULL);
   deduce($$, new Token[]{$1}, true);
  }
 | NAME '(' ')' {
   $$ = TokenHelper.member($1, new Object[0]);
   deduce($$, new Token[]{$1, $2, $3}, true);
  }
 | NAME '(' exprs ')' {
   $$ = TokenHelper.member($1, $3);
   deduce($$, new Token[]{$1, $2, $3, $4}, true);
  }
 ;

expr: NUMBER {
   $$ = TokenHelper.clone($1);
  }
 | STRING
 | NAME {
   $$ = TokenHelper.name($1, context, this);
   notifyTokenVisitors($$);
  }
 | expr '+' expr {
   $$ = TokenHelper.add($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr '-' expr {
   $$ = TokenHelper.sub($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr '*' expr {
   $$ = TokenHelper.mul($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr '/' expr {
   $$ = TokenHelper.div($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr '%' expr {
   $$ = TokenHelper.mod($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | '-' expr %prec UMINUS {
   $$ = TokenHelper.uminus($2);
   notifyExprVisitors(Constant.NULL, $1, $2);
   deduce($$, new Token[]{$1, $2});
  }
 | '+' expr %prec UPLUS {
   $$ = TokenHelper.uplus($2);
   notifyExprVisitors(Constant.NULL, $1, $2);
   deduce($$, new Token[]{$1, $2});
  }
 | expr '^' expr {
   $$ = TokenHelper.power($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr LT expr {
   $$ = TokenHelper.lt($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr GT expr {
   $$ = TokenHelper.gt($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr LE expr {
   $$ = TokenHelper.le($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr GE expr {
   $$ = TokenHelper.ge($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr EQ expr {
   $$ = TokenHelper.eq($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr NE expr {
   $$ = TokenHelper.ne($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr IE expr {
   $$ = TokenHelper.ie($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr AND expr {
   $$ = TokenHelper.and($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr OR expr {
   $$ = TokenHelper.or($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | NOT expr {
   $$ = TokenHelper.not($2);
   notifyExprVisitors(Constant.NULL, $1, $2);
   deduce($$, new Token[]{$1, $2});
  }
 | '(' expr ')' {
   $$ = $2;
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | array
 | list
 | expr '.' member {
   $$ = TokenHelper.call($1, $3);
   notifyExprVisitors($1, $2, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
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
private Token result;
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
	Object value = Constant.PENDING;
	pindex = index;
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
				value = new ParseException("parse.string.truncation.error");
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
				value = new ParseException("parse.string.uncompleted.error");
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
	if(fin && state == NA) {
		while(index < ins.length() && fin) {
			ch = ins.charAt(index++);
			if(ch == ' ' || ch == '\t' || ch == '\r') {
				sb.append(ch);
			} else {
				index--;
				fin = false;
			}
		}
	}
	if(yydebug) System.out.println("tok:" + sb.toString());
	yytext = sb.toString();
	text = yytext.trim();
	switch(tok) {
		case NUMBER:
			if(".".equals(yytext)) {
				tok = '.';
				value = ".";
			} else {
				if(indot == 1) value = Double.valueOf("0" + text);
				else value = Long.valueOf(text);
			}
			break;
		case STRING:
			value = text.substring(1, text.length() - 1);
			break;
		case NAME:
			value = text;
			break;
		default:
			if(!ParseException.class.isInstance(value))value = text;
			break;
	}
	yylval = new Token(1, pindex, yytext, value);
	notifyTokenVisitors(yylval);
	return tok;
}

private void deduce(Token token, Token[] tokens) {
	deduce(token, tokens, false);
}

private void deduce(Token token, Token[] tokens, boolean atomic) {
	int line = tokens[0].getLine();
	int column = tokens[0].getColumn();
	StringBuffer sb = new StringBuffer();
	for(int i = 0; i < tokens.length; i++) {
		sb.append(tokens[i].getText());
		//notifyTokenVisitors(tokens[i]);
	}
	token.setLine(line);
	token.setColumn(column);
	token.setText(sb.toString());
	token.setAtomic(atomic);
	notifyTokenVisitors(token);
}