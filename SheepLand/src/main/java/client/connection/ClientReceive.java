package client.connection;

import client.game.ClientGameSetting;

/**
 * middle class to inkove the GUI with queue threads
 * 
 * @author mirko conti
 * 
 */
public class ClientReceive implements Runnable {

	private Object obj;
	private ClientGameSetting gameSet;

	/**
	 * get the game setting and the message to pass
	 * 
	 * @param gameSet
	 * @param obj
	 */
	public ClientReceive(ClientGameSetting gameSet, Object obj) {
		this.gameSet = gameSet;
		this.obj = obj;
	}

	/**
	 * when it's its turn run this and send the comand
	 */
	public void run() {
		this.gameSet.serverToClient(this.obj);
	}

}
