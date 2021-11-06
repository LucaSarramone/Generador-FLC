//### This file created by BYACC 1.8(/Java extension  1.15)
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






//#line 2 ".\Gramatica.y"
package parser;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.stream.Collectors;

import lexical_analysis.LexicalAnalyzer;
import code_generation.CodeGenerator;
import code_generation.defuzz.CentroidDefuzz;
import code_generation.defuzz.Defuzzifier;
import code_generation.evaluation_method.EvalMethod;
import code_generation.evaluation_method.MinMaxMethod;
import code_generation.variables.IOVars;
import code_generation.variables.Variable;
import code_generation.variables.fuzzy_set.*;
import compiler.Compiler;
import compiler.LexemeInfo;
import gui.MainFrame;
//#line 39 "Parser.java"




public class Parser
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
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CONST_INT=258;
public final static short FUZZ=259;
public final static short BEGIN=260;
public final static short END=261;
public final static short DECLARE=262;
public final static short RULES=263;
public final static short IN=264;
public final static short OUT=265;
public final static short DEFUZZ=266;
public final static short TRIANGULAR=267;
public final static short SINGLETON=268;
public final static short Z_SHAPE=269;
public final static short S_SHAPE=270;
public final static short MINMAX=271;
public final static short MANDANI=272;
public final static short CENTROID=273;
public final static short TRAPEZOID=274;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    3,    3,    3,    3,
    3,    4,    4,    4,    8,    8,    9,    9,   10,   10,
    5,    5,    5,   11,   11,   12,   12,   13,   13,   14,
   14,   15,   15,   15,   15,   15,   16,   16,   17,    6,
    6,    6,    6,    6,   18,   18,   19,   20,   20,   23,
   23,   21,   21,   22,   22,    7,    7,    7,   24,   24,
   25,   25,   26,
};
final static short yylen[] = {                            2,
    1,    1,    2,    9,    6,    8,    4,    3,    3,    3,
    3,    4,    3,    1,    2,    1,    4,    3,    1,    1,
    4,    3,    1,    2,    1,    5,    4,    2,    1,    7,
    6,    1,    1,    1,    1,    1,    3,    1,    1,    8,
    7,    6,    4,    5,    1,    1,    5,    2,    1,    4,
    3,    1,    3,    1,    3,    4,    3,    1,    2,    1,
    4,    3,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    2,    0,    0,    3,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   22,    0,   25,    0,   13,
    0,   16,    5,    0,    0,    0,    0,   10,    9,    8,
    0,    0,   21,   24,    0,   12,   15,   45,   46,    0,
    0,    0,    0,    7,    0,    0,    0,   29,   19,   20,
    0,    0,   52,    0,    0,    0,   49,    0,   57,    0,
   60,    4,    0,    0,   28,   17,    0,    0,    0,   44,
   48,    0,    0,   56,   59,   32,   33,   35,   34,   36,
    0,   26,   42,    0,    0,   54,    0,   53,    0,   63,
    0,    0,    0,   41,   47,    0,   50,   61,   39,    0,
   38,   40,   55,    0,    0,   30,   37,
};
final static short yydgoto[] = {                          2,
    3,    4,   13,   14,   15,   22,   38,   31,   32,   61,
   27,   28,   57,   58,   91,  110,  111,   50,   52,   65,
   66,   97,   67,   70,   71,  101,
};
final static short yysindex[] = {                      -233,
  -25,    0, -233,    0, -207, -200,    0,    3, -210,    8,
  -62,  -56, -193, -217, -194, -190, -123, -122,   12,  -43,
 -213, -192, -192, -210,  -51,    0, -121,    0,   15,    0,
 -120,    0,    0, -227,   35,  -46, -192,    0,    0,    0,
 -185, -179,    0,    0, -201,    0,    0,    0,    0,   17,
 -176, -176, -119,    0,   23,   22, -118,    0,    0,    0,
   25,  -37,    0,  -23, -117,  -22,    0,   26,    0, -116,
    0,    0, -240,   29,    0,    0,  -40, -168, -167,    0,
    0, -168, -182,    0,    0,    0,    0,    0,    0,    0,
   52,    0,    0, -176, -115,    0,   13,    0,  -18,    0,
   34, -164, -114,    0,    0, -162,    0,    0,    0,   14,
    0,    0,    0,   37, -164,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,   97,    0,    0,    0,    0,    0,    0,    0,
 -204, -216,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -163, -161,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,    0,    0,    0,
 -113, -229,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -112,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -111,    0,
 -110,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -109,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   96,   77,    0,   88,   10,   -1,    0,   72,    0,
    0,   78,    0,   47,    0,    0,   -9,    0,   30,  -54,
   57,   27,  -47,    0,   40,    0,
};
final static int YYTABLESIZE=258;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         51,
    6,   26,   30,   43,   46,   69,   74,   80,   84,  104,
  112,   18,   27,   51,   62,   31,   34,   81,   79,   79,
   39,   40,   95,    1,   23,  106,   86,   87,   88,   89,
   37,   43,    6,   90,    5,   54,   43,   78,   82,  103,
  107,   11,   14,   48,   49,   20,   14,   81,   11,   20,
    8,   12,   36,  105,  114,   81,  106,  115,   23,    9,
   17,   23,   59,   60,   10,   16,   18,   19,   20,   24,
   33,   42,   45,   36,   51,   55,   53,   56,   62,   35,
   63,   72,   73,   76,   93,   77,   83,   92,   96,   98,
  100,  102,  108,  109,  113,  116,    1,   58,    7,   11,
   41,   21,   47,   75,   44,  117,   94,   64,   99,   85,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   25,   29,   25,   29,   68,   56,   63,
   68,   63,   63,   18,   27,   51,   62,   31,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   63,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    6,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,  125,  125,  125,  125,  125,  125,  125,  125,  125,
  125,  125,  125,  125,  125,  125,   60,   65,   42,   42,
   22,   23,   77,  257,   15,   44,  267,  268,  269,  270,
   21,  261,   58,  274,   60,   37,  266,   61,   61,   94,
   59,  259,  259,  271,  272,  263,  263,   95,  259,  263,
  258,  262,  266,   41,   41,  103,   44,   44,  263,  260,
  123,  266,  264,  265,   62,   58,  123,  261,  263,  260,
   59,  123,   58,  266,   40,  261,  123,  257,   62,  123,
  257,   59,   61,   59,  125,  123,   61,   59,  257,  257,
  273,   40,   59,  258,  257,   59,    0,  261,    3,  261,
   24,   14,   31,   57,   27,  115,   77,   51,   82,   70,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  257,  257,  257,  257,  257,  257,
  257,  257,  257,  257,  257,  257,  257,  257,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=274;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'",null,"','",
null,null,null,null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CONST_INT","FUZZ","BEGIN","END",
"DECLARE","RULES","IN","OUT","DEFUZZ","TRIANGULAR","SINGLETON","Z_SHAPE",
"S_SHAPE","MINMAX","MANDANI","CENTROID","TRAPEZOID",
};
final static String yyrule[] = {
"$accept : program",
"program : fuzzy_controller_set",
"fuzzy_controller_set : fuzzy_controller",
"fuzzy_controller_set : fuzzy_controller_set fuzzy_controller",
"fuzzy_controller : ID '<' CONST_INT '>' ':' BEGIN fuzzy_controller_body END ';'",
"fuzzy_controller : ID ':' BEGIN fuzzy_controller_body END ';'",
"fuzzy_controller : ID '<' CONST_INT '>' ':' BEGIN fuzzy_controller_body END",
"fuzzy_controller_body : var_declarations fuzz_part rules_part defuzz_part",
"fuzzy_controller_body : fuzz_part rules_part defuzz_part",
"fuzzy_controller_body : var_declarations rules_part defuzz_part",
"fuzzy_controller_body : var_declarations fuzz_part defuzz_part",
"fuzzy_controller_body : var_declarations fuzz_part rules_part",
"var_declarations : DECLARE '{' var_list '}'",
"var_declarations : DECLARE '{' '}'",
"var_declarations : DECLARE",
"var_list : var_list var",
"var_list : var",
"var : ID ':' var_type ';'",
"var : ID ':' var_type",
"var_type : IN",
"var_type : OUT",
"fuzz_part : FUZZ '{' fuzz_declaration_list '}'",
"fuzz_part : FUZZ '{' '}'",
"fuzz_part : FUZZ",
"fuzz_declaration_list : fuzz_declaration_list fuzz_declaration",
"fuzz_declaration_list : fuzz_declaration",
"fuzz_declaration : ID '{' fuzzy_set_list '}' ';'",
"fuzz_declaration : ID '{' fuzzy_set_list '}'",
"fuzzy_set_list : fuzzy_set_list fuzzy_set",
"fuzzy_set_list : fuzzy_set",
"fuzzy_set : ID '=' fuzz_type '(' parameter_list ')' ';'",
"fuzzy_set : ID '=' fuzz_type '(' parameter_list ')'",
"fuzz_type : TRIANGULAR",
"fuzz_type : SINGLETON",
"fuzz_type : S_SHAPE",
"fuzz_type : Z_SHAPE",
"fuzz_type : TRAPEZOID",
"parameter_list : parameter_list ',' parameter",
"parameter_list : parameter",
"parameter : CONST_INT",
"rules_part : RULES '<' rule_method '>' '{' rules_form rules_set '}'",
"rules_part : RULES '<' rule_method '>' '{' rules_set '}'",
"rules_part : RULES '<' rule_method '>' '{' '}'",
"rules_part : RULES '<' rule_method '>'",
"rules_part : RULES '{' rules_form rules_set '}'",
"rule_method : MINMAX",
"rule_method : MANDANI",
"rules_form : '(' expression '=' result ')'",
"rules_set : rules_set rule",
"rules_set : rule",
"rule : expression '=' result ';'",
"rule : expression '=' result",
"expression : ID",
"expression : expression '*' ID",
"result : ID",
"result : result ',' ID",
"defuzz_part : DEFUZZ '{' defuzz_declaration_list '}'",
"defuzz_part : DEFUZZ '{' '}'",
"defuzz_part : DEFUZZ",
"defuzz_declaration_list : defuzz_declaration_list defuzz_declaration",
"defuzz_declaration_list : defuzz_declaration",
"defuzz_declaration : ID '=' defuzz_type ';'",
"defuzz_declaration : ID '=' defuzz_type",
"defuzz_type : CENTROID",
};

//#line 160 ".\Gramatica.y"

private LexicalAnalyzer lex;
private EvalMethod method;
private HashMap<String, Defuzzifier> defuzz;
private String filePath;

private Vector<Integer> fuzzySetParameters;
private Vector<FuzzySet> fuzzySetList;
private FuzzySet currentFuzzSet;
private int numberOfVars = 0;

private Vector<String> expressionList;
private Vector<String> resultList;
private String[] ruleOrder;
private HashSet<String> rulesCombinations;
private int rulesMatrix[][];
private int matrixRow = 0;

private Defuzzifier currentDefuzz;
private int numberOfDefuzzifiers = 0;

private boolean errorsFound = false; //Any error found -> Avoid generating code
private boolean fuzzSetErrors = false; //Errors found inside a fuzz declaration -> Avoid creating faulty var
private boolean ruleFormErrors = false; //Errors found on rule form sentence -> Avoid cheking list of rules
private boolean rulesFormDefined = false; //Rules form is not defined -> Used for syntax errors 


// General ----------------------------------------------
public Parser (String filePath) {
	this.filePath = filePath;
	lex = new LexicalAnalyzer(filePath, this);
	fuzzySetParameters = new Vector<Integer>();
	fuzzySetList = new Vector<FuzzySet>();
	expressionList = new Vector<String>();
	resultList = new Vector<String>();
	rulesCombinations = new HashSet<String>();
	defuzz = new HashMap<String, Defuzzifier>();
}

public void yyout(String s) {
	MainFrame.printOutput("Line: " + LexicalAnalyzer.lineNumber + " DEBUG: " + s);
}
	
public void yyerror(String s) {
	errorsFound = true;
	MainFrame.printOutput("Line: " + LexicalAnalyzer.lineNumber + " ERROR: " + s);
}

public void yywarning(String s) {
	MainFrame.printOutput("Line: " + LexicalAnalyzer.lineNumber + " WARNING: " + s);
}

public void debugMode(){
	yydebug = true;
}

private int yylex() {
	return lex.yylex();
}

public int compile(){
	return yyparse();
}

public void setLexeme(ParserVal lexeme){
	this.yylval = lexeme;
}

public boolean hasErrors(){
	return errorsFound;
}
//---------------------------------------------------------


// Declare ------------------------------------------------

private void addDeclaration(String type, String name){
	if(!Compiler.table.get(name).varDeclared){
		Compiler.table.get(name).idRole = "variable";
		Compiler.table.get(name).varType = type;
		Compiler.table.get(name).varDeclared = true;
		numberOfVars++;
	}
	else{
		errorsFound = true;
		yyerror("Redeclared variable");
	}
}
//---------------------------------------------------------


// Fuzz ---------------------------------------------------

							 
private void addFuzzySetParameter(String parameter){
	fuzzySetParameters.add(Integer.parseInt(parameter));
}

private void addFuzzySet(String fuzzSetId){
	boolean error = false;
	for(int i=0; i<fuzzySetList.size(); i++){
		if(fuzzySetList.get(i).getName().equals(fuzzSetId)){
			yyerror("Fuzzy set already declared");
			error = true;
			fuzzSetErrors = true;
		}
	}
	if(!error){
		if(currentFuzzSet.setParameters(fuzzySetParameters)){
			currentFuzzSet.setName(fuzzSetId);
			fuzzySetList.add(currentFuzzSet);
		}
		else{
			yyerror("Incorrect number of parameters");
			fuzzSetErrors = true;
		}
	}
}

private void changeFuzzySetTableParams(String varName){
	for(int i=0; i<fuzzySetList.size(); i++){
		String setName = fuzzySetList.get(i).getName();
		LexemeInfo newLex = new LexemeInfo(LexicalAnalyzer.idToken, "id");
		newLex.idRole = "fuzz_set";
		newLex.setDeclared = true;
		newLex.fuzzSetPosition = i;
		newLex.fuzzType = fuzzySetList.get(i).getType();
		
		Compiler.table.put(varName + "_" + setName, newLex);
	}

}


private void addVarFuzzDefinition(String varId){
	if(!fuzzSetErrors){
		if(Compiler.table.get(varId).varDeclared){
			if(!Compiler.table.get(varId).fuzzDeclared){
				Variable newVar = new Variable(varId);
				changeFuzzySetTableParams(varId);
				newVar.addSetVector(fuzzySetList);
				if(Compiler.table.get(varId).varType.equals("IN"))
					IOVars.inVars.add(newVar);
				else
					IOVars.outVars.add(newVar);
				Compiler.table.get(varId).fuzzDeclared = true;
				Compiler.table.get(varId).varSize = fuzzySetList.size();
				Compiler.table.get(varId).isSingleton = newVar.isSingleton(); 
				numberOfVars--;
			}
			else
				yyerror("Variable " + varId + " fuzz sets already declared");
		}
		else
			yyerror("Variable " + varId + " not declared");
	}
		
	fuzzySetList.clear();
	fuzzSetErrors = false;
}

private void checkMissingDeclarations(){
	if(numberOfVars != 0)
		yyerror("Missing variable fuzz declaration");

}
//---------------------------------------------------------

// Rules --------------------------------------------------
private void generateRulesCombinations() {
	
	if(!errorsFound){
		int[] positions = new int[IOVars.inVars.size()];
		for(int i=0; i<positions.length; i++)
			positions[i] = 0;
		
		int possibilities = 1;
		
		for(int i=0; i<IOVars.inVars.size(); i++)
			possibilities = possibilities * IOVars.inVars.get(i).getSize();
		
		String newPos = "";
		for(int j=0; j<possibilities; j++) {
			
			newPos = Arrays.stream(positions)
					.mapToObj(String::valueOf)
					.collect(Collectors.joining(","));
			
			rulesCombinations.add(newPos);
			
			boolean nextElem = true;
			for(int i=positions.length - 1; i>=0; i--) {
				if(nextElem) {
					positions[i]++;
					if(positions[i] == Compiler.table.get(ruleOrder[i]).varSize)
						positions[i] = 0;
					else
						nextElem = false;
				}
			}
		}
	}
	
}

private void addExpression(String exp){
	expressionList.add(exp);
}

private void addResult(String res){
	resultList.add(res);
}

private void checkRulesForm(){ 									
	
	ruleOrder = new String[IOVars.inVars.size() + IOVars.outVars.size()];
	
	int inVarsUsed = 0;
	for(int i=0; i<expressionList.size(); i++){
		LexemeInfo inVar = Compiler.table.get(expressionList.get(i));
		if(inVar.fuzzDeclared && inVar.varType.equals("IN") && inVar.idRole.equals("variable")){
			if(!inVar.useInRule){
				ruleOrder[i] = expressionList.get(i);
				inVar.useInRule = true;
				inVarsUsed++;
			}
			else{
				yyerror("Variable " + expressionList.get(i) + " already used as part of the expression");
				ruleFormErrors = true;
			}
		}
		else{
			if (!inVar.fuzzDeclared) yyerror("Variable " + expressionList.get(i) + " was not declared or fuzz defined");
			else{
				if (!inVar.varType.equals("IN")) yyerror("Variable " + expressionList.get(i) + " is an ouput variable");
				if (!inVar.idRole.equals("variable")) yyerror("Variable " + expressionList.get(i) + " is not a variable");
			}
			ruleFormErrors = true;
		}			
	}
	
	if(inVarsUsed < IOVars.inVars.size()){
		yyerror("Missing input variables on left side");
		ruleFormErrors = true;
	}
	else{
		if(inVarsUsed == 1){
			yyerror("Rules must have more than one input variable");
			ruleFormErrors = true;
		}
	}
	

	int outVarsUsed = 0;
	for(int i=0; i<resultList.size(); i++){
		LexemeInfo outVar = Compiler.table.get(resultList.get(i));
		if(outVar.fuzzDeclared && outVar.varType.equals("OUT") && outVar.idRole.equals("variable")){
			if(!outVar.useInRule){
				ruleOrder[IOVars.inVars.size() + i] = resultList.get(i);
				outVar.useInRule = true;
				outVarsUsed++;
			}
			else{
			yyerror("Variable " + resultList.get(i) + " already used as result");
				ruleFormErrors = true;
			}
		}
		else{
			if (!outVar.fuzzDeclared) yyerror("Variable " + resultList.get(i) + " was not declared or fuzz defined");
			else{				
				if (!outVar.varType.equals("OUT")) yyerror("Variable " + resultList.get(i) + " is an input variable");
				if (!outVar.idRole.equals("variable")) yyerror("Variable " + resultList.get(i) + " is not a variable");
			}
			ruleFormErrors = true;
		}
		
	}
	
	if(outVarsUsed < IOVars.outVars.size()){
		yyerror("Missing output variables on right side");
		ruleFormErrors = true;
	}
	
	rulesFormDefined = true;
}

private void initMatrix(){

	int rows = 0;
	int columns = 0;
	
	rows = IOVars.inVars.get(0).getSize();
	for(int i = 1; i < IOVars.inVars.size(); i++) {
		rows = rows * IOVars.inVars.get(i).getSize();
	}
	columns = IOVars.inVars.size() + IOVars.outVars.size();
	
	rulesMatrix = new int[rows][columns];
	
}

private void checkRule(){
	
	String combination = null;
	int inVarsUsed = 0;
	for(int i=0; i<expressionList.size(); i++){
		String fuzzySetName = ruleOrder[i] + "_" + expressionList.get(i);
		if(Compiler.table.containsKey(fuzzySetName)){
			if(!errorsFound){
				rulesMatrix[matrixRow][i] = Compiler.table.get(fuzzySetName).fuzzSetPosition;
				inVarsUsed++;				
				if(combination == null)
					combination = String.valueOf(Compiler.table.get(fuzzySetName).fuzzSetPosition);
				else
					combination = combination + "," + Compiler.table.get(fuzzySetName).fuzzSetPosition;
				
			}
		}
		else{
			yyerror("Fuzzy set " + expressionList.get(i) + " does not exists on variable " + ruleOrder[i]);
		}
	}

	if(!errorsFound && !rulesCombinations.remove(combination)){
		if(inVarsUsed < IOVars.inVars.size() )
			yyerror("Parameters missing on left side. Try using all possible combination with the missing variables.");
		else
			yywarning("Rule: "+ combination + " already declared");
	}
	
	int outVarsUsed = 0;
	for(int i=0; i<resultList.size(); i++){
		String fuzzySetName = ruleOrder[IOVars.inVars.size() + i] + "_" + resultList.get(i);
		if(Compiler.table.containsKey(fuzzySetName)){
			if(!errorsFound){
				rulesMatrix[matrixRow][IOVars.inVars.size() + i] = Compiler.table.get(fuzzySetName).fuzzSetPosition;
				outVarsUsed++;
			}
		}
		else{
			yyerror("Fuzzy set " + resultList.get(i) + " does not exists on variable " + ruleOrder[IOVars.inVars.size() + i]);
		}
	}
	
	if(!errorsFound && outVarsUsed < IOVars.outVars.size())
		yyerror("Parameters missing on right side");
	
	matrixRow++;
		
}

private void checkMissingRules(){
	if(rulesCombinations.size() > 0 && !errorsFound)
		yywarning("Some rules combinations are missing");
}

//---------------------------------------------------------
// Defuzz -------------------------------------------------


private void addDefuzzifier(String var){
	
	if(Compiler.table.containsKey(var)){
		LexemeInfo outVar = Compiler.table.get(var);
		if(outVar.fuzzDeclared && outVar.varType.equals("OUT") &&  outVar.idRole.equals("variable")){
			if(!outVar.defuzzDeclared){
				if(currentDefuzz.getType().equals("Centroid") && Compiler.table.get(var).isSingleton){
					defuzz.put(var, currentDefuzz);
					outVar.defuzzDeclared = true;
					numberOfDefuzzifiers++;
				}
				else
					yyerror("For centroid defuzz all fuzzy set from " + var + " must be singleton" );
			}
			else
				yyerror("Variable " + var + " already declared" );
		}
		else{
			if (!outVar.fuzzDeclared) 
				yyerror("Variable " + var + " was not declared or fuzz defined");
			else{				
				if (!outVar.varType.equals("OUT")) 
					yyerror("Variable " + var + " is an input variable");
				if (!outVar.idRole.equals("variable")) 
					yyerror("Variable " + var + " is not a variable");
			}
		}
	}
	else
		yyerror("Variable " + var + " not exists" );

}

private void checkMissingDefuzz(){
	if(numberOfDefuzzifiers < IOVars.outVars.size())
		yyerror("Missing defuzz declarations");
}

//---------------------------------------------------------
public void generateCode(){
	
	if(!errorsFound){
		String fileName = new File(filePath).getName().split("\\.")[0].replace(" ", "_");
		String outputDirectory = new File(filePath).getParentFile().getAbsolutePath();
		
		String cpppath = outputDirectory + "\\" +  fileName + "_out.cpp";
		String headerpath = outputDirectory + "\\" +  fileName + "_out.h";
		String testpath = outputDirectory + "\\" +  fileName + "_outTB.cpp";

		MainFrame.printOutput("Output directory: " + outputDirectory);
		
		CodeGenerator codeGenerator = new CodeGenerator(method, defuzz);
		codeGenerator.generateCpp(cpppath);
		codeGenerator.generateHeader(headerpath);
		codeGenerator.generateTestBench(testpath, fileName+"_out.h");
	}
}
//#line 770 "Parser.java"
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
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
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
      if (yydebug) debug("reduce");
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
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 33 ".\Gramatica.y"
{ generateCode(); }
break;
case 2:
//#line 36 ".\Gramatica.y"
{yyout("-- Controller");}
break;
case 3:
//#line 37 ".\Gramatica.y"
{yyout("-- Controller");}
break;
case 4:
//#line 40 ".\Gramatica.y"
{ IOVars.converterSize = Integer.parseInt(val_peek(6).sval); }
break;
case 5:
//#line 41 ".\Gramatica.y"
{ yyerror("Missing converter size for this controller"); }
break;
case 6:
//#line 42 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 7:
//#line 45 ".\Gramatica.y"
{}
break;
case 8:
//#line 46 ".\Gramatica.y"
{yyerror("Missing declaration segment");}
break;
case 9:
//#line 47 ".\Gramatica.y"
{yyerror("Missing fuzz segment");}
break;
case 10:
//#line 48 ".\Gramatica.y"
{yyerror("Missing rules segment");}
break;
case 11:
//#line 49 ".\Gramatica.y"
{yyerror("Missing defuzz segment");}
break;
case 12:
//#line 54 ".\Gramatica.y"
{ yyout("-- Declare"); }
break;
case 13:
//#line 55 ".\Gramatica.y"
{yyerror("Missing declaration body");}
break;
case 14:
//#line 56 ".\Gramatica.y"
{yyerror("Missing declaration body");}
break;
case 15:
//#line 59 ".\Gramatica.y"
{}
break;
case 16:
//#line 60 ".\Gramatica.y"
{}
break;
case 17:
//#line 63 ".\Gramatica.y"
{addDeclaration(val_peek(1).sval, val_peek(3).sval);}
break;
case 18:
//#line 64 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 20:
//#line 67 ".\Gramatica.y"
{}
break;
case 21:
//#line 72 ".\Gramatica.y"
{ checkMissingDeclarations(); yyout("-- Fuzz"); }
break;
case 22:
//#line 73 ".\Gramatica.y"
{yyerror("Missing fuzz body");}
break;
case 23:
//#line 74 ".\Gramatica.y"
{yyerror("Missing fuzz body");}
break;
case 24:
//#line 77 ".\Gramatica.y"
{}
break;
case 26:
//#line 81 ".\Gramatica.y"
{ addVarFuzzDefinition(val_peek(4).sval); }
break;
case 27:
//#line 82 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 28:
//#line 85 ".\Gramatica.y"
{}
break;
case 29:
//#line 86 ".\Gramatica.y"
{}
break;
case 30:
//#line 89 ".\Gramatica.y"
{ addFuzzySet(val_peek(6).sval); }
break;
case 31:
//#line 90 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 32:
//#line 93 ".\Gramatica.y"
{ currentFuzzSet = new TriangularFS(); }
break;
case 33:
//#line 94 ".\Gramatica.y"
{ currentFuzzSet = new SingletonFS(); }
break;
case 34:
//#line 95 ".\Gramatica.y"
{ currentFuzzSet = new SShapeFS(); }
break;
case 35:
//#line 96 ".\Gramatica.y"
{ currentFuzzSet = new ZShapeFS(); }
break;
case 36:
//#line 97 ".\Gramatica.y"
{ currentFuzzSet = new TrapezoidFS();}
break;
case 37:
//#line 99 ".\Gramatica.y"
{ addFuzzySetParameter(val_peek(0).sval); }
break;
case 38:
//#line 100 ".\Gramatica.y"
{ fuzzySetParameters.clear();
							 addFuzzySetParameter(val_peek(0).sval); }
break;
case 39:
//#line 104 ".\Gramatica.y"
{}
break;
case 40:
//#line 109 ".\Gramatica.y"
{ checkMissingRules();
																	 if(!errorsFound) { method.setMatrix(this.rulesMatrix); }; 
																	 yyout("-- Rules");}
break;
case 41:
//#line 112 ".\Gramatica.y"
{ yyerror("Missing rules form"); }
break;
case 42:
//#line 113 ".\Gramatica.y"
{ yyerror("Missing rules body"); }
break;
case 43:
//#line 114 ".\Gramatica.y"
{ yyerror("Missing rules body"); }
break;
case 44:
//#line 115 ".\Gramatica.y"
{ yyerror("Missing rules evaluation method"); }
break;
case 45:
//#line 118 ".\Gramatica.y"
{method = new MinMaxMethod();}
break;
case 46:
//#line 119 ".\Gramatica.y"
{method = new MinMaxMethod();}
break;
case 47:
//#line 122 ".\Gramatica.y"
{ checkRulesForm(); if(!errorsFound) { generateRulesCombinations(); initMatrix(); }}
break;
case 48:
//#line 125 ".\Gramatica.y"
{}
break;
case 49:
//#line 126 ".\Gramatica.y"
{}
break;
case 50:
//#line 129 ".\Gramatica.y"
{ if(!ruleFormErrors && rulesFormDefined){ checkRule(); } }
break;
case 51:
//#line 130 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 52:
//#line 133 ".\Gramatica.y"
{ expressionList.clear(); addExpression(val_peek(0).sval);}
break;
case 53:
//#line 134 ".\Gramatica.y"
{addExpression(val_peek(0).sval);}
break;
case 54:
//#line 137 ".\Gramatica.y"
{resultList.clear(); addResult(val_peek(0).sval);}
break;
case 55:
//#line 138 ".\Gramatica.y"
{addResult(val_peek(0).sval);}
break;
case 56:
//#line 143 ".\Gramatica.y"
{checkMissingDefuzz(); yyout("-- Defuzz");}
break;
case 57:
//#line 144 ".\Gramatica.y"
{ yyerror("Missing defuzz body"); }
break;
case 58:
//#line 145 ".\Gramatica.y"
{ yyerror("Missing defuzz body"); }
break;
case 59:
//#line 148 ".\Gramatica.y"
{}
break;
case 60:
//#line 149 ".\Gramatica.y"
{}
break;
case 61:
//#line 152 ".\Gramatica.y"
{addDefuzzifier(val_peek(3).sval);}
break;
case 62:
//#line 153 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 63:
//#line 156 ".\Gramatica.y"
{currentDefuzz = new CentroidDefuzz();}
break;
//#line 1166 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
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
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
