package qengine.program;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * Le RDFHandler intervient lors du parsing de données et permet d'appliquer un traitement pour chaque élément lu par le parseur.
 * 
 * <p>
 * Ce qui servira surtout dans le programme est la méthode {@link #handleStatement(Statement)} qui va permettre de traiter chaque triple lu.
 * </p>
 * <p>
 * À adapter/réécrire selon vos traitements.
 * </p>
 */
public final class MainRDFHandler extends AbstractRDFHandler {
	
	
	Map<String,Integer > Dictionnary = new HashMap<String,Integer>();
	
	 
	
	ArrayList<ArrayList<Integer>> SPO = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> POS = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> OSP = new ArrayList<ArrayList<Integer>>();
	
	ArrayList<ArrayList<Integer>> SOP = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> OPS = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> PSO = new ArrayList<ArrayList<Integer>>();
	
	private int subject;
	private int predicate;   
	private int object;
	
	
	private int cmpt = 0;
	@Override
	public void handleStatement(Statement st) {
		
		ArrayList<Integer> spo = new ArrayList<Integer>();
		ArrayList<Integer> pos = new ArrayList<Integer>();
		ArrayList<Integer> osp = new ArrayList<Integer>();
		
		ArrayList<Integer> sop = new ArrayList<Integer>();
		ArrayList<Integer> ops = new ArrayList<Integer>();
		ArrayList<Integer> pso = new ArrayList<Integer>();
		
		//System.out.println("\n" + st.getSubject() + "\t " + st.getPredicate() + "\t " + st.getObject());
		//System.out.println(Dictionnary.get(st.getSubject().toString()));
		if(Dictionnary.get(st.getSubject().toString()) == null) {
			cmpt++;
			Dictionnary.put(st.getSubject().toString(), cmpt  );
		}
		
		subject = Dictionnary.get(st.getSubject().toString());
		
		
		
		if(Dictionnary.get(st.getPredicate().toString()) == null) {
			cmpt++;
			Dictionnary.put(st.getPredicate().toString(), cmpt  );
		}
		
		
		predicate = Dictionnary.get(st.getPredicate().toString());
		
		
		if(Dictionnary.get(st.getObject().toString()) == null) {
			cmpt++;
			Dictionnary.put(st.getObject().toString(), cmpt  );
		}
		
		object = Dictionnary.get(st.getObject().toString());
		
		// test unitaire 
		// Verif pour tout
		//comment faire decodage
		// map de map puis liste
		
		spo.add(subject);
		spo.add(predicate);
		spo.add(object);
		
		pos.add(predicate);
		pos.add(object);
		pos.add(subject);
		
		osp.add(object);
		osp.add(subject);
		osp.add(predicate);
		
		sop.add(subject);
		sop.add(object);
		sop.add(predicate);
		
		ops.add(object);
		ops.add(predicate);
		ops.add(subject);
		
		pso.add(predicate);
		pso.add(subject);
		pso.add(object);

		SPO.add(spo);
		POS.add(pos);
		OSP.add(osp);
		
		SOP.add(sop);
		OPS.add(ops);
		PSO.add(pso);
	};
	

}


