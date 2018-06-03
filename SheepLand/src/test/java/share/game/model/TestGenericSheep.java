package share.game.model;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class TestGenericSheep {

	@Test public void ageTest(){
		GenericSheep sheep = new GenericSheep(SheepType.LAMB);
		assertEquals(sheep.getAge(),0);
		sheep.incAge();
		assertEquals(sheep.getAge(),1);
		assertEquals(sheep.getSheepType(),SheepType.LAMB);
		assertEquals(sheep.canUpgradeToSheep(),false);
		
		sheep.incAge();
		assertEquals(sheep.getAge(),2);
		assertEquals(sheep.getSheepType(),SheepType.LAMB);
		assertEquals(sheep.canUpgradeToSheep(),true);
		
		sheep.upgradeToSheep();
		Assert.assertNotEquals(sheep.getSheepType(), SheepType.LAMB);
	}

}
