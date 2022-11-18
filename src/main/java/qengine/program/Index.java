package qengine.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Index {
	private  Map<Integer, Map<Integer,List<Integer>>> index;
	


	public Index() {
		this.index = new HashMap<>();
		
	}
	
	public Map<Integer, Map<Integer,List<Integer>>> getIndex(){
		return index;
	}
	
	public void add(int un, int deux, int trois) {
		
		if(index.containsKey(un)) {
			if(index.get(un).containsKey(deux)) {
				if(!index.get(un).get(deux).contains(trois)) {
					index.get(un).get(deux).add(trois);
				}
			}
			else {
				index.get(un).put(deux, new ArrayList<Integer>() {{add(trois);}});
			}
		}
		else {
			index.put(un, new HashMap<Integer, List<Integer>>(){{put(deux, new ArrayList<Integer>() {{add(trois);}});}});
					
		}
		
	}
	
	public void affiche() {
		// Print keys and values
		/*for (Map<Integer,List<Integer>> i : index.values()) {
			System.out.println("Un : "+ index.);
		  for()
		}*/
		System.out.println(index);
	}
}
