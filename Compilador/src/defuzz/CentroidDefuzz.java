package defuzz;

import java.io.IOException;

import rules_elements.InOut;
import writer.Writer;

public class CentroidDefuzz extends Defuzzifier{

	
	public void compileDefuzz(int varNumber) throws IOException {
		
		InOut.outVars.get(varNumber).compileOutputSetsBuffer();
		
		String varName = InOut.outVars.get(varNumber).getName();

		Writer.file.write("\nfixed_int defuzzifier" + varName + "(){ \n\n");
		Writer.file.write("\tfixed_aux_" + varName + " numerator = 0; \n ");
		Writer.file.write("\tfixed_aux_" + varName + " denominator = 0; \n ");
			
		Writer.file.write("\tfor(int i=0; i<"+ InOut.outVars.get(varNumber).getSize() +"; i++){ \n");
		Writer.file.write("\t\tnumerator = numerator + " + varName + "MembershipValues[i] * outputValues" + varName + "[i]; \n");
		Writer.file.write("\t\tdenominator = denominator + " + varName + "MembershipValues[i]; \n");
		Writer.file.write("\t} \n");

		
		Writer.file.write("\treturn numerator/denominator; \n");
		Writer.file.write("}\n");
			
	}
}
