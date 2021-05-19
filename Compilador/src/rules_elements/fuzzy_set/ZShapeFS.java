package rules_elements.fuzzy_set;

import java.io.IOException;

import rules_elements.InOut;
import writer.Writer;

public class ZShapeFS extends FuzzySet {
	
	private int c = 0;
	private int d = 0;

	public ZShapeFS(String n) {
		super(n, "Z-Shape");
	}
	
	public ZShapeFS(String n, int c, int d) {
		super(n,"Z-Shape");
		this.setParameters(c, d);
	}
	
	public void setParameters(int c, int d) {
		this.c = c;
		this.d = d;
	}
	
	
	public int getC() {
		return c;
	}
	
	public int getD() {
		return d;
	}
	
	
	@Override
	public void compileFuzzSet(String varName, int setNumber) throws IOException {
		Writer.file.write("\tif (" + varName + " < " + c + ") \n");
		Writer.file.write("\t\t" + varName + "Fuzz[" + setNumber + "] = " + InOut.getConverterRange() + ";\n");
		Writer.file.write("\telse \n");
		Writer.file.write("\t\tif (" + varName + " > " + d + ") \n");
		Writer.file.write("\t\t\t" + varName + "Fuzz[" + setNumber + "] = 0; \n");
		Writer.file.write("\t\telse \n");
		Writer.file.write("\t\t\t" + varName + "Fuzz[" + setNumber + "] = function" + varName + setNumber +
								" * (" + d + " - " + varName + ");\n");
	}
	
	@Override
	public void compileFunctionSlope(String varName) throws IOException {
		Writer.file.write("const fixed_int " + varName + " = ");
		Writer.file.write(InOut.getConverterRange() + "/(" + d + " - " + c + "); \n");
	}
	
}