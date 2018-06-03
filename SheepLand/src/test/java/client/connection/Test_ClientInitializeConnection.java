package client.connection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import share.connection.ConnectionConstants;
import share.game.comunication.ConnectionProtocol;
import client.connection.socket.ClientSockets;
import client.game.ClientGameSetting;
import client.view.ClientLoginInterface;

import com.esotericsoftware.minlog.Log;

public class Test_ClientInitializeConnection {

	@Mock
	private ClientGameSetting clientGameSet;
	@Mock
	private ClientLoginInterface graficLogin;

	private ClientSockets clientSockets;

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
		this.clientGameSet = Mockito.mock(ClientGameSetting.class);
		this.graficLogin = Mockito.mock(ClientLoginInterface.class);
		Mockito.when(this.clientGameSet.getClientLogin()).thenReturn(
				this.graficLogin);

		this.clientSockets = new ClientSockets(this.clientGameSet,
				ConnectionConstants.IPLOCAL, ConnectionConstants.SOCKET_PORT,
				null);
	}

	@Test
	public void testAskUsrPass() {

		this.clientSockets.askUsrPass();
	}

	@Test
	public void testHandleAnswer() {

		this.clientSockets.handleAnswer(ConnectionProtocol.NEW_GAMER, "test");
		this.clientSockets.handleAnswer(ConnectionProtocol.GAMER_IS_PLAYING,
				"test");
		this.clientSockets.handleAnswer(ConnectionProtocol.OLD_GAME_RESUME,
				"test");
		this.clientSockets.handleAnswer(
				ConnectionProtocol.URS_EXISTING_PASS_WRONG, "test");
	}

}
