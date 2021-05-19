package rules_elements.fuzzy_set;

import java.io.IOException;

public abstract class FuzzySet {
	
	protected String name;
	protected String type;
	
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
	

}
