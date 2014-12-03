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



//#line 2 "shell.y"
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.sophia.parser.ParseContext;
import com.sophia.parser.util.DebugHelper;
//#line 26 "Shell.java"




public class Shell
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
//## **user defined:Object
String   yytext;//user variable to return contextual strings
Object yyval; //used to return semantic vals from action routines
Object yylval;//the 'lval' (result) I got from yylex()
Object valstk[] = new Object[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new Object();
  yylval=new Object();
  valptr=-1;
}
final void val_push(Object val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    Object[] newstack = new Object[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final Object val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final Object val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short UMINUS=257;
public final static short UPLUS=258;
public final static short NUMBER=259;
public final static short STRING=260;
public final static short NAME=261;
public final static short ERROR=262;
public final static short NEW=263;
public final static short IMPORT=264;
public final static short RETURN=265;
public final static short IF=266;
public final static short ELSE=267;
public final static short WHILE=268;
public final static short DO=269;
public final static short FOR=270;
public final static short SWITCH=271;
public final static short CASE=272;
public final static short DEFAULT=273;
public final static short BREAK=274;
public final static short CONTINUE=275;
public final static short DPLUS=276;
public final static short DMINUS=277;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    3,    4,    4,    5,    5,    5,    5,    6,    6,    7,
    7,    7,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,
};
final static short yylen[] = {                            2,
    1,    2,    2,    2,    4,    1,    4,    1,    8,    4,
    3,    2,    2,    2,    2,    3,    3,    1,    0,    4,
    3,    1,    3,    2,    2,    3,    3,    2,    3,    1,
    3,    4,    1,    1,    1,    3,    3,    3,    3,    3,
    2,    2,    3,    3,    3,    4,    4,    4,    4,    4,
    4,    4,    2,    3,    1,    1,    3,    2,    2,    2,
    2,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   33,   34,    0,   63,    0,    0,
    0,    0,    6,    0,    8,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   18,    0,    0,    0,    1,    0,
    0,   55,   56,    0,    0,    0,    0,   15,    0,    0,
   59,   60,    0,   58,    0,    0,    0,    0,    0,    0,
    0,   12,   13,   14,   61,   62,   24,    0,    0,   25,
    0,    0,   28,    0,    2,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    3,
    4,    0,    0,    0,   16,   17,    0,    0,    0,    0,
    0,   11,   54,   26,    0,   27,   29,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   57,    0,   31,    0,    5,    7,    0,   10,
    0,    0,    0,    0,    0,    0,    0,    0,   32,    0,
    0,    0,    9,
};
final static short yydgoto[] = {                         28,
   29,   61,   31,   59,   32,   33,   44,
};
final static short yysindex[] = {                       -10,
   75,   75,   75,  -16,    0,    0,  -59,    0, -241, -221,
   75,    3,    0,    7,    0,   11,   13,   75,    4,   -2,
    2, -191, -186,  -32,    0,    9,   15,  -10,    0,  655,
   19,    0,    0, -238,  179,  -12,  -12,    0,   75,   23,
    0,    0,   46,    0,   30,  691,   75,   75, -171, -167,
  722,    0,    0,    0,    0,    0,    0,  758,   27,    0,
  932,  -28,    0,  -26,    0,  -14,   65,   50,   34,   40,
   51,   53,   75,   75,   75,   75,   75,   75, -241,    0,
    0,  932,   75,   64,    0,    0,  789,  825,  -58,   58,
   78,    0,    0,    0,   75,    0,    0,   75,   75,   75,
   75,  -18,   75,  -18,   75,   75,   -1,   -1,  -12,  -12,
  -12,  -12,    0,  932,    0,   28,    0,    0,   75,    0,
  932,  999,  179,  -18,  -18,  -18,  -18,  -18,    0,  861,
 -171,   81,    0,
};
final static short yyrindex[] = {                        70,
    0,    0,    0,    0,    0,    0,  968,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   70,    0,    0,
    0,    0,    0,  341,   95,  443,  482,    0,    0,    0,
    0,    0,  413,    0,    0,    0,    0,    0,   70,    0,
    0,    0,    0,    0,    0,    0,    0,   79,    0,    0,
  -29,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -15,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  271,    0, 1010,    0,    0,   83,  898,  512,  551,
  581,  620,    0,   -9,    0,    0,    0,    0,    0,    0,
  -27,  -37,  155, 1040, 1047, 1083, 1119, 1155,    0,    0,
   89,    0,    0,
};
final static short yygindex[] = {                         0,
  106,  342,  -39,  -21,    0,    0,   56,
};
final static int YYTABLESIZE=1281;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         25,
    1,   39,   39,   52,   62,   64,   52,   24,   57,   90,
    3,   22,    2,   23,   22,   95,   23,   95,   77,   43,
   52,   52,    1,   75,   74,   21,   73,   79,   76,   24,
   38,   20,    3,   79,    2,   77,    4,   41,   42,   45,
   75,    1,   47,   21,   79,   76,   48,    1,   24,   20,
   49,    3,   50,    2,   24,   52,   53,    3,   26,    2,
   54,   52,  116,   22,   96,   23,    1,   94,  129,   55,
   95,   95,    1,   24,   56,   78,    3,   81,    2,   24,
   26,   78,    3,   83,    2,   84,   52,   52,   85,   89,
   27,  132,   78,   91,  101,   22,    1,   23,   97,   26,
  103,   60,   99,   24,  115,   26,    3,    1,    2,   98,
  100,  105,   27,  106,   24,   37,  119,    3,  120,    2,
   37,  133,   22,   37,   26,   37,   37,   37,   19,   19,
   26,   27,   53,   65,  113,   53,    0,   27,   53,   63,
   37,   37,   37,   37,   37,    0,    0,    0,    0,    0,
    0,    0,   53,   53,   26,    0,   27,    0,    0,    0,
    0,    0,   27,    0,    0,   26,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   37,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   27,   53,    0,    0,
    0,    0,   51,    0,    0,   51,    0,   27,   51,    0,
    0,   40,   40,    0,    0,    0,   37,   37,   37,    0,
    0,   68,   51,   51,    0,   77,   41,   42,   53,   53,
   75,   74,    0,   73,   79,   76,    5,    6,   34,    8,
    9,    0,    0,    0,    0,    0,    0,    0,   69,   71,
   70,    0,    0,   22,   23,    0,    0,   51,    5,    6,
    7,    8,    9,   10,   11,   12,   13,   14,   15,   16,
   17,   18,   19,   20,   21,   22,   23,    5,    6,   34,
    8,    9,   78,    5,    6,   34,    8,    9,   51,   51,
    0,    0,    0,    0,   22,   23,    0,    0,    0,    0,
   22,   23,    5,    6,   34,    8,    9,    0,    5,    6,
   34,    8,    9,   44,   72,    0,    0,    0,   44,   22,
   23,   44,    0,    0,   44,   22,   23,    0,    0,    0,
    0,    0,    5,    6,   34,    8,    9,    0,   44,   44,
   44,   44,   44,    5,    6,   34,    8,    9,    0,   22,
   23,   30,   35,   36,   37,    0,    0,    0,    0,    0,
   22,   23,   46,    0,    0,    0,    0,    0,    0,   51,
    0,    0,    0,   44,    0,   58,    0,    0,    0,   30,
    0,    0,    0,   35,    0,    0,    0,   35,   35,    0,
   82,   35,   35,   35,   35,   35,   35,   35,   87,   88,
    0,    0,    0,    0,   44,   44,   44,    0,   35,   35,
   35,   35,   35,    0,    0,    0,    0,    0,    0,    0,
  102,  104,    0,    0,  107,  108,  109,  110,  111,  112,
    0,    0,    0,    0,  114,    0,    0,    0,    0,    0,
    0,    0,    0,   35,   35,    0,  121,    0,    0,  122,
  123,  124,  125,    0,  126,   30,  127,  128,    0,   30,
   30,    0,    0,   30,   30,   30,   30,   30,   30,   30,
  130,    0,    0,    0,   35,   35,   35,    0,    0,    0,
   30,   30,   30,   30,   30,   41,    0,    0,    0,   41,
   41,    0,    0,   41,   41,   41,   41,   41,    0,   41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   41,   41,   41,   41,   41,   30,   30,    0,    0,    0,
    0,    0,    0,    0,   42,    0,    0,    0,   42,   42,
    0,    0,   42,   42,   42,   42,   42,    0,   42,    0,
    0,    0,    0,    0,    0,   41,   30,   30,   30,   42,
   42,   42,   42,   42,   38,    0,    0,    0,   38,   38,
    0,    0,   38,   38,   38,   38,   38,    0,   38,    0,
    0,    0,    0,    0,    0,    0,   41,   41,   41,   38,
   38,   38,   38,   38,   42,    0,    0,    0,    0,    0,
    0,    0,    0,   39,    0,    0,    0,   39,   39,    0,
    0,   39,   39,   39,   39,   39,    0,   39,    0,    0,
    0,    0,    0,    0,   38,   42,   42,   42,   39,   39,
   39,   39,   39,   40,    0,    0,    0,   40,   40,    0,
    0,   40,   40,   40,   40,   40,    0,   40,    0,    0,
    0,    0,    0,    0,    0,   38,   38,   38,   40,   40,
   40,   40,   40,   39,    0,    0,    0,    0,    0,    0,
    0,    0,   43,    0,    0,    0,   43,   43,    0,    0,
   43,   43,   43,   43,   43,    0,   43,    0,    0,    0,
    0,    0,    0,   40,   39,   39,   39,   43,   43,   43,
   43,   43,    0,    0,    0,    0,    0,   68,    0,    0,
    0,   77,   67,    0,    0,    0,   75,   74,    0,   73,
   79,   76,    0,    0,   40,   40,   40,    0,    0,    0,
    0,    0,   43,   80,   69,   71,   70,    0,    0,    0,
    0,    0,    0,   68,    0,    0,    0,   77,   67,    0,
    0,    0,   75,   74,    0,   73,   79,   76,    0,    0,
    0,    0,    0,   43,   43,   43,    0,    0,   78,   86,
   69,   71,   70,    0,   68,    0,    0,    0,   77,   67,
    0,    0,    0,   75,   74,    0,   73,   79,   76,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   66,   92,
   72,   69,   71,   70,   78,    0,    0,    0,    0,    0,
   68,    0,    0,    0,   77,   67,    0,    0,   93,   75,
   74,    0,   73,   79,   76,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   66,   78,   72,   69,   71,   70,
    0,   68,    0,    0,    0,   77,   67,    0,    0,  117,
   75,   74,    0,   73,   79,   76,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   66,    0,   72,   69,   71,
   70,   78,    0,    0,    0,    0,    0,   68,    0,    0,
    0,   77,   67,    0,    0,  118,   75,   74,    0,   73,
   79,   76,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   66,   78,   72,   69,   71,   70,    0,    0,    0,
    0,    0,    0,   68,    0,    0,    0,   77,   67,    0,
    0,    0,   75,   74,    0,   73,   79,   76,    0,    0,
    0,    0,   66,    0,   72,    0,    0,    0,   78,  131,
   69,   71,   70,    0,    0,    0,    0,    0,    0,    0,
   36,    0,    0,    0,    0,   36,    0,    0,   36,    0,
   36,   36,   36,    0,    0,    0,    0,    0,   66,    0,
   72,    0,    0,    0,   78,   36,   36,   36,   36,   36,
    0,    0,    0,    0,   68,    0,    0,    0,   77,   67,
    0,    0,    0,   75,   74,    0,   73,   79,   76,    0,
    0,    0,    0,    0,   66,    0,   72,    0,    0,    0,
   36,   69,   71,   70,    0,    0,    0,    0,    0,    0,
   35,    0,    0,    0,   35,   35,    0,    0,    0,   35,
   35,    0,   35,   35,   35,    0,    0,    0,    0,    0,
    0,   36,   36,   36,    0,   78,   35,   35,    0,   35,
    0,   68,    0,    0,    0,   77,   67,    0,    0,    0,
   75,   74,   45,   73,   79,   76,    0,   45,    0,    0,
   45,    0,    0,   45,    0,   66,    0,   72,   69,   71,
   70,   35,    0,    0,    0,    0,    0,   45,   45,   45,
   45,   45,   49,    0,    0,    0,    0,   49,    0,   46,
   49,    0,    0,   49,   46,    0,    0,   46,    0,    0,
   46,   35,   78,   35,    0,    0,    0,   49,   49,   49,
   49,   49,   45,    0,   46,   46,   46,   46,   46,    0,
    0,    0,    0,    0,    0,   47,    0,    0,    0,    0,
   47,    0,    0,   47,   72,    0,   47,    0,    0,    0,
    0,    0,   49,   45,   45,   45,    0,    0,    0,   46,
   47,   47,   47,   47,   47,    0,    0,    0,    0,    0,
    0,   48,    0,    0,    0,    0,   48,    0,    0,   48,
    0,    0,   48,   49,   49,   49,    0,    0,    0,    0,
   46,   46,   46,    0,    0,   47,   48,   48,   48,   48,
   48,    0,    0,    0,    0,    0,    0,   50,    0,    0,
    0,    0,   50,    0,    0,   50,    0,    0,   50,    0,
    0,    0,    0,    0,    0,    0,   47,   47,   47,    0,
    0,   48,   50,   50,   50,   50,   50,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   48,   48,   48,    0,    0,   50,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   50,   50,
   50,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         10,
   33,   61,   61,   41,   26,   27,   44,   40,   41,   49,
   43,   41,   45,   41,   44,   44,   44,   44,   37,  261,
   58,   59,   33,   42,   43,   41,   45,   46,   47,   40,
   47,   41,   43,   46,   45,   37,   47,  276,  277,  261,
   42,   33,   40,   59,   46,   47,   40,   33,   40,   59,
   40,   43,   40,   45,   40,   93,   59,   43,   91,   45,
   59,   58,   84,   93,   93,   93,   33,   41,   41,  261,
   44,   44,   33,   40,  261,   94,   43,   59,   45,   40,
   91,   94,   43,   61,   45,   40,  124,  125,   59,  261,
  123,  131,   94,  261,   61,  125,   33,  125,  125,   91,
   61,   93,   38,   40,   41,   91,   43,   33,   45,  124,
   61,   61,  123,   61,   40,   33,   59,   43,   41,   45,
   38,   41,   44,   41,   91,   43,   44,   45,   59,   41,
   91,  123,   38,   28,   79,   41,   -1,  123,   44,  125,
   58,   59,   60,   61,   62,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   58,   59,   91,   -1,  123,   -1,   -1,   -1,
   -1,   -1,  123,   -1,   -1,   91,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  123,   93,   -1,   -1,
   -1,   -1,   38,   -1,   -1,   41,   -1,  123,   44,   -1,
   -1,  261,  261,   -1,   -1,   -1,  124,  125,  126,   -1,
   -1,   33,   58,   59,   -1,   37,  276,  277,  124,  125,
   42,   43,   -1,   45,   46,   47,  259,  260,  261,  262,
  263,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   60,   61,
   62,   -1,   -1,  276,  277,   -1,   -1,   93,  259,  260,
  261,  262,  263,  264,  265,  266,  267,  268,  269,  270,
  271,  272,  273,  274,  275,  276,  277,  259,  260,  261,
  262,  263,   94,  259,  260,  261,  262,  263,  124,  125,
   -1,   -1,   -1,   -1,  276,  277,   -1,   -1,   -1,   -1,
  276,  277,  259,  260,  261,  262,  263,   -1,  259,  260,
  261,  262,  263,   33,  126,   -1,   -1,   -1,   38,  276,
  277,   41,   -1,   -1,   44,  276,  277,   -1,   -1,   -1,
   -1,   -1,  259,  260,  261,  262,  263,   -1,   58,   59,
   60,   61,   62,  259,  260,  261,  262,  263,   -1,  276,
  277,    0,    1,    2,    3,   -1,   -1,   -1,   -1,   -1,
  276,  277,   11,   -1,   -1,   -1,   -1,   -1,   -1,   18,
   -1,   -1,   -1,   93,   -1,   24,   -1,   -1,   -1,   28,
   -1,   -1,   -1,   33,   -1,   -1,   -1,   37,   38,   -1,
   39,   41,   42,   43,   44,   45,   46,   47,   47,   48,
   -1,   -1,   -1,   -1,  124,  125,  126,   -1,   58,   59,
   60,   61,   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   69,   70,   -1,   -1,   73,   74,   75,   76,   77,   78,
   -1,   -1,   -1,   -1,   83,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   93,   94,   -1,   95,   -1,   -1,   98,
   99,  100,  101,   -1,  103,   33,  105,  106,   -1,   37,
   38,   -1,   -1,   41,   42,   43,   44,   45,   46,   47,
  119,   -1,   -1,   -1,  124,  125,  126,   -1,   -1,   -1,
   58,   59,   60,   61,   62,   33,   -1,   -1,   -1,   37,
   38,   -1,   -1,   41,   42,   43,   44,   45,   -1,   47,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   58,   59,   60,   61,   62,   93,   94,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   33,   -1,   -1,   -1,   37,   38,
   -1,   -1,   41,   42,   43,   44,   45,   -1,   47,   -1,
   -1,   -1,   -1,   -1,   -1,   93,  124,  125,  126,   58,
   59,   60,   61,   62,   33,   -1,   -1,   -1,   37,   38,
   -1,   -1,   41,   42,   43,   44,   45,   -1,   47,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  124,  125,  126,   58,
   59,   60,   61,   62,   93,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   33,   -1,   -1,   -1,   37,   38,   -1,
   -1,   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,
   -1,   -1,   -1,   -1,   93,  124,  125,  126,   58,   59,
   60,   61,   62,   33,   -1,   -1,   -1,   37,   38,   -1,
   -1,   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  124,  125,  126,   58,   59,
   60,   61,   62,   93,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   33,   -1,   -1,   -1,   37,   38,   -1,   -1,
   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,   -1,
   -1,   -1,   -1,   93,  124,  125,  126,   58,   59,   60,
   61,   62,   -1,   -1,   -1,   -1,   -1,   33,   -1,   -1,
   -1,   37,   38,   -1,   -1,   -1,   42,   43,   -1,   45,
   46,   47,   -1,   -1,  124,  125,  126,   -1,   -1,   -1,
   -1,   -1,   93,   59,   60,   61,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   33,   -1,   -1,   -1,   37,   38,   -1,
   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   -1,   -1,   -1,  124,  125,  126,   -1,   -1,   94,   59,
   60,   61,   62,   -1,   33,   -1,   -1,   -1,   37,   38,
   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  124,   58,
  126,   60,   61,   62,   94,   -1,   -1,   -1,   -1,   -1,
   33,   -1,   -1,   -1,   37,   38,   -1,   -1,   41,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  124,   94,  126,   60,   61,   62,
   -1,   33,   -1,   -1,   -1,   37,   38,   -1,   -1,   41,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  124,   -1,  126,   60,   61,
   62,   94,   -1,   -1,   -1,   -1,   -1,   33,   -1,   -1,
   -1,   37,   38,   -1,   -1,   41,   42,   43,   -1,   45,
   46,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  124,   94,  126,   60,   61,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   33,   -1,   -1,   -1,   37,   38,   -1,
   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   -1,   -1,  124,   -1,  126,   -1,   -1,   -1,   94,   59,
   60,   61,   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   33,   -1,   -1,   -1,   -1,   38,   -1,   -1,   41,   -1,
   43,   44,   45,   -1,   -1,   -1,   -1,   -1,  124,   -1,
  126,   -1,   -1,   -1,   94,   58,   59,   60,   61,   62,
   -1,   -1,   -1,   -1,   33,   -1,   -1,   -1,   37,   38,
   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,   -1,   -1,  124,   -1,  126,   -1,   -1,   -1,
   93,   60,   61,   62,   -1,   -1,   -1,   -1,   -1,   -1,
   33,   -1,   -1,   -1,   37,   38,   -1,   -1,   -1,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,   -1,
   -1,  124,  125,  126,   -1,   94,   59,   60,   -1,   62,
   -1,   33,   -1,   -1,   -1,   37,   38,   -1,   -1,   -1,
   42,   43,   33,   45,   46,   47,   -1,   38,   -1,   -1,
   41,   -1,   -1,   44,   -1,  124,   -1,  126,   60,   61,
   62,   94,   -1,   -1,   -1,   -1,   -1,   58,   59,   60,
   61,   62,   33,   -1,   -1,   -1,   -1,   38,   -1,   33,
   41,   -1,   -1,   44,   38,   -1,   -1,   41,   -1,   -1,
   44,  124,   94,  126,   -1,   -1,   -1,   58,   59,   60,
   61,   62,   93,   -1,   58,   59,   60,   61,   62,   -1,
   -1,   -1,   -1,   -1,   -1,   33,   -1,   -1,   -1,   -1,
   38,   -1,   -1,   41,  126,   -1,   44,   -1,   -1,   -1,
   -1,   -1,   93,  124,  125,  126,   -1,   -1,   -1,   93,
   58,   59,   60,   61,   62,   -1,   -1,   -1,   -1,   -1,
   -1,   33,   -1,   -1,   -1,   -1,   38,   -1,   -1,   41,
   -1,   -1,   44,  124,  125,  126,   -1,   -1,   -1,   -1,
  124,  125,  126,   -1,   -1,   93,   58,   59,   60,   61,
   62,   -1,   -1,   -1,   -1,   -1,   -1,   33,   -1,   -1,
   -1,   -1,   38,   -1,   -1,   41,   -1,   -1,   44,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  124,  125,  126,   -1,
   -1,   93,   58,   59,   60,   61,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  124,  125,  126,   -1,   -1,   93,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  124,  125,
  126,
};
}
final static short YYFINAL=28;
final static short YYMAXTOKEN=277;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,"'\\n'",null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,"'!'",null,null,null,"'%'","'&'",null,"'('","')'","'*'",
"'+'","','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,
"':'","';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'['",null,"']'","'^'",null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'{'","'|'","'}'","'~'",null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"UMINUS","UPLUS","NUMBER",
"STRING","NAME","ERROR","NEW","IMPORT","RETURN","IF","ELSE","WHILE","DO","FOR",
"SWITCH","CASE","DEFAULT","BREAK","CONTINUE","DPLUS","DMINUS",
};
final static String yyrule[] = {
"$accept : input",
"input : statement",
"input : input statement",
"statement : expr ';'",
"statement : set ';'",
"statement : IF '(' expr ')'",
"statement : ELSE",
"statement : WHILE '(' expr ')'",
"statement : DO",
"statement : FOR '(' set ';' expr ';' set ')'",
"statement : SWITCH '(' NAME ')'",
"statement : CASE expr ':'",
"statement : DEFAULT ':'",
"statement : BREAK ';'",
"statement : CONTINUE ';'",
"statement : '/' '/'",
"statement : IMPORT NAME ';'",
"statement : RETURN expr ';'",
"statement : '\\n'",
"set :",
"set : NAME NAME '=' expr",
"set : NAME '=' expr",
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
"expr : expr '<' expr",
"expr : expr '>' expr",
"expr : expr '<' '=' expr",
"expr : expr '>' '=' expr",
"expr : expr '=' '=' expr",
"expr : expr '!' '=' expr",
"expr : expr '~' '=' expr",
"expr : expr '&' '&' expr",
"expr : expr '|' '|' expr",
"expr : '!' expr",
"expr : '(' expr ')'",
"expr : array",
"expr : list",
"expr : expr '.' member",
"expr : NEW member",
"expr : NAME DPLUS",
"expr : NAME DMINUS",
"expr : DPLUS NAME",
"expr : DMINUS NAME",
"expr : ERROR",
};

//#line 111 "shell.y"
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
//#line 759 "Shell.java"
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
//#line 31 "shell.y"
{ this.pindex = this.index; }
break;
case 3:
//#line 35 "shell.y"
{ yyval = val_peek(1); }
break;
case 17:
//#line 49 "shell.y"
{ this.result = val_peek(1); }
break;
case 18:
//#line 50 "shell.y"
{ this.line++; this.column = 0; }
break;
case 20:
//#line 54 "shell.y"
{ yyval = ValueHelper.set(val_peek(3), val_peek(2), val_peek(0), this.context); }
break;
case 21:
//#line 55 "shell.y"
{ yyval = ValueHelper.set(null, val_peek(2), val_peek(0), this.context); }
break;
case 22:
//#line 58 "shell.y"
{ yyval = ValueHelper.exprs(null, val_peek(0)); }
break;
case 23:
//#line 59 "shell.y"
{ yyval = ValueHelper.exprs(val_peek(2), val_peek(0)); }
break;
case 24:
//#line 62 "shell.y"
{ yyval = ValueHelper.array(null); }
break;
case 25:
//#line 63 "shell.y"
{ yyval = ValueHelper.array(null); }
break;
case 26:
//#line 64 "shell.y"
{ yyval = ValueHelper.array(val_peek(1)); }
break;
case 27:
//#line 65 "shell.y"
{ yyval = ValueHelper.array(val_peek(1)); }
break;
case 28:
//#line 68 "shell.y"
{ yyval = ValueHelper.list(null); }
break;
case 29:
//#line 69 "shell.y"
{ yyval = ValueHelper.list(val_peek(1)); }
break;
case 30:
//#line 72 "shell.y"
{ yyval = val_peek(0); }
break;
case 31:
//#line 73 "shell.y"
{ yyval = ValueHelper.member(val_peek(2), new Object[0]); }
break;
case 32:
//#line 74 "shell.y"
{ yyval = ValueHelper.member(val_peek(3), val_peek(1)); }
break;
case 33:
//#line 77 "shell.y"
{ yyval = val_peek(0); }
break;
case 35:
//#line 79 "shell.y"
{ yyval = ValueHelper.name(yyval, this.context, this); }
break;
case 36:
//#line 80 "shell.y"
{ yyval = ValueHelper.add(val_peek(2), val_peek(0)); }
break;
case 37:
//#line 81 "shell.y"
{ yyval = ValueHelper.sub(val_peek(2), val_peek(0)); }
break;
case 38:
//#line 82 "shell.y"
{ yyval = ValueHelper.mul(val_peek(2), val_peek(0)); }
break;
case 39:
//#line 83 "shell.y"
{ yyval = ValueHelper.div(val_peek(2), val_peek(0)); }
break;
case 40:
//#line 84 "shell.y"
{ yyval = ValueHelper.mod(val_peek(2), val_peek(0)); }
break;
case 41:
//#line 85 "shell.y"
{ yyval = ValueHelper.uminus(val_peek(0)); }
break;
case 42:
//#line 86 "shell.y"
{ yyval = ValueHelper.uplus(val_peek(0)); }
break;
case 43:
//#line 87 "shell.y"
{ yyval = ValueHelper.power(val_peek(2), val_peek(0)); }
break;
case 44:
//#line 88 "shell.y"
{ yyval = ValueHelper.lt(val_peek(2), val_peek(0)); }
break;
case 45:
//#line 89 "shell.y"
{ yyval = ValueHelper.gt(val_peek(2), val_peek(0)); }
break;
case 46:
//#line 90 "shell.y"
{ yyval = ValueHelper.le(val_peek(3), val_peek(0)); }
break;
case 47:
//#line 91 "shell.y"
{ yyval = ValueHelper.ge(val_peek(3), val_peek(0)); }
break;
case 48:
//#line 92 "shell.y"
{ yyval = ValueHelper.eq(val_peek(3), val_peek(0)); }
break;
case 49:
//#line 93 "shell.y"
{ yyval = ValueHelper.ne(val_peek(3), val_peek(0)); }
break;
case 50:
//#line 94 "shell.y"
{ yyval = ValueHelper.ie(val_peek(3), val_peek(0)); }
break;
case 51:
//#line 95 "shell.y"
{ yyval = ValueHelper.and(val_peek(3), val_peek(0)); }
break;
case 52:
//#line 96 "shell.y"
{ yyval = ValueHelper.or(val_peek(3), val_peek(0)); }
break;
case 53:
//#line 97 "shell.y"
{ yyval = ValueHelper.not(val_peek(0)); }
break;
case 54:
//#line 98 "shell.y"
{ yyval = val_peek(1); }
break;
case 57:
//#line 101 "shell.y"
{ yyval = ValueHelper.call(val_peek(2), val_peek(0)); }
break;
case 58:
//#line 102 "shell.y"
{ yyval = ValueHelper.construct(val_peek(0)); }
break;
case 63:
//#line 107 "shell.y"
{}
break;
//#line 1060 "Shell.java"
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
