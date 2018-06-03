package server.connection;

import java.util.Random;

/**
 * class to get and set the port with a range of 100 port for each type of
 * connection
 * 
 * @author mirko conti
 * 
 */
public class ServerPort {

	private int deltaPort;
	private int initialPort;

	/**
	 * constructor of ServerPort
	 * 
	 * @param port
	 *            the initial default port that change only if it's already
	 *            taken
	 */
	public ServerPort(int initialPort) {
		this.initialPort = initialPort;
		this.deltaPort = 0;
	}

	/**
	 * add a random value to the value of initial port
	 */
	public void setOtherRandomPort() {
		Random num = new Random();
		// +1 because nextInt can return 0.
		this.deltaPort = num.nextInt(99) + 1;
	}

	/**
	 * return the intial port plus the random
	 * 
	 * @return
	 */
	public int gerPort() {
		return this.initialPort + this.deltaPort;
	}
}
