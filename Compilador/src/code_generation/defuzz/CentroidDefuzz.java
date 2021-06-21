package code_generation.defuzz;

import java.io.IOException;

import code_generation.variables.IOVars;
import code_generation.writer.Writer;

public class CentroidDefuzz extends Defuzzifier{
	
	public CentroidDefuzz() { };
	
	public void compileDefuzz(int varNumber) throws IOException {
		
		IOVars.outVars.get(varNumber).compileOutputSetsBuffer();
		
		String varName = IOVars.outVars.get(varNumber).getName();

		Writer.file.write("\nfixed_int defuzzifier" + varName + "(){ \n\n");
		Writer.file.write("\tfixed_aux_" + varName + " numerator = 0; \n ");
		Writer.file.write("\tfixed_aux_" + varName + " denominator = 0; \n ");
			
		Writer.file.write("\tfor(int i=0; i<"+ IOVars.outVars.get(varNumber).getSize() +"; i++){ \n");
		Writer.file.write("\t\tnumerator = numerator + " + varName + "MembershipValues[i] * outputValues" + varName + "[i]; \n");
		Writer.file.write("\t\tdenominator = denominator + " + varName + "MembershipValues[i]; \n");
		Writer.file.write("\t} \n");

		
		Writer.file.write("\treturn numerator/denominator; \n");
		Writer.file.write("}\n");
			
	}
}
