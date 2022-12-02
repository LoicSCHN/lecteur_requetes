package qengine.program;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.junit.jupiter.api.Test;


class MainTest {


	@Test
	public void testGetDico() throws FileNotFoundException, IOException {
		String str = "<http://db.uwaterloo.ca/~galuc/wsdbm/User0>";
		//Main.createDictionnary();
		String data = Main.dataFile100k;
		String queries = "data/queries/";
		List<ParsedQuery> allQueries = new ArrayList<ParsedQuery>();
		Main.h.getOSP().affiche();
		int res = Main.dico.getinDico(str);
		assertEquals(0,res);
		assertNotEquals(4, res);
		assertEquals(107338, Main.nbLigneData(data));
		allQueries = Main.parseQueriesInArray(queries, allQueries);
		assertEquals(1200, allQueries.size());
		
	}
	
	@Test
	public void testIndex() throws FileNotFoundException, IOException
	{
		//Main.createDictionnary();
		assertEquals(Main.h.getOPS().getIndex().size(), Main.h.getOSP().getIndex().size());
		assertEquals(Main.h.getPSO().getIndex().size(), Main.h.getPOS().getIndex().size());
		assertEquals(Main.h.getSOP().getIndex().size(), Main.h.getSPO().getIndex().size());
	}
	
	// verifier qu'il y a tous les indexs
	// tests sur les indexs

}
