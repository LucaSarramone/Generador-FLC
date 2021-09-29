package code_generation.evaluation_method;

import java.io.IOException;

import code_generation.variables.IOVars;
import code_generation.writer.Writer;

public class MinMaxMethod extends EvalMethod{
	
	public MinMaxMethod() {
		super("MinMax");
	}
	
	public void compileEvalMethod() throws IOException{
		
		boolean isFirst = true;
		Writer.file.write("void rulesEvaluation(){ \n");
		
		Writer.file.write(" #pragma HLS PIPELINE \n");
		for(int i=0; i<IOVars.inVars.size(); i++)
			Writer.file.write(" #pragma HLS ARRAY_PARTITION variable=" + IOVars.inVars.get(i).getName() + "Fuzz complete dim=1 \n" );
		for(int i=0; i<IOVars.outVars.size(); i++)
			Writer.file.write(" #pragma HLS ARRAY_PARTITION variable=" + IOVars.outVars.get(i).getName() + "MembershipValues complete dim=1 \n" );
		
		Writer.file.write("\n");
		Writer.file.write("\tfixed_int aux = 0; \n");
		for(int j = IOVars.inVars.size(); j < columns; j++ ) {
			for(int conjunto = 0; conjunto < IOVars.outVars.get(columns - j - 1).getSize(); conjunto++) {
				isFirst = true;
				for(int i=0; i<rows; i++) {
					if(rulesMatrix[i][j] == conjunto) {
						compileRow(isFirst, i, columns-j-1, conjunto);
						Writer.file.write("\n");
						isFirst = false;
					}
				}
			}
		}
		Writer.file.write("} \n");
		Writer.file.write("\n");
	}
	
	private void compileRow(boolean isFirst, int row, int varNumber, int setNumber) throws IOException {
		Writer.file.write("\t");
		if(isFirst) { 
			Writer.file.write(IOVars.outVars.get(varNumber).getName() + "MembershipValues" + 
									"[" + setNumber + "] = min(");
			for(int inVar=0; inVar<IOVars.inVars.size(); inVar++) {
				Writer.file.write(IOVars.inVars.get(inVar).getName() + "Fuzz[" +
										rulesMatrix[row][inVar] + "]");
				if(inVar != IOVars.inVars.size()-1)
					Writer.file.write(",");
			}
			Writer.file.write("); \n");
		}
		else {
			Writer.file.write("aux = min(");
			for(int inVar=0; inVar<IOVars.inVars.size(); inVar++) {
				Writer.file.write(IOVars.inVars.get(inVar).getName() + "Fuzz[" +
										rulesMatrix[row][inVar] + "]");
				if(inVar != IOVars.inVars.size()-1)
					Writer.file.write(", ");
			}
			Writer.file.write("); \n");
			Writer.file.write("\tif (" + IOVars.outVars.get(varNumber).getName() + "MembershipValues" + 
									"[" + setNumber + "] < aux) \n");
			Writer.file.write("\t\t " + IOVars.outVars.get(varNumber).getName() + "MembershipValues" + 
									"[" + setNumber + "] = aux; \n");
			
		}
			
	}

	@Override
	public void compileHeader() throws IOException {
		Writer.file.write("void rulesEvaluation(); \n");
	}

}
