package plantillas;

import java.util.Vector;

public abstract class Plantilla {
	
	protected Vector<Integer> parametros;
	protected int cantidadParametros;
	
	public Plantilla() {
		parametros = new Vector<Integer>();
	}
	
	public abstract void compilar();
	
	public boolean verificarCantidad(int p) {
		return parametros.size() == p;
	}
	
	public boolean cambiarParametros(Vector<Integer> params) {
		parametros.clear();
		if(verificarCantidad(params.size()))
			return parametros.addAll(params);
		
		return false;
	}
	
	
	
}
