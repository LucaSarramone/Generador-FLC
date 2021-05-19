package fuzz;

import java.io.IOException;

import rules_elements.InOut;
import writer.Writer;

public class Fuzzifier {
	
	
	public Fuzzifier() { }
	
	
	public void compileFunctionsSlope() throws IOException {
		for(int i=0; i<InOut.inVars.size(); i++) {
			InOut.inVars.get(i).compileFunctionsSlope();
			Writer.file.write("\n");
		}
	}
	
	public void compilerInputVariablesBuffers() throws IOException {
		for(int i=0; i<InOut.inVars.size(); i++) {
			InOut.inVars.get(i).compileBuffer("Fuzz");
		}
	}
	
	public void compileFuzzifier() throws IOException {
		Writer.file.write("void fuzzifier (fixed_int " +  InOut.inVars.get(0).getName());
		for(int i=1; i<InOut.inVars.size(); i++) {
			Writer.file.write(", fixed_int " + InOut.inVars.get(i).getName());
		}
		Writer.file.write(") { \n \n");
		
		for(int i=0; i<InOut.inVars.size(); i++) {
			Writer.file.write(" //"+ InOut.inVars.get(i).getName() +"------------------------------------\n\n");
			InOut.inVars.get(i).compile();
		}
		
		Writer.file.write("} \n \n");
	}
	
}
