package client.connection.socket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import share.connection.socket.SocketInput;
import client.game.ClientGameSetting;

import com.esotericsoftware.minlog.Log;

public class Test_ClientSocketsReceive {

	@Mock
	private ClientGameSetting game;
	@Mock
	private SocketInput socket;

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
		this.game = Mockito.mock(ClientGameSetting.class);
		this.socket = Mockito.mock(SocketInput.class);
	}

	@Test(timeout = 2000)
	public void sendToGameNullReceive() {
		Mockito.when(this.socket.receiveObj()).thenReturn(null);
		ClientSocketsReceive clientSocketsReceive = new ClientSocketsReceive(
				this.game, this.socket);
		clientSocketsReceive.run();
	}
}
