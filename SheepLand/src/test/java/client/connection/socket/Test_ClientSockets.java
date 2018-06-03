package client.connection.socket;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import server.connection.socket.ServerSockets;
import share.connection.ConnectionConstants;
import share.connection.socket.SocketInput;
import share.connection.socket.SocketOutput;
import share.game.comunication.ConnectionProtocol;
import client.game.ClientGameSetting;
import client.view.ClientLoginInterface;

import com.esotericsoftware.minlog.Log;

public class Test_ClientSockets {

	private ClientGameSetting clientGame;
	private ClientLoginInterface graficLogin;

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
		this.clientGame = Mockito.mock(ClientGameSetting.class);
		this.graficLogin = Mockito.mock(ClientLoginInterface.class);
		Mockito.when(this.clientGame.getClientLogin()).thenReturn(
				this.graficLogin);
	}

	@Test(timeout = 2000)
	public void startandcloseconnection() throws IOException,
			InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		ExecutorService executor2 = Mockito.mock(ExecutorService.class);

		ServerSockets socketserver = new ServerSockets(executor2);

		executor.execute(socketserver);

		ClientSockets clientsockets = new ClientSockets(this.clientGame,
				ConnectionConstants.IPLOCAL, ConnectionConstants.SOCKET_PORT,
				executor);
		executor.execute(clientsockets);

		Thread.sleep(200);
		socketserver.killSocketWay();
	}

	@Test(timeout = 2000)
	public void setUsrnameAndPasswordNEW_GAMER() throws Exception {

		SocketInput socketInput = Mockito.mock(SocketInput.class);
		ConnectionProtocol res = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.NEW_GAMER);

		Mockito.when(socketInput.receiveObj()).thenReturn(res);
		SocketOutput socketOutput = Mockito.mock(SocketOutput.class);

		ClientSockets clientSockets = new ClientSockets(this.clientGame,
				ConnectionConstants.IPLOCAL, ConnectionConstants.SOCKET_PORT,
				null);

		Whitebox.setInternalState(clientSockets, "socketInput", socketInput);
		Whitebox.setInternalState(clientSockets, "socketOutput", socketOutput);

		clientSockets.setUsrnameAndPassword("test", "pass");
	}

	@Test(timeout = 2000)
	public void setUsrnameAndPasswordGAMER_IS_PLAYING() throws Exception {

		SocketInput socketInput = Mockito.mock(SocketInput.class);
		ConnectionProtocol res = new ConnectionProtocol(0, null);
		res.setReply(ConnectionProtocol.GAMER_IS_PLAYING);

		Mockito.when(socketInput.receiveObj()).thenReturn(res);
		SocketOutput socketOutput = Mockito.mock(SocketOutput.class);

		ClientSockets clientSockets = new ClientSockets(this.clientGame,
				ConnectionConstants.IPLOCAL, ConnectionConstants.SOCKET_PORT,
				null);

		Whitebox.setInternalState(clientSockets, "socketInput", socketInput);
		Whitebox.setInternalState(clientSockets, "socketOutput", socketOutput);

		clientSockets.setUsrnameAndPassword("test", "pass");
	}
}
