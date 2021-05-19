package evaluation_method;

import java.io.IOException;

import rules_elements.InOut;

public abstract class EvalMethod {
	
	protected int rulesMatrix[][];
	protected int rows = 0; //Number of rules
	protected int columns = 0; //Number of variables
	protected String type;
	
	public EvalMethod(String type) {		
		this.type = type;
		
		rows = InOut.inVars.get(0).getSize();
		for(int i = 1; i < InOut.inVars.size(); i++) {
			rows = rows * InOut.inVars.get(i).getSize();
		}
		columns = InOut.inVars.size() + InOut.outVars.size();
		
		rulesMatrix = new int[rows][columns];
	}
	
	public abstract void compileEvalMethod() throws IOException;
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	
	public void initMatrix() {
		int varColumn = 0;
		int repetitions = 0;
		for(int j=0; j<InOut.inVars.size(); j++) {
			varColumn = InOut.inVars.size() - 1 - j;
			if(j == 0)
				repetitions = 1;
			else 
				repetitions = j * InOut.inVars.get(varColumn).getSize();
			InOut.inVars.get(varColumn).initColumn(rulesMatrix, repetitions, varColumn);
		}
	}
	
	public void printMatrix() {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				System.out.print(rulesMatrix[i][j] + " | ");
			}
			System.out.println("");
		}
	}

}
