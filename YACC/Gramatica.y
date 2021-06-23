%{
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
import gui.MainFrame;
%}

%token ID CONST_INT FUZZ BEGIN END DECLARE RULES IN OUT DEFUZZ TRIANGULAR SINGLETON Z_SHAPE S_SHAPE MINMAX MANDANI CENTROID TRAPEZOID

%start program

%%

	/* Base ---------------------------------------------- */
	
program : fuzzy_controller_set { generateCode(); }
;

fuzzy_controller_set : fuzzy_controller {yyout("-- Controller");}
					| fuzzy_controller_set fuzzy_controller {yyout("-- Controller");}
;

fuzzy_controller : ID '<' CONST_INT '>'':' BEGIN fuzzy_controller_body END { IOVars.converterSize = Integer.parseInt($3.sval); }
				| ID ':' BEGIN fuzzy_controller_body END { yyerror("Missing converter size for this controller"); }
;

fuzzy_controller_body : var_declarations fuzz_part rules_part defuzz_part {}
					 | fuzz_part rules_part defuzz_part {yyerror("Missing declaration segment");} //SYNTAX ERROR RULE
					 | var_declarations rules_part defuzz_part {yyerror("Missing fuzz segment");} //SYNTAX ERROR RULE
					 | var_declarations fuzz_part defuzz_part {yyerror("Missing rules segment");} //SYNTAX ERROR RULE
					 | var_declarations fuzz_part rules_part {yyerror("Missing defuzz segment");} //SYNTAX ERROR RULE
;

	/* Declare ---------------------------------------------- */
	
var_declarations : DECLARE '{' var_list '}' { yyout("-- Declare"); }
				 | DECLARE '{' '}' {yyerror("Missing declaration body");} //SYNTAX ERROR RULE
				 | DECLARE {yyerror("Missing declaration body");} //SYNTAX ERROR RULE

var_list : var_list var {}
		 | var {}
;

var : ID ':' var_type ';' {addDeclaration($3.sval, $1.sval);}
	| ID ':' var_type { yyerror("Missing semicolon"); } //SYNTAX ERROR RULE
;

var_type : IN | OUT {}
;

	/* Fuzz ---------------------------------------------- */
	
fuzz_part : FUZZ '{' fuzz_declaration_list '}' { checkMissingDeclarations(); yyout("-- Fuzz"); }
		  | FUZZ '{' '}' {yyerror("Missing fuzz body");} //SYNTAX ERROR RULE
		  | FUZZ {yyerror("Missing fuzz body");} //SYNTAX ERROR RULE
;

fuzz_declaration_list : fuzz_declaration_list fuzz_declaration {}
					  | fuzz_declaration
;

fuzz_declaration : ID '{' fuzzy_set_list '}' ';' { addVarFuzzDefinition($1.sval); }
				 | ID '{' fuzzy_set_list '}' { yyerror("Missing semicolon"); } //SYNTAX ERROR RULE
;

fuzzy_set_list : fuzzy_set_list fuzzy_set {}
			   | fuzzy_set {}
;

fuzzy_set : ID '=' fuzz_type '(' parameter_list ')' ';' { addFuzzySet($1.sval); }
		  | ID '=' fuzz_type '(' parameter_list ')' { yyerror("Missing semicolon"); } //SYNTAX ERROR RULE
;

fuzz_type : TRIANGULAR { currentFuzzSet = new TriangularFS(); } 
		  | SINGLETON { currentFuzzSet = new SingletonFS(); } 
		  | S_SHAPE { currentFuzzSet = new SShapeFS(); }
		  | Z_SHAPE { currentFuzzSet = new ZShapeFS(); }
		  | TRAPEZOID { currentFuzzSet = new TrapezoidFS();}

parameter_list : parameter_list ',' parameter { addFuzzySetParameter($3.sval); }
			   | parameter { fuzzySetParameters.clear();
							 addFuzzySetParameter($1.sval); }
;

parameter : CONST_INT {}
;

	/* Rules ---------------------------------------------- */

rules_part : RULES '<' rule_method '>' '{' rules_form rules_set '}' { checkMissingRules();
																	 if(!errorsFound) { method.setMatrix(this.rulesMatrix); }; 
																	 yyout("-- Rules");}
		   | RULES '<' rule_method '>' '{' rules_set '}' { yyerror("Missing rules form"); } //SYNTAX ERROR RULE
		   | RULES '<' rule_method '>' '{' '}' { yyerror("Missing rules body"); } //SYNTAX ERROR RULE
		   | RULES '<' rule_method '>' { yyerror("Missing rules body"); } //SYNTAX ERROR RULE
		   | RULES '{' rules_form rules_set '}' { yyerror("Missing rules evaluation method"); } //SYNTAX ERROR RULE
;

rule_method : MINMAX {method = new MinMaxMethod();}
			| MANDANI {method = new MinMaxMethod();}
;

rules_form : '(' expression '=' result ')' { checkRulesForm(); if(!errorsFound) { generateRulesCombinations(); initMatrix(); }}
;

rules_set : rules_set rule {}
		  | rule {}
;

rule : expression '=' result ';'  { if(!ruleFormErrors && rulesFormDefined){ checkRule(); } }
	 | expression '=' result  { yyerror("Missing semicolon"); } //SYNTAX ERROR RULE

expression : ID { expressionList.clear(); addExpression($1.sval);}
		   | expression '*' ID {addExpression($3.sval);}
;

result : ID {resultList.clear(); addResult($1.sval);}
	   | result ',' ID {addResult($3.sval);}
;

	/* Defuzz ---------------------------------------------- */

defuzz_part : DEFUZZ '{' defuzz_declaration_list '}' {checkMissingDefuzz(); yyout("-- Defuzz");}
			| DEFUZZ '{' '}' { yyerror("Missing defuzz body"); } //SYNTAX ERROR RULE
			| DEFUZZ { yyerror("Missing defuzz body"); } //SYNTAX ERROR RULE
;

defuzz_declaration_list : defuzz_declaration_list defuzz_declaration {}
						| defuzz_declaration {}
;

defuzz_declaration : ID '=' defuzz_type ';' {addDefuzzifier($1.sval);}
				   | ID '=' defuzz_type { yyerror("Missing semicolon"); } //SYNTAX ERROR RULE
;

defuzz_type : CENTROID {currentDefuzz = new CentroidDefuzz();}
;

%%

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
		
		String[] stringArray = filePath.split("\\.");
		stringArray[stringArray.length - 2] = stringArray[stringArray.length - 2] + "_output";
		String outpath = String.join(".", stringArray);
		MainFrame.printOutput("Output filepath: " + outpath);
		
		CodeGenerator codeGenerator = new CodeGenerator(method, defuzz);
		codeGenerator.generate(outpath);
	}
}
