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
%}

%token ID CONST_INT FUZZ BEGIN END DECLARE RULES IN OUT DEFUZZ TRIANGULAR SINGLETON Z_SHAPE S_SHAPE MINMAX MANDANI CENTROID

%start program

%%

	/* Base ---------------------------------------------- */
	
program : fuzzy_controler_set { generateCode(); }
;

fuzzy_controler_set : fuzzy_controler {yyout("-- Controlador");}
					| fuzzy_controler_set fuzzy_controler {yyout("-- Controlador");}
;

fuzzy_controler : ID '<' CONST_INT '>'':' BEGIN fuzzy_controler_body END { IOVars.converterSize = Integer.parseInt($3.sval); }
;

fuzzy_controler_body : var_declarations fuzz_part rules_part defuzz_part {}
;

	/* Declare ---------------------------------------------- */
	
var_declarations : DECLARE '{' var_list '}' { yyout("-- Declare"); }

var_list : var_list var {}
		 | var {}
;

var : ID ':' var_type ';' {addDeclaration($3.sval, $1.sval);}
	| ID ':' var_type { yyerror("Missing semicolon"); } //SYNTAX ERROR RULE
;

var_type : IN | OUT {}
;

	/* Fuzz ---------------------------------------------- */
	
fuzz_part : FUZZ '{' fuzz_declaration_list '}' { checkMissingDeclarations(); generateRulesCombinations(); yyout("-- Fuzzificador"); }
;

fuzz_declaration_list : fuzz_declaration_list fuzz_declaration {}
					  | fuzz_declaration
;

fuzz_declaration : ID '{' fuzzy_set_list '}' ';' { addFuzzVariable($1.sval); }
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

parameter_list : parameter_list ',' parameter { addFuzzySetParameter($3.sval); }
			   | parameter { fuzzySetParameters.clear();
							 addFuzzySetParameter($1.sval); }
;

parameter : CONST_INT {}
;

	/* Rules ---------------------------------------------- */

rules_part : RULES '<' rule_method '>' '{' rules_form rules_set '}' { checkMissingRules();
																	 if(!errorsFound) { method.setMatrix(this.rulesMatrix); }; 
																	 yyout("-- Motor de reglas");}
;

rule_method : MINMAX {method = new MinMaxMethod();}
			| MANDANI {method = new MinMaxMethod();}
;

rules_form : '(' expression '=' result ')' { checkRulesForm(); initMatrix();}
;

rules_set : rules_set rule {}
		  | rule {}
;

rule : expression '=' result ';'  { if(!ruleFormErrors){ checkRule(); } }
	 | expression '=' result  { yyerror("Missing semicolon"); } //SYNTAX ERROR RULE

expression : ID { expressionList.clear(); addExpression($1.sval);}
		   | expression '*' ID {addExpression($3.sval);}
;

result : ID {resultList.clear(); addResult($1.sval);}
	   | result ',' ID {addResult($3.sval);}
;

	/* Defuzz ---------------------------------------------- */

defuzz_part : DEFUZZ '{' defuzz_declaration_list '}' {checkMissingDefuzz(); yyout("-- Defuzzificador");}
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
