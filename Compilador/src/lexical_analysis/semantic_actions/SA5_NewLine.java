package lexical_analysis.semantic_actions;

import lexical_analysis.LexicalAnalyzer;

public class SA5_NewLine implements SemanticAction{

	@Override
	public void execute() {
		LexicalAnalyzer.lineNumber++;
	}

}
