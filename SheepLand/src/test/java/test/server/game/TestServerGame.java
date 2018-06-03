package test.server.game;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import server.game.ServerGame;
import server.game.ServerGameSender;
import server.game.ServerGameStatus;
import server.game.ServerMove;
import server.game.ServerMoveProgression;
import server.game.ServerMoveWolfBlackSheep;
import server.game.ServerPlayer;
import server.game.ServerTurnStatus;
import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.comunication.Message;
import share.game.comunication.Request;
import share.game.comunication.RequestType;
import share.game.model.Deck;
import share.game.model.Field;
import share.game.model.MapCreator;
import share.game.model.MapHandler;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;
import share.game.model.TypeField;

public class TestServerGame {

	@Test(timeout = 5000)
	public void testMessageHandleRequestMove() throws RemoteException {
		ServerMoveWolfBlackSheep specialMove = Mockito
				.mock(ServerMoveWolfBlackSheep.class);
		ServerGameSender mockedSender = Mockito.mock(ServerGameSender.class);
		ServerGameStatus gameStatus = Mockito.mock(ServerGameStatus.class);
		ServerTurnStatus turn = Mockito.mock(ServerTurnStatus.class);
		ServerMove mockedMove = Mockito.mock(ServerMove.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		Shepherd shep = new Shepherd(new NumberedSpace(0, 0));
		ServerPlayer pl = new ServerPlayer(null, "", "");

		Whitebox.setInternalState(game, "serverGameSender", mockedSender);
		Whitebox.setInternalState(game, "statusGame", gameStatus);
		Whitebox.setInternalState(game, "move", turn);
		Whitebox.setInternalState(game, "specialMove", specialMove);

		Mockito.doReturn(shep).when(turn).getShepherd();
		Mockito.doReturn(false).when(gameStatus).getSync();
		Mockito.doReturn(pl).when(gameStatus).getActualPlayer();
		Mockito.doReturn(false).when(turn).isTurnEnd();
		Mockito.doReturn(mockedMove).when(turn).getActualMove();
		Mockito.doNothing().when(mockedMove).findInformation();
		Mockito.doCallRealMethod().when(game)
				.handleMessage((Message) Mockito.isNotNull());
		Mockito.doCallRealMethod().when(game)
				.handleRequest((Request) Mockito.notNull());

		Request messageBuyField = new Request(-1, RequestType.MOVEBUYFIELD,
				pl.getAsPlayer());
		game.handleMessage(messageBuyField);

		Request messageMoveSheep = new Request(-1, RequestType.MOVESHEEP,
				pl.getAsPlayer());
		game.handleMessage(messageMoveSheep);

		Request messageMoveShepherd = new Request(-1, RequestType.MOVESHEPHERD,
				pl.getAsPlayer());
		game.handleMessage(messageMoveShepherd);

		Request messageMoveCoupling = new Request(-1, RequestType.COUPLING,
				pl.getAsPlayer());
		game.handleMessage(messageMoveCoupling);

		Request messageMoveKilling = new Request(-1, RequestType.KILL,
				pl.getAsPlayer());
		game.handleMessage(messageMoveKilling);

		Request newMessageBlacSheep = new Request(-1,
				RequestType.MOVEBLACKSHEEP, pl.getAsPlayer());
		game.handleMessage(newMessageBlacSheep);
	}

	@Test(timeout = 5000)
	public void handleRequestNextMoveERROR() throws RemoteException {
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerGameStatus gameStatus = Mockito.mock(ServerGameStatus.class);
		ServerTurnStatus turnStatus = Mockito.mock(ServerTurnStatus.class);
		ServerMoveWolfBlackSheep specialMove = Mockito
				.mock(ServerMoveWolfBlackSheep.class);

		ServerMove mockedMove = Mockito.mock(ServerMove.class);

		Request req = new Request(-1, RequestType.ACK, null);

		Mockito.doReturn(false).when(turnStatus).isTurnEnd();
		Mockito.doReturn(false).when(gameStatus).getSync();
		Mockito.doCallRealMethod().when(game).handleRequest(Mockito.eq(req));
		Mockito.doReturn(mockedMove).when(turnStatus).getActualMove();
		Mockito.doReturn(ServerMoveProgression.ERRORMOVE).when(mockedMove)
				.getProgres();
		Whitebox.setInternalState(game, "serverGameSender", sender);
		Whitebox.setInternalState(game, "specialMove", specialMove);
		Whitebox.setInternalState(game, "statusGame", gameStatus);
		Whitebox.setInternalState(game, "move", turnStatus);

		game.handleRequest(req);
	}

	@Test(timeout = 5000)
	public void handleRequestNextMoveENDMOVE() throws RemoteException {
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerGameStatus gameStatus = Mockito.mock(ServerGameStatus.class);
		ServerTurnStatus turnStatus = Mockito.mock(ServerTurnStatus.class);
		ServerMoveWolfBlackSheep specialMove = Mockito
				.mock(ServerMoveWolfBlackSheep.class);

		ServerMove mockedMove = Mockito.mock(ServerMove.class);

		Request req = new Request(-1, RequestType.ACK, null);

		Mockito.doReturn(false).when(turnStatus).isTurnEnd();
		Mockito.doReturn(false).when(gameStatus).getSync();
		Mockito.doCallRealMethod().when(game).handleRequest(Mockito.eq(req));
		Mockito.doReturn(mockedMove).when(turnStatus).getActualMove();
		Mockito.doReturn(ServerMoveProgression.ENDMOVE).when(mockedMove)
				.getProgres();
		Whitebox.setInternalState(game, "serverGameSender", sender);
		Whitebox.setInternalState(game, "specialMove", specialMove);
		Whitebox.setInternalState(game, "statusGame", gameStatus);
		Whitebox.setInternalState(game, "move", turnStatus);

		game.handleRequest(req);
	}

	@Test(timeout = 5000)
	// player >2
	// shepindex = 0
	public void handleRequestSync() throws RemoteException {
		ServerGameStatus gameSatus = Mockito.mock(ServerGameStatus.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerTurnStatus status = Mockito.mock(ServerTurnStatus.class);
		ServerMove move = Mockito.mock(ServerMove.class);
		Request req = new Request(-1, RequestType.ACK, null);
		ServerPlayer pl = new ServerPlayer(null, "", "");

		Mockito.doReturn(3).when(gameSatus).getNumberPlayer();
		Mockito.doReturn(true).when(gameSatus).getSync();
		Mockito.doReturn(pl).when(gameSatus).getActualPlayer();
		Mockito.doCallRealMethod().when(game).handleRequest(req);
		Mockito.doReturn(move).when(status).getActualMove();

		Whitebox.setInternalState(game, "serverGameSender", sender);
		Whitebox.setInternalState(game, "statusGame", gameSatus);
		Whitebox.setInternalState(game, "shepherdIndex", 0);
		Whitebox.setInternalState(game, "move", status);

		game.handleRequest(req);

	}

	@Test(timeout = 5000)
	// player = 2
	// shepindex = 0
	public void handleRequestSync2() throws RemoteException {
		ServerGameStatus gameSatus = Mockito.mock(ServerGameStatus.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerTurnStatus status = Mockito.mock(ServerTurnStatus.class);
		ServerMove move = Mockito.mock(ServerMove.class);
		Request req = new Request(-1, RequestType.ACK, null);
		ServerPlayer pl = new ServerPlayer(null, "", "");

		Mockito.doReturn(2).when(gameSatus).getNumberPlayer();
		Mockito.doReturn(true).when(gameSatus).getSync();
		Mockito.doReturn(pl).when(gameSatus).getActualPlayer();
		Mockito.doCallRealMethod().when(game).handleRequest(req);
		Mockito.doReturn(move).when(status).getActualMove();

		Whitebox.setInternalState(game, "serverGameSender", sender);
		Whitebox.setInternalState(game, "statusGame", gameSatus);
		Whitebox.setInternalState(game, "shepherdIndex", 0);
		Whitebox.setInternalState(game, "move", status);

		game.handleRequest(req);

		assertEquals(pl.isSync(), false);

	}

	@Test(timeout = 5000)
	public void handleInformationShepherdId() throws RemoteException {
		ServerGameSender mockedSender = Mockito.mock(ServerGameSender.class);
		ServerGameStatus status = new ServerGameStatus();
		NumberedSpace mockedSpace = Mockito.mock(NumberedSpace.class);
		Shepherd mockedShep = Mockito.mock(Shepherd.class);
		NumberedSpace space = Mockito.mock(NumberedSpace.class);
		MapHandler mapHandler = Mockito.mock(MapHandler.class);
		ServerPlayer pl = new ServerPlayer(null, "", "");
		Shepherd shep = new Shepherd(null);

		shep.setCode(0);
		pl.addShepherd(shep, 0);

		status.addPlayer(pl);

		ServerGame game = new ServerGame(status, mockedSender);
		Whitebox.setInternalState(status, "gameGraph", mapHandler);

		Mockito.doReturn(space).when(mapHandler).getNodeById(Mockito.anyInt());
		Mockito.doReturn(shep).when(space).getSHepherd();
		Mockito.doReturn(mockedShep).when(mockedSpace).getSHepherd();
		Mockito.doReturn(0).when(mockedShep).getCode();
		Information info = new Information(-1, InformationType.SHEPHERDID, pl);
		info.setInformation(Mockito.anyInt());

		game.handleInformation(info);
	}

	@Test(timeout = 5000)
	public void handleInformationEndOperation() throws RemoteException {
		ServerGameSender mockedSender = Mockito.mock(ServerGameSender.class);
		ServerGameStatus status = Mockito.mock(ServerGameStatus.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerPlayer pl = new ServerPlayer(null, "", "");
		ServerTurnStatus turn = Mockito.mock(ServerTurnStatus.class);
		ServerMove move = Mockito.mock(ServerMove.class);

		Information info = new Information(-1, InformationType.ENDOPERATION, pl);
		info.setInformation(0);

		Mockito.doCallRealMethod().when(game)
				.handleInformation(Mockito.eq(info));
		Mockito.doReturn(move).when(turn).getActualMove();

		Whitebox.setInternalState(game, "statusGame", status);
		Whitebox.setInternalState(game, "serverGameSender", mockedSender);
		Whitebox.setInternalState(game, "move", turn);

		game.handleInformation(info);
	}

	@Test
	// (timeout = 5000)
	public void testNextMoveShepIndex() {
		ServerGameStatus statusGame = Mockito.mock(ServerGameStatus.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerTurnStatus turnStatus = Mockito.mock(ServerTurnStatus.class);
		ServerPlayer actualPl = Mockito.mock(ServerPlayer.class);
		ServerMoveWolfBlackSheep special = Mockito
				.mock(ServerMoveWolfBlackSheep.class);

		Information infoWolf = new Information(-1, null, null);

		Whitebox.setInternalState(game, "specialMove", special);
		Whitebox.setInternalState(game, "move", turnStatus);
		Whitebox.setInternalState(game, "statusGame", statusGame);
		Whitebox.setInternalState(game, "serverGameSender", sender);

		Mockito.doReturn(actualPl).when(statusGame).getActualPlayer();
		Mockito.doReturn(actualPl).when(statusGame).getNextPlayer();
		Mockito.doReturn(infoWolf).when(special).moveWolf();
		Mockito.doReturn(infoWolf).when(special).moveRandomBlackSheep();

		Mockito.doReturn(true).when(actualPl).isAlive();
		Mockito.doReturn(true).when(actualPl).isCanPlay();
		Mockito.doReturn(true).when(statusGame).isAlive();
		Mockito.doReturn(true).when(turnStatus).isTurnEnd();
		Mockito.doReturn(false).when(statusGame).isPaused();

		Mockito.doReturn(2).when(statusGame).getNumberPlayer();
		Mockito.doReturn(0).when(statusGame).getPlayerIndex();

		Mockito.doCallRealMethod().when(game).nextMove();

		infoWolf.setInformation(new ArrayList<Field>(Arrays.asList(new Field(0,
				null), new Field(1, null))));
		game.nextMove();

		infoWolf.setInformation(new ArrayList<Field>(Arrays.asList(new Field(0,
				null))));
		game.nextMove();
	}

	//@Test
	// (timeout = 5000)
	public void testNextMoveMoreThanTwoPlayer() {
		ServerGameStatus statusGame = Mockito.mock(ServerGameStatus.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerTurnStatus turnStatus = Mockito.mock(ServerTurnStatus.class);
		ServerPlayer actualPl = Mockito.mock(ServerPlayer.class);
		ServerMoveWolfBlackSheep special = Mockito
				.mock(ServerMoveWolfBlackSheep.class);
		Shepherd shep = Mockito.mock(Shepherd.class);
		Information infoWolf = new Information(-1, null, null);

		Whitebox.setInternalState(game, "specialMove", special);
		Whitebox.setInternalState(game, "move", turnStatus);
		Whitebox.setInternalState(game, "statusGame", statusGame);
		Whitebox.setInternalState(game, "serverGameSender", sender);

		Mockito.doReturn(actualPl).when(statusGame).getActualPlayer();
		Mockito.doReturn(actualPl).when(statusGame).getNextPlayer();
		Mockito.doReturn(shep).when(actualPl).getShepherd(Mockito.anyInt());
		Mockito.doReturn(infoWolf).when(special).moveWolf();
		Mockito.doReturn(infoWolf).when(special).moveRandomBlackSheep();

		Mockito.doReturn(true).when(actualPl).isAlive();
		Mockito.doReturn(true).when(actualPl).isCanPlay();
		Mockito.doReturn(true).when(statusGame).isAlive();
		Mockito.doReturn(true).when(turnStatus).isTurnEnd();
		Mockito.doReturn(false).when(statusGame).isPaused();

		Mockito.doReturn(3).when(statusGame).getNumberPlayer();
		Mockito.doReturn(0).when(statusGame).getPlayerIndex();

		Mockito.doCallRealMethod().when(game).nextMove();

		infoWolf.setInformation(new ArrayList<Field>(Arrays.asList(new Field(0,
				null), new Field(1, null))));
		game.nextMove();
	}

	//@Test
	// (timeout = 5000)
	public void testNextMoveMoreTurnNotEnd() {
		ServerGameStatus statusGame = Mockito.mock(ServerGameStatus.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerTurnStatus turnStatus = Mockito.mock(ServerTurnStatus.class);
		ServerPlayer actualPl = Mockito.mock(ServerPlayer.class);
		ServerMoveWolfBlackSheep special = Mockito
				.mock(ServerMoveWolfBlackSheep.class);
		Shepherd shep = Mockito.mock(Shepherd.class);

		Whitebox.setInternalState(game, "specialMove", special);
		Whitebox.setInternalState(game, "move", turnStatus);
		Whitebox.setInternalState(game, "statusGame", statusGame);
		Whitebox.setInternalState(game, "serverGameSender", sender);

		Mockito.doReturn(actualPl).when(statusGame).getActualPlayer();
		Mockito.doReturn(actualPl).when(statusGame).getNextPlayer();
		Mockito.doReturn(shep).when(actualPl).getShepherd(Mockito.anyInt());

		Mockito.doReturn(true).when(actualPl).isAlive();
		Mockito.doReturn(true).when(actualPl).isCanPlay();
		Mockito.doReturn(true).when(statusGame).isAlive();
		Mockito.doReturn(false).when(turnStatus).isTurnEnd();
		Mockito.doReturn(false).when(statusGame).isPaused();

		Mockito.doReturn(3).when(statusGame).getNumberPlayer();
		Mockito.doReturn(0).when(statusGame).getPlayerIndex();

		Mockito.doCallRealMethod().when(game).nextMove();

		game.nextMove();
	}

	@Test
	// (timeout = 5000)
	public void testNextMoveMoreGamePaused() {
		ServerGameStatus statusGame = Mockito.mock(ServerGameStatus.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerTurnStatus turnStatus = Mockito.mock(ServerTurnStatus.class);
		ServerPlayer actualPl = Mockito.mock(ServerPlayer.class);
		ServerMoveWolfBlackSheep special = Mockito
				.mock(ServerMoveWolfBlackSheep.class);
		Shepherd shep = Mockito.mock(Shepherd.class);

		Whitebox.setInternalState(game, "specialMove", special);
		Whitebox.setInternalState(game, "move", turnStatus);
		Whitebox.setInternalState(game, "statusGame", statusGame);
		Whitebox.setInternalState(game, "serverGameSender", sender);

		Mockito.doReturn(actualPl).when(statusGame).getActualPlayer();
		Mockito.doReturn(actualPl).when(statusGame).getNextPlayer();
		Mockito.doReturn(shep).when(actualPl).getShepherd(Mockito.anyInt());

		Mockito.doReturn(true).when(actualPl).isAlive();
		Mockito.doReturn(true).when(actualPl).isCanPlay();
		Mockito.doReturn(true).when(statusGame).isAlive();
		Mockito.doReturn(true).when(turnStatus).isTurnEnd();
		Mockito.doReturn(true).when(statusGame).isPaused();

		Mockito.doReturn(3).when(statusGame).getNumberPlayer();
		Mockito.doReturn(0).when(statusGame).getPlayerIndex();

		Mockito.doCallRealMethod().when(game).nextMove();

		game.nextMove();
	}

	@Test
	// (timeout = 5000)
	public void testNextMoveEndGame() {
		ServerGameStatus statusGame = Mockito.mock(ServerGameStatus.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerTurnStatus turnStatus = Mockito.mock(ServerTurnStatus.class);
		ServerPlayer actualPl = Mockito.mock(ServerPlayer.class);
		ServerMoveWolfBlackSheep special = Mockito
				.mock(ServerMoveWolfBlackSheep.class);
		Shepherd shep = Mockito.mock(Shepherd.class);
		MapCreator mapCreator = new MapCreator();
		MapHandler mapHandler = new MapHandler();

		mapCreator.addNodeToMap();
		mapCreator.connect();

		mapHandler.setMap(mapCreator.getCreatedMap());

		Whitebox.setInternalState(game, "specialMove", special);
		Whitebox.setInternalState(game, "move", turnStatus);
		Whitebox.setInternalState(game, "statusGame", statusGame);
		Whitebox.setInternalState(game, "serverGameSender", sender);

		Mockito.doReturn(mapHandler).when(statusGame).getGameGraph();
		Mockito.doReturn(new ArrayList<ServerPlayer>(Arrays.asList(actualPl)))
				.when(statusGame).getPlayers();
		Mockito.doReturn(actualPl).when(statusGame).getActualPlayer();
		Mockito.doReturn(actualPl).when(statusGame).getNextPlayer();
		Mockito.doReturn(shep).when(actualPl).getShepherd(Mockito.anyInt());
		Mockito.doReturn(new ArrayList<ServerPlayer>(Arrays.asList(actualPl)))
				.when(statusGame).getPlayers();

		Mockito.doReturn(true).when(actualPl).isAlive();
		Mockito.doReturn(true).when(actualPl).isCanPlay();
		Mockito.doReturn(true).when(statusGame).isAlive();
		Mockito.doReturn(true).when(turnStatus).isTurnEnd();
		Mockito.doReturn(false).when(statusGame).isPaused();
		Mockito.doReturn(true).when(statusGame).isGameEnd();
		Mockito.doReturn(true).when(statusGame).lastPlayer();

		Mockito.doReturn(2).when(statusGame).getNumberPlayer();
		Mockito.doReturn(0).when(statusGame).getPlayerIndex();

		Mockito.doCallRealMethod().when(game).nextMove();

		game.nextMove();
	}

	// @Test
	// (timeout = 5000)
	public void testCanIPlayNotSync() {
		ServerGameStatus statusGame = Mockito.mock(ServerGameStatus.class);
		ServerGameSender sender = Mockito.mock(ServerGameSender.class);
		ServerGame game = Mockito.mock(ServerGame.class);
		ServerTurnStatus turnStatus = Mockito.mock(ServerTurnStatus.class);
		ServerPlayer actualPl = Mockito.mock(ServerPlayer.class);
		ServerMoveWolfBlackSheep special = Mockito
				.mock(ServerMoveWolfBlackSheep.class);
		Shepherd shep = Mockito.mock(Shepherd.class);

		Whitebox.setInternalState(game, "specialMove", special);
		Whitebox.setInternalState(game, "move", turnStatus);
		Whitebox.setInternalState(game, "statusGame", statusGame);
		Whitebox.setInternalState(game, "serverGameSender", sender);

		Mockito.doReturn(actualPl).when(statusGame).getActualPlayer();
		Mockito.doReturn(actualPl).when(statusGame).getNextPlayer();
		Mockito.doReturn(shep).when(actualPl).getShepherd(Mockito.anyInt());
		Mockito.doReturn(new ArrayList<ServerPlayer>(Arrays.asList(actualPl)))
				.when(statusGame).getPlayers();
		Mockito.doReturn(new Deck()).when(statusGame).getCards();
		Mockito.doReturn(1).when(actualPl).getNumberCard(TypeField.DESERT);
		Mockito.doReturn(1).when(actualPl).getNumberCard(TypeField.HAY);
		Mockito.doReturn(1).when(actualPl).getNumberCard(TypeField.HILL);
		Mockito.doReturn(1).when(actualPl).getNumberCard(TypeField.MOUNTAIN);
		Mockito.doReturn(1).when(actualPl).getNumberCard(TypeField.WOOD);
		Mockito.doReturn(1).when(actualPl).getNumberCard(TypeField.SWAMP);

		Mockito.doReturn(true).when(actualPl).isAlive();
		Mockito.doReturn(false).when(actualPl).isSync();
		Mockito.doReturn(true).when(actualPl).isCanPlay();
		Mockito.doReturn(true).when(statusGame).isAlive();
		Mockito.doReturn(true).when(turnStatus).isTurnEnd();
		Mockito.doReturn(false).when(statusGame).isPaused();
		Mockito.doReturn(true).when(statusGame).isGameEnd();
		Mockito.doReturn(true).when(statusGame).isPlaying();

		Mockito.doCallRealMethod().when(game).canIplay(actualPl);

		game.canIplay(actualPl);
	}

}
