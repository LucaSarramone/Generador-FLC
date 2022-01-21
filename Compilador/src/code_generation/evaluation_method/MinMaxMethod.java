package code_generation.evaluation_method;

import java.io.IOException;

import code_generation.variables.IOVars;
import code_generation.writer.Writer;
import compiler.Compiler;

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
		int varColumn = 0;
		for(int j = IOVars.inVars.size(); j < columns; j++ ) {
			varColumn = Compiler.table.get(IOVars.outVars.get(columns - j - 1).getName()).varColumn;
			for(int conjunto = 0; conjunto < IOVars.outVars.get(columns - j - 1).getSize(); conjunto++) {
				isFirst = true;
				for(int i=0; i<rows; i++) {
					if(rulesMatrix.get(i)[varColumn] == conjunto) {
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
		int varsLeft = IOVars.inVars.size();
		int minOpened = 1;
		if(isFirst) { 
			Writer.file.write(IOVars.outVars.get(varNumber).getName() + "MembershipValues" + 
									"[" + setNumber + "] = min(");
			for(int inVar=0; inVar<IOVars.inVars.size(); inVar++) {
				int varColumn = Compiler.table.get(IOVars.inVars.get(inVar).getName()).varColumn;
				Writer.file.write(IOVars.inVars.get(inVar).getName() + "Fuzz[" +
										rulesMatrix.get(row)[varColumn] + "]");
				if(inVar != IOVars.inVars.size()-1) {
					if(varsLeft > 2) {
						Writer.file.write(", min(");
						varsLeft--;
						minOpened++;
					}
					else
						Writer.file.write(", ");
				}
			}
			for(int i = 0; i<minOpened; i++)
				Writer.file.write(")");
			Writer.file.write("; \n");
		}
		else {
			Writer.file.write("aux = min(");
			for(int inVar=0; inVar<IOVars.inVars.size(); inVar++) {
				int varColumn = Compiler.table.get(IOVars.inVars.get(inVar).getName()).varColumn;
				Writer.file.write(IOVars.inVars.get(inVar).getName() + "Fuzz[" +
										rulesMatrix.get(row)[varColumn] + "]");
				if(inVar != IOVars.inVars.size()-1) {
					if(varsLeft > 2) {
						Writer.file.write(", min(");
						varsLeft--;
						minOpened++;
					}
					else
						Writer.file.write(", ");
				}
			}
			for(int i = 0; i<minOpened; i++)
				Writer.file.write(")");
			Writer.file.write("; \n");
			
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
