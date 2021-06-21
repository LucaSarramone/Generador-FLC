package principal;

import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

public class Principal {
	
	public static void main (String args[]) {
		
		Vector<Integer> vars = new Vector<Integer>();
		vars.add(3);
		vars.add(2);
		vars.add(3);
		
		int[] position = {0, 0, 0};
		
		int pos = 1;
		for(int i=0; i<vars.size(); i++)
			pos = pos * vars.get(i);
		
		String newPos = "";
		for(int j=0; j<pos; j++) {
			
			newPos = Arrays.stream(position)
			        .mapToObj(String::valueOf)
			        .collect(Collectors.joining(","));
			
			System.out.println(newPos);
			
			boolean nextElem = true;
			for(int i=position.length - 1; i>=0; i--) {
				if(nextElem) {
					position[i]++;
					if(position[i] == vars.get(i)) 
						position[i] = 0;
					else
						nextElem = false;
				}
			}
			
		}
		
	}
}
