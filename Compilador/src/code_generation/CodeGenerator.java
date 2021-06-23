package code_generation;

import java.io.IOException;
import java.util.HashMap;

import code_generation.defuzz.Defuzzifier;
import code_generation.evaluation_method.EvalMethod;
import code_generation.fuzz.Fuzzifier;
import code_generation.variables.IOVars;
import code_generation.writer.Writer;
import gui.MainFrame;

public class CodeGenerator {
	
	 Fuzzifier fuzzifier;
	 EvalMethod evalMethod;
	 HashMap<String, Defuzzifier> defuzzifiers;
	
	public CodeGenerator(EvalMethod method, HashMap<String, Defuzzifier> defuzz) {
		this.fuzzifier = new Fuzzifier();
		this.evalMethod = method;
		this.defuzzifiers = defuzz;
	}
	
	public void generateCpp(String outputPath) {
		 try {
			 Writer.openFile(outputPath);
			 Writer.file.write( "#include <iostream> \n" + 
					 				 "#include <ap_int.h> \n \n" );
			 
			 Writer.file.write( "typedef ap_uint<" + IOVars.converterSize + "> fixed_int; \n" );
			 
			 for(int i=0; i<IOVars.outVars.size(); i++) {
				 Writer.file.write( "typedef ap_uint<" + IOVars.getAuxTypeSize(i) + 
						 				 "> fixed_aux_"+ IOVars.outVars.get(i).getName() +"; \n" );
			 }
			 
			 Writer.file.write( "typedef ap_uint<" + IOVars.getOutTypeSize() + "> fixed_out; \n");
			 
			 Writer.file.write( "using namespace std; \n \n" );
			 						 
			 					 	 
			 
			 fuzzifier.compileFunctionsSlope();
			 fuzzifier.compilerInputVariablesBuffers();
			 for(String var: defuzzifiers.keySet())
				 defuzzifiers.get(var).compileOutputVariablesBuffers();
			 
			 Writer.file.write("\n \n");
			 
			 fuzzifier.compileFuzzifier();
			 evalMethod.compileEvalMethod();
			 for(int i=0; i<IOVars.outVars.size(); i++) {
				 defuzzifiers.get(IOVars.outVars.get(i).getName()).compileDefuzz(i);
			 }
			 
			 Writer.file.write("\n");
			 Writer.file.write("fixed_out fuzzyController(fixed_int " + IOVars.inVars.get(0).getName());
			 for(int i=1; i<IOVars.inVars.size(); i++) {
				 Writer.file.write(", fixed_int " + IOVars.inVars.get(i).getName());
			 }
			 Writer.file.write("){ \n");
			 Writer.file.write(" #pragma HLS DATAFLOW \n");
			 Writer.file.write("\n");
			 
			 Writer.file.write("\tfuzzifier(" + IOVars.inVars.get(0).getName());
			 for(int i=1; i<IOVars.inVars.size(); i++) {
				 Writer.file.write(", " + IOVars.inVars.get(i).getName());
			 }
			 Writer.file.write(");\n");
			 Writer.file.write("\trulesEvaluation(); \n");
			 
			 Writer.file.write("\n");
			 Writer.file.write("\tfixed_out output = defuzzifier" +  IOVars.outVars.get(0).getName() + "(); \n");
			 
			for(int i=1; i<IOVars.outVars.size(); i++ ){ 
				Writer.file.write("\toutput <<" + IOVars.converterSize +";\n");
				Writer.file.write("\toutput = output + defuzzifier" + IOVars.outVars.get(i).getName() + "();\n");
			}
			
			Writer.file.write("\treturn output;\n");
			Writer.file.write("}\n");
			 
			 Writer.closeFile();
			 
		 }
		 catch(IOException e) {
			 MainFrame.printOutput("ERROR. While generating cpp file");
		 }
	}
	
	public void generateHeader(String path) {
		
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
			
			fuzzifier.compileHeader();
			evalMethod.compileHeader();
			 for(int i=0; i<IOVars.outVars.size(); i++) {
				 defuzzifiers.get(IOVars.outVars.get(i).getName()).compileHeader(i);
			 }
			
			 Writer.file.write("fixed_out fuzzyController(fixed_int " + IOVars.inVars.get(0).getName());
			 for(int i=1; i<IOVars.inVars.size(); i++) {
				 Writer.file.write(", fixed_int " + IOVars.inVars.get(i).getName());
			 }
			 Writer.file.write("); \n");
			 Writer.closeFile();
		} catch (IOException e) {
			 MainFrame.printOutput("ERROR. While generating header file");
		}
	}
	
	public void generateTestBench(String path, String headerFile) {
		try {
			Writer.openFile(path);
			Writer.file.write(
					"#include <iostream> \n" + 
					"#include \"" + headerFile + "\" \n" + 
					"#include <ap_int.h> \n" + 
					"\n" + 
					"typedef ap_uint<8> fixed_int;\n" + 
					"typedef ap_uint<18> fixed_aux;\n" + 
					"\n" + 
					"\n" + 
					"using namespace std;\n" + 
					"\n" + 
					"int main(int argc, char **argv){ \n\n");
			
			for(int i=0; i<IOVars.inVars.size(); i++) {
				Writer.file.write( "\tfixed_int " + IOVars.inVars.get(i).getName() + " = " + (int)(IOVars.getConverterRange()/2) + "; \n"); 
			}
			
			Writer.file.write("\n");
			
			Writer.file.write("\tcout <<  fuzzyController(" + IOVars.inVars.get(0).getName());
			for(int i=1; i<IOVars.inVars.size(); i++) {
				Writer.file.write( ", " + IOVars.inVars.get(i).getName()); 
			}
			Writer.file.write("); \n\n");
			
			Writer.file.write("\treturn 0;\n}");
			Writer.closeFile();
		} catch (IOException e) {
			 MainFrame.printOutput("ERROR. While generating header file");
		}
	}
	

}
