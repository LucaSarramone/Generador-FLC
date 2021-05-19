package compiler;

import java.io.IOException;

import defuzz.CentroidDefuzz;
import evaluation_method.MinMaxMethod;
import fuzz.Fuzzifier;
import rules_elements.InOut;
import rules_elements.Variable;
import rules_elements.fuzzy_set.SShapeFS;
import rules_elements.fuzzy_set.SingletonFS;
import rules_elements.fuzzy_set.TriangularFS;
import rules_elements.fuzzy_set.ZShapeFS;
import writer.Writer;

public class CodeGenerator {
	
	public static void main(String[] args) {
		
		
		Variable velocidad = new Variable("Velocidad", -30, 30);
		
		ZShapeFS VelNL = new ZShapeFS("NL", -24, -15);
		velocidad.addSet(VelNL);
		TriangularFS VelNM = new TriangularFS("NM", -30, -15, -5);
		velocidad.addSet(VelNM);
		TriangularFS VelNS = new TriangularFS("NS", -15, -5, 0);
		velocidad.addSet(VelNS);
		TriangularFS VelZ = new TriangularFS("Z", -5, 0, 5);
		velocidad.addSet(VelZ);
		TriangularFS VelPS = new TriangularFS("PS", 0, 5, 15);
		velocidad.addSet(VelPS);
		TriangularFS VelPM = new TriangularFS("PM", 5, 15, 30);
		velocidad.addSet(VelPM);
		SShapeFS VelPL = new SShapeFS("PL", 15, 24);
		velocidad.addSet(VelPL);
		
		Variable angulo = new Variable("Angulo", -180, 180);
		
		ZShapeFS AngNL = new ZShapeFS("NL", -70, -54);
		angulo.addSet(AngNL);
		TriangularFS AngNM = new TriangularFS("NM", -180, -40, -20);
		angulo.addSet(AngNM);
		TriangularFS AngNS = new TriangularFS("NS", -35, -15, 0);
		angulo.addSet(AngNS);
		TriangularFS AngZ = new TriangularFS("Z", -10, 0, 10);
		angulo.addSet(AngZ);
		TriangularFS AngPS = new TriangularFS("PS", 0, 15, 35);
		angulo.addSet(AngPS);
		TriangularFS AngPM = new TriangularFS("PM", 20, 40, 180);
		angulo.addSet(AngPM);
		SShapeFS AngPL = new SShapeFS("PL", 54, 70);
		angulo.addSet(AngPL);
		
		Variable fuerza = new Variable("Fuerza", -30, 30);
		SingletonFS FuerzaNL = new SingletonFS("NL", -30);
		fuerza.addSet(FuerzaNL);
		SingletonFS FuerzaNM = new SingletonFS("NM", -18);
		fuerza.addSet(FuerzaNM);
		SingletonFS FuerzaNS = new SingletonFS("NS", -5);
		fuerza.addSet(FuerzaNS);
		SingletonFS FuerzaZ = new SingletonFS("Z", 0);
		fuerza.addSet(FuerzaZ);
		SingletonFS FuerzaPS = new SingletonFS("PS", 5);
		fuerza.addSet(FuerzaPS);
		SingletonFS FuerzaPM = new SingletonFS("PM", 18);
		fuerza.addSet(FuerzaPM);
		SingletonFS FuerzaPL = new SingletonFS("PL", 30);
		fuerza.addSet(FuerzaPL);
		
		
		 InOut.init(8);
		 InOut.inVars.add(velocidad);
		 InOut.inVars.add(angulo);
		 InOut.outVars.add(fuerza);
		 
		 Fuzzifier fuzzificador = new Fuzzifier();
		 MinMaxMethod motorDeReglas = new MinMaxMethod();
		 motorDeReglas.initMatrix();
		 CentroidDefuzz defuzzificador = new CentroidDefuzz();
		 
		 String path = "C:\\Users\\Luca\\Downloads\\PruebasCompilador\\Test.txt";
		 try {
			 Writer.openFile(path);
			 Writer.file.write( "#include <iostream> \n" + 
					 				 "#include <ap_int.h> \n \n" );
			 
			 Writer.file.write( "typedef ap_uint<" + InOut.converterSize + "> fixed_int; \n" );
			 
			 for(int i=0; i<InOut.outVars.size(); i++) {
				 Writer.file.write( "typedef ap_uint<" + InOut.getAuxTypeSize(i) + 
						 				 "> fixed_aux_"+ InOut.outVars.get(i).getName() +"; \n" );
			 }
			 
			 Writer.file.write( "typedef ap_uint<" + InOut.getOutTypeSize() + "> fixed_out; \n");
			 
			 Writer.file.write( "using namespace std; \n \n" );
			 						 
			 					 	 
			 
			 fuzzificador.compileFunctionsSlope();
			 fuzzificador.compilerInputVariablesBuffers();
			 defuzzificador.compileOutputVariablesBuffers();
			 
			 Writer.file.write("\n \n");
			 
			 fuzzificador.compileFuzzifier();
			 motorDeReglas.compileEvalMethod();
			 for(int i=0; i<InOut.outVars.size(); i++) {
				 defuzzificador.compileDefuzz(i);
			 }
			 
			 Writer.file.write("\n");
			 Writer.file.write("fixed_out fuzzyController(fixed_int " + InOut.inVars.get(0).getName());
			 for(int i=1; i<InOut.inVars.size(); i++) {
				 Writer.file.write(", fixed_int " + InOut.inVars.get(i).getName());
			 }
			 Writer.file.write("){ \n\n");
			 Writer.file.write("\tfuzzifier(" + InOut.inVars.get(0).getName());
			 for(int i=1; i<InOut.inVars.size(); i++) {
				 Writer.file.write(", " + InOut.inVars.get(i).getName());
			 }
			 Writer.file.write(");\n");
			 Writer.file.write("\trulesEvaluation(); \n");
			 
			 Writer.file.write("\n");
			 Writer.file.write("\tfixed_out output = defuzzifier" +  InOut.outVars.get(0).getName() + "(); \n");
			 
			for(int i=1; i<InOut.outVars.size(); i++ ){ 
				Writer.file.write("\toutput <<8;\n");
				Writer.file.write("\toutput = output + defuzzifier" + InOut.outVars.get(i).getName() + "();\n");
			}
			
			Writer.file.write("\treturn output;\n");
			Writer.file.write("}\n");
			 
			 Writer.closeFile();
			 
		 }
		 catch(IOException e) {
			 e.printStackTrace();
		 }
		 
		//motorDeReglas.printMatriz();
		System.out.println("Listo!");
		
		

	}

}
