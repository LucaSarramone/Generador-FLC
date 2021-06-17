%{
package parser;

import lexical_analysis.LexicalAnalyzer;
import java.util.Vector;

import code_generation.variables.IOVars;
import code_generation.variables.Variable;
import code_generation.variables.fuzzy_set.*;
import compiler.Compiler;

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

fuzzy_controler : ID '<' CONST_INT '>'':' BEGIN fuzzy_controler_body END ';' { }//AÑADIR LA CANTIDAD DE BITS AL IO.
;

fuzzy_controler_body : var_declarations fuzz_part rules_part defuzz_part {}
;

	/* Declare ---------------------------------------------- */
	
var_declarations : DECLARE '{' var_list '}' { yyout("-- Declare"); }

var_list : var_list var {}
		 | var {}
;

var : ID ':' var_type ';' {addDeclaration($3.sval, $1.sval);}
;

var_type : IN | OUT {}
;

	/* Fuzz ---------------------------------------------- */
	
fuzz_part : FUZZ '{' fuzz_declaration_list '}' { checkMissingDeclarations(); yyout("-- Fuzzificador");}
;

fuzz_declaration_list : fuzz_declaration_list fuzz_declaration {}
					  | fuzz_declaration
;

fuzz_declaration : ID '{' fuzzy_set_list '}' ';' {addFuzzVariable($1.sval);}
;

fuzzy_set_list : fuzzy_set_list fuzzy_set {}
			   | fuzzy_set {}
;

fuzzy_set : ID '=' fuzz_type '(' parameter_list ')' ';' { addFuzzySet($1.sval); }
;

fuzz_type : TRIANGULAR {currentFuzzSet = new TriangularFS();} 
		  | SINGLETON {currentFuzzSet = new SingletonFS();} 
		  | S_SHAPE {currentFuzzSet = new SShapeFS();}
		  | Z_SHAPE {currentFuzzSet = new ZShapeFS();}

parameter_list : parameter_list ',' parameter {addFuzzySetParameter($3.sval);}
			   | parameter { clearFuzzySetParameterList();
							 addFuzzySetParameter($1.sval);}
;

parameter : CONST_INT {}
;

	/* Rules ---------------------------------------------- */

rules_part : RULES '<' rule_method '>' '{' rules_set '}' {yyout("-- Motor de reglas");}
;

rule_method : MINMAX | MANDANI {}
;

rules_set : rules_set rule {}
		  | rule {}
;

rule : expression '=' result ';' {}

expression : ID {}
		   | expression '*' ID {}
;

result : ID {}
	   | result ',' ID {}
;

	/* Defuzz ---------------------------------------------- */

defuzz_part : DEFUZZ '{' defuzz_declaration_list '}' {yyout("-- Defuzzificador");}
;

defuzz_declaration_list : defuzz_declaration_list defuzz_declaration {}
						| defuzz_declaration {}
;

defuzz_declaration : ID '=' defuzz_type ';' {}
;

defuzz_type : CENTROID {}
;

%%

private LexicalAnalyzer lex;
private Vector<Integer> fuzzySetParameters;
private Vector<FuzzySet> fuzzySetList;
private FuzzySet currentFuzzSet;

private int numberOfVars = 0;

private boolean errorsFound = false; //Any error found -> Avoid generating code
private boolean fuzzSetErrors = false; //Errods found inside a fuzz declaration -> Avoid creating faulty var


// General ----------------------------------------------
public Parser (String filePath) {
	lex = new LexicalAnalyzer(filePath, this);
	fuzzySetParameters = new Vector<Integer>();
	fuzzySetList = new Vector<FuzzySet>();
}

public void yyout(String s) {
	System.out.println("DEBUG: " + s);
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
		yyout("Variable declarada: " +  name);
	}
	else{
		errorsFound = true;
		yyerror("Redeclared variable");
	}
}
//---------------------------------------------------------


// Fuzz ---------------------------------------------------
public void clearFuzzySetParameterList(){
	fuzzySetParameters.clear();
}
							 
public void addFuzzySetParameter(String parameter){
	yyout(parameter);
	fuzzySetParameters.add(Integer.parseInt(parameter));
}

public void addFuzzySet(String fuzzSetId){
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
			Compiler.table.get(fuzzSetId).idRole = "fuzz_set";
		}
		else{
			yyerror("Incorrect number of parameters");
			fuzzSetErrors = true;
		}
	}
}

public void addFuzzVariable(String varId){
	if(!fuzzSetErrors){
		if(Compiler.table.get(varId).varDeclared){
			if(!Compiler.table.get(varId).fuzzDeclared){
				Variable newVar = new Variable(varId);
				newVar.addSetVector(fuzzySetList);
				if(Compiler.table.get(varId).varType.equals("IN"))
					IOVars.inVars.add(newVar);
				else
					IOVars.outVars.add(newVar);
				Compiler.table.get(varId).fuzzDeclared = true;
				numberOfVars--;
			}
			else
				yyerror("Variable" + varId + "redeclared");
		}
		else
			yyerror("Variable " + varId + " not declared");
	}
		
	fuzzySetList.clear();
	fuzzSetErrors = false;
}

public void checkMissingDeclarations(){
	if(numberOfVars != 0)
		yyerror("Missing variable fuzz declaration");

}
//---------------------------------------------------------

public void generateCode(){
	for(int i=0; i<IOVars.inVars.size(); i++)
		yyout(IOVars.inVars.get(i).toString());
	
	for(int i=0; i<IOVars.outVars.size(); i++)
		yyout(IOVars.outVars.get(i).toString());
	
	
}
