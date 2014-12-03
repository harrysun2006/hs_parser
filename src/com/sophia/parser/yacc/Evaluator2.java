//### This file created by BYACC 1.8(/Java extension  1.11)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package com.sophia.parser.yacc;



//#line 2 "evaluator2.y"
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.sophia.parser.Constant;
import com.sophia.parser.ParseContext;
import com.sophia.parser.ParseException;
import com.sophia.parser.Token;
//#line 28 "Evaluator2.java"




public class Evaluator2
             extends com.sophia.parser.AbstractParser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:Token
String   yytext;//user variable to return contextual strings
Token yyval; //used to return semantic vals from action routines
Token yylval;//the 'lval' (result) I got from yylex()
Token valstk[] = new Token[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new Token();
  yylval=new Token();
  valptr=-1;
}
final void val_push(Token val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    Token[] newstack = new Token[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final Token val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final Token val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short OR=257;
public final static short AND=258;
public final static short NOT=259;
public final static short LT=260;
public final static short GT=261;
public final static short EQ=262;
public final static short IE=263;
public final static short NE=264;
public final static short LE=265;
public final static short GE=266;
public final static short UMINUS=267;
public final static short UPLUS=268;
public final static short NUMBER=269;
public final static short STRING=270;
public final static short NAME=271;
public final static short RESERVEDWORD=272;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    2,    2,    3,    3,    3,    3,    4,    4,
    5,    5,    5,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,
};
final static short yylen[] = {                            2,
    0,    1,    1,    3,    2,    2,    3,    3,    2,    3,
    1,    3,    4,    1,    1,    1,    3,    3,    3,    3,
    3,    2,    2,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    2,    3,    1,    1,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   14,   15,   16,    0,    0,    0,    0,
    0,   36,   37,    0,    0,    0,    5,    0,    0,    6,
    0,    0,    9,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   35,    0,    7,    8,   10,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   38,    0,    0,   12,    0,   13,
};
final static short yydgoto[] = {                         10,
   21,   19,   12,   13,   62,
};
final static short yysindex[] = {                       479,
  479,  479,  479,    0,    0,    0,  446,  450,  459,    0,
  502,    0,    0,  561,  -33,  -33,    0,  534,  -30,    0,
  502,  -11,    0,  -42,  479,  479,  479,  479,  479,  479,
  479,  479,  479,  479,  479,  479,  479,  479,  479, -267,
    0,  479,    0,    0,    0,  548,  561,  -21,  -21,  -21,
  -21,  -21,  -21,  -21,   -6,   -6,  -33,  -33,  -33,  -33,
  -23,    0,  502,  465,    0,  -26,    0,
};
final static short yyrindex[] = {                         5,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   10,    0,    0,   62,   12,   23,    0,  -25,    0,    0,
  -41,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  408,  404,  157,  178,  197,
  211,  302,  357,  387,   69,  120,   34,   87,   98,  109,
    1,    0,  -35,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  656,   -1,    0,    0,    0,
};
final static int YYTABLESIZE=827;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          3,
   11,   42,    3,   61,    1,    4,   22,   24,    4,    2,
   43,   22,   40,   42,   67,   38,   64,   42,    3,    0,
   36,   35,   23,   34,   40,   37,    0,    0,    0,    0,
   38,    0,   42,   19,    0,   36,    0,   11,    0,   40,
   37,   11,   11,   11,   11,   11,   11,   11,   22,    0,
    0,    3,   22,   22,   22,   22,   22,    4,   22,   23,
   39,   34,   66,   23,   23,   23,   23,   23,   18,   23,
   19,    0,   39,    0,   19,   19,   19,   19,   19,    0,
   19,   44,   45,    3,    0,    0,   20,   39,    0,    4,
    0,    0,    0,   11,   11,    0,    0,   21,    0,    0,
    0,    0,   34,    0,   22,   34,    0,    0,   24,   18,
    0,   18,   18,   18,    0,   23,    0,    0,    0,   17,
    0,    0,    0,   20,    0,   11,   19,   20,   20,   20,
   20,   20,    0,   20,   21,    0,   22,    0,   21,   21,
   21,   21,   21,    0,   21,   24,    0,   23,    0,   24,
   24,   24,   24,   24,   34,   24,   25,    0,   19,    0,
   17,   18,   17,   17,   17,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   26,    0,   20,
    0,    0,    0,    0,    0,    0,   34,    0,    0,    0,
   21,    0,    0,   18,    0,    0,   29,   25,    0,    0,
   25,   24,    0,    0,    0,    0,    0,    0,    0,    0,
   31,   20,   17,    0,    0,    0,    0,    0,   26,    0,
    0,   26,   21,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   24,    0,    0,    0,   29,    0,    0,
   29,    0,    0,    0,   17,    0,    0,    0,    0,   25,
    0,   31,    0,    0,   31,    0,    0,   11,   11,    0,
   11,   11,   11,   11,   11,   11,   11,    0,   22,   22,
   26,   22,   22,   22,   22,   22,   22,   22,    0,   23,
   23,   25,   23,   23,   23,   23,   23,   23,   23,   29,
   19,   19,    0,   19,   19,   19,   19,   19,   19,   19,
    0,   30,   26,   31,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   34,   34,
    0,   29,    0,    0,    0,   18,   18,    0,   18,   18,
   18,   18,   18,   18,   18,   31,    0,    0,    0,    0,
    0,    0,   30,   20,   20,   30,   20,   20,   20,   20,
   20,   20,   20,    0,   21,   21,   27,   21,   21,   21,
   21,   21,   21,   21,    0,   24,   24,    0,   24,   24,
   24,   24,   24,   24,   24,    0,   17,   17,    0,   17,
   17,   17,   17,   17,   17,   17,   28,    0,    0,    0,
    0,    0,    0,    0,   30,    0,    0,   27,    0,    0,
   27,    0,    0,   32,    0,    0,    0,   33,    0,    0,
    0,    0,    0,   25,   25,    0,   25,   25,   25,   25,
   25,   25,   25,    0,    0,    0,   30,   28,    0,    0,
   28,    0,    0,    0,   26,   26,    0,   26,   26,   26,
   26,   26,   26,   26,   32,    0,    0,   32,   33,   27,
    0,   33,    0,   29,   29,    0,   29,   29,   29,   29,
   29,   29,   29,    0,    0,    0,    0,   31,   31,    0,
   31,   31,   31,   31,   31,   31,   31,    0,    0,   28,
    0,   27,    0,    0,    0,    7,   17,    0,    3,    7,
    2,    0,    3,    0,    2,    0,   32,    0,    7,    0,
   33,    3,    0,    2,    7,   65,    0,    3,    0,    2,
    0,   28,    0,    0,    0,    0,    0,    0,    7,    0,
    0,    3,    0,    2,    0,    0,    0,    0,   32,    0,
    0,    0,   33,    0,    0,    0,    8,    0,   38,    0,
    8,    0,   20,   36,   35,    0,   34,   40,   37,    8,
    0,    0,    0,    0,    0,    8,    0,    0,   30,   30,
    0,   30,   30,   30,   30,   30,   30,   30,    9,    8,
   38,    0,    9,    0,   41,   36,   35,    0,   34,   40,
   37,    9,    0,   23,   38,    0,    0,    9,    0,   36,
   35,    0,   34,   40,   37,   39,    0,   38,    0,    0,
    0,    9,   36,   35,    0,   34,   40,   37,    0,    0,
    0,    0,    0,   27,   27,    0,   27,   27,   27,   27,
   27,   27,   27,    0,    0,    0,    0,   39,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   39,    0,   28,   28,    0,   28,   28,   28,   28,
   28,   28,   28,    0,   39,   11,   14,   15,   16,    0,
   32,   32,   18,    0,   33,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   46,   47,   48,   49,   50,   51,   52,   53,   54,   55,
   56,   57,   58,   59,   60,    0,    0,   63,    0,    0,
    0,    0,    0,    0,    1,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    4,    5,    6,    1,    4,    5,
    6,    0,    0,    1,    0,    0,    0,    4,    5,    6,
    0,    0,    0,    4,    5,    6,    0,    1,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    4,    5,    6,
    0,    0,    0,    0,    0,    0,    0,    0,   25,   26,
    0,   27,   28,   29,   30,   31,   32,   33,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   25,   26,    0,   27,   28,   29,   30,   31,   32,   33,
    0,    0,    0,    0,    0,   26,    0,   27,   28,   29,
   30,   31,   32,   33,    0,    0,    0,    0,    0,    0,
   27,   28,   29,   30,   31,   32,   33,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   44,   44,  271,    0,   41,    8,    9,   44,    0,
   41,    0,   46,   44,   41,   37,   40,   44,   44,   -1,
   42,   43,    0,   45,   46,   47,   -1,   -1,   -1,   -1,
   37,   -1,   44,    0,   -1,   42,   -1,   37,   -1,   46,
   47,   41,   42,   43,   44,   45,   46,   47,   37,   -1,
   -1,   93,   41,   42,   43,   44,   45,   93,   47,   37,
   94,    0,   64,   41,   42,   43,   44,   45,    0,   47,
   37,   -1,   94,   -1,   41,   42,   43,   44,   45,   -1,
   47,   93,  125,  125,   -1,   -1,    0,   94,   -1,  125,
   -1,   -1,   -1,   93,   94,   -1,   -1,    0,   -1,   -1,
   -1,   -1,   41,   -1,   93,   44,   -1,   -1,    0,   41,
   -1,   43,   44,   45,   -1,   93,   -1,   -1,   -1,    0,
   -1,   -1,   -1,   37,   -1,  125,   93,   41,   42,   43,
   44,   45,   -1,   47,   37,   -1,  125,   -1,   41,   42,
   43,   44,   45,   -1,   47,   37,   -1,  125,   -1,   41,
   42,   43,   44,   45,   93,   47,    0,   -1,  125,   -1,
   41,   93,   43,   44,   45,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,    0,   -1,   93,
   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,   -1,
   93,   -1,   -1,  125,   -1,   -1,    0,   41,   -1,   -1,
   44,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
    0,  125,   93,   -1,   -1,   -1,   -1,   -1,   41,   -1,
   -1,   44,  125,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  125,   -1,   -1,   -1,   41,   -1,   -1,
   44,   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,   93,
   -1,   41,   -1,   -1,   44,   -1,   -1,  257,  258,   -1,
  260,  261,  262,  263,  264,  265,  266,   -1,  257,  258,
   93,  260,  261,  262,  263,  264,  265,  266,   -1,  257,
  258,  125,  260,  261,  262,  263,  264,  265,  266,   93,
  257,  258,   -1,  260,  261,  262,  263,  264,  265,  266,
   -1,    0,  125,   93,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
   -1,  125,   -1,   -1,   -1,  257,  258,   -1,  260,  261,
  262,  263,  264,  265,  266,  125,   -1,   -1,   -1,   -1,
   -1,   -1,   41,  257,  258,   44,  260,  261,  262,  263,
  264,  265,  266,   -1,  257,  258,    0,  260,  261,  262,
  263,  264,  265,  266,   -1,  257,  258,   -1,  260,  261,
  262,  263,  264,  265,  266,   -1,  257,  258,   -1,  260,
  261,  262,  263,  264,  265,  266,    0,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   93,   -1,   -1,   41,   -1,   -1,
   44,   -1,   -1,    0,   -1,   -1,   -1,    0,   -1,   -1,
   -1,   -1,   -1,  257,  258,   -1,  260,  261,  262,  263,
  264,  265,  266,   -1,   -1,   -1,  125,   41,   -1,   -1,
   44,   -1,   -1,   -1,  257,  258,   -1,  260,  261,  262,
  263,  264,  265,  266,   41,   -1,   -1,   44,   41,   93,
   -1,   44,   -1,  257,  258,   -1,  260,  261,  262,  263,
  264,  265,  266,   -1,   -1,   -1,   -1,  257,  258,   -1,
  260,  261,  262,  263,  264,  265,  266,   -1,   -1,   93,
   -1,  125,   -1,   -1,   -1,   40,   41,   -1,   43,   40,
   45,   -1,   43,   -1,   45,   -1,   93,   -1,   40,   -1,
   93,   43,   -1,   45,   40,   41,   -1,   43,   -1,   45,
   -1,  125,   -1,   -1,   -1,   -1,   -1,   -1,   40,   -1,
   -1,   43,   -1,   45,   -1,   -1,   -1,   -1,  125,   -1,
   -1,   -1,  125,   -1,   -1,   -1,   91,   -1,   37,   -1,
   91,   -1,   93,   42,   43,   -1,   45,   46,   47,   91,
   -1,   -1,   -1,   -1,   -1,   91,   -1,   -1,  257,  258,
   -1,  260,  261,  262,  263,  264,  265,  266,  123,   91,
   37,   -1,  123,   -1,   41,   42,   43,   -1,   45,   46,
   47,  123,   -1,  125,   37,   -1,   -1,  123,   -1,   42,
   43,   -1,   45,   46,   47,   94,   -1,   37,   -1,   -1,
   -1,  123,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   -1,   -1,   -1,  257,  258,   -1,  260,  261,  262,  263,
  264,  265,  266,   -1,   -1,   -1,   -1,   94,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   94,   -1,  257,  258,   -1,  260,  261,  262,  263,
  264,  265,  266,   -1,   94,    0,    1,    2,    3,   -1,
  257,  258,    7,   -1,  257,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   25,   26,   27,   28,   29,   30,   31,   32,   33,   34,
   35,   36,   37,   38,   39,   -1,   -1,   42,   -1,   -1,
   -1,   -1,   -1,   -1,  259,   -1,   -1,   -1,  259,   -1,
   -1,   -1,   -1,   -1,  269,  270,  271,  259,  269,  270,
  271,   -1,   -1,  259,   -1,   -1,   -1,  269,  270,  271,
   -1,   -1,   -1,  269,  270,  271,   -1,  259,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  269,  270,  271,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
   -1,  260,  261,  262,  263,  264,  265,  266,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,  258,   -1,  260,  261,  262,  263,  264,  265,  266,
   -1,   -1,   -1,   -1,   -1,  258,   -1,  260,  261,  262,
  263,  264,  265,  266,   -1,   -1,   -1,   -1,   -1,   -1,
  260,  261,  262,  263,  264,  265,  266,
};
}
final static short YYFINAL=10;
final static short YYMAXTOKEN=272;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'","'^'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"OR","AND","NOT","LT","GT","EQ","IE",
"NE","LE","GE","UMINUS","UPLUS","NUMBER","STRING","NAME","RESERVEDWORD",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : expr",
"exprs : expr",
"exprs : exprs ',' expr",
"array : '(' ')'",
"array : '[' ']'",
"array : '(' exprs ')'",
"array : '[' exprs ']'",
"list : '{' '}'",
"list : '{' exprs '}'",
"member : NAME",
"member : NAME '(' ')'",
"member : NAME '(' exprs ')'",
"expr : NUMBER",
"expr : STRING",
"expr : NAME",
"expr : expr '+' expr",
"expr : expr '-' expr",
"expr : expr '*' expr",
"expr : expr '/' expr",
"expr : expr '%' expr",
"expr : '-' expr",
"expr : '+' expr",
"expr : expr '^' expr",
"expr : expr LT expr",
"expr : expr GT expr",
"expr : expr LE expr",
"expr : expr GE expr",
"expr : expr EQ expr",
"expr : expr NE expr",
"expr : expr IE expr",
"expr : expr AND expr",
"expr : expr OR expr",
"expr : NOT expr",
"expr : '(' expr ')'",
"expr : array",
"expr : list",
"expr : expr '.' member",
};

//#line 198 "evaluator2.y"
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
//#line 656 "Evaluator2.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        //if (yychar < 0)    //it it didn't work/error
        //  {
        //  yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
        //  }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 30 "evaluator2.y"
{ result = Constant.NULL; }
break;
case 2:
//#line 31 "evaluator2.y"
{ result = val_peek(0); }
break;
case 3:
//#line 34 "evaluator2.y"
{
   yyval = TokenHelper.exprs(Constant.NULL, val_peek(0)); 
   deduce(yyval, new Token[]{val_peek(0)});
  }
break;
case 4:
//#line 38 "evaluator2.y"
{
   yyval = TokenHelper.exprs(val_peek(2), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 5:
//#line 44 "evaluator2.y"
{
   yyval = TokenHelper.array(Constant.NULL);
   deduce(yyval, new Token[]{val_peek(1), val_peek(0)});
  }
break;
case 6:
//#line 48 "evaluator2.y"
{
   yyval = TokenHelper.array(Constant.NULL);
   deduce(yyval, new Token[]{val_peek(1), val_peek(0)});
  }
break;
case 7:
//#line 52 "evaluator2.y"
{
   yyval = TokenHelper.array(val_peek(1));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 8:
//#line 56 "evaluator2.y"
{
   yyval = TokenHelper.array(val_peek(1));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 9:
//#line 62 "evaluator2.y"
{
   yyval = TokenHelper.list(Constant.NULL);
   deduce(yyval, new Token[]{val_peek(1), val_peek(0)});
  }
break;
case 10:
//#line 66 "evaluator2.y"
{
   yyval = TokenHelper.list(val_peek(1));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 11:
//#line 72 "evaluator2.y"
{
   yyval = TokenHelper.member(val_peek(0), Constant.NULL);
   deduce(yyval, new Token[]{val_peek(0)}, true);
  }
break;
case 12:
//#line 76 "evaluator2.y"
{
   yyval = TokenHelper.member(val_peek(2), new Object[0]);
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)}, true);
  }
break;
case 13:
//#line 80 "evaluator2.y"
{
   yyval = TokenHelper.member(val_peek(3), val_peek(1));
   deduce(yyval, new Token[]{val_peek(3), val_peek(2), val_peek(1), val_peek(0)}, true);
  }
break;
case 14:
//#line 86 "evaluator2.y"
{
   yyval = TokenHelper.clone(val_peek(0));
  }
break;
case 16:
//#line 90 "evaluator2.y"
{
   yyval = TokenHelper.name(val_peek(0), context, this);
   notifyTokenVisitors(yyval);
  }
break;
case 17:
//#line 94 "evaluator2.y"
{
   yyval = TokenHelper.add(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 18:
//#line 99 "evaluator2.y"
{
   yyval = TokenHelper.sub(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 19:
//#line 104 "evaluator2.y"
{
   yyval = TokenHelper.mul(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 20:
//#line 109 "evaluator2.y"
{
   yyval = TokenHelper.div(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 21:
//#line 114 "evaluator2.y"
{
   yyval = TokenHelper.mod(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 22:
//#line 119 "evaluator2.y"
{
   yyval = TokenHelper.uminus(val_peek(0));
   notifyExprVisitors(Constant.NULL, val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(1), val_peek(0)});
  }
break;
case 23:
//#line 124 "evaluator2.y"
{
   yyval = TokenHelper.uplus(val_peek(0));
   notifyExprVisitors(Constant.NULL, val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(1), val_peek(0)});
  }
break;
case 24:
//#line 129 "evaluator2.y"
{
   yyval = TokenHelper.power(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 25:
//#line 134 "evaluator2.y"
{
   yyval = TokenHelper.lt(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 26:
//#line 139 "evaluator2.y"
{
   yyval = TokenHelper.gt(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 27:
//#line 144 "evaluator2.y"
{
   yyval = TokenHelper.le(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 28:
//#line 149 "evaluator2.y"
{
   yyval = TokenHelper.ge(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 29:
//#line 154 "evaluator2.y"
{
   yyval = TokenHelper.eq(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 30:
//#line 159 "evaluator2.y"
{
   yyval = TokenHelper.ne(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 31:
//#line 164 "evaluator2.y"
{
   yyval = TokenHelper.ie(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 32:
//#line 169 "evaluator2.y"
{
   yyval = TokenHelper.and(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 33:
//#line 174 "evaluator2.y"
{
   yyval = TokenHelper.or(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 34:
//#line 179 "evaluator2.y"
{
   yyval = TokenHelper.not(val_peek(0));
   notifyExprVisitors(Constant.NULL, val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(1), val_peek(0)});
  }
break;
case 35:
//#line 184 "evaluator2.y"
{
   yyval = val_peek(1);
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
case 38:
//#line 191 "evaluator2.y"
{
   yyval = TokenHelper.call(val_peek(2), val_peek(0));
   notifyExprVisitors(val_peek(2), val_peek(1), val_peek(0));
   deduce(yyval, new Token[]{val_peek(2), val_peek(1), val_peek(0)});
  }
break;
//#line 1051 "Evaluator2.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        //if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
