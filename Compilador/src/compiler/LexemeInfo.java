package compiler;

public class LexemeInfo {
	
	public int tokenNumber; // Number use by the parser
	public String tokenType; // Type of token: id, number or reserved word
	public int numberOfReferences; // Number of times the id/number has appeared
	public String idRole = null; //If id then role can be: variable, fuzz set or controler
	public String varType = null; // IN or OUT var
	public String varFuzzType = null; // Algorithm used in fuzzification
	public boolean varDeclared = false; // Set true when var declaration found
	public boolean fuzzDeclared = false; // Set true when var fuzzy sets are declared
	
	public LexemeInfo(int tokenId, String tokenType, int numberOfReference) {
		this.tokenNumber = tokenId;
		this.tokenType = tokenType;
		this.numberOfReferences = numberOfReference;
	}
	
	
}
