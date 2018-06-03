package client.connection.rmi;

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import client.game.ClientGameSetting;

import com.esotericsoftware.minlog.Log;

public class Test_ClientRMIImpl {

	@Mock
	ClientGameSetting clientGame;

	@Before
	public void Configure() {
		Log.INFO = false;
		Log.ERROR = false;
		this.clientGame = Mockito.mock(ClientGameSetting.class);
		// Mockito.when(clientGame.ServerToClient("test")).thenReturn(null);
	}

	@Test
	public void StartRMIClientVerifyPing() throws RemoteException {
		ClientRMIImpl clientRMIRemote = new ClientRMIImpl(this.clientGame);
		assertTrue(clientRMIRemote.ping());
	}

	@Test
	public void SendObjCheckException() throws RemoteException {
		ClientRMIImpl clientRMIRemote = new ClientRMIImpl(this.clientGame);
		clientRMIRemote.sendObj("test");
	}
}
