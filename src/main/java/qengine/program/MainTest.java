package qengine.program;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class MainTest {


	@Test
	public void testGetDico() throws FileNotFoundException, IOException {
		String str = "http://db.uwaterloo.ca/~galuc/wsdbm/User0";
		//Main.createDictionnary();
		Main.rdf.h.getOSP().affiche();
		int res = Main.getInDico(str);
		assertEquals(1,res);
		assertNotEquals(4, res);
		
	}
	
	@Test
	public void testIndex() throws FileNotFoundException, IOException
	{
		//Main.createDictionnary();
		assertEquals(Main.rdf.h.getOPS().getIndex().size(), Main.rdf.h.getOSP().getIndex().size());
		assertEquals(Main.rdf.h.getPSO().getIndex().size(), Main.rdf.h.getPOS().getIndex().size());
		assertEquals(Main.rdf.h.getSOP().getIndex().size(), Main.rdf.h.getSPO().getIndex().size());
	}
	
	// verifier qu'il y a tous les indexs
	// tests sur les indexs

}
