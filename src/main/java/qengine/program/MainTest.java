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
		Main.h.getOSP().affiche();
		int res = Main.dico.getinDico(str);
		assertEquals(1,res);
		assertNotEquals(4, res);
		
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
