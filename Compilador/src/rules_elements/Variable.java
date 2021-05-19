package rules_elements;

import java.io.IOException;
import java.util.Vector;

import rules_elements.fuzzy_set.FuzzySet;
import rules_elements.fuzzy_set.SingletonFS;
import writer.Writer;

public class Variable {
	
	private String name;
	private Vector<FuzzySet> fuzzySets;
	
	public Variable(String n, int min, int max) {
		this.name = n;
		fuzzySets = new Vector<FuzzySet>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public int getSize() {
		return fuzzySets.size();
	}
	
	public Vector<FuzzySet> getSets(){
		return fuzzySets;
	}
	
	public boolean checkSetName(String name) {
		
		for(int i=0; i<fuzzySets.size(); i++) {
			if(fuzzySets.get(i).getName().equals(name)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean addSet(FuzzySet fs) {
		if(checkSetName(fs.getName())) {
			return fuzzySets.add(fs);
		}
		return false;
	}
	
	public boolean removeSet(String name) {
		
		for(int i=0; i<fuzzySets.size(); i++) {
			if(fuzzySets.get(i).getName().equals(name)) {
				return fuzzySets.remove(fuzzySets.get(i));
			}
		}
		return false;
	}
	
	
	public void compileFunctionsSlope() throws IOException {
		String nombrePendiente = "function" + this.name;
		for(int i=0; i<fuzzySets.size(); i++) {
			fuzzySets.get(i).compileFunctionSlope(nombrePendiente + i);
		}
	}
	
	public void compile() throws IOException {
		for(int i=0; i<fuzzySets.size(); i++) {
			fuzzySets.get(i).compileFuzzSet(name, i);
			Writer.file.write("\n\n");
		}
	}

	public void initColumn(int[][] rulesMatrix, int repetition, int column) {
		
		int auxCounter = 0;
		int fuzzySetNumber = 0;
		for(int i=0; i<rulesMatrix.length; i++) {
			rulesMatrix[i][column] = fuzzySetNumber % fuzzySets.size();
			auxCounter++;
			if(auxCounter == repetition) {
				auxCounter = 0;
				fuzzySetNumber++;
			}
		}
		
	}

	public boolean isSingleton() {
		for(int i=0; i<fuzzySets.size(); i++) {
			if(!fuzzySets.get(i).getType().equals("Singleton"))
				return false;
		}
		return true;
	}

	public void compileBuffer(String bufferType) throws IOException {
		Writer.file.write("fixed_int " + name.toLowerCase() + bufferType + 
								"[" + this.getSize() + "] = {0");

		for(int j=0; j<this.getSize() - 1; j++) 
			Writer.file.write(", 0");
		
		Writer.file.write("}; \n");
	}
	
	public void compileOutputSetsBuffer() throws IOException {
		Writer.file.write("const fixed_int outputValues" + name + "[" + this.getSize() + "] = ");
		Writer.file.write("{" + ((SingletonFS)fuzzySets.get(0)).getValue());
		for(int i=1; i<this.getSize(); i++) {
			Writer.file.write(", " + ((SingletonFS)fuzzySets.get(i)).getValue());
		}
		Writer.file.write("}; \n");
	}
	
}
