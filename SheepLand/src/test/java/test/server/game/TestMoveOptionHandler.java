package test.server.game;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import server.game.ServerMoveOptionHandler;
import server.game.ServerGameStatus;
import server.game.ServerMove;
import server.game.ServerMoveSheep;
import server.game.ServerMoveShepHerd;
import server.game.ServerPlayer;
import share.game.model.Deck;
import share.game.model.Field;
import share.game.model.GenericSheep;
import share.game.model.MapHandler;
import share.game.model.MapHandler.NoReachableNode;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.SheepType;
import share.game.model.Shepherd;
import share.game.model.TypeField;
import share.game.move.MoveOption;

public class TestMoveOptionHandler {

	ServerGameStatus status = new ServerGameStatus();
	MapHandler map = new MapHandler();

	List<Node> nodes = new ArrayList<Node>();

	Field field1 = new Field(0, TypeField.HAY);
	Field field2 = new Field(1, TypeField.DESERT);
	Field field3 = new Field(2, TypeField.HILL);
	Field field4 = new Field(3, TypeField.MOUNTAIN);

	NumberedSpace space1 = new NumberedSpace(100, 1);
	NumberedSpace space2 = new NumberedSpace(101, 1);

	List<Node> nospaceMap = new ArrayList<Node>();

	Field field5 = new Field(4, TypeField.SWAMP);
	Field field6 = new Field(5, TypeField.WOOD);

	Deck deck = new Deck();

	NumberedSpace space3 = new NumberedSpace(102, 1);

	@Before
	public void setUp() {
		nodes.add(field1);
		nodes.add(field2);
		nodes.add(field3);
		nodes.add(space1);
		nodes.add(space2);

		// this group does not have problem with coupling, killing
		// and move sheep
		space1.insertNewNearNode(field1);
		space1.insertNewNearNode(field2);
		field1.addShep(new GenericSheep(SheepType.SHEEP));
		field1.addShep(new GenericSheep(SheepType.SHEEP));
		field2.addShep(new GenericSheep(SheepType.SHEEP));

		// this group does not have sheep so coupling, killing
		// and move sheep are not allowed
		space2.insertNewNearNode(field3);
		space2.insertNewNearNode(field4);

		this.status.setGameGraph(map);
		this.map.setMap(nodes);

		// //////////////////////////
		nospaceMap.add(space3);
		nospaceMap.add(field5);
		nospaceMap.add(field6);

		space3.insertNewNearNode(field5);
		space3.insertNewNearNode(field6);

		field5.addShep(new GenericSheep(SheepType.SHEEP));
		field5.addShep(new GenericSheep(SheepType.SHEEP));

		field6.addShep(new GenericSheep(SheepType.SHEEP));
		field6.addShep(new GenericSheep(SheepType.SHEEP));

		deck.setDeck();
		this.status.setCards(deck);
	}

	@Test
	public void testFirstMove() throws NoReachableNode {

		ServerMoveOptionHandler moh = new ServerMoveOptionHandler(status);
		List<ServerMove> moves = new ArrayList<ServerMove>();
		Shepherd shep = new Shepherd(space1);
		ServerPlayer pl = new ServerPlayer(null, "c", "");
		pl.setMoney(30);
		pl.addShepherd(shep, 0);
		status.addPlayer(pl);

		List<MoveOption> om = moh.getMoveOption(0, moves, shep);

		assertEquals(om.size(), 5);
	}

	@Test
	public void testNoShepherdWasMove() {
		ServerMoveOptionHandler moh = new ServerMoveOptionHandler(status);
		List<ServerMove> moves = new ArrayList<ServerMove>();
		Shepherd shep = new Shepherd(space1);
		ServerPlayer pl = new ServerPlayer(null, "c", "");
		pl.setMoney(30);
		pl.addShepherd(shep, 0);
		status.addPlayer(pl);

		moves.add(0, new ServerMoveSheep(status, shep, null));

		List<MoveOption> mo = moh.getMoveOption(1, moves, shep);

		moves.add(1, new ServerMoveSheep(status, shep, null));

		mo = moh.getMoveOption(2, moves, shep);

		assertEquals(mo.size(), 1);
	}

	@Test
	public void testSecondMoveNotShepherd() {
		ServerMoveOptionHandler moh = new ServerMoveOptionHandler(status);
		List<ServerMove> moves = new ArrayList<ServerMove>();
		Shepherd shep = new Shepherd(space1);
		ServerPlayer pl = new ServerPlayer(null, "c", "");
		pl.setMoney(30);
		pl.addShepherd(shep, 0);
		status.addPlayer(pl);

		moves.add(0, new ServerMoveShepHerd(status, shep, null));
		List<MoveOption> mo = moh.getMoveOption(1, moves, shep);
		moves.add(1, new ServerMoveSheep(status, shep, null));

		mo = moh.getMoveOption(2, moves, shep);

		assertEquals(mo.size(), 4);
	}

	@Test
	public void testSecondMoveShepher() {

		ServerMoveOptionHandler moh = new ServerMoveOptionHandler(status);
		List<ServerMove> moves = new ArrayList<ServerMove>();
		Shepherd shep = new Shepherd(space1);
		ServerPlayer pl = new ServerPlayer(null, "c", "");
		pl.setMoney(30);
		pl.addShepherd(shep, 0);
		status.addPlayer(pl);

		moves.add(0, new ServerMoveSheep(status, shep, null));

		List<MoveOption> mo = moh.getMoveOption(1, moves, shep);

		moves.add(1, new ServerMoveShepHerd(status, shep, null));

		mo = moh.getMoveOption(2, moves, shep);

		assertEquals(mo.size(), 5);
	}

	@Test
	public void testNoCouplingKillingMoveSheep() {

		ServerMoveOptionHandler moh = new ServerMoveOptionHandler(status);
		List<ServerMove> moves = new ArrayList<ServerMove>();
		Shepherd shep = new Shepherd(space2);
		ServerPlayer pl = new ServerPlayer(null, "c", "");
		pl.setMoney(30);
		pl.addShepherd(shep, 0);
		status.addPlayer(pl);

		List<MoveOption> mo = moh.getMoveOption(0, moves, shep);

		assertEquals(mo.size(), 2);
	}

	@Test
	public void testNoreachableNode() {

		ServerGameStatus status2 = new ServerGameStatus();
		ServerMoveOptionHandler moh = new ServerMoveOptionHandler(status2);

		MapHandler map2 = new MapHandler();

		map2.setMap(nospaceMap);
		status2.setGameGraph(map2);

		ServerPlayer pl = new ServerPlayer(null, "", "");
		Shepherd shep = new Shepherd(space3);
		pl.setMoney(30);
		space3.setShepherd(shep);
		pl.addShepherd(shep, 0);

		status2.addPlayer(pl);
		status2.setCards(deck);

		List<ServerMove> moves = new ArrayList<ServerMove>();

		List<MoveOption> mo = moh.getMoveOption(0, moves, shep);

		assertEquals(mo.size(), 4);

	}

}
