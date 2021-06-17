package lexical_analysis.semantic_actions;

import lexical_analysis.LexicalAnalyzer;

public class SA2_AddCharacter implements SemanticAction{

	@Override
	public void execute() {
		LexicalAnalyzer.currentLexeme = LexicalAnalyzer.currentLexeme + (char)LexicalAnalyzer.currentChar;
	}

}
