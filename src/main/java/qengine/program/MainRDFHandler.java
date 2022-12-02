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
 * À adapter/reecrire selon vos traitements.
 * </p>
 */
public final class MainRDFHandler extends AbstractRDFHandler {
	
	List<String> parsedData = new ArrayList<String>();
	


	@Override
	public void handleStatement(Statement st) {

		parsedData.add(st.getSubject().toString()+' '+st.getPredicate().toString()+' '+st.getObject().toString());


	}
	
	

}


