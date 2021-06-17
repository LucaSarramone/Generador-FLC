package lexical_analysis.semantic_actions;

import lexical_analysis.LexicalAnalyzer;

public class SA1_NewLexeme implements SemanticAction{
	
	@Override
	public void execute() {
		LexicalAnalyzer.currentLexeme =  Character.toString((char)LexicalAnalyzer.currentChar);
	}

}
