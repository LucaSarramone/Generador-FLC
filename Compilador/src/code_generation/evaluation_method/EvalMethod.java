package code_generation.evaluation_method;

import java.io.IOException;
import java.util.Vector;

public abstract class EvalMethod {
	
	protected Vector<int[]> rulesMatrix = new Vector<int[]>();
	protected int rows = 0; //Number of rules
	protected int columns = 0; //Number of variables
	protected String type;
	
	public EvalMethod(String type, Vector<int[]> matrix) {
		this.type = type;
		this.rulesMatrix = matrix;
		rows = matrix.size();
		columns = matrix.get(0).length;
	}
	
	public EvalMethod(String type) {		
		this.type = type;
	}
	
	public void setMatrix(Vector<int[]> matrix) {
		this.rulesMatrix = matrix;
		rows = matrix.size();
		columns = matrix.get(0).length;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	
//	public void initMatrix() {
//		int varColumn = 0;
//		int repetitions = 0;
//		for(int j=0; j<IOVars.inVars.size(); j++) {
//			varColumn = IOVars.inVars.size() - 1 - j;
//			if(j == 0)
//				repetitions = 1;
//			else 
//				repetitions = j * IOVars.inVars.get(varColumn).getSize();
//			IOVars.inVars.get(varColumn).initColumn(rulesMatrix, repetitions, varColumn);
//		}
//	}
	
	public void printMatrix() {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				System.out.print(rulesMatrix.get(i)[j] + " | ");
			}
			System.out.println("");
		}
	}
	
	public abstract void compileEvalMethod() throws IOException;
	
	public abstract void compileHeader() throws IOException;

}
