package lexical_analysis;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import lexical_analysis.semantic_actions.*;
import parser.*;
import compiler.Compiler;

public class LexicalAnalyzer {
	
	private BufferedInputStream reader;
	private int state = 0;
	private Parser parser;
	
	public static String currentLexeme;
	public static int currentChar;
	public static int lineNumber = 1;
	
	public static final int idToken = 257;
	public static final int intToken = 258;
	
	//									   L   N   C   _   /   *  \t  \n  others
	private final int stateMatrix[][] = {{ 1,  2, -1,  1,  3, -1,  0,  0, -2},
								  		 { 1,  1, -1,  1, -1, -1, -1, -1, -2},
								  		 {-1,  2, -1, -1, -2, -1, -1, -1, -2},
								  		 {-2, -2, -2, -2,  4,  5, -2, -2, -2},
								  		 { 4,  4,  4,  4,  4,  4,  4,  0,  4},
								  		 { 5,  5,  5,  5,  5,  6,  5,  5,  5},
								  		 { 5,  5,  5,  5,  0,  6,  5,  5,  5}};
	
	private SemanticAction[][] actionsMatrix;
	
	public LexicalAnalyzer(String programPath, Parser parser) {
		lineNumber = 1;
		this.parser = parser;
		try {
			reader = new BufferedInputStream(new FileInputStream(programPath));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR. Input file error.");
		}
		
		actionsMatrix = initActionsMatrix();
	}
	
	private SemanticAction[][] initActionsMatrix() {
		
		SA1_NewLexeme SA1 = new SA1_NewLexeme();
		SA2_AddCharacter SA2 = new SA2_AddCharacter();
		SA3_CheckReservedWord SA3 = new SA3_CheckReservedWord(reader);
		SA4_NewNumber SA4 = new SA4_NewNumber(reader);
		SA5_NewLine SA5 = new SA5_NewLine();
		
		SemanticAction[][] initMatrix = {{SA1,  SA2,  null, SA1,  null, null, null, SA5,  null},
										 {SA2,  SA2,  SA3,  SA2,  SA3,  SA3,  SA3,  SA3,  SA3},
										 {SA4,  SA2,  SA4,  SA4,  SA4,  SA4,  SA4,  SA4,  SA4},
										 {null, null, null, null, null, null, null, null, null},
										 {null, null, null, null, null, null, null, null, null},
										 {null, null, null, null, null, null, null, null, null},
										 {null, null, null, null, null, null, null, null, null}};
		return initMatrix;
	}
	
	
	
	private int getColumn(int readValue) {
		
		if(readValue >= 65 && readValue <= 90) //UPPERCASE
			return 0;
		
		if(readValue >= 97 && readValue <= 122) //LOWERCASE
			return 0;
		
		if(readValue >= 48 && readValue <= 57) //NUMBER
			return 1;
		
		HashSet<Integer> possibleChars = new HashSet<Integer>();
		possibleChars.addAll(Arrays.asList(40,41,44,59,60,61,62,123,125,58));
		
		if(possibleChars.contains(readValue)) //ANY ALLOWED CHAR
			return 2;
		
		if(readValue == 95)
			return 3;
		
		if(readValue == 47) //SLASH
			return 4;
		
		if(readValue == 42) //ASTERISK
			return 5;
		
		if((readValue == 32) || (readValue == 9) || (readValue == 13)) //TAB, SPACE or CR
			return 6;
		
		if(readValue == 10) //NEW LINE
			return 7;
		
		return 8; //ANY OTHER CHAR	
	}
	

	public int yylex() {
		try {
			currentLexeme = "";
			state = 0;
			reader.mark(2);
			currentChar = reader.read();
			if(currentChar >= 0) {
				while(state >= 0) {
					if(actionsMatrix[state][getColumn(currentChar)] != null)
						actionsMatrix[state][getColumn(currentChar)].execute();
					state = stateMatrix[state][getColumn(currentChar)];
					if(state >= 0) {
						reader.mark(2);
						currentChar = reader.read();
					}
				}
				
				if(state == -1)
					if(!Compiler.table.containsKey(currentLexeme))
						return currentChar;
					else {
						parser.setLexeme(new ParserVal(currentLexeme));
						return Compiler.table.get(currentLexeme).tokenNumber;
					}
				else
					if(currentChar == -1)
						return 0;
					else
						if(state == -2)
							System.out.println("Lexical error in line " +  lineNumber);
			}
			else
				return 0;
			
		} catch (IOException e) {
			System.out.println("ERROR. While reading input file.");
		}
		return 0;
	}

	
	
	
	
}
