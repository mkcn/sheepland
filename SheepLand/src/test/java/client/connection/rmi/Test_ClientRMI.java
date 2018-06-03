package client.connection.rmi;

import static org.mockito.Matchers.any;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import server.connection.rmi.ServerRMI;
import share.connection.ConnectionConstants;
import share.connection.RemoteSendInterface;
import share.connection.rmi.RMIRemoteInitInterface;
import share.game.comunication.ConnectionProtocol;
import client.game.ClientGameSetting;
import client.view.ClientLoginInterface;

import com.esotericsoftware.minlog.Log;

public class Test_ClientRMI {

	private ClientGameSetting clientGameSet;
	private ClientLoginInterface graficLogin;

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
		this.clientGameSet = Mockito.mock(ClientGameSetting.class);
		this.graficLogin = Mockito.mock(ClientLoginInterface.class);
		Mockito.when(this.clientGameSet.getClientLogin()).thenReturn(
				this.graficLogin);
	}

	@Test(timeout = 5000)
	public void startRMIClientVerifyCallException() throws Exception {
		ExecutorService ex = Executors.newSingleThreadExecutor();
		ClientRMI clientRmi = new ClientRMI(this.clientGameSet, "localhost",
				ConnectionConstants.RMI_PORT, ex);
		clientRmi.run();
	}

	@Test(timeout = 5000)
	public void startRMIClientsConnectionAndClose() throws Exception {

		ServerRMI serverRmi = new ServerRMI();

		ExecutorService ex = Executors.newSingleThreadExecutor();
		ClientRMI clientRmi = new ClientRMI(this.clientGameSet,
				ConnectionConstants.IPLOCAL, ConnectionConstants.RMI_PORT, ex);
		ex.execute(clientRmi);
		Thread.sleep(200);
		serverRmi.killServerRMI();
		clientRmi.killClientRMI();
	}

	@Test(timeout = 5000)
	public void setUsrnameAndPassword() throws Exception {

		RMIRemoteInitInterface remoteServer = Mockito
				.mock(RMIRemoteInitInterface.class);
		// RemoteSendInterface client =
		// Mockito.mock(RemoteSe<>ndInterface.class);
		ConnectionProtocol res = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.NEW_GAMER);

		Mockito.when(
				remoteServer.setClientConfiguration(
						any(RemoteSendInterface.class), any(String.class),
						any(String.class))).thenReturn(res);

		ClientRMI clientRmi = new ClientRMI(this.clientGameSet,
				ConnectionConstants.IPLOCAL, ConnectionConstants.RMI_PORT, null);
		Whitebox.setInternalState(clientRmi, "remoteServer", remoteServer);
		// Whitebox.setInternalState(clientRmi, "client", client);

		clientRmi.setUsrnameAndPassword("test", "pass");
	}

	@Test(timeout = 5000)
	public void setUsrnameAndPasswordWrong() throws Exception {

		RMIRemoteInitInterface remoteServer = Mockito
				.mock(RMIRemoteInitInterface.class);
		// RemoteSendInterface client = Mockito.mock(RemoteSendInterface.class);
		ConnectionProtocol res = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.GAMER_IS_PLAYING);
		Mockito.when(
				remoteServer.setClientConfiguration(
						any(RemoteSendInterface.class), any(String.class),
						any(String.class))).thenReturn(res);
		//
		// Mockito.verify(remoteServer).setClientConfiguration(
		// Matchers.<RemoteSendInterface>any(), "test", "pass")).t

		ClientRMI clientRmi = new ClientRMI(this.clientGameSet,
				ConnectionConstants.IPLOCAL, ConnectionConstants.RMI_PORT, null);
		Whitebox.setInternalState(clientRmi, "remoteServer", remoteServer);
		// Whitebox.setInternalState(clientRmi, "client", client);

		clientRmi.setUsrnameAndPassword("test", "pass");
	}
}