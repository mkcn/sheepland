package client.connection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import share.connection.ConnectionConstants;
import client.game.ClientGameSetting;
import client.view.ClientLoginInterface;

import com.esotericsoftware.minlog.Log;

public class Test_ClientStartConn {

	private ClientStartConn clientStartConn;
	private ClientGameSetting clientGameSetting;
	private ClientLoginInterface graphicLogin;

	@Before
	public void before() {
		Log.INFO = false;
		Log.ERROR = false;
		this.clientGameSetting = Mockito.mock(ClientGameSetting.class);
		this.graphicLogin = Mockito.mock(ClientLoginInterface.class);
		this.clientStartConn = new ClientStartConn(this.clientGameSetting);

	}

	@Test
	public void initializeRmiFailConnectionCalledTwice() throws Exception {

		this.clientStartConn.initializeRmi(ConnectionConstants.IPLOCAL,
				ConnectionConstants.SOCKET_PORT, this.graphicLogin);
		this.clientStartConn.initializeRmi(ConnectionConstants.IPLOCAL,
				ConnectionConstants.SOCKET_PORT, this.graphicLogin);
	}

	@Test
	public void initializeSocketFailConnection() throws Exception {

		this.clientStartConn.initializeSocket(ConnectionConstants.IPLOCAL,
				ConnectionConstants.RMI_PORT, this.graphicLogin);
	}

}