package code_generation.defuzz;

import java.io.IOException;

import code_generation.variables.IOVars;

public abstract class Defuzzifier {
	
	public Defuzzifier() {};
	
	public void compileOutputVariablesBuffers() throws IOException {
		for(int i=0; i<IOVars.outVars.size(); i++) {
			IOVars.outVars.get(i).compileBuffer("MembershipValues");
		}
	}
	
	public abstract void compileDefuzz(int varNumber) throws IOException;
	
	
}
