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
import server.game.ServerMoveCoupling;
import server.game.ServerPlayer;
import share.game.comunication.Information;
import share.game.model.Field;
import share.game.model.GenericSheep;
import share.game.model.MapHandler;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.SheepType;
import share.game.model.Shepherd;
import share.game.model.TypeField;

public class TestServerMoveCoupling {

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

		field1.addShep(new GenericSheep(SheepType.SHEEP));
		field1.addShep(new GenericSheep(SheepType.SHEEP));
		field2.addShep(new GenericSheep(SheepType.SHEEP));
		field2.addShep(new GenericSheep(SheepType.SHEEP));

		field4.addShep(new GenericSheep(SheepType.SHEEP));
		field4.addShep(new GenericSheep(SheepType.SHEEP));
	}

	@Test
	public void testMoveCoupling() throws RemoteException {

		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh1 = new Shepherd(space2);
		pl.addShepherd(sh1, 0);
		testStatus.addPlayer(pl);

		ServerMove testMove = new ServerMoveCoupling(this.testStatus, sh1,
				this.mockedSender);

		testMove.findInformation();

	}

	@Test
	public void testMoveCouplingForced() throws RemoteException {

		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh1 = new Shepherd(space1);
		pl.addShepherd(sh1, 0);
		testStatus.addPlayer(pl);

		ServerMove testMove = new ServerMoveCoupling(this.testStatus, sh1,
				this.mockedSender);

		testMove.findInformation();

	}

	@Test
	public void testMoveCouplingNo() throws RemoteException {

		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh1 = new Shepherd(space3);
		pl.addShepherd(sh1, 0);
		testStatus.addPlayer(pl);

		ServerMove testMove = new ServerMoveCoupling(this.testStatus, sh1,
				this.mockedSender);

		testMove.findInformation();

	}

	@Test
	public void testEndMove() throws RemoteException {

		ServerPlayer pl = new ServerPlayer(null, "c", "c");
		Shepherd sh1 = new Shepherd(space2);
		pl.addShepherd(sh1, 0);
		testStatus.addPlayer(pl);

		ArrayList<Field> fields = new ArrayList<Field>();
		fields.add(field1);
		fields.add(field2);

		ServerMove testMove = new ServerMoveCoupling(this.testStatus, sh1,
				this.mockedSender);

		Whitebox.setInternalState(testMove, "optionList", fields);

		Information info = new Information(-1, null, null);
		info.setInformation(field2);

		testMove.endMove(info);

	}

}
