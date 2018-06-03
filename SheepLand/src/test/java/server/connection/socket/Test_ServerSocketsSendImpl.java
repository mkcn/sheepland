package server.connection.socket;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import share.connection.socket.SocketOutput;

import com.esotericsoftware.minlog.Log;

public class Test_ServerSocketsSendImpl {

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
	}

	@Test
	public void implementSend() {
		SocketOutput socketOutput = Mockito.mock(SocketOutput.class);
		ServerSocketsSendImpl serverSocketsSendImpl = new ServerSocketsSendImpl(
				socketOutput);
		serverSocketsSendImpl.sendObj("test");

	}

	@Test
	public void implementPing() {
		SocketOutput socketOutput = Mockito.mock(SocketOutput.class);
		ServerSocketsSendImpl serverSocketsSendImpl = new ServerSocketsSendImpl(
				socketOutput);
		assertTrue(serverSocketsSendImpl.ping());
	}
}
