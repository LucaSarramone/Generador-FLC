package code_generation.defuzz;

import java.io.IOException;

import code_generation.variables.IOVars;

public abstract class Defuzzifier {
	
	String type;
	
	public Defuzzifier(String t) {
		this.type = t;
	};
	
	public String getType() {
		return this.type;
	}
	
	public static void compileOutputVariablesBuffers() throws IOException {
		for(int i=0; i<IOVars.outVars.size(); i++) {
			IOVars.outVars.get(i).compileBuffer("MembershipValues");
		}
	}
	
	public abstract void compileDefuzz(int varNumber) throws IOException;

	public abstract void compileHeader(int i) throws IOException;
	
	
}
