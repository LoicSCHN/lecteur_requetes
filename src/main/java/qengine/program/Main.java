package qengine.program;

import java.io.BufferedReader;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
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
	static final String queryDir = "data/queries/";
	/**
	 * Fichier contenant les requetes sparql
	 */
	static final String queryFile = queryDir + "sample_query.queryset";

	static final String queryFileALL = queryDir + "STAR_ALL_workload.queryset";

	/**
	 * Fichier contenant des donnees rdf
	 */
	static final String dataFile = workingDir + "sample_data.nt";

	static final String dataFile100k = workingDir + "100K.nt";

	static final MainRDFHandler rdf = new MainRDFHandler();

	static final Hexastore h = new Hexastore();
	static Dictionnary dico = new Dictionnary();
	// ========================================================================

	/**
	 * Methode utilisee ici lors du parsing de requete sparql pour agir sur l'objet obtenu.
	 */


	/**
	 * Entree du programme
	 */
	public static void main(String[] args) throws Exception {
		
		long tTotalDebut, tTotalFin;
		double tTotal;
		long tLectureReqDebut, tLectureReqFin;
		double tLectureReqTotal;
		long tWorkloadDebut, tWorkloadFin;
		double tWorkloadTotal;
		long tDataDebut, tDataFin;
		double tDataTotal;
		long tDicoDebut, tDicoFin;
		double tDicoTotal;
		
		tTotalDebut = System.currentTimeMillis();
		
		String queries = "";
		String data = "";
		String output = "";
		boolean Jena = false;
		float warm = 0;
		boolean shuffle = false;
		boolean queryResult = false;
		
		String resQ = "resQuery.csv";
		String resOutput = "resOutput.csv";
		List<ParsedQuery> allQueries = new ArrayList<ParsedQuery>();

		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-queries")) {
				queries = args[i+1];
			}
			if(args[i].equals("-data")) {
				data = args[i+1];
			}
			if(args[i].equals("-output")) {
				output = args[i+1];
			}
			if(args[i].equals("-Jena")) {
				Jena = true;
			}
			if(args[i].equals("-warm")) {
				warm =Integer.parseInt(args[i+1]) ;
			}
			if(args[i].equals("-shuffle")) {
				shuffle = true;
			}
			if(args[i].equals("-export_query_results")) {
				queryResult = true;
			}
		}
		System.out.println(queries);
		System.out.println(data);
		System.out.println(output);
		System.out.println(Jena);
		System.out.println(warm);
		System.out.println(shuffle);
		
		tDataDebut = System.currentTimeMillis();
		parseData(data);
		tDataFin = System.currentTimeMillis();
		tDataTotal = tDataFin - tDataDebut;
		
		tDicoDebut = System.currentTimeMillis();
		 createDico(rdf.parsedData);
		 //System.out.println(dico.getinDico("http://db.uwaterloo.ca/~galuc/wsdbm/Country135"));
		tDicoFin = System.currentTimeMillis();
		tDicoTotal = tDicoFin - tDicoDebut;
		
		long tIndexDebut = System.currentTimeMillis();
		createIndex(rdf.parsedData);
		long tIndexFin = System.currentTimeMillis();
		long tIndexTotal = tIndexFin - tIndexDebut;
		//createDictionnary(data);
		//System.out.println(rdf.parsedData.size());
		tLectureReqDebut = System.currentTimeMillis();
		allQueries = parseQueriesInArray( queries, allQueries);
		tLectureReqFin = System.currentTimeMillis();
		tLectureReqTotal = tLectureReqFin - tLectureReqDebut;
		
		if(warm != 0) {
			System.out.println("Echauffement  : ");
			List<ParsedQuery> warmList = new ArrayList<ParsedQuery>();
			for(ParsedQuery q : allQueries) {
				warmList.add(q);
			}
			warmIt( warm,warmList);
		}
		if(shuffle) {
			Collections.shuffle(allQueries);
		}
		tWorkloadDebut = System.currentTimeMillis();
		parseQueries(createFileResult(output, resQ), queryResult,allQueries,Jena,data);
		tWorkloadFin = System.currentTimeMillis();
		tWorkloadTotal = tWorkloadFin-tWorkloadDebut;
		
		tTotalFin = System.currentTimeMillis();
		tTotal = (tTotalFin - tTotalDebut);
		
		System.out.println("Temps workload (ms): "+tWorkloadTotal);
		System.out.println("Temps total (ms): "+tTotal);
		
		File outputFile = createFileResult(output, resOutput);
		PrintWriter writerRes = new PrintWriter(outputFile);
		writerRes.write("nom du fichier de donn?es"+','+"nom dossier requetes"+','+"nombre de tripletsRDF"+','+"nombre de requetes"+','+"temps lecture des donn?es (ms)"+','+"temps lecture des requetes (ms)"+','+" temps cr?ation dico (ms) "+','+" nombre d?index"+','+"temps de cr?ation des index (ms)"+','+"temps total d??valuation du workload (ms)"+','+"temps total (du d?but ? la fin du programme) (ms)");
		
		writerRes.write('\n');
		
		writerRes.write(data+','+output+','+nbLigneData(data)+','+allQueries.size()+','+tDataTotal+','+tLectureReqTotal+','+tDicoTotal+','+"6"+','+tIndexTotal+','+tWorkloadTotal+','+tTotal);
		//mettre fichier data.len
		writerRes.close();
		



	}
	
	private static void warmIt(float warm,List<ParsedQuery> Queries) {
		Collections.shuffle(Queries);
		float nb = Queries.size()*(warm/100.0f);
		//System.out.println(warm/100);
		System.out.println("Taille de l'?chauffement : "+nb);
		for(int i=0;i<nb;i++) {
			List<StatementPattern> patterns = StatementPatternCollector.process(Queries.get(i).getTupleExpr());
			Set<Integer> listRep = new HashSet<Integer>(); 
			List<String> listRepString = new ArrayList<String>();
			boolean isFirst = true;
			for(StatementPattern pat : patterns) {
				//System.out.println(pat.getObjectVar().getValue().toString());
				int pred = dico.getinDico(pat.getPredicateVar().getValue().toString());
				int obj = dico.getinDico(pat.getObjectVar().getValue().toString());

				List<Integer> res = h.getPOS().search(pred, obj);
				if(res != null) {
					if(listRep.isEmpty() && isFirst == true) {
						for(int k: res) {
							listRep.add(k);
						}
					}else {
						listRep.retainAll(res);
					}
				}
				isFirst = false;
			}

			for(int j : listRep) {
				listRepString.add(dico.getinDicoInv(j));
				
			}
		}
		
		
	}

	private static File createFileResult(String output,String name) throws IOException {
		String chemin = output.concat(name);
		//System.out.println(chemin);
		//File file = new File("data/result/"+name);
		File file = new File(chemin);
		//System.out.println(file);
		if (file.createNewFile()) {
			//System.out.println("File created: " + file.getName());
		} else {
			//System.out.println("File already exists.");
			if (file.delete()) {
				//System.out.println("File deleted successfully");
				file.createNewFile();
			}
			else {
				//System.out.println("Failed to delete the file");
			}
		}
		return file;
	}
	

	
	public static void parseData(String data) throws FileNotFoundException, IOException{

		try (Reader dataReader = new FileReader(data)) {
			// On va parser des donnees au format ntriples
			RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

			// On utilise notre implementation de handler
			rdfParser.setRDFHandler(rdf);

			// Parsing et traitement de chaque triple par le handler
			rdfParser.parse(dataReader, baseURI);
		}
	}
	
	public static void createDico(List<String> datas) {
		//Dictionnary dictionnary = new Dictionnary();
		for (String data : datas) {
            String[] split = data.split(" ");
            dico.add(split[0]);
            dico.add(split[1]);
            dico.add(split[2]);
            
		}
		//return dictionnary;
	}
	
	public static void createIndex(List<String> datas) {
		for (String data : datas) {
            String[] split = data.split(" ");
            String s = split[0];
            String p = split[1];
            String o = split[2];
            
            h.getOPS().add(dico.getDictionnary().get(o), dico.getDictionnary().get(p), dico.getDictionnary().get(s));
            h.getOSP().add(dico.getDictionnary().get(o), dico.getDictionnary().get(s), dico.getDictionnary().get(p));
            h.getSPO().add(dico.getDictionnary().get(s), dico.getDictionnary().get(p), dico.getDictionnary().get(o));
            h.getSOP().add(dico.getDictionnary().get(s), dico.getDictionnary().get(o), dico.getDictionnary().get(p));
            h.getPSO().add(dico.getDictionnary().get(p), dico.getDictionnary().get(s), dico.getDictionnary().get(o));
            h.getPOS().add(dico.getDictionnary().get(p), dico.getDictionnary().get(o), dico.getDictionnary().get(s));
		}
		
	}
	
	public static List<String> processAQuery2(ParsedQuery query, PrintWriter writer, boolean queryResult) throws FileNotFoundException, UnsupportedEncodingException {
		/*PrintWriter writer = new PrintWriter("data/result/res.txt", "UTF-8");
		writer.println(rdf.dictionnary);
		writer.close();*/
		List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());
		Set<Integer> listRep = new HashSet<Integer>(); 
		List<String> listRepString = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		//sb.append(query.toString());
		boolean isFirst = true;
		for(StatementPattern pat : patterns) {
			int pred = dico.getinDico(pat.getPredicateVar().getValue().toString());
			int obj = dico.getinDico(pat.getObjectVar().getValue().toString());
			List<Integer> res = h.getPOS().search(pred, obj);
			if(res != null) {
				if(isFirst == true) {
					for(int i: res) {
						listRep.add(i);//addAll
					}
				}else {
					listRep.retainAll(res);
				}
			}
			isFirst = false;
		}

		for(int i : listRep) {
			listRepString.add(dico.getinDicoInv(i));
			sb.append(dico.getinDicoInv(i));
			sb.append(',');
		}
		if(listRepString.isEmpty()) {
			sb.append("null");
			//System.out.println("null");
		}else {
			//System.out.println(listRepString);
		}
		sb.append('\n');
		if(queryResult) {
			writer.write(sb.toString());
		}
		return listRepString;


	}


	// ========================================================================


	private static void parseQueries(File file, boolean queryResult, List<ParsedQuery> Queries, boolean Jena,String data) throws FileNotFoundException, IOException {
		PrintWriter writer = new PrintWriter(file);
		
		int ok = 0;
		int pasok = 0;
		int cmptRep = 0;
		Model model = modelData(data);
		System.out.println("Jena :");
		List<String> r = new ArrayList<String>();
		for(ParsedQuery query: Queries) {
			Set<String> myResult = new HashSet<String>();
			Set<String> jenaResult = new HashSet<String>();
			myResult.addAll(processAQuery2(query,writer,queryResult));
			 r = processAQuery2(query,writer,queryResult);
			cmptRep = cmptRep+r.size();
			//System.out.println("myResult : "+myResult);// Traitement de la requete, a adapter/reecrire pour votre programme
			if(Jena == true) {
				jenaResult.addAll(jenaQuery(queryForJena(query, data), model)) ;
				//System.out.println("jenaResult : "+jenaResult);
				if(myResult.equals(jenaResult)) {
					ok++;
				}else {
					System.out.println("me : "+myResult);
					System.out.println("jena"+ jenaResult);
					pasok++;
				}
			}

			
		}
		System.out.println("Valid?es : "+ok);
		System.out.println("Non valid?es : "+pasok);
		System.out.println("Compteur r?ponses : "+cmptRep);
		writer.close();
		if(!queryResult) {
			file.delete();
		}
	}

	public static List<ParsedQuery> parseQueriesInArray(String folderPath, List<ParsedQuery> Queries) throws FileNotFoundException, IOException {
		File dir  = new File(folderPath);
		File[] liste = dir.listFiles();
		for(File item : liste){
			if(item.isFile())
			{ 
				//System.out.format("Nom du fichier: %s%n", item.getName()); 
				try (Stream<String> lineStream = Files.lines(Paths.get(item.getPath()))) {

					SPARQLParser sparqlParser = new SPARQLParser();
					Iterator<String> lineIterator = lineStream.iterator();
					StringBuilder queryString = new StringBuilder();

					while (lineIterator.hasNext())
					{
						String line = lineIterator.next();
						queryString.append(line);

						if (line.trim().endsWith("}")) {

							ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), baseURI);
							Queries.add(query);

							queryString.setLength(0); // Reset le buffer de la requete en chaine vide
						}
					}
				}
			} 
		}

		return Queries;
	}

	public static int nbLigneData(String data) throws IOException {
		int nbrLine = 0;
		File file = new File(data);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);  
		String str;
		while((str = br.readLine()) != null)
		{
			nbrLine++;               

		}
		fr.close();
		return nbrLine;
	}
	

	
	public static Model modelData(String data) {
        Model model = ModelFactory.createDefaultModel();
        model.read(data);
        return model;
    }
	
	

    public static List<String> jenaQuery(String query, Model model) {
        List<String> answers = new ArrayList<>();
        //System.out.println("query : "+query);
        Query queryJena = QueryFactory.create(query);
        try (QueryExecution qexec = QueryExecutionFactory.create(queryJena, model)) {
            ResultSet results = qexec.execSelect();
            if (!results.hasNext()) {
                //answers.add("");
            } else {
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    answers.add(soln.get("v0").toString());
                }
            }
        }
        //System.out.println("ans : "+answers);
        return answers;
    }
    
    public static String queryForJena(ParsedQuery query, String data) {
        List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());

        Set<Integer> answers = new HashSet<>();
        
        String request = "";
        request += "SELECT ?v0 WHERE {";
        for (StatementPattern pattern : patterns) {
			request += "?" + pattern.getSubjectVar().getName() + " <" + pattern.getPredicateVar().getValue() + "> " + (pattern.getObjectVar().getValue().isLiteral()?pattern.getObjectVar().getValue():("<"+pattern.getObjectVar().getValue()+">")) + " .";
            //System.out.println(pattern.getPredicateVar().toString());
        }
		request += "}";
        //System.out.println(result);
        return request;
    }

}





