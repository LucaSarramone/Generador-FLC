package principal;

import java.io.File;
import java.util.Vector;

public class Variable {
	
	private String nombre;
	private Vector<ConjuntoDifuso> conjuntos;
	
	public Variable(String n) {
		this.nombre = n;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String n) {
		this.nombre = n;
	}
	
	public boolean verificarNombreConjunto(String nombre) {
		
		for(int i=0; i<conjuntos.size(); i++) {
			if(conjuntos.get(i).getNombre().equals(nombre)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean agregarConjunto(ConjuntoDifuso cd) {
		if(verificarNombreConjunto(cd.getNombre())) {
			return conjuntos.add(cd);
		}
		return false;
	}
	
	public boolean eliminarConjunto(String nombre) {
		
		for(int i=0; i<conjuntos.size(); i++) {
			if(conjuntos.get(i).getNombre().equals(nombre)) {
				return conjuntos.remove(conjuntos.get(i));
			}
		}
		return false;
	}
	
	public void compilar(File f) {
		//TODO por cada conjunto difuso, invocar a su metodo compilar.
	}
	
}
