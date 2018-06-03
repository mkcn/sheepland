package share.game.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import share.game.model.MapHandler.NoReachableNode;

public class TestMapGraph {

	ArrayList<Node> fakeMap = new ArrayList<Node>();
	MapHandler map = new MapHandler();

	NumberedSpace space = new NumberedSpace(105, 1);
	Field fieldBlackSheep = new Field(6, TypeField.SHEEPSBURG);

	@Before
	public void setUpFakeMap() {

		this.fakeMap.add(new Field(0, TypeField.DESERT));
		this.fakeMap.add(new Field(1, TypeField.HAY));
		this.fakeMap.add(new Field(2, TypeField.HILL));
		this.fakeMap.add(new Field(3, TypeField.MOUNTAIN));
		this.fakeMap.add(new Field(4, TypeField.SWAMP));
		this.fakeMap.add(new Field(5, TypeField.WOOD));

		this.fakeMap.add(space);
		this.fakeMap.add(new NumberedSpace(106, 1));
		this.fakeMap.add(new NumberedSpace(107, 1));

	}

	@Test
	public void testGetReachableNode() throws NoReachableNode {
		this.map.setMap(fakeMap);
		assertEquals(this.map.getAllReachableNode().size(), 3);
	}

	@Test
	public void testFieldWithBlackSheep() {
		this.map.setMap(fakeMap);
		assertEquals(this.map.fieldWithBlackSheep(), null);

		fieldBlackSheep.addShep(new BlackSheep());
		this.fakeMap.add(fieldBlackSheep);
		this.map.setMap(fakeMap);

		assertEquals(this.map.fieldWithBlackSheep(), fieldBlackSheep);
	}

	@Test
	public void testFence() {
		this.map.setMap(fakeMap);
		assertEquals(this.map.getFenceNumber(), 0);
		space.setFence(false);
		assertEquals(this.map.getFenceNumber(), 1);
	}

	@Test
	public void testFenceSingleNode() {
		this.map.setMap(fakeMap);

		Field field1 = new Field(7, TypeField.DESERT);

		NumberedSpace space1 = new NumberedSpace(109, 6);
		NumberedSpace space3 = new NumberedSpace(110, 6);
		NumberedSpace space2 = new NumberedSpace(111, 6);

		field1.insertNewNearNode(space2);
		field1.insertNewNearNode(space3);
		field1.insertNewNearNode(space1);

		space1.setFence(false);
		space2.setFence(false);
		space3.setFence(false);

		this.fakeMap.add(field1);

		assertEquals(this.map.getFenceNumber(field1), 3);
	}

	@Test
	public void testWolf() {
		this.map.setMap(fakeMap);
		assertEquals(this.map.fieldWithWolf(), null);

		Field field2 = new Field(10, TypeField.HAY);
		this.fakeMap.add(field2);
		field2.setWolf(true);
		assertEquals(this.map.fieldWithWolf(), field2);

	}

	@Test(expected = NoReachableNode.class)
	public void noReachableNode() throws NoReachableNode {
		ArrayList<Node> emptyMap = new ArrayList<Node>();

		this.map.setMap(emptyMap);

		this.map.getAllReachableNode();
	}

	@Test
	public void testDeleteNearNodeField(){
		MapCreator mapCreator = new MapCreator();
		mapCreator.addNodeToMap();
		mapCreator.connect();
		
		MapHandler map = new MapHandler();
		map.setMap(mapCreator.getCreatedMap());
		
		assertEquals(map.deleteNumberedSpaceFromNearNode(null).size(),19);
	}
}
