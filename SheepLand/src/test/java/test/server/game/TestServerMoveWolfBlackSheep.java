package test.server.game;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import server.game.ServerGameStatus;
import server.game.ServerMoveWolfBlackSheep;
import server.game.ServerPlayer;
import server.game.ServerRandomValue;
import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.model.BlackSheep;
import share.game.model.Field;
import share.game.model.MapHandler;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;
import share.game.model.TypeField;

public class TestServerMoveWolfBlackSheep {

	ArrayList<Node> fakeMap = new ArrayList<Node>();
	ServerGameStatus statusTest = new ServerGameStatus();
	ServerPlayer pl = new ServerPlayer(null, "c", "c");
	MapHandler map = new MapHandler();
	Field field1 = new Field(0, TypeField.DESERT);
	Field field2 = new Field(1, TypeField.HILL);
	NumberedSpace space = new NumberedSpace(100, 1);
	ServerRandomValue rand = Mockito.mock(ServerRandomValue.class);

	@Before
	public void setUp() {
		Whitebox.setInternalState(this.statusTest, "randomValue", this.rand);
		Mockito.doReturn(1).when(this.rand).getDiceValue();

		this.fakeMap.add(this.space);
		this.fakeMap.add(this.field2);
		this.fakeMap.add(this.field1);

		this.field1.insertNewNearNode(this.space);
		this.field2.insertNewNearNode(this.space);
		this.space.insertNewNearNode(this.field2);
		this.space.insertNewNearNode(this.field1);

		Shepherd sh = new Shepherd(this.space);
		this.pl.addShepherd(sh, 0);
		this.map.setMap(this.fakeMap);
		this.statusTest.setGameGraph(this.map);

	}

	@Test
	public void testRandomMoveBlackSheepOk() {
		// cambia a 1 il dice value
		Information info;
		ServerMoveWolfBlackSheep moveTes = new ServerMoveWolfBlackSheep(
				this.statusTest);

		this.field2.addShep(new BlackSheep());

		info = moveTes.moveRandomBlackSheep();

		assertEquals(info.getInfoType(), InformationType.BLACKRNDMMOVE);
		@SuppressWarnings("unchecked")
		ArrayList<Field> fields = (ArrayList<Field>) info.getInformation();

		if (fields != null) {
			assertEquals(fields.get(1), this.field2);
			assertEquals(fields.get(0), this.field1);
		}

	}

	@Test
	public void testRandomMoveBlackSheepNo() {
		// cambia a 1 il dice value
		ArrayList<Node> noSpace = new ArrayList<Node>();
		this.map.setMap(noSpace);

		ServerMoveWolfBlackSheep moveTes = new ServerMoveWolfBlackSheep(
				this.statusTest);

		this.field2.addShep(new BlackSheep());
		moveTes.moveRandomBlackSheep();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPlayerMoveBlack() {

		Information info;
		ServerMoveWolfBlackSheep moveTes = new ServerMoveWolfBlackSheep(
				this.statusTest);

		this.field2.addShep(new BlackSheep());

		info = moveTes.playerMoveBlackSheep(this.pl.getShepherd(0));

		ArrayList<Node> fields = new ArrayList<Node>();

		fields = (ArrayList<Node>) info.getInformation();

		assertEquals(fields.get(1), this.field2);
		assertEquals(fields.get(0), this.field1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void moveWolf() {
		Information info;
		ServerMoveWolfBlackSheep moveTes = new ServerMoveWolfBlackSheep(
				this.statusTest);

		this.field2.setWolf(true);

		info = moveTes.moveWolf();

		ArrayList<Node> fields = new ArrayList<Node>();

		fields = (ArrayList<Node>) info.getInformation();

		assertEquals(fields.get(1), this.field2);
		assertEquals(fields.get(0), this.field1);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void wolfJump() {
		NumberedSpace space2 = new NumberedSpace(2, 3);

		this.fakeMap.add(space2);

		space2.setFence(false);
		this.space.setFence(false);

		Information info;
		ServerMoveWolfBlackSheep moveTes = new ServerMoveWolfBlackSheep(
				this.statusTest);

		this.field2.setWolf(true);

		this.field2.insertNewNearNode(space2);

		info = moveTes.moveWolf();

		assertEquals(info.getInfoType(), InformationType.WOLFMOVE);
		ArrayList<Node> fields = new ArrayList<Node>();

		fields = (ArrayList<Node>) info.getInformation();

		assertEquals(fields.get(1), this.field2);
		assertEquals(fields.get(0), this.field1);
	}

}
