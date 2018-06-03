package server.connection.rmi;

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import server.connection.ServerSettingConnection;
import server.game.ServerGameSetting;
import server.game.ServerGameStatus;
import server.game.ServerPlayer;
import share.connection.RemoteSendInterface;
import share.game.comunication.ConnectionProtocol;

import com.esotericsoftware.minlog.Log;

public class Test_ServerRMIInitImpl {

	private ServerSettingConnection commonPart;
	private RemoteSendInterface remoteClient;

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
		this.commonPart = Mockito.mock(ServerSettingConnection.class);

		ServerGameSetting gameSetting = Mockito.mock(ServerGameSetting.class);
		Mockito.when(this.commonPart.getGameSetting()).thenReturn(gameSetting);
		ServerGameStatus gameStatus = Mockito.mock(ServerGameStatus.class);
		Mockito.when(gameSetting.getServerGameStatus()).thenReturn(gameStatus);
		List<ServerPlayer> listPlayers = new ArrayList<ServerPlayer>();
		listPlayers.add(null);
		listPlayers.add(null);
		listPlayers.add(null);
		listPlayers.add(null);
		Mockito.when(gameStatus.getPlayers()).thenReturn(listPlayers);
		this.remoteClient = Mockito.mock(RemoteSendInterface.class);

	}

	// @Test
	// public void testClientConfigurationURS_EXISTING_PASS_WRONG()
	// throws RemoteException {
	// ConnectionProtocol res = new ConnectionProtocol(0, null);
	// res.setReply(ConnectionProtocol.URS_EXISTING_PASS_WRONG);
	//
	// Mockito.when(
	// this.commonPart.checkAndSetCredentials(this.remoteClient,
	// "test", "pass")).thenReturn(res);
	//
	// ServerRMIInitImpl serverRMIInitImpl = new ServerRMIInitImpl();
	//
	// ConnectionProtocol setClientConfiguration = serverRMIInitImpl
	// .setClientConfiguration(this.remoteClient, "test", "pass");
	// assertTrue(setClientConfiguration.getReply() ==
	// ConnectionProtocol.URS_EXISTING_PASS_WRONG);
	// }

	@Test
	public void testClientConfigurationNEW_GAMER() throws RemoteException {
		ConnectionProtocol res = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.NEW_GAMER);
		Mockito.when(
				this.commonPart.checkAndSetCredentials(this.remoteClient,
						"test", "pass")).thenReturn(res);

		ServerRMIInitImpl serverRMIInitImpl = new ServerRMIInitImpl();

		ConnectionProtocol setClientConfiguration = serverRMIInitImpl
				.setClientConfiguration(this.remoteClient, "test", "pass");
		assertTrue(setClientConfiguration.getReply() == ConnectionProtocol.NEW_GAMER);
	}

	@Test
	public void testSendInterface() throws RemoteException {
		ConnectionProtocol res = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.NEW_GAMER);
		Mockito.when(
				this.commonPart.checkAndSetCredentials(this.remoteClient,
						"test", "pass")).thenReturn(res);

		ServerRMIInitImpl serverRMIInitImpl = new ServerRMIInitImpl();

		serverRMIInitImpl.getServerSendInterface(false);
	}
}
