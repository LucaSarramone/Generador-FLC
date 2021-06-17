package code_generation.variables.fuzzy_set;

import java.io.IOException;
import java.util.Vector;

import code_generation.variables.IOVars;
import code_generation.writer.Writer;

public class SingletonFS extends FuzzySet {
	
	private int value = 0; 
	
	public SingletonFS() {
		super("Singleton");
	}
	
	public SingletonFS(String n) {
		super(n, "Singleton");
	}
	
	public SingletonFS(String n, int valor) {
		super(n, "Singleton");
		this.value = valor;
	}
	
	public int getValue() {
		return value;
	}


	@Override
	public void compileFuzzSet(String varName, int setNumber) throws IOException {
		Writer.file.write("\tif (" + varName + " == " + value + ") \n");
		Writer.file.write("\t\t" + varName + "Fuzz[" + setNumber + "] = " + IOVars.getConverterRange() + ";\n");
		Writer.file.write("\telse \n");
		Writer.file.write("\t\t" + varName + "Fuzz[" + setNumber + "] = 0;\n");
	}

	@Override
	public void compileFunctionSlope(String nombreVar) throws IOException { /**Sin pendiente**/ }

	@Override
	public boolean setParameters(Vector<Integer> parameters) {
		if(parameters.size() != 1 ) 
			return false;
		
		this.value = parameters.get(0);
		return true;
	}

	@Override
	public String toString() {
		return super.name + "<" + value + ">";
	}


}
