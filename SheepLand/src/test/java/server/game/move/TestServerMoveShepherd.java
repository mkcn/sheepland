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
import server.game.ServerMoveShepHerd;
import server.game.ServerPlayer;
import share.game.comunication.Information;
import share.game.model.MapHandler;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;

public class TestServerMoveShepherd {

	MapHandler testMap = new MapHandler();
	ServerGameStatus testStatus = new ServerGameStatus();

	NumberedSpace space1 = new NumberedSpace(0, 1);
	NumberedSpace space2 = new NumberedSpace(1, 1);
	NumberedSpace space3 = new NumberedSpace(2, 1);
	
	ServerGameSender mockedSender = Mockito.mock(ServerGameSender.class);

	@Before
	public void setUpTest() {

		ArrayList<Node> nodes = new ArrayList<Node>();

		nodes.add(space1);
		nodes.add(space2);
		nodes.add(space3);

		this.testMap.setMap(nodes);
		this.testStatus.setGameGraph(testMap);
	}

	@Test
	public void ReachableNode() throws RemoteException {

		ServerPlayer pl1 = new ServerPlayer(null, "a", "");
		Shepherd sh = new Shepherd(space1);
		pl1.addShepherd(sh, 0);
		testStatus.addPlayer(pl1);

		space1.setShepherd(sh);

		ServerMove testMove = new ServerMoveShepHerd(this.testStatus, sh,this.mockedSender);

		testMove.findInformation();

	}

	 @Test
	public void noReachableNode() throws RemoteException {
		ServerPlayer pl1 = new ServerPlayer(null, "a", "4");
		Shepherd sh = new Shepherd(space2);
		pl1.addShepherd(sh, 0);
		testStatus.addPlayer(pl1);
		space2.setShepherd(sh);
		space1.setShepherd(sh);

		space3.setFence(false);

		ServerMove testMove = new ServerMoveShepHerd(this.testStatus, sh,this.mockedSender);

		testMove.findInformation();
	}

	@Test
	public void testEndMoveShepherd() throws RemoteException {
		NumberedSpace space6 = new NumberedSpace(19,4);
		NumberedSpace space7 = new NumberedSpace(22,3);
		
		ArrayList<Node> optionList = new ArrayList<Node>();
		optionList.add(space6);
		optionList.add(space7);
		
		MapHandler map = new MapHandler();
		map.setMap(optionList);
		
		ServerGameStatus testStatus = new ServerGameStatus();
		testStatus.setGameGraph(map);
		
		space7.insertNewNearNode(space6);
		space6.insertNewNearNode(space7);
		
		ServerGameStatus status = new ServerGameStatus();
		ServerPlayer pl = new ServerPlayer(null, "", "");
		status.addPlayer(pl);
		Shepherd sh = new Shepherd(space6);
		pl.addShepherd(sh, 0);	
	
		ServerMove testMove = new ServerMoveShepHerd(status, sh,this.mockedSender);


		Whitebox.setInternalState(testMove, "optionList", optionList);

		Information infoMessage = new Information(-1, null,null);
		infoMessage.setInformation(space7);

		testMove.endMove(infoMessage);

	}

	@Test
	public void testEndMovePay() throws RemoteException {
		ServerGameStatus status = new ServerGameStatus();
		ServerPlayer pl = new ServerPlayer(null, "", "");
		status.addPlayer(pl);
		Shepherd sh = new Shepherd(space3);
		pl.addShepherd(sh, 0);
		
		pl.setMoney(0);
		
		NumberedSpace space4 = new NumberedSpace(5,2);

		ServerMove testMove = new ServerMoveShepHerd(status, sh,this.mockedSender);

		ArrayList<Node> optionList = new ArrayList<Node>();
		optionList.add(space1);
		optionList.add(space2);
		Whitebox.setInternalState(testMove, "optionList", optionList);

		Information infoMessage = new Information(-1, null,null);
		infoMessage.setInformation(space4);
		
		testMove.endMove(infoMessage);

	
	}

}
