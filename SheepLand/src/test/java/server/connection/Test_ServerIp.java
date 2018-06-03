package server.connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import share.connection.ConnectionConstants;

import com.esotericsoftware.minlog.Log;

public class Test_ServerIp {

	private ServerIp serverIp;

	@Before
	public void createObj() {
		Log.INFO = false;
		Log.ERROR = false;
		this.serverIp = new ServerIp();
	}

	@Test(timeout = 1000)
	public void testGetMyPublicIp() {
		Log.DEBUG = false;
		String ip = this.serverIp.getMyPublicIp();
		assertFalse(ip == null);
	}

	@Test(timeout = 1000)
	public void testGetMyPublicIpDebug() {
		Log.DEBUG = true;
		String ip = this.serverIp.getMyPublicIp();
		assertFalse(ip == null);
	}

	@Test(timeout = 1000)
	public void testGetMyPrivateIp() {
		String ip = this.serverIp.getMyPrivateIp();
		assertFalse(ip == null);
	}

	@Test(timeout = 1000)
	public void testGetMyLocalIp() {
		String ip = this.serverIp.getMyLocalIp();
		assertTrue(ip == ConnectionConstants.IPLOCAL);
	}

	@After
	public void close() {
		Log.DEBUG = false;
	}
}
