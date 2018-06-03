package server.connection.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import client.game.ClientGameSetting;
import client.view.ClientLoginInterface;

import com.esotericsoftware.minlog.Log;

public class Test_ServerSockets {

	private ClientGameSetting clientGame;
	private ClientLoginInterface graficLogin;

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
		this.clientGame = Mockito.mock(ClientGameSetting.class);
		// this.commonPart = Mockito.mock(ServerSettingConnection.class);
		this.graficLogin = Mockito.mock(ClientLoginInterface.class);
		Mockito.when(this.clientGame.getClientLogin()).thenReturn(
				this.graficLogin);
	}

	@Test
	public void checkErrorPort() {
		ExecutorService executor = Executors.newCachedThreadPool();

		ServerSockets socketserver = new ServerSockets(executor);
		executor.execute(socketserver);
		executor.execute(socketserver);
	}
}
