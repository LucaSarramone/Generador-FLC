package code_generation.variables.fuzzy_set;

import java.io.IOException;
import java.util.Vector;

public abstract class FuzzySet {
	
	protected String name;
	protected String type;
	
	public FuzzySet(String t) {
		this.type = t;
	}
	
	public FuzzySet(String n, String t) {
		this.name = n;
		this.type = t;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public String getType() {
		return this.type;
	}
	
	public abstract void compileFuzzSet(String varName, int setNumber) throws IOException;
	 
	public abstract void compileFunctionSlope(String varName) throws IOException;
	
	public abstract boolean setParameters(Vector<Integer> fuzzySetParameters);
	
	public abstract String toString();

}
