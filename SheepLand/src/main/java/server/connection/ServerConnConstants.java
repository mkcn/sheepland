package server.connection;

/**
 * class with global constants used by server about connection
 * 
 * @author mirko conti
 * 
 */
public class ServerConnConstants {

	// set num of seconds
	public static final int SEC_TIMEOUT_START_GAME = 10;
	public static final int SEC_TIMEOUT_COME_BACK = 20;
	public static final int PING_MS = 3000;
	public static final Object MUTEX = new Object();

	/**
	 * private constructor ServerConstants to override the public one
	 */
	private ServerConnConstants() {
	}

}
