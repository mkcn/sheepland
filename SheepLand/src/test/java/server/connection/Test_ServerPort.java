package server.connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import share.connection.ConnectionConstants;

import com.esotericsoftware.minlog.Log;

public class Test_ServerPort {
	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
	}

	@Test
	public void testGetPort() {
		ServerPort serverPort = new ServerPort(ConnectionConstants.SOCKET_PORT);
		int port = serverPort.gerPort();
		assertTrue(port == ConnectionConstants.SOCKET_PORT);
	}

	@Test
	public void testGetOtherPort() {
		ServerPort serverPort = new ServerPort(ConnectionConstants.SOCKET_PORT);
		serverPort.setOtherRandomPort();
		int port = serverPort.gerPort();
		assertFalse(port == ConnectionConstants.SOCKET_PORT);
	}
}
