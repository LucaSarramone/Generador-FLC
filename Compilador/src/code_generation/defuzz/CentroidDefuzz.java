package code_generation.defuzz;

import java.io.IOException;

import code_generation.variables.IOVars;
import code_generation.writer.Writer;

public class CentroidDefuzz extends Defuzzifier{
	
	public CentroidDefuzz() { super("Centroid"); };
	
	
	
	public void compileDefuzz(int varNumber) throws IOException {
		
		IOVars.outVars.get(varNumber).compileOutputSetsBuffer();
		
		String varName = IOVars.outVars.get(varNumber).getName();
		
		Writer.file.write("fixed_int defuzzifier" + varName + "() {\n");

		Writer.file.write(" #pragma HLS PIPELINE \n");
		for(int i=0; i<IOVars.outVars.size(); i++) {
			Writer.file.write(" #pragma HLS ARRAY_PARTITION variable=" + IOVars.outVars.get(i).getName() + "MembershipValues complete dim=1 \n" );
			Writer.file.write(" #pragma HLS ARRAY_PARTITION variable=outputValues" + IOVars.outVars.get(i).getName() + " complete dim=1 \n" );
		}
		
		Writer.file.write("\n");
		
		Writer.file.write("\tfixed_aux_" + varName + " numerator = 0; \n ");
		Writer.file.write("\tfixed_aux_" + varName + " denominator = 0; \n ");
			
		Writer.file.write("\tfor(int i=0; i<"+ IOVars.outVars.get(varNumber).getSize() +"; i++){ \n");
		Writer.file.write("\t\tnumerator = numerator + " + varName + "MembershipValues[i] * outputValues" + varName + "[i]; \n");
		Writer.file.write("\t\tdenominator = denominator + " + varName + "MembershipValues[i]; \n");
		Writer.file.write("\t} \n");

		
		Writer.file.write("\treturn numerator/denominator; \n");
		Writer.file.write("}\n");
			
	}



	@Override
	public
	
	
	void compileHeader(int varNumber) throws IOException{
		String varName = IOVars.outVars.get(varNumber).getName();
		
		Writer.file.write("fixed_int defuzzifier" + varName + "();\n");
	}
}
