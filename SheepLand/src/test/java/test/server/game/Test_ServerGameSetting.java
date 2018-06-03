package test.server.game;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import server.game.ServerGame;
import server.game.ServerGameSetting;
import server.game.ServerGameStatus;
import server.game.ServerPlayer;
import share.connection.RemoteSendInterface;

import com.esotericsoftware.minlog.Log;

public class Test_ServerGameSetting {

	private ServerGameSetting serverGameSetting;
	private ServerPlayer player;
	private RemoteSendInterface clientRemote;

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
		this.serverGameSetting = new ServerGameSetting();
		this.clientRemote = Mockito.mock(RemoteSendInterface.class);
		this.player = new ServerPlayer(this.clientRemote, "usr1", "pass");
		this.serverGameSetting.addPlayer(this.player);
	}

	@Test
	public void testAddPlayer() {

		ServerPlayer player2 = new ServerPlayer(this.clientRemote, "usr1",
				"pass");
		this.serverGameSetting.addPlayer(player2);

	}

	@Test
	public void testAddPlayerNotAlive() {

		ServerGameStatus serverGameStatus = Mockito
				.mock(ServerGameStatus.class);
		Mockito.when(serverGameStatus.isAlive()).thenReturn(false);
		Whitebox.setInternalState(this.serverGameSetting, "serverGameStatus",
				serverGameStatus);

		ServerPlayer player2 = new ServerPlayer(this.clientRemote, "usr1",
				"pass");
		player2.setAlive(false);
		this.serverGameSetting.addPlayer(player2);

	}

	@Test
	public void testDisconnectAndComeBakc() {

		ServerGame serverGame = Mockito.mock(ServerGame.class);
		Whitebox.setInternalState(this.serverGameSetting, "serverGame",
				serverGame);

		this.serverGameSetting.alertPlayerDisconnected(this.player);
		this.serverGameSetting.playerComeBack(this.player);

		this.serverGameSetting.startTimeoutPlayerDisconnected();
		this.serverGameSetting.startTimeoutPlayerDisconnected();
	}

	@Test
	public void testDisconnectAndComeBackWithGamePaused() {

		ServerGame serverGame = Mockito.mock(ServerGame.class);
		Whitebox.setInternalState(this.serverGameSetting, "serverGame",
				serverGame);

		ServerGameStatus serverGameStatus = Mockito
				.mock(ServerGameStatus.class);
		Mockito.when(serverGameStatus.isPaused()).thenReturn(false);
		Whitebox.setInternalState(this.serverGameSetting, "serverGameStatus",
				serverGameStatus);

		this.serverGameSetting.alertPlayerDisconnected(this.player);
		this.serverGameSetting.playerComeBack(this.player);

		this.serverGameSetting.startTimeoutPlayerDisconnected();
		this.serverGameSetting.startTimeoutPlayerDisconnected();
	}

	@Test
	public void testDisconnectAndComeBakcSomeO() {

		ServerPlayer player3 = new ServerPlayer(this.clientRemote, "usr2",
				"pass2");
		this.serverGameSetting.addPlayer(player3);

		this.serverGameSetting.playerComeBack(player3);
	}

	@Test
	public void testGets() {
		assertTrue(this.serverGameSetting.getServerGame() != null);
		assertTrue(this.serverGameSetting.getServerGameStatus() != null);
	}

	@Test
	public void testStartGame() {
		this.serverGameSetting.startGame();
	}

}