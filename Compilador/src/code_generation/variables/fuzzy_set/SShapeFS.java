package code_generation.variables.fuzzy_set;

import java.io.IOException;
import java.util.Vector;

import code_generation.variables.IOVars;
import code_generation.writer.Writer;

public class SShapeFS extends FuzzySet {
	
	private int a = 0;
	private int b = 0;
	
	public SShapeFS() {
		super("S-Shape");
	}
	
	public SShapeFS(String n) {
		super(n,"S-Shape");
	}
	
	public SShapeFS(String n, int a, int b) {
		super(n,"S-Shape");
		this.a = a;
		this.b = b;
	}

	
	public int getA() {
		return a;
	}
	
	public int getB() {
		return b;
	}
	
	
	@Override
	public void compileFuzzSet(String varName, int setNumber) throws IOException {
		Writer.file.write("\tif (" + varName + " < " + a + ") \n");
		Writer.file.write("\t\t" + varName + "Fuzz[" + setNumber + "] = 0;\n");
		Writer.file.write("\telse \n");
		Writer.file.write("\t\tif (" + varName + " > " + b + ") \n");
		Writer.file.write("\t\t\t" + varName + "Fuzz[" + setNumber + "] = " + IOVars.getConverterRange()  +";\n");
		Writer.file.write("\t\telse \n");
		Writer.file.write("\t\t\t" + varName + "Fuzz[" + setNumber + "] = function" + varName + setNumber +
								" * (" + varName + " - " + a + ");\n");
	}
	
	@Override
	public void compileFunctionSlope(String varName) throws IOException {
		Writer.file.write("const fixed_int " + varName + " = ");
		Writer.file.write(IOVars.getConverterRange() + "/(" + b + " - " + a + "); \n");
	}

	@Override
	public boolean setParameters(Vector<Integer> parameters) {
		if(parameters.size() != 2)
			return false;
		
		this.a = parameters.get(0);
		this.b = parameters.get(1);
		return true;
	}
	
	@Override
	public String toString() {
		return super.name + "<" + a + ", " + b + ">";
	}

	
	
}
