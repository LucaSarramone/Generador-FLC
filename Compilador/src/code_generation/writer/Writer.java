package code_generation.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
	
	public static FileWriter file;
	
	public static void openFile(String path) throws IOException {
		File f = new File(path);
		file = new FileWriter(f);
	}
	
	public static void closeFile() throws IOException {
		file.flush();
		file.close();
	}

}
