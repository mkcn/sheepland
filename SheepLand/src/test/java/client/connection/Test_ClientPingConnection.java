package client.connection;

import java.rmi.RemoteException;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import share.connection.RemoteSendInterface;
import client.game.ClientGameSetting;

import com.esotericsoftware.minlog.Log;

public class Test_ClientPingConnection {

	@Test
	public void checkSendPingAndFail() throws RemoteException {
		Log.INFO = false;
		Log.ERROR = false;
		ClientGameSetting clientGameSet = Mockito.mock(ClientGameSetting.class);
		RemoteSendInterface serverImpl = Mockito
				.mock(RemoteSendInterface.class);
		Mockito.when(serverImpl.ping()).thenReturn(false);

		ClientPingConnection clientPingConnection = new ClientPingConnection(
				clientGameSet, serverImpl);
		Whitebox.setInternalState(clientPingConnection, "timeout", 1);

		clientPingConnection.run();
	}

}
