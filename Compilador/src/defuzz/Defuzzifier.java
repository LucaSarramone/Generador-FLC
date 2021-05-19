package defuzz;

import java.io.IOException;

import rules_elements.InOut;

public abstract class Defuzzifier {
	
	public Defuzzifier() {};
	
	public void compileOutputVariablesBuffers() throws IOException {
		for(int i=0; i<InOut.outVars.size(); i++) {
			InOut.outVars.get(i).compileBuffer("MembershipValues");
		}
	}
	
	public abstract void compileDefuzz(int varNumber) throws IOException;
	
	
}
