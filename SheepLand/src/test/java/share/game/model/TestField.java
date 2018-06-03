package share.game.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestField {

	@Test
	public void testRemeoveSheep() {

		Field tf = new Field(0, TypeField.DESERT);
		
		GenericSheep sheep0 = new GenericSheep(SheepType.SHEEP);
		sheep0.setId(0);
		tf.addShep(sheep0);

		GenericSheep sheep1 = new GenericSheep(SheepType.SHEEP);
		sheep0.setId(1);
		tf.addShep(sheep1);
		
		assertTrue(tf.isThereASheep());
		assertEquals(tf.isBlackSheepHere(),-1);
		assertTrue(tf.canLamb1());
		
		assertFalse(tf.isWolf());
		tf.setWolf(true);
		assertTrue(tf.isWolf());
		
		tf.removeSheep();
		tf.removeSheep();
		assertFalse(tf.canLamb1());
		
		BlackSheep black = new BlackSheep();
		black.setId(3);
		tf.addShep(black);
		assertEquals(tf.isBlackSheepHere(),3);

	}

}
