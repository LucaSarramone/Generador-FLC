package rules_elements.fuzzy_set;

import java.io.IOException;

import rules_elements.InOut;
import writer.Writer;

public class SingletonFS extends FuzzySet {
	
	private int value = 0; 
	
	public SingletonFS(String n, int valor) {
		super(n, "Singleton");
		this.value = valor;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int valor) {
		this.value = valor;
	}


	@Override
	public void compileFuzzSet(String varName, int setNumber) throws IOException {
		Writer.file.write("\tif (" + varName + " == " + value + ") \n");
		Writer.file.write("\t\t" + varName + "Fuzz[" + setNumber + "] = " + InOut.getConverterRange() + ";\n");
		Writer.file.write("\telse \n");
		Writer.file.write("\t\t" + varName + "Fuzz[" + setNumber + "] = 0;\n");
	}

	@Override
	public void compileFunctionSlope(String nombreVar) throws IOException { /**Sin pendiente**/ }


}
