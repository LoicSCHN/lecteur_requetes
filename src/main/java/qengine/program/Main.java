package qengine.program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;

/**
 * Programme simple lisant un fichier de requete et un fichier de donnees.
 * 
 * <p>
 * Les entrees sont donnees ici de maniere statique, a vous de programmer les entrees par passage d'arguments en ligne de commande comme demande dans l'enonce.
 * </p>
 * 
 * <p>
 * Le present programme se contente de vous montrer la voie pour lire les triples et requetes
 * depuis les fichiers ; ce sera a vous d'adapter/reecrire le code pour finalement utiliser les requetes et interroger les donnees.
 * On ne s'attend pas forcemment a ce que vous gardiez la meme structure de code, vous pouvez tout reecrire.
 * </p>
 * 
 * @author Olivier Rodriguez <olivier.rodriguez1@umontpellier.fr>
 */
final class Main {
	static final String baseURI = null;

	/**
	 * Votre repertoire de travail a vont se trouver les fichiers a lire
	 */
	static final String workingDir = "data/";

	/**
	 * Fichier contenant les requetes sparql
	 */
	static final String queryFile = workingDir + "sample_query.queryset";

	static final String queryFileALL = workingDir + "STAR_ALL_workload.queryset";

	/**
	 * Fichier contenant des donnees rdf
	 */
	static final String dataFile = workingDir + "sample_data.nt";

	static final String dataFile100k = workingDir + "100K.nt";

	static final MainRDFHandler rdf = new MainRDFHandler();

	// ========================================================================

	/**
	 * Methode utilisee ici lors du parsing de requete sparql pour agir sur l'objet obtenu.
	 */


	/**
	 * Entree du programme
	 */
	public static void main(String[] args) throws Exception {
		File file = new File("data/result/resQuery.txt");
		if (file.createNewFile()) {
			System.out.println("File created: " + file.getName());
		} else {
			System.out.println("File already exists.");
			if (file.delete()) {
				System.out.println("File deleted successfully");
				file.createNewFile();
			}
			else {
				System.out.println("Failed to delete the file");
			}
		}
		createDictionnary();
		parseQueries();
		//rdf.h.getOPS().affiche();
		//System.out.println(rdf.getInDico("http://db.uwaterloo.ca/~galuc/wsdbm/User0"));



	}

	public static int getInDico(String value){		
		if (rdf.Dictionnary.get(value) != null)
			return rdf.Dictionnary.get(value);
		else 
			return 0;
	}
	public static String getInDicoInv(int value){		
		if (rdf.DictionnaryInv.get(value) != null)
			return rdf.DictionnaryInv.get(value);
		else 
			return null;
	}

	public static void createDictionnary() throws FileNotFoundException, IOException{

		try (Reader dataReader = new FileReader(dataFile100k)) {
			// On va parser des donnees au format ntriples
			RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);



			// On utilise notre implementation de handler
			rdfParser.setRDFHandler(rdf);

			// Parsing et traitement de chaque triple par le handler
			rdfParser.parse(dataReader, baseURI);
		}



	}
	public static void processAQuery2(ParsedQuery query) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("data/result/res.txt", "UTF-8");
		writer.println(rdf.Dictionnary);
		writer.close();
		List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());
		Set<Integer> listRep = new HashSet<Integer>(); 
		List<String> listRepString = new ArrayList<String>();
		boolean isFirst = true;
		for(StatementPattern pat : patterns) {
			System.out.println("Predicat : " + pat.getPredicateVar().getValue());
			System.out.println("Object : " + pat.getObjectVar().getValue());
			int pred = getInDico(pat.getPredicateVar().getValue().toString());
			int obj = getInDico(pat.getObjectVar().getValue().toString());
			System.out.println(pred);
			System.out.println(obj);
			List<Integer> res = rdf.h.getPOS().search(pred, obj);
			if(res != null) {
				if(listRep.isEmpty() && isFirst == true) {
					for(int i: res)
						listRep.add(i);
				}else {
					listRep.retainAll(res);
				}
			}
			isFirst = false;
		}
		
		for(int i : listRep) {
			listRepString.add(getInDicoInv(i));
		}
		System.out.println(listRepString);

	}


	// ========================================================================

	/**
	 * Traite chaque requete lue dans {@link #queryFile} avec {@link #processAQuery(ParsedQuery)}.
	 */
	private static void parseQueries() throws FileNotFoundException, IOException {
		/**
		 * Try-with-resources
		 * 
		 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
		 */
		/*
		 * On utilise un stream pour lire les lignes une par une, sans avoir a toutes les stocker
		 * entierement dans une collection.
		 */

		try (Stream<String> lineStream = Files.lines(Paths.get(queryFileALL))) {

			SPARQLParser sparqlParser = new SPARQLParser();
			Iterator<String> lineIterator = lineStream.iterator();
			StringBuilder queryString = new StringBuilder();
			int boucle = 0;

			while (lineIterator.hasNext())
				/*
				 * On stocke plusieurs lignes jusqu'a ce que l'une d'entre elles se termine par un '}'
				 * On considere alors que c'est la fin d'une requete
				 */
			{
				String line = lineIterator.next();
				queryString.append(line);

				if (line.trim().endsWith("}")) {
					boucle++;
					System.out.println(boucle);
					//ici on passe a la requete suivante
					ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), baseURI);

					processAQuery2(query); // Traitement de la requete, a adapter/reecrire pour votre programme

					queryString.setLength(0); // Reset le buffer de la requete en chaine vide
				}
			}
		}
	}


	public static void processAQuery(ParsedQuery query) {
		List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());

		System.out.println("first pattern : " + patterns.get(0));

		System.out.println("object of the first pattern : " + patterns.get(0).getObjectVar().getValue());

		System.out.println("variables to project : ");

		// Utilisation d'une classe anonyme
		query.getTupleExpr().visit(new AbstractQueryModelVisitor<RuntimeException>() {

			public void meet(Projection projection) {
				System.out.println(projection.getProjectionElemList().getElements());
			}
		});
	}



}

//RUN>RunConfiguration

