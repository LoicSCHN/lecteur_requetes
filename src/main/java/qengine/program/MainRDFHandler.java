package qengine.program;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;


import java.util.List;
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
 * Ã€ adapter/reecrire selon vos traitements.
 * </p>
 */
public final class MainRDFHandler extends AbstractRDFHandler {
	
	List<String> parsedData = new ArrayList<String>();
	


	@Override
	public void handleStatement(Statement st) {
		//séparer en 3
		// arrayList
		// credeux dictionnaireser le dico
		//split pour faire les indexs
		
		parsedData.add(st.getSubject().toString()+' '+st.getPredicate().toString()+' '+st.getObject().toString());
		/*
		if(dictionnary.get(st.getSubject().toString()) == null) {
			cmpt++;
			dictionnary.put(st.getSubject().toString(), cmpt  );
			dictionnaryInv.put(cmpt,st.getSubject().toString()  );
		}
		
		subject = dictionnary.get(st.getSubject().toString());
		
		
		
		if(dictionnary.get(st.getPredicate().toString()) == null) {
			cmpt++;
			dictionnary.put(st.getPredicate().toString(), cmpt  );
			dictionnaryInv.put(cmpt,st.getPredicate().toString()  );
		}
		
		
		predicate = dictionnary.get(st.getPredicate().toString());
		
		
		if(dictionnary.get(st.getObject().toString()) == null) {
			cmpt++;
			dictionnary.put(st.getObject().toString(), cmpt  );
			dictionnaryInv.put(cmpt,st.getObject().toString()  );
		}
		
		object = dictionnary.get(st.getObject().toString());
		
		
		h.getOPS().add(object, predicate, subject);
		h.getOSP().add(object, subject, predicate);
		h.getSPO().add(subject, predicate, object);
		h.getSOP().add(subject,object , predicate);
		h.getPSO().add(predicate, subject, object);
		h.getPOS().add(predicate, object, subject);*/

		// test unitaire 
		
	
		

	}
	
	

}


