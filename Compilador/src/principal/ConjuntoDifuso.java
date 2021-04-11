package principal;

import java.io.File;
import java.util.Vector;

public class ConjuntoDifuso {
	
	private String nombre;
	private String tipo;
	Vector<Integer> parametros;
	
	public ConjuntoDifuso(String n, String t) {
		this.nombre = n;
		this.tipo = t;
		parametros = new Vector<Integer>();
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setNombre(String n) {
		this.nombre = n;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public void setTipo(String t) {
		this.tipo = t;
	}
	
	public boolean cambiarParametros( Vector<Integer> nuevosParametros ) {
		
		parametros.clear();
		return parametros.addAll(nuevosParametros);
		
	}
	
	public void compilar(File salida) {
		
		//TODO llamar a la plantilla
		
	}
	
	

}
