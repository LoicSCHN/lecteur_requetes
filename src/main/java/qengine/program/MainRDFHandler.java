package qengine.program;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

import com.github.andrewoma.dexx.collection.List;

import java.util.HashMap;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Le RDFHandler intervient lors du parsing de donnees et permet d'appliquer un traitement pour chaque element lu par le parseur.
 * 
 * <p>
 * Ce qui servira surtout dans le programme est la methode {@link #handleStatement(Statement)} qui va permettre de traiter chaque triple lu.
 * </p>
 * <p>
 * À adapter/reecrire selon vos traitements.
 * </p>
 */
public final class MainRDFHandler extends AbstractRDFHandler {
	
	
	Map<String,Integer > Dictionnary = new HashMap<String,Integer>();
	Map<Integer,String > DictionnaryInv = new HashMap<Integer,String>();
	Hexastore h = new Hexastore();
	private int subject;
	private int predicate;   
	private int object;
	
	private int boucle = 0;
	private int cmpt = 0;
	@Override
	public void handleStatement(Statement st) {
		
		
		if(Dictionnary.get(st.getSubject().toString()) == null) {
			cmpt++;
			Dictionnary.put(st.getSubject().toString(), cmpt  );
			DictionnaryInv.put(cmpt,st.getSubject().toString()  );
		}
		
		subject = Dictionnary.get(st.getSubject().toString());
		
		
		
		if(Dictionnary.get(st.getPredicate().toString()) == null) {
			cmpt++;
			Dictionnary.put(st.getPredicate().toString(), cmpt  );
			DictionnaryInv.put(cmpt,st.getPredicate().toString()  );
		}
		
		
		predicate = Dictionnary.get(st.getPredicate().toString());
		
		
		if(Dictionnary.get(st.getObject().toString()) == null) {
			cmpt++;
			Dictionnary.put(st.getObject().toString(), cmpt  );
			DictionnaryInv.put(cmpt,st.getObject().toString()  );
		}
		
		object = Dictionnary.get(st.getObject().toString());
		
		
		h.getOPS().add(object, predicate, subject);
		h.getOSP().add(object, subject, predicate);
		h.getSPO().add(subject, predicate, object);
		h.getSOP().add(subject,object , predicate);
		h.getPSO().add(predicate, subject, object);
		h.getPOS().add(predicate, object, subject);

		// test unitaire 
		
		//comment faire decodage
	
		

	}
	
	

}


