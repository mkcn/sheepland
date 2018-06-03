package client.connection;

import server.connection.ServerConnConstants;
import share.connection.RemoteSendInterface;
import client.game.ClientGameSetting;

import com.esotericsoftware.minlog.Log;

/**
 * class to check the connection
 * 
 * @author mirko conti
 * 
 */
public class ClientPingConnection implements Runnable {
	private ClientGameSetting clientGameSet;
	private RemoteSendInterface serverImpl;
	private int timeout;

	/**
	 * constructor of ClientPingConnection
	 * 
	 * @param clientGameSet
	 *            the game to aller if player is disconnected
	 * @param RemoteSendInterface
	 *            the implementation on server where call the ping
	 */
	public ClientPingConnection(ClientGameSetting clientGameSet,
			RemoteSendInterface serverImpl) {
		this.clientGameSet = clientGameSet;
		this.serverImpl = serverImpl;
		this.timeout = ServerConnConstants.PING_MS;
	}

	/**
	 * an infinite loop with a sleep and a ping
	 */
	public void run() {
		boolean goOn = true;
		while (goOn) {
			try {
				Thread.sleep(this.timeout);
				if (!this.serverImpl.ping()) {
					goOn = false;
				}
			} catch (Exception e) {
				// if send fail it's go here
				Log.debug("ServerPingConnection", e);
				goOn = false;
			}
		}
		// and send the "message of fail" to the game
		this.clientGameSet.notifyConnectionFail();
	}
}
