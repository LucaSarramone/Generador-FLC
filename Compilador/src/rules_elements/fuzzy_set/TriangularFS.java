package rules_elements.fuzzy_set;

import java.io.IOException;

import rules_elements.InOut;
import writer.Writer;

public class TriangularFS extends FuzzySet {
	
	private int a = 0;
	private int b = 0;
	private int c = 0;

	public TriangularFS(String n) {
		super(n,"Triangular");
	}
	
	public TriangularFS(String n, int a, int b, int c) {
		super(n,"Triangular");
		this.setParameters(a, b, c);
	}
	
	public void setParameters(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
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

	
	@Override
	public void compileFuzzSet(String varName, int setNumber) throws IOException {
		Writer.file.write("\tif (" + varName + " < " + a + " || " +
										varName + " > " + c + ") \n");
		
		Writer.file.write("\t\t" + varName + "Fuzz[" + setNumber + "] = 0;\n");
		Writer.file.write("\telse \n");
		Writer.file.write("\t\tif (" + varName + " > " + b + ") \n");
		
		Writer.file.write("\t\t\t" + varName + "Fuzz[" + setNumber + "] = function" + varName + setNumber+ "1" + 
								" * (" + varName + "-" + a +"); \n");
		Writer.file.write("\t\telse \n");
		Writer.file.write("\t\t\t" + varName + "Fuzz[" + setNumber + "] = function" + varName + setNumber + "2" +
								" * (" + c + " - " + varName + ");\n");
	}
	
	@Override
	public void compileFunctionSlope(String varName) throws IOException {
		Writer.file.write("const fixed_int " + varName + "1 = ");
		Writer.file.write(InOut.getConverterRange() + "/(" + b + " - " + a + "); \n");
		Writer.file.write("const fixed_int " + varName + "2 = ");
		Writer.file.write(InOut.getConverterRange() + "/(" + c + " - " + b + "); \n");
	}
	
}
