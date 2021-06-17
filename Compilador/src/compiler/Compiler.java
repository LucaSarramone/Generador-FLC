package compiler;

import java.util.HashMap;

import code_generation.variables.IOVars;
import parser.Parser;

public class Compiler {
	
	public static HashMap<String, LexemeInfo> table;
	
	private static void initTable() {
		table = new  HashMap<String, LexemeInfo>();
		
		table.put("FUZZ", new LexemeInfo(259, "reserved", 1));
		table.put("BEGIN", new LexemeInfo(260, "reserved", 1));
		table.put("END", new LexemeInfo(261, "reserved", 1));
		table.put("DECLARE", new LexemeInfo(262, "reserved", 1));
		table.put("RULES", new LexemeInfo(263, "reserved", 1));
		table.put("IN", new LexemeInfo(264, "reserved", 1));
		table.put("OUT", new LexemeInfo(265, "reserved", 1));
		table.put("DEFUZZ", new LexemeInfo(266, "reserved", 1));
		table.put("TRIANGULAR", new LexemeInfo(267, "reserved", 1));
		table.put("SINGLETON", new LexemeInfo(268, "reserved", 1));
		table.put("Z_SHAPE", new LexemeInfo(269, "reserved", 1));
		table.put("S_SHAPE", new LexemeInfo(270, "reserved", 1));
		table.put("MINMAX", new LexemeInfo(271, "reserved", 1));
		table.put("MANDANI", new LexemeInfo(272, "reserved", 1));
		table.put("CENTROID", new LexemeInfo(273, "reserved", 1));
	}
	
	public static void main(String args[]) {
		initTable();
		IOVars.init(8);
		Parser parser = new Parser("C:\\Users\\Luca\\Downloads\\CompilerTest\\Lenguaje_ejemplo.txt");
		//parser.debugMode();
		parser.compile();

	}
}
