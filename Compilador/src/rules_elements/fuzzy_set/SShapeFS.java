package rules_elements.fuzzy_set;

import java.io.IOException;

import rules_elements.InOut;
import writer.Writer;

public class SShapeFS extends FuzzySet {
	
	private int a = 0;
	private int b = 0;
	
	public SShapeFS(String n) {
		super(n,"S-Shape");
	}
	
	public SShapeFS(String n, int a, int b) {
		super(n,"S-Shape");
		this.setParameters(a, b);
	}
	
	public void setParameters(int a, int b) {
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
		Writer.file.write("\t\t\t" + varName + "Fuzz[" + setNumber + "] = " + InOut.getConverterRange()  +";\n");
		Writer.file.write("\t\telse \n");
		Writer.file.write("\t\t\t" + varName + "Fuzz[" + setNumber + "] = function" + varName + setNumber +
								" * (" + varName + " - " + a + ");\n");
	}
	
	@Override
	public void compileFunctionSlope(String varName) throws IOException {
		Writer.file.write("const fixed_int " + varName + " = ");
		Writer.file.write(InOut.getConverterRange() + "/(" + b + " - " + a + "); \n");
	}
	
}
