package qengine.program;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import com.github.andrewoma.dexx.collection.List;

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
	
	private int subject;
	private int predicate;   
	private int object;
	
	
	private int cmpt = 0;
	@Override
	public void handleStatement(Statement st) {
		
		Hexastore h = new Hexastore();
		
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
		
		
		h.getOPS().add( object, predicate, subject);
		h.getOSP().add( object, subject, predicate);
		h.getSPO().add( subject, predicate, object);
		h.getSOP().add( subject,object , predicate);
		h.getPSO().add( predicate, subject, object);
		h.getPOS().add( predicate, object, subject);
		// test unitaire 
		// Verif pour tout
		//comment faire decodage
		// map de map puis liste
		

	}
	
	public int getInDico(String value){
		if (Dictionnary.get(value) != null)
			return Dictionnary.get(value);
		else 
			return 0;
	}
	

}


