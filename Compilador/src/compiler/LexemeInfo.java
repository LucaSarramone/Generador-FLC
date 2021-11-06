package compiler;

public class LexemeInfo {
	
	public int tokenNumber; // Number use by the parser
	public String tokenType; // Type of token: id, number or reserved word
	
	public String idRole = "-"; //If id then role can be: variable, fuzz_set or controller
	public String varType = "-"; // IN or OUT var
	public String fuzzType = "-"; // Algorithm used in fuzzification
	
	public int fuzzSetPosition = 0; //Fuzzy set index in variable vector
	public int varSize = 0; //Number of fuzzy sets associated with the variable
	
	public boolean setDeclared = false; // Set true when fuzzy_set declaration found
	public boolean varDeclared = false; // Set true when var declaration found
	public boolean fuzzDeclared = false; // Set true when var's fuzzy sets are declared
	public boolean useInRule = false; // Set true when var is used in generic rule
	public boolean defuzzDeclared = false; //Set true when output variable defuzzifier is declared
	public boolean isSingleton =  false; //Set true if all var's fuzzy sets are singleton
	
	public LexemeInfo(int tokenId, String tokenType) {
		this.tokenNumber = tokenId;
		this.tokenType = tokenType;
	}
	
	public String toString() {
		return "<" + tokenType + ", " + idRole + ", " + varType + ", " + fuzzType + ", " + fuzzSetPosition + ", " + varSize + ">";    
	}
	
}
