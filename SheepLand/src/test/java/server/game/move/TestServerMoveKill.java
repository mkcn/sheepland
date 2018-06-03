package server.game.move;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import server.game.ServerGameSender;
import server.game.ServerGameStatus;
import server.game.ServerMove;
import server.game.ServerMoveKilling;
import server.game.ServerPlayer;
import share.game.comunication.Information;
import share.game.model.BlackSheep;
import share.game.model.Field;
import share.game.model.GenericSheep;
import share.game.model.MapHandler;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.SheepType;
import share.game.model.Shepherd;
import share.game.model.TypeField;

public class TestServerMoveKill {

	private NumberedSpace space1 = new NumberedSpace(107, 0);
	private NumberedSpace space2 = new NumberedSpace(108, 0);
	private NumberedSpace space3 = new NumberedSpace(109, 4);

	private Field field2 = new Field(1, TypeField.HAY);
	private Field field1 = new Field(2, TypeField.HAY);
	private Field field3 = new Field(3, TypeField.DESERT);
	private Field field4 = new Field(4, TypeField.HAY);
	private Field field5 = new Field(5, TypeField.DESERT);
	private Field field6 = new Field(6, TypeField.HAY);

	ArrayList<Node> nodes = new ArrayList<Node>();

	ServerGameStatus testStatus = new ServerGameStatus();
	MapHandler tetsMap = new MapHandler();

	ServerGameSender mockedSender = Mockito.mock(ServerGameSender.class);

	@Before
	public void setNodes() {
		this.nodes.add(field1);
		this.nodes.add(field2);
		this.nodes.add(field3);
		this.nodes.add(field4);
		this.nodes.add(field5);
		this.nodes.add(field6);

		this.nodes.add(space1);
		this.nodes.add(space2);
		this.nodes.add(space3);

		space2.insertNewNearNode(field1);
		space2.insertNewNearNode(field2);

		space1.insertNewNearNode(field3);
		space1.insertNewNearNode(field4);

		space3.insertNewNearNode(field5);
		space3.insertNewNearNode(field6);

		this.tetsMap.setMap(nodes);
		this.testStatus.setGameGraph(tetsMap);

	}

	@Test
	public void noKill() throws RemoteException {
		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh3 = new Shepherd(space2);
		pl.addShepherd(sh3, 0);

		this.testStatus.addPlayer(pl);

		ServerMove testMove = new ServerMoveKilling(this.testStatus, sh3,
				this.mockedSender);

		testMove.findInformation();

	}

	@Test
	public void testMoveKill2() throws RemoteException {

		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh1 = new Shepherd(space1);
		pl.addShepherd(sh1, 0);
		testStatus.addPlayer(pl);

		// add sheep
		this.field3.addShep(new GenericSheep(SheepType.LAMB));
		this.field4.addShep(new GenericSheep(SheepType.RAM));

		ServerMove testMove = new ServerMoveKilling(this.testStatus, sh1,
				this.mockedSender);

		testMove.findInformation();

	}

	@Test
	public void testMoveKillForced() throws RemoteException {

		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh2 = new Shepherd(this.space3);
		pl.addShepherd(sh2, 0);
		testStatus.addPlayer(pl);

		this.field5.addShep(new GenericSheep(SheepType.SHEEP));

		ServerMove testMove = new ServerMoveKilling(this.testStatus, sh2,
				this.mockedSender);

		testMove.findInformation();

	}

	@Test
	public void testMoveKill() throws RemoteException {

		ArrayList<Node> nodes = new ArrayList<Node>();
		NumberedSpace space4 = new NumberedSpace(123, 3);
		Field fieldBlack = new Field(124, TypeField.HAY);
		Field fieldNoBlack = new Field(125, TypeField.HILL);

		fieldBlack.addShep(new BlackSheep());
		fieldNoBlack.addShep(new GenericSheep(null));

		nodes.add(fieldNoBlack);
		nodes.add(fieldBlack);
		nodes.add(space4);

		space4.insertNewNearNode(fieldNoBlack);
		space4.insertNewNearNode(fieldBlack);

		fieldBlack.insertNewNearNode(space4);
		fieldNoBlack.insertNewNearNode(space4);

		MapHandler map = new MapHandler();
		map.setMap(nodes);

		ServerGameStatus status = new ServerGameStatus();
		status.setGameGraph(map);
		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh2 = new Shepherd(space4);
		pl.addShepherd(sh2, 0);
		status.addPlayer(pl);

		ServerMove move = new ServerMoveKilling(status, sh2, mockedSender);

		move.findInformation();

	}

	@Test
	public void testForcedKill() throws RemoteException {

		ArrayList<Node> nodes = new ArrayList<Node>();
		NumberedSpace space4 = new NumberedSpace(123, 3);
		Field fieldBlack = new Field(124, TypeField.HAY);
		Field fieldNoBlack = new Field(125, TypeField.HILL);

		fieldBlack.addShep(new BlackSheep());

		nodes.add(fieldNoBlack);
		nodes.add(fieldBlack);
		nodes.add(space4);

		space4.insertNewNearNode(fieldNoBlack);
		space4.insertNewNearNode(fieldBlack);

		fieldBlack.insertNewNearNode(space4);
		fieldNoBlack.insertNewNearNode(space4);

		MapHandler map = new MapHandler();
		map.setMap(nodes);

		ServerGameStatus status = new ServerGameStatus();
		status.setGameGraph(map);
		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh2 = new Shepherd(space4);
		pl.addShepherd(sh2, 0);
		status.addPlayer(pl);

		ServerMove move = new ServerMoveKilling(status, sh2, mockedSender);

		move.findInformation();
	}

	@Test
	public void testEndMove() throws RemoteException {
		ArrayList<Node> nodes = new ArrayList<Node>();
		NumberedSpace space4 = new NumberedSpace(123, 3);
		Field field34 = new Field(124, TypeField.HAY);
		Field field35 = new Field(125, TypeField.HILL);

		field34.addShep(new GenericSheep(SheepType.SHEEP));
		field35.addShep(new GenericSheep(SheepType.SHEEP));

		nodes.add(field35);
		nodes.add(field34);
		nodes.add(space4);

		space4.insertNewNearNode(field35);
		space4.insertNewNearNode(field34);

		field34.insertNewNearNode(space4);
		field35.insertNewNearNode(space4);

		MapHandler map = new MapHandler();
		map.setMap(nodes);

		ServerGameStatus status = new ServerGameStatus();
		status.setGameGraph(map);
		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh2 = new Shepherd(space4);
		pl.addShepherd(sh2, 0);
		status.addPlayer(pl);

		ServerMove move = new ServerMoveKilling(status, sh2, mockedSender);
		ArrayList<Field> optionList = new ArrayList<Field>();
		optionList.add(field35);
		optionList.add(field34);
		Whitebox.setInternalState(move, "optionList", optionList);

		Information infoMEssage = new Information(-1, null, null);
		infoMEssage.setInformation(field34);
		move.endMove(infoMEssage);
	}

}
