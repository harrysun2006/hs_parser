%{
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.sophia.parser.ParseContext;
import com.sophia.parser.util.DebugHelper;
%}

/* YACC Declarations */
%left  '|'
%left  '&'
%right '!'
%left  '<', '>', '=', '~'
%left  '-' '+'
%left  '*' '/' '%'
%right UMINUS	/* negation--unary minus */
%right UPLUS	/* positiveness--unary minus */
%right '^'		/* exponentiation */
%left  '.'

%token NUMBER STRING NAME ERROR
%token NEW IMPORT RETURN
%token IF ELSE WHILE DO FOR SWITCH CASE DEFAULT BREAK CONTINUE
%token DPLUS DMINUS

/* Grammar follows */
%%
input: statement { this.pindex = this.index; }
 | input statement
 ;

statement: expr ';' { $$ = $1; }
 | set ';'
 | IF '(' expr ')' 
 | ELSE
 | WHILE '(' expr ')'
 | DO
 | FOR '(' set ';' expr ';' set ')'
 | SWITCH '(' NAME ')'
 | CASE expr ':'
 | DEFAULT ':'
 | BREAK ';'
 | CONTINUE ';'
 | '/''/'
 | IMPORT NAME ';'
 | RETURN expr ';' { this.result = $2; }
 | '\n' { this.line++; this.column = 0; }
 ;

set: /* empty */
 | NAME NAME '=' expr { $$ = ValueHelper.set($1, $2, $4, this.context); }
 | NAME '=' expr { $$ = ValueHelper.set(null, $1, $3, this.context); }
 ;

exprs: expr { $$ = ValueHelper.exprs(null, $1); }
 | exprs ',' expr { $$ = ValueHelper.exprs($1, $3); }
 ;

array: '(' ')' { $$ = ValueHelper.array(null); }
 | '[' ']' { $$ = ValueHelper.array(null); }
 | '(' exprs ')' { $$ = ValueHelper.array($2); }
 | '[' exprs ']' { $$ = ValueHelper.array($2); }
 ;

list: '{''}' { $$ = ValueHelper.list(null); }
 | '{' exprs '}' { $$ = ValueHelper.list($2); }
 ;

member: NAME { $$ = $1; }
 | NAME '(' ')' { $$ = ValueHelper.member($1, new Object[0]); }
 | NAME '(' exprs ')' { $$ = ValueHelper.member($1, $3); }
 ;

expr: NUMBER { $$ = $1; }
 | STRING
 | NAME { $$ = ValueHelper.name($$, this.context, this); }
 | expr '+' expr { $$ = ValueHelper.add($1, $3); }
 | expr '-' expr { $$ = ValueHelper.sub($1, $3); }
 | expr '*' expr { $$ = ValueHelper.mul($1, $3); }
 | expr '/' expr { $$ = ValueHelper.div($1, $3); }
 | expr '%' expr { $$ = ValueHelper.mod($1, $3); }
 | '-' expr %prec UMINUS { $$ = ValueHelper.uminus($2); }
 | '+' expr %prec UPLUS { $$ = ValueHelper.uplus($2); }
 | expr '^' expr { $$ = ValueHelper.power($1, $3); }
 | expr '<' expr { $$ = ValueHelper.lt($1, $3); }
 | expr '>' expr { $$ = ValueHelper.gt($1, $3); }
 | expr '<''=' expr { $$ = ValueHelper.le($1, $4); }
 | expr '>''=' expr { $$ = ValueHelper.ge($1, $4); }
 | expr '=''=' expr { $$ = ValueHelper.eq($1, $4); }
 | expr '!''=' expr { $$ = ValueHelper.ne($1, $4); }
 | expr '~''=' expr { $$ = ValueHelper.ie($1, $4); }
 | expr '&''&' expr { $$ = ValueHelper.and($1, $4); }
 | expr '|''|' expr { $$ = ValueHelper.or($1, $4); }
 | '!' expr { $$ = ValueHelper.not($2); }
 | '(' expr ')' { $$ = $2; }
 | array
 | list
 | expr '.' member { $$ = ValueHelper.call($1, $3); }
 | NEW member { $$ = ValueHelper.construct($2); }
 | NAME DPLUS
 | NAME DMINUS
 | DPLUS NAME
 | DMINUS NAME
 | ERROR {}
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
	_keywords.put("new", new Integer(NEW));
	_keywords.put("import", new Integer(IMPORT));
	_keywords.put("return", new Integer(RETURN));
	_keywords.put("if", new Integer(IF));
	_keywords.put("else", new Integer(ELSE));
	_keywords.put("while", new Integer(WHILE));
	_keywords.put("do", new Integer(DO));
	_keywords.put("for", new Integer(FOR));
	_keywords.put("switch", new Integer(SWITCH));
	_keywords.put("case", new Integer(CASE));
	_keywords.put("default", new Integer(DEFAULT));
	_keywords.put("break", new Integer(BREAK));
	_keywords.put("continue", new Integer(CONTINUE));
};

private String ins;
private Object result;
private ParseContext context;
private int index;
private int pindex;
private int line;
private int column;
private int pcolumn;

public Object evaluate(String s, ParseContext context) throws Exception {
	this.ins = s;
	this.result = null;
	this.index = 0;
	this.pindex = 0;
	this.line = 1;
	this.column = 0;
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
	System.err.println("a syntax error occured at line " + this.line + " column " + this.pcolumn + "!" + yytext);
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
	int token = 0;
	char ch, ch2;
	char quote = 0;
	boolean fin = false;
	StringBuffer sb = new StringBuffer();
	pindex = index;
	pcolumn = column;
	while(index < ins.length() && !fin) {
		ch = ins.charAt(index++);
		column++;
		if(index < ins.length()) ch2 = ins.charAt(index);
		else ch2 = 0;
		sb.append(ch);
		token = ch;
		switch(state) {
		case NA:
			if(ch == ' ' || ch == '\t'  || ch == '\r') {
				sb.deleteCharAt(sb.length() - 1);
			} else if(Character.isLetter(ch) || ch == '_') {
				state = INNAME;
			} else if(Character.isDigit(ch) || ( ch == '.' && indot == 0) ) {
				state = INNUMBER;
			} else if(isQuote(ch)) {
				quote = ch;
				state = INSTRING;
			} else if(ch == '+' && ch2 == '+') {
				index++;
				column++;
				sb.append(ch2);
				token = DPLUS;
				fin = true;
			} else if(ch == '-' && ch2 == '-') {
				index++;
				column++;
				sb.append(ch2);
				token = DMINUS;
				fin = true;
			} else if(ch == '{') {
				//TODO: deal with {
				sb.deleteCharAt(sb.length() - 1);
			} else if(ch == '}') {
				//TODO: deal with }
				sb.deleteCharAt(sb.length() - 1);
			} else {
				fin = true;
			}
			break;
		case INNUMBER:
			if(Character.isDigit(ch)) {
			} else if(ch == '.' && indot == 0) {
				indot = 1;
			} else {
				index--;
				column--;
				sb.deleteCharAt(sb.length() - 1);
				token = NUMBER;
				fin = true;
			}
			break;
		case INSTRING: 
			if(ch == quote) {
				if(ch2 == quote) {
					index++;
					column++;
				} else {
					token = STRING;
					fin = true;
				}
			} else if(ch == '\n' || ch == '\r') {
				//TODO: yylval = new ERROR();
				fin = true;
			} 
			break;
		case INNAME:
			if(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') {
			} else {
				index--;
				column--;
				sb.deleteCharAt(sb.length() - 1);
				token = NAME;
				fin = true;
			}
		}
	}
	if(!fin) {
		switch(state){
			case INSTRING:
				token = ERROR;
				//TODO: yylval = new ERROR();
				break;
			case INNAME:
				token = NAME;
				break;
			case INNUMBER:
				token = NUMBER;
				break;
			case NA:
			default:
				token = 0;
				break;
		}
	}
	yytext = sb.toString();
	switch(token) {
		case NUMBER:
			if(".".equals(yytext)) token = '.';
			else yylval = Double.valueOf(yytext);/*this may fail*/
			break;
		case STRING:
			yylval = sb.substring(1, sb.length() - 1);
			break;
		case NAME:
			Object keyword = _keywords.get(yytext);
			if(keyword != null) token = ((Integer)keyword).intValue();
			yylval = yytext;
			break;
		default:
			yylval = yytext;
			break;
	}
	if(yydebug) System.out.println("token: " + token + ", text: " + yytext);
	return token;
}

private void yydebug() {
	if(!yydebug) return;
	System.out.println("stack: " + DebugHelper.debug(valstk));
}