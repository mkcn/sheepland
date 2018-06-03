package server.connection.socket;

import static org.mockito.Matchers.any;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import server.connection.ServerSettingConnection;
import server.game.ServerGameSetting;
import server.game.ServerGameStatus;
import server.game.ServerPlayer;
import share.connection.RemoteSendInterface;
import share.connection.socket.SocketInput;
import share.connection.socket.SocketOutput;
import share.game.comunication.ConnectionProtocol;

import com.esotericsoftware.minlog.Log;

public class Test_ServerSocketsEcho {

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
	}

	@Test
	public void startSocketEchoAndReceiveOkNewPlayer() throws IOException,
			InterruptedException {
		ServerSettingConnection commonPart = Mockito
				.mock(ServerSettingConnection.class);

		ServerGameSetting gameSetting = Mockito.mock(ServerGameSetting.class);
		Mockito.when(commonPart.getGameSetting()).thenReturn(gameSetting);
		ServerGameStatus gameStatus = Mockito.mock(ServerGameStatus.class);
		Mockito.when(gameSetting.getServerGameStatus()).thenReturn(gameStatus);
		List<ServerPlayer> listPlayers = new ArrayList<ServerPlayer>();
		listPlayers.add(null);
		listPlayers.add(null);
		listPlayers.add(null);
		listPlayers.add(null);
		Mockito.when(gameStatus.getPlayers()).thenReturn(listPlayers);

		SocketInput socketInput = Mockito.mock(SocketInput.class);
		SocketOutput socketOutput = Mockito.mock(SocketOutput.class);

		// RemoteSendInterface remoteClient = new ServerSocketsSendImpl(
		// socketOutput);

		ConnectionProtocol res = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.NEW_GAMER);

		Mockito.when(
				commonPart.checkAndSetCredentials(
						any(RemoteSendInterface.class), any(String.class),
						any(String.class))).thenReturn(res);

		ServerSocketsEcho serverSocketsEcho = new ServerSocketsEcho();

		Mockito.when(socketInput.receiveString()).thenReturn("test");

		Whitebox.setInternalState(serverSocketsEcho, "socketInput", socketInput);
		Whitebox.setInternalState(serverSocketsEcho, "socketOutput",
				socketOutput);

		serverSocketsEcho.run();
	}

	@Test
	public void startSocketEchoAndReceiveURS_EXISTING_PASS_WRONG()
			throws IOException, InterruptedException {
		ServerSettingConnection commonPart = Mockito
				.mock(ServerSettingConnection.class);

		ServerGameSetting gameSetting = Mockito.mock(ServerGameSetting.class);
		Mockito.when(commonPart.getGameSetting()).thenReturn(gameSetting);
		ServerGameStatus gameStatus = Mockito.mock(ServerGameStatus.class);
		Mockito.when(gameSetting.getServerGameStatus()).thenReturn(gameStatus);
		List<ServerPlayer> listPlayers = new ArrayList<ServerPlayer>();
		listPlayers.add(null);
		listPlayers.add(null);
		listPlayers.add(null);
		listPlayers.add(null);
		Mockito.when(gameStatus.getPlayers()).thenReturn(listPlayers);

		SocketInput socketInput = Mockito.mock(SocketInput.class);
		SocketOutput socketOutput = Mockito.mock(SocketOutput.class);

		ConnectionProtocol res = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.URS_EXISTING_PASS_WRONG);

		Mockito.when(
				commonPart.checkAndSetCredentials(
						any(RemoteSendInterface.class), any(String.class),
						any(String.class))).thenReturn(res);

		ServerSocketsEcho serverSocketsEcho = new ServerSocketsEcho();

		Mockito.when(socketInput.receiveString()).thenReturn("test");

		Whitebox.setInternalState(serverSocketsEcho, "socketInput", socketInput);
		Whitebox.setInternalState(serverSocketsEcho, "socketOutput",
				socketOutput);

		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(serverSocketsEcho);
		Thread.sleep(100);
		ConnectionProtocol res2 = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.NEW_GAMER);
		Mockito.when(
				commonPart.checkAndSetCredentials(
						any(RemoteSendInterface.class), any(String.class),
						any(String.class))).thenReturn(res2);
	}

	@Test
	public void startSocketEchoAndReceiveOLD_GAME_RESUME() throws IOException,
			InterruptedException {
		ServerSettingConnection commonPart = Mockito
				.mock(ServerSettingConnection.class);

		ServerGameSetting gameSetting = Mockito.mock(ServerGameSetting.class);
		Mockito.when(commonPart.getGameSetting()).thenReturn(gameSetting);
		ServerGameStatus gameStatus = Mockito.mock(ServerGameStatus.class);
		Mockito.when(gameSetting.getServerGameStatus()).thenReturn(gameStatus);
		List<ServerPlayer> listPlayers = new ArrayList<ServerPlayer>();
		listPlayers.add(null);
		listPlayers.add(null);
		listPlayers.add(null);
		listPlayers.add(null);
		Mockito.when(gameStatus.getPlayers()).thenReturn(listPlayers);

		SocketInput socketInput = Mockito.mock(SocketInput.class);
		SocketOutput socketOutput = Mockito.mock(SocketOutput.class);

		ConnectionProtocol res = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.OLD_GAME_RESUME);

		Mockito.when(
				commonPart.checkAndSetCredentials(
						any(RemoteSendInterface.class), any(String.class),
						any(String.class))).thenReturn(res);

		ServerSocketsEcho serverSocketsEcho = new ServerSocketsEcho();

		Mockito.when(socketInput.receiveString()).thenReturn("test");

		Whitebox.setInternalState(serverSocketsEcho, "socketInput", socketInput);
		Whitebox.setInternalState(serverSocketsEcho, "socketOutput",
				socketOutput);

		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(serverSocketsEcho);
		Thread.sleep(100);
		ConnectionProtocol res2 = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.NEW_GAMER);
		Mockito.when(
				commonPart.checkAndSetCredentials(
						any(RemoteSendInterface.class), any(String.class),
						any(String.class))).thenReturn(res2);
	}

}
