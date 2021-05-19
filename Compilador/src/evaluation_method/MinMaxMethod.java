package evaluation_method;

import java.io.IOException;

import rules_elements.InOut;
import writer.Writer;

public class MinMaxMethod extends EvalMethod{
	
	public MinMaxMethod() {
		super("MinMax");
	}
	
	public void compileEvalMethod() throws IOException{
		
		boolean isFirst = true;
		Writer.file.write("void rulesEvaluation(){ \n\n");
		Writer.file.write("\tfixed_int aux = 0; \n");
		for(int j = InOut.inVars.size(); j < columns; j++ ) {
			for(int conjunto = 0; conjunto < InOut.outVars.get(columns - j - 1).getSize(); conjunto++) {
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
	}
	
	private void compileRow(boolean isFirst, int row, int varNumber, int setNumber) throws IOException {
		Writer.file.write("\t");
		if(isFirst) { 
			Writer.file.write("membershipValues" + InOut.outVars.get(varNumber).getName() + 
									"[" + setNumber + "] = min(");
			for(int inVar=0; inVar<InOut.inVars.size(); inVar++) {
				Writer.file.write(InOut.inVars.get(inVar).getName() + "Fuzz[" +
										rulesMatrix[row][inVar] + "]");
				if(inVar != InOut.inVars.size())
					Writer.file.write(",");
			}
			Writer.file.write("); \n");
		}
		else {
			Writer.file.write("aux = min(");
			for(int inVar=0; inVar<InOut.inVars.size(); inVar++) {
				Writer.file.write(InOut.inVars.get(inVar).getName() + "Fuzz[" +
										rulesMatrix[row][inVar] + "]");
				if(inVar != InOut.inVars.size()-1)
					Writer.file.write(", ");
			}
			Writer.file.write("); \n");
			Writer.file.write("\tif (membershipValues" + InOut.outVars.get(varNumber).getName() + 
									"[" + setNumber + "] < aux \n");
			Writer.file.write("\t\t membershipValues" + InOut.outVars.get(varNumber).getName() + 
									"[" + setNumber + "] = aux; \n");
			
		}
			
	}

}
