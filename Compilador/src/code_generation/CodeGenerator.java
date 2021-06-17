package code_generation;

import java.io.IOException;

import code_generation.defuzz.CentroidDefuzz;
import code_generation.evaluation_method.MinMaxMethod;
import code_generation.fuzz.Fuzzifier;
import code_generation.variables.IOVars;
import code_generation.writer.Writer;

public class CodeGenerator {
	
	public void generate() {
				 
		 Fuzzifier fuzzificador = new Fuzzifier();
		 MinMaxMethod motorDeReglas = new MinMaxMethod();
		 motorDeReglas.initMatrix();
		 CentroidDefuzz defuzzificador = new CentroidDefuzz();
		 
		 String path = "C:\\Users\\Luca\\Downloads\\PruebasCompilador\\Test.txt";
		 try {
			 Writer.openFile(path);
			 Writer.file.write( "#include <iostream> \n" + 
					 				 "#include <ap_int.h> \n \n" );
			 
			 Writer.file.write( "typedef ap_uint<" + IOVars.converterSize + "> fixed_int; \n" );
			 
			 for(int i=0; i<IOVars.outVars.size(); i++) {
				 Writer.file.write( "typedef ap_uint<" + IOVars.getAuxTypeSize(i) + 
						 				 "> fixed_aux_"+ IOVars.outVars.get(i).getName() +"; \n" );
			 }
			 
			 Writer.file.write( "typedef ap_uint<" + IOVars.getOutTypeSize() + "> fixed_out; \n");
			 
			 Writer.file.write( "using namespace std; \n \n" );
			 						 
			 					 	 
			 
			 fuzzificador.compileFunctionsSlope();
			 fuzzificador.compilerInputVariablesBuffers();
			 defuzzificador.compileOutputVariablesBuffers();
			 
			 Writer.file.write("\n \n");
			 
			 fuzzificador.compileFuzzifier();
			 motorDeReglas.compileEvalMethod();
			 for(int i=0; i<IOVars.outVars.size(); i++) {
				 defuzzificador.compileDefuzz(i);
			 }
			 
			 Writer.file.write("\n");
			 Writer.file.write("fixed_out fuzzyController(fixed_int " + IOVars.inVars.get(0).getName());
			 for(int i=1; i<IOVars.inVars.size(); i++) {
				 Writer.file.write(", fixed_int " + IOVars.inVars.get(i).getName());
			 }
			 Writer.file.write("){ \n\n");
			 Writer.file.write("\tfuzzifier(" + IOVars.inVars.get(0).getName());
			 for(int i=1; i<IOVars.inVars.size(); i++) {
				 Writer.file.write(", " + IOVars.inVars.get(i).getName());
			 }
			 Writer.file.write(");\n");
			 Writer.file.write("\trulesEvaluation(); \n");
			 
			 Writer.file.write("\n");
			 Writer.file.write("\tfixed_out output = defuzzifier" +  IOVars.outVars.get(0).getName() + "(); \n");
			 
			for(int i=1; i<IOVars.outVars.size(); i++ ){ 
				Writer.file.write("\toutput <<8;\n");
				Writer.file.write("\toutput = output + defuzzifier" + IOVars.outVars.get(i).getName() + "();\n");
			}
			
			Writer.file.write("\treturn output;\n");
			Writer.file.write("}\n");
			 
			 Writer.closeFile();
			 
		 }
		 catch(IOException e) {
			 e.printStackTrace();
		 }
		 
		//motorDeReglas.printMatriz();
		System.out.println("Listo!");
		
		

	}

}
