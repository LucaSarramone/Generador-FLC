package code_generation.variables;

import java.util.Vector;

public class IOVars {
	
	public static Vector<Variable> inVars;
	public static Vector<Variable> outVars;
	public static int converterSize = 0;
	
	public static void init(){
		inVars = new Vector<Variable>();
		outVars = new Vector<Variable>();
	}

	
	public static void init(int size){
		inVars = new Vector<Variable>();
		outVars = new Vector<Variable>();
		converterSize = size;
	}

	public static int getConverterRange() {
		return (int) Math.pow(2, converterSize) - 1;
	}

	public static int getAuxTypeSize(int outVarNumber) {
		return (2 * converterSize) + outVars.get(outVarNumber).getSize();
	}

	public static int getOutTypeSize() {	
		return outVars.size() * converterSize; 
	}
}
