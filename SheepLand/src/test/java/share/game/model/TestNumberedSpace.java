package share.game.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestNumberedSpace {

	NumberedSpace ns = new NumberedSpace(1, 2);

	@Test
	public void testSHepherdPosition() {
		this.ns.setShepherd(new Shepherd(ns));

		assertEquals(this.ns.getSHepherd().getPositioOnMapn(), ns);
	}

	@Test
	public void fenceTest() {
		this.ns.setFence(false);
		assertTrue(this.ns.isFence());
	}

	@Test
	public void testisNear() {
		assertFalse(this.ns.isNear(3));

		this.ns.insertNewNearNode(new NumberedSpace(2, 3));
		assertTrue(this.ns.isNear(2));
	}

	@Test
	public void voidtestEquals() {
		assertTrue(this.ns.equalsNumberedSpace(new NumberedSpace(1, 2)));
		assertFalse(this.ns.equalsNumberedSpace(new NumberedSpace(1, 3)));
		assertFalse(this.ns.equalsNumberedSpace(new NumberedSpace(2, 3)));
		assertFalse(this.ns.equalsNumberedSpace(new NumberedSpace(1, 4)));
	}

	@Test
	public void testValue() {
		assertEquals(this.ns.getValue(), 2);
	}

}
