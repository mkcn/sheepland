package share.game.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestMapCreator {

	MapCreator testCreator = new MapCreator();

	@Test
	public void testMapCreator() {
		this.testCreator.addNodeToMap();
		assertEquals(this.testCreator.getCreatedMap().size(), 61);
       
		this.testCreator.connect();
	}

}
