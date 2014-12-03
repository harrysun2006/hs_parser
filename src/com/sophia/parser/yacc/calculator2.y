%{
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.sophia.parser.ParseContext;
import com.sophia.parser.ParseException;
import com.sophia.parser.Token;
%}

/* YACC Declarations */
%left  '-' '+'
%left  '*' '/' '%'
%right UMINUS	/* negation--unary minus */
%right UPLUS	/* positiveness--unary minus */
%right '^'		/* exponentiation */

%token NUMBER ERROR

/* Grammar follows */
%%
input: { result = null; }
 | expr { result = $1.getValue(); }
 ;

expr: NUMBER {
   $$ = TokenHelper.clone($1);
   deduce($$, new Token[]{$1});
  }
 | expr '+' expr {
   $$ = TokenHelper.add($1, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr '-' expr {
   $$ = TokenHelper.sub($1, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr '*' expr {
   $$ = TokenHelper.mul($1, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr '/' expr {
   $$ = TokenHelper.div($1, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | expr '%' expr {
   $$ = TokenHelper.mod($1, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | '-' expr %prec UMINUS {
   $$ = TokenHelper.uminus($2);
   deduce($$, new Token[]{$1, $2});
  }
 | '+' expr %prec UPLUS {
   $$ = TokenHelper.uplus($2);
   deduce($$, new Token[]{$1, $2});
  }
 | expr '^' expr {
   $$ = TokenHelper.power($1, $3);
   deduce($$, new Token[]{$1, $2, $3});
  }
 | '(' expr ')' {
   $$ = $2;
   deduce($$, new Token[]{$1, $2, $3});
  }
 ;
%%
private final static String[] _operators = new String[] {
	"+", "-", "*", "/", "%", "^",
};
private final static Map _keywords = new Hashtable();
private final static Set operators = new HashSet();

static {
	operators.addAll(Arrays.asList(_operators));
	_keywords.put("null", new Integer(ERROR));
	_keywords.put("true", new Integer(ERROR));
	_keywords.put("false", new Integer(ERROR));
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
	return result;
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

private int yylex1() {
	final short NA = 0;
	final short INNUMBER = 1;
	final short INNAME = 2;
	final short INSTRING = 3;
	short state = NA;
	int indot = 0;	// 0: false; 1: true
	int tok = 0;
	char ch;
	boolean fin = false;
	StringBuffer sb = new StringBuffer();
	String text;
	Object value;
	pindex = index;
	if(yydebug) System.out.print("yylex ");
	while(index < ins.length() && !fin) {
		ch = ins.charAt(index++);
		sb.append(ch);
		tok = ch;
		switch(state) {
	   	case NA:
		   	if(ch == ' ' || ch == '\t'  || ch == '\r') {
		   	} else if(Character.isDigit(ch)) {
		      state = INNUMBER;
		    } else if(ch == '.' && indot == 0) {
		    	state = INNUMBER;
		    	indot = 1;
		   	} else  {
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
		}
	}
	if(!fin) {
		switch(state){
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
			if(indot == 1) value = Double.valueOf("0" + text);
			else value = Long.valueOf(text);
			break;
		default:
			value = text;
			break;
	}
	yylval = new Token(1, pindex, yytext, value);
	notifyTokenVisitors(yylval);
	return tok;
}

private void deduce(Token token, Token[] tokens) {
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
	token.setAtomic(false);
	notifyTokenVisitors(token);
}
