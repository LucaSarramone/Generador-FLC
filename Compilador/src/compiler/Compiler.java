package compiler;

import java.util.HashMap;

import code_generation.variables.IOVars;
import parser.Parser;

public class Compiler {
	
	public static HashMap<String, LexemeInfo> table;
	
	private static void initTable() {
		table = new  HashMap<String, LexemeInfo>();
		
		table.put("FUZZ", new LexemeInfo(259, "reserved"));
		table.put("BEGIN", new LexemeInfo(260, "reserved"));
		table.put("END", new LexemeInfo(261, "reserved"));
		table.put("DECLARE", new LexemeInfo(262, "reserved"));
		table.put("RULES", new LexemeInfo(263, "reserved"));
		table.put("IN", new LexemeInfo(264, "reserved"));
		table.put("OUT", new LexemeInfo(265, "reserved"));
		table.put("DEFUZZ", new LexemeInfo(266, "reserved"));
		table.put("TRIANGULAR", new LexemeInfo(267, "reserved"));
		table.put("SINGLETON", new LexemeInfo(268, "reserved"));
		table.put("Z_SHAPE", new LexemeInfo(269, "reserved"));
		table.put("S_SHAPE", new LexemeInfo(270, "reserved"));
		table.put("MINMAX", new LexemeInfo(271, "reserved"));
		table.put("MANDANI", new LexemeInfo(272, "reserved"));
		table.put("CENTROID", new LexemeInfo(273, "reserved"));
	}
	
	public static void main(String args[]) {
		initTable();
		IOVars.init();
		Parser parser = new Parser("C:\\Users\\Luca\\Downloads\\CompilerTest\\Lenguaje_ejemplo.txt");
		//parser.debugMode();
		parser.compile();
		printTable();

	}
	
	public static void printTable() {
		System.out.println();
		System.out.println();
		System.out.println(" TABLA DE SIMBOLOS ----------------------------");
		for(String s: table.keySet()) {
			
			if(table.get(s).tokenType.equals("id")) {
				System.out.print(s + " = ");
				System.out.println(table.get(s).toString());
			}
		}
	}
}
