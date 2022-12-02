package qengine.program;

import java.util.HashMap;
import java.util.Map;

public class Dictionnary {
	private Map<String, Integer> dictionnary;
    private Map<Integer,String> dictionnaryInv;
    private int index;
    
    public Dictionnary() {
        this.dictionnary = new HashMap<>();
        this.dictionnaryInv = new HashMap<>();
        this.index = 0;
    }
    
    public Dictionnary(Map<String, Integer> dictionnary, Map<Integer,String> dictionnaryInv) {
        this.dictionnary = dictionnary;
        this.dictionnaryInv = dictionnaryInv;
        this.index = dictionnary.size();
    }
    
    public Map<String, Integer> getDictionnary() {
        return dictionnary;
    }

    public Map<Integer, String> getDictionnaryInv() {
        return dictionnaryInv;
    }
    
    public int getinDico(String value) {
    	if(dictionnary.get(value) != null)
    		return dictionnary.get(value);
    	else
    		return 0;
    }

    public String getinDicoInv(int value) {
    	if(dictionnaryInv.get(value) != null)
    		return dictionnaryInv.get(value);
    	else
    		return null;
    }
    
    public void add(String value) {
        if (!dictionnary.containsKey(value)) {
        	dictionnary.put(value, index);
        	dictionnaryInv.put(index, value);
        	index++;
        }
    }
    
    @Override
    public String toString() {
        return "Dictionnaire{" +
                "dico=" + dictionnary +
                
                ", size=" + index +
                '}';
    }
}
