package pt.ulisboa.tecnico.BlockkeyserverTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.blockkeyserver.Block;

public class BlockTest {

	Block b;
	
	@Before
	public void setUp() throws Exception {
		b = new Block();
	}

	@Test
	public void testEscreve() {
		byte result[], dado[] = "abc".getBytes();
		result = b.escreve(dado, 7, dado.length);
		byte dado2[] = "def".getBytes();
		result = b.escreve(dado2, 6, dado2.length);
		System.out.println("Saida: " + new String(result));
		assertNotNull(result);
	}

}
