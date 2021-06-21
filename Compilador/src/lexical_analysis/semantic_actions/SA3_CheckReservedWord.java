package lexical_analysis.semantic_actions;

import java.io.BufferedInputStream;
import java.io.IOException;
import compiler.Compiler;
import compiler.LexemeInfo;
import lexical_analysis.LexicalAnalyzer;

public class SA3_CheckReservedWord implements SemanticAction{
	
	private BufferedInputStream reader;
	
	public SA3_CheckReservedWord(BufferedInputStream reader) {
		this.reader = reader;
	}
	
	@Override
	public void execute() {

		if (!Compiler.table.keySet().contains(LexicalAnalyzer.currentLexeme)) {
			Compiler.table.put(LexicalAnalyzer.currentLexeme, new LexemeInfo(LexicalAnalyzer.idToken, "id"));
		}
		
		try {
			reader.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
