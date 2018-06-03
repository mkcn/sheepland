package client.connection.socket;

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import share.connection.socket.SocketOutput;

import com.esotericsoftware.minlog.Log;

public class Test_ClientSocketsSend {

	@Mock
	private SocketOutput socket;

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
		this.socket = Mockito.mock(SocketOutput.class);
	}

	@Test
	public void testSendObj() throws RemoteException {
		ClientSocketsSend clientSocketsSend = new ClientSocketsSend(this.socket);
		clientSocketsSend.sendObj("test");
	}

	@Test
	public void testPing() throws RemoteException {
		ClientSocketsSend clientSocketsSend = new ClientSocketsSend(this.socket);
		assertTrue(clientSocketsSend.ping());
	}

}
