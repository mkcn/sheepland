package client.game;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.comunication.Request;
import share.game.comunication.RequestType;
import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.MapHandler;
import share.game.model.NumberedSpace;
import share.game.model.Player;
import share.game.move.MoveOption;
import share.game.move.ReturnMoveChoice;
import client.view.ClientGameAbstract;

public class TestClientGame {

	ClientGame game;
	RemoteSendInterface remoteServer;
	ClientGameAbstract graphicGame;
	ClientMove actualMove;
	MapHandler map;

	@Before
	public void setUp() {
		remoteServer = Mockito.mock(RemoteSendInterface.class);
		this.graphicGame = Mockito.mock(ClientGameAbstract.class);
		actualMove = Mockito.mock(ClientMove.class);

		game = new ClientGame(remoteServer);
		game.setGrafic(graphicGame);
		Whitebox.setInternalState(game, "actualMove", actualMove);
	}

	@Test
	public void testHandleInfoMove() {

		this.game.handleMessage(new Information(-1,
				InformationType.MOVEBLACKSHEEP, null));
		this.game.handleMessage(new Information(-1, InformationType.INFOMOVE,
				null));

		Information messageMoveOption = new Information(-1,
				InformationType.MOVEOPTION, null);
		messageMoveOption.setInformation(new ArrayList<MoveOption>());

		this.game.handleMessage(messageMoveOption);

	}

	@Test
	public void testHandleInfoMoveOtherPlayer() {

		Player pl = new Player("");
		Field field = new Field(0, null);
		ArrayList<Field> listField = new ArrayList<Field>();
		ArrayList<NumberedSpace> listNumbSpace = new ArrayList<NumberedSpace>();

		listField.add(new Field(0, null));
		listField.add(new Field(0, null));

		listNumbSpace.add(new NumberedSpace(0, 0));
		listNumbSpace.add(new NumberedSpace(0, 0));

		Information infoBuy = new Information(-1,
				InformationType.OTHERPLAYERBUY, pl);
		infoBuy.setInformation(new FieldCard(0, null));
		infoBuy.setSecondInformation(1);
		this.game.handleMessage(infoBuy);

		Information infoOtherMoveSheep = new Information(-1,
				InformationType.OTHERPLAYERMOVESHEEP, pl);
		infoOtherMoveSheep.setInformation(listField);
		this.game.handleMessage(infoOtherMoveSheep);

		Information infoOtherShep = new Information(-1,
				InformationType.OTHERPLAYERSHPHERD, pl);
		infoOtherShep.setInformation(listNumbSpace);
		this.game.handleMessage(infoOtherShep);

		Information infoOtherCoupling = new Information(-1,
				InformationType.OTHERPLAYERCOUPLING, pl);
		infoOtherCoupling.setInformation(field);
		this.game.handleMessage(infoOtherCoupling);

		Information infoOtherKill = new Information(-1,
				InformationType.OTHERPLAYKILL, pl);
		infoOtherKill.setInformation(field);
		this.game.handleMessage(infoOtherKill);
	}

	@Test
	public void testHandleInformationEndMoveTurnShep() {
		Information infoShep = new Information(-1,
				InformationType.TURNSHEPHERD, null);
		infoShep.setInformation(new NumberedSpace(0, 0));
		this.game.handleMessage(infoShep);

		this.game.handleMessage(new Information(-1, InformationType.ENDMOVE,
				null));
		this.game.handleMessage(new Information(-1,
				InformationType.ENDOPERATION, null));
	}

	@Test
	public void handleInformationRandomBlackWolf() {

		ArrayList<Field> list = new ArrayList<Field>();
		list.add(0, new Field(0, null));
		list.add(1, new Field(1, null));

		Information blackRandom = new Information(-1,
				InformationType.BLACKRNDMMOVE, null);
		blackRandom.setInformation(list);
		this.game.handleMessage(blackRandom);

		Information wolfInfo = new Information(-1, InformationType.WOLFMOVE,
				null);
		wolfInfo.setInformation(list);
		this.game.handleMessage(wolfInfo);

		Information wolfInfoEat = new Information(-1, InformationType.WOLFEAT,
				null);
		wolfInfoEat.setInformation(list);
		this.game.handleMessage(wolfInfoEat);
	}

	@Test
	public void testHandleInfoPlayerConnDisconn() {
		this.game.handleMessage(new Information(-1, InformationType.NEWPLAYER,
				null));
		this.game.handleMessage(new Information(-1,
				InformationType.PLAYERCOMEBACK, null));
		this.game.handleMessage(new Information(-1,
				InformationType.PLAYERDISCONNECT, null));

		this.game.handleMessage(new Information(-1,
				InformationType.PLAYERDISCONNECT, null));
		Information infosync = new Information(-1, InformationType.SYNC, null);
		infosync.setInformation(new MapHandler());
		infosync.setSecondInformation(new ArrayList<Player>());
		this.game.handleMessage(infosync);

		Information info = new Information(-1, InformationType.INITIALCARD,
				new Player(""));
		info.setInformation(new FieldCard(0, null));
		info.setSecondInformation(1);
		this.game.handleMessage(info);
	}

	@Test
	public void testHandleRequest() {
		this.game.handleMessage(new Request(-1, RequestType.ENDMOVE, null));
		this.game.handleMessage(new Request(-1, RequestType.ERRORMOVE, null));
		this.game.handleMessage(new Request(-1, RequestType.ENDMOVEPAY, null));
		this.game.handleMessage(new Request(-1, RequestType.STARTGAME, null));
		this.game.handleMessage(new Request(-1, RequestType.STARTSYNC, null));
		this.game.handleMessage(new Request(-1, RequestType.ENDSYNC, null));
		this.game.handleMessage(new Request(-1, RequestType.WAITFORYOURTURN,
				null));
	}

	@Test
	public void tesHandleInterfaceEvantBoolean() {

		Mockito.doReturn(ClientMoveSheep.class).when(this.actualMove)
				.getClass();
		this.game.handleInterfaceEvent(true);
		this.game.handleInterfaceEvent(false);
	}

	@Test
	public void testHandleInterfaceEventIndexChar() throws RemoteException {
		Whitebox.setInternalState(game, "shepIndexRequest", true);
		this.game.handleInterfaceEvent('h', 0);

	}

	@Test
	public void testHandleInterfaceEventIndexChar2() throws RemoteException {
		ClientMove mockedMove = Mockito.mock(ClientMove.class);
		Whitebox.setInternalState(game, "actualMove", mockedMove);
		Whitebox.setInternalState(game, "shepIndexRequest", false);

		Mockito.doReturn(false).when(mockedMove).isEnd();
		this.game.handleInterfaceEvent('h', 0);
	}

	@Test
	public void testHandleInterfaceEventMoveChoice() throws RemoteException {

		Mockito.doReturn(true).when(this.actualMove).isEnd();
		this.game.handleInterfaceEvent(ReturnMoveChoice.MOVE_TYPE, 0);
		Mockito.doReturn(true).when(this.actualMove).isEnd();
		this.game.handleInterfaceEvent(ReturnMoveChoice.MOVE_TYPE, 1);
		Mockito.doReturn(true).when(this.actualMove).isEnd();
		this.game.handleInterfaceEvent(ReturnMoveChoice.MOVE_TYPE, 2);
		Mockito.doReturn(true).when(this.actualMove).isEnd();
		this.game.handleInterfaceEvent(ReturnMoveChoice.MOVE_TYPE, 3);
		Mockito.doReturn(true).when(this.actualMove).isEnd();
		this.game.handleInterfaceEvent(ReturnMoveChoice.MOVE_TYPE, 4);
	}
}
