package principal;

import java.util.Vector;

public class Fuzzificador {
	
	private Vector<Variable> variablesEntrada;
	
	public Fuzzificador() {
		variablesEntrada = new Vector<Variable>();
	}
	
	public boolean verificarNombreVariable(String nombre) {
		for(int i=0; i<variablesEntrada.size(); i++) {
			if(variablesEntrada.get(i).getNombre().equals(nombre)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean agregarVariable(Variable v) {
		
		if(verificarNombreVariable(v.getNombre())) {
			return variablesEntrada.add(v);
		}
		
		return false;
	}
	
	
	public boolean eliminarVariable(String nombre) {
		
		for(int i=0; i<variablesEntrada.size(); i++) {
			if(variablesEntrada.get(i).getNombre().equals(nombre)) {
				return variablesEntrada.remove(variablesEntrada.get(i));
			}
		}
		return false;
		
	}
	
}
