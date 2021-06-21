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


import lexical_analysis.LexicalAnalyzer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.stream.Collectors;

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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    3,    4,    8,    8,    9,    9,
   10,   10,    5,   11,   11,   12,   12,   13,   13,   14,
   14,   15,   15,   15,   15,   16,   16,   17,    6,   18,
   18,   19,   20,   20,   23,   23,   21,   21,   22,   22,
    7,   24,   24,   25,   25,   26,
};
final static short yylen[] = {                            2,
    1,    1,    2,    8,    4,    4,    2,    1,    4,    3,
    1,    1,    4,    2,    1,    5,    4,    2,    1,    7,
    6,    1,    1,    1,    1,    3,    1,    1,    8,    1,
    1,    5,    2,    1,    4,    3,    1,    3,    1,    3,
    4,    2,    1,    4,    3,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    2,    0,    3,    0,    0,    0,    0,
    0,    0,    0,    0,    4,    0,    0,    0,    0,    8,
    0,    0,    0,    0,    6,    7,    0,    0,   15,    0,
    0,    5,   11,   12,    0,    0,   13,   14,   30,   31,
    0,    0,    9,    0,    0,   19,    0,    0,    0,   43,
    0,    0,   18,    0,    0,   41,   42,   22,   23,   25,
   24,    0,   16,    0,    0,   46,    0,    0,   37,    0,
    0,    0,   34,   44,   28,    0,   27,    0,    0,   29,
   33,    0,    0,    0,   39,    0,   38,    0,   20,   26,
   32,    0,   35,   40,
};
final static short yydgoto[] = {                          2,
    3,    4,   12,   13,   17,   23,   32,   19,   20,   35,
   28,   29,   45,   46,   62,   76,   77,   41,   65,   71,
   72,   86,   73,   49,   50,   67,
};
final static short yysindex[] = {                      -238,
  -34,    0, -238,    0, -231,    0,  -30,  -25, -232, -228,
  -88, -225, -222, -219,    0,  -84, -223,  -17, -125,    0,
 -215,  -16, -221, -242,    0,    0,  -80, -124,    0, -247,
  -77,    0,    0,    0,  -12, -209,    0,    0,    0,    0,
  -13, -207,    0,  -10, -123,    0,  -71,   -8, -122,    0,
 -255,   -5,    0,   15, -217,    0,    0,    0,    0,    0,
    0,   17,    0, -199, -199,    0,    1, -197,    0,  -32,
 -121,  -31,    0,    0,    0,  -24,    0, -198, -195,    0,
    0, -198,    4, -197,    0,  -23,    0,  -28,    0,    0,
    0, -193,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,   65,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -120,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -119,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -118,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -117,    0,    0,    0,    0, -116,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   63,    0,    0,    0,    0,    0,    0,   48,    0,
    0,   40,    0,   24,    0,    0,  -14,    0,    0,    0,
    7,   -9,    3,    0,   23,    0,
};
final static int YYTABLESIZE=141;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         25,
   37,   52,   56,   80,   10,   17,   45,   21,   36,   79,
   79,   58,   59,   60,   61,   92,   83,   91,    1,   84,
   92,   33,   34,   39,   40,    5,    7,   10,   78,   82,
   93,    8,    9,   11,   14,   15,   16,   18,   21,   22,
   24,   27,   36,   30,   31,   42,   43,   44,   47,   48,
   51,   54,   55,   63,   64,   66,   68,   69,   85,   74,
   75,   87,   89,   94,    1,    6,   26,   38,   53,   90,
   70,   57,   88,   81,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   18,   27,   44,   48,   69,   10,   17,   45,   21,
   36,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        125,
  125,  125,  125,  125,  125,  125,  125,  125,  125,   42,
   42,  267,  268,  269,  270,   44,   41,   41,  257,   44,
   44,  264,  265,  271,  272,   60,  258,  260,   61,   61,
   59,   62,   58,  262,  123,  261,  259,  257,  123,  263,
   58,  257,  123,   60,  266,  123,   59,  257,   62,  257,
   61,  123,   61,   59,   40,  273,   40,  257,  257,   59,
  258,  257,   59,  257,    0,    3,   19,   28,   45,   84,
   64,   49,   82,   71,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  257,  257,  257,  257,  257,  257,  257,  257,
  257,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=273;
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
"S_SHAPE","MINMAX","MANDANI","CENTROID",
};
final static String yyrule[] = {
"$accept : program",
"program : fuzzy_controler_set",
"fuzzy_controler_set : fuzzy_controler",
"fuzzy_controler_set : fuzzy_controler_set fuzzy_controler",
"fuzzy_controler : ID '<' CONST_INT '>' ':' BEGIN fuzzy_controler_body END",
"fuzzy_controler_body : var_declarations fuzz_part rules_part defuzz_part",
"var_declarations : DECLARE '{' var_list '}'",
"var_list : var_list var",
"var_list : var",
"var : ID ':' var_type ';'",
"var : ID ':' var_type",
"var_type : IN",
"var_type : OUT",
"fuzz_part : FUZZ '{' fuzz_declaration_list '}'",
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
"parameter_list : parameter_list ',' parameter",
"parameter_list : parameter",
"parameter : CONST_INT",
"rules_part : RULES '<' rule_method '>' '{' rules_form rules_set '}'",
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
"defuzz_declaration_list : defuzz_declaration_list defuzz_declaration",
"defuzz_declaration_list : defuzz_declaration",
"defuzz_declaration : ID '=' defuzz_type ';'",
"defuzz_declaration : ID '=' defuzz_type",
"defuzz_type : CENTROID",
};

//#line 141 ".\Gramatica.y"

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
	System.out.println(LexicalAnalyzer.lineNumber + " DEBUG: " + s);
}
	
public void yyerror(String s) {
	errorsFound = true;
	System.out.println(LexicalAnalyzer.lineNumber + " ERROR: " + s);
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


private void addFuzzVariable(String varId){
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
					if(positions[i] == IOVars.inVars.get(i).getSize()) 
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
			if (!inVar.fuzzDeclared) yyerror("Variable " + expressionList.get(i) + " was not declared");
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
			if (!outVar.fuzzDeclared) yyerror("Variable " + resultList.get(i) + " was not declared");
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
}

private void initMatrix(){
	if(!errorsFound){ 
		int rows = 0;
		int columns = 0;
		
		rows = IOVars.inVars.get(0).getSize();
		for(int i = 1; i < IOVars.inVars.size(); i++) {
			rows = rows * IOVars.inVars.get(i).getSize();
		}
		columns = IOVars.inVars.size() + IOVars.outVars.size();
		
		rulesMatrix = new int[rows][columns];
	}
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
			yyerror("Parameters missing on left side");
		else
			yyerror("Rule: "+ combination + " already declared");
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
		yyerror("Missing rules declarations"); //Se puede desambiguar;
}

//---------------------------------------------------------
// Defuzz -------------------------------------------------


private void addDefuzzifier(String var){
	
	if(Compiler.table.containsKey(var)){
	LexemeInfo outVar = Compiler.table.get(var);
		if(outVar.fuzzDeclared && outVar.varType.equals("OUT") &&  outVar.idRole.equals("variable")){
			if(!outVar.defuzzDeclared){
				defuzz.put(var, currentDefuzz);
				outVar.defuzzDeclared = true;
				numberOfDefuzzifiers++;
			}
			else
				yyerror("Variable " + var + " already declared" );
		}
		else{
			if (!outVar.fuzzDeclared) 
				yyerror("Variable " + var + " was not declared");
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
	// for(int i=0; i<IOVars.inVars.size(); i++)
		// yyout(IOVars.inVars.get(i).toString());
	
	// for(int i=0; i<IOVars.outVars.size(); i++)
		// yyout(IOVars.outVars.get(i).toString());
	
	if(!errorsFound)
		for(int i=0; i<rulesMatrix.length; i++) {
			for(int j=0; j<rulesMatrix[i].length; j++) {
				System.out.print(rulesMatrix[i][j] + " | ");
			}
			System.out.println("");
		}
	
	if(!errorsFound){
		
		String[] stringArray = filePath.split("\\.");
		stringArray[stringArray.length - 2] = stringArray[stringArray.length - 2] + "_output";
		String outpath = String.join(".", stringArray);
		yyout(outpath);
		
		CodeGenerator codeGenerator = new CodeGenerator(method, defuzz);
		codeGenerator.generate(outpath);
	}
	

}
//#line 705 "Parser.java"
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
{yyout("-- Controlador");}
break;
case 3:
//#line 37 ".\Gramatica.y"
{yyout("-- Controlador");}
break;
case 4:
//#line 40 ".\Gramatica.y"
{ IOVars.converterSize = Integer.parseInt(val_peek(5).sval); }
break;
case 5:
//#line 43 ".\Gramatica.y"
{}
break;
case 6:
//#line 48 ".\Gramatica.y"
{ yyout("-- Declare"); }
break;
case 7:
//#line 50 ".\Gramatica.y"
{}
break;
case 8:
//#line 51 ".\Gramatica.y"
{}
break;
case 9:
//#line 54 ".\Gramatica.y"
{addDeclaration(val_peek(1).sval, val_peek(3).sval);}
break;
case 10:
//#line 55 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 12:
//#line 58 ".\Gramatica.y"
{}
break;
case 13:
//#line 63 ".\Gramatica.y"
{ checkMissingDeclarations(); generateRulesCombinations(); yyout("-- Fuzzificador"); }
break;
case 14:
//#line 66 ".\Gramatica.y"
{}
break;
case 16:
//#line 70 ".\Gramatica.y"
{ addFuzzVariable(val_peek(4).sval); }
break;
case 17:
//#line 71 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 18:
//#line 74 ".\Gramatica.y"
{}
break;
case 19:
//#line 75 ".\Gramatica.y"
{}
break;
case 20:
//#line 78 ".\Gramatica.y"
{ addFuzzySet(val_peek(6).sval); }
break;
case 21:
//#line 79 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 22:
//#line 82 ".\Gramatica.y"
{ currentFuzzSet = new TriangularFS(); }
break;
case 23:
//#line 83 ".\Gramatica.y"
{ currentFuzzSet = new SingletonFS(); }
break;
case 24:
//#line 84 ".\Gramatica.y"
{ currentFuzzSet = new SShapeFS(); }
break;
case 25:
//#line 85 ".\Gramatica.y"
{ currentFuzzSet = new ZShapeFS(); }
break;
case 26:
//#line 87 ".\Gramatica.y"
{ addFuzzySetParameter(val_peek(0).sval); }
break;
case 27:
//#line 88 ".\Gramatica.y"
{ fuzzySetParameters.clear();
							 addFuzzySetParameter(val_peek(0).sval); }
break;
case 28:
//#line 92 ".\Gramatica.y"
{}
break;
case 29:
//#line 97 ".\Gramatica.y"
{ checkMissingRules();
																	 if(!errorsFound) { method.setMatrix(this.rulesMatrix); }; 
																	 yyout("-- Motor de reglas");}
break;
case 30:
//#line 102 ".\Gramatica.y"
{method = new MinMaxMethod();}
break;
case 31:
//#line 103 ".\Gramatica.y"
{method = new MinMaxMethod();}
break;
case 32:
//#line 106 ".\Gramatica.y"
{ checkRulesForm(); initMatrix();}
break;
case 33:
//#line 109 ".\Gramatica.y"
{}
break;
case 34:
//#line 110 ".\Gramatica.y"
{}
break;
case 35:
//#line 113 ".\Gramatica.y"
{ if(!ruleFormErrors){ checkRule(); } }
break;
case 36:
//#line 114 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 37:
//#line 116 ".\Gramatica.y"
{ expressionList.clear(); addExpression(val_peek(0).sval);}
break;
case 38:
//#line 117 ".\Gramatica.y"
{addExpression(val_peek(0).sval);}
break;
case 39:
//#line 120 ".\Gramatica.y"
{resultList.clear(); addResult(val_peek(0).sval);}
break;
case 40:
//#line 121 ".\Gramatica.y"
{addResult(val_peek(0).sval);}
break;
case 41:
//#line 126 ".\Gramatica.y"
{checkMissingDefuzz(); yyout("-- Defuzzificador");}
break;
case 42:
//#line 129 ".\Gramatica.y"
{}
break;
case 43:
//#line 130 ".\Gramatica.y"
{}
break;
case 44:
//#line 133 ".\Gramatica.y"
{addDefuzzifier(val_peek(3).sval);}
break;
case 45:
//#line 134 ".\Gramatica.y"
{ yyerror("Missing semicolon"); }
break;
case 46:
//#line 137 ".\Gramatica.y"
{currentDefuzz = new CentroidDefuzz();}
break;
//#line 1033 "Parser.java"
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
