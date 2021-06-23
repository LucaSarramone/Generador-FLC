package code_generation.fuzz;

import java.io.IOException;

import code_generation.variables.IOVars;
import code_generation.writer.Writer;

public class Fuzzifier {
	
	
	public Fuzzifier() { }
	
	
	public void compileFunctionsSlope() throws IOException {
		for(int i=0; i<IOVars.inVars.size(); i++) {
			IOVars.inVars.get(i).compileFunctionsSlope();
			Writer.file.write("\n");
		}
	}
	
	public void compilerInputVariablesBuffers() throws IOException {
		for(int i=0; i<IOVars.inVars.size(); i++) {
			IOVars.inVars.get(i).compileBuffer("Fuzz");
		}
	}
	
	public void compileFuzzifier() throws IOException {
		Writer.file.write("void fuzzifier (fixed_int " +  IOVars.inVars.get(0).getName());
		for(int i=1; i<IOVars.inVars.size(); i++) {
			Writer.file.write(", fixed_int " + IOVars.inVars.get(i).getName());
		}
		Writer.file.write(") { \n");
		Writer.file.write(" #pragma HLS PIPELINE \n");
		for(int i=0; i<IOVars.inVars.size();	i++)
			Writer.file.write(" #pragma #pragma HLS ARRAY_PARTITION variable=" + IOVars.inVars.get(i).getName() + "Fuzz complete dim=1 \n" );
		Writer.file.write("\n");
		
		for(int i=0; i<IOVars.inVars.size(); i++) {
			Writer.file.write(" //"+ IOVars.inVars.get(i).getName() +"------------------------------------\n\n");
			IOVars.inVars.get(i).compile();
		}
		
		Writer.file.write("} \n \n");
	}
	
}
