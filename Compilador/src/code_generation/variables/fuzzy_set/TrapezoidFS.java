package code_generation.variables.fuzzy_set;

import java.io.IOException;
import java.util.Vector;

import code_generation.variables.IOVars;
import code_generation.writer.Writer;

public class TrapezoidFS extends FuzzySet {
	
	int a = 0;
	int b = 0;
	int c = 0;
	int d = 0;
	
	public TrapezoidFS() {
		super("Trapezoid");
	}
	
	public TrapezoidFS(String n) {
		super(n, "Trapezoid");
	}
	
	public int getA() {
		return a;
	}
	
	public int getB() {
		return b;
	}
	public int getC() {
		return c;
	}
	public int getD() {
		return d;
	}
	
	@Override
	public void compileFuzzSet(String varName, int setNumber) throws IOException {
		Writer.file.write("\tif (" + varName + " < " + a + " || " +
										varName + " > " + d + ") \n");
		
		Writer.file.write("\t\tif (" + varName + " < " + b + ") \n");
		Writer.file.write("\t\t\t" + varName + "Fuzz[" + setNumber + "] = function" + varName + setNumber+ "1" + 
				" * (" + varName + "-" + a +"); \n");
		
		Writer.file.write("\t\telse");
		Writer.file.write("\t\t\tif (" + varName + " > " + c + ") \n");
		Writer.file.write("\t\t\t\t" + varName + "Fuzz[" + setNumber + "] = function" + varName + setNumber + "2" +
				" * (" + d + " - " + varName + ");\n");
		
		Writer.file.write("\t\t\telse");
		Writer.file.write("\t\t\t\t" + varName + "Fuzz[" + setNumber + "] = 1;\n");
		
		Writer.file.write("\telse \n");
			Writer.file.write("\t\t" + varName + "Fuzz[" + setNumber + "] = 0;\n");
		

	}

	@Override
	public void compileFunctionSlope(String varName) throws IOException {
		Writer.file.write("const fixed_int " + varName + "1 = ");
		Writer.file.write(IOVars.getConverterRange() + "/(" + b + " - " + a + "); \n");
		Writer.file.write("const fixed_int " + varName + "2 = ");
		Writer.file.write(IOVars.getConverterRange() + "/(" + d + " - " + c + "); \n");
	}

	@Override
	public boolean setParameters(Vector<Integer> fuzzySetParameters) {
		if(fuzzySetParameters.size() != 4) 
			return false;

		this.a = fuzzySetParameters.get(0);
		this.b = fuzzySetParameters.get(1);
		this.c = fuzzySetParameters.get(2);
		this.d = fuzzySetParameters.get(3);
		return true;
	}

	@Override
	public String toString() {
		return super.name + "<" + a + "," + b + "," + c + ","+ d + ">";
	}
	
	
}
