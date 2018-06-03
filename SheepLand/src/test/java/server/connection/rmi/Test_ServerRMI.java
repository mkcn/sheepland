package server.connection.rmi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import client.game.ClientGameSetting;
import client.view.ClientLoginInterface;

import com.esotericsoftware.minlog.Log;

public class Test_ServerRMI {
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

	@Test
	public void rmiException() {
		new ServerRMI();
		new ServerRMI();
	}
}
