package server.connection;

import java.rmi.RemoteException;

import server.game.ServerGameSetting;
import server.game.ServerPlayer;

import com.esotericsoftware.minlog.Log;

/**
 * class that each n second send a ping to the client to check the connection
 * 
 * @author mirko conti
 * 
 */
public class ServerPingConnection implements Runnable {

	private ServerPlayer serverPlayer;
	private ServerGameSetting serverGameSet;
	private int timeout;

	/**
	 * constructor of ServerPingConnection
	 * 
	 * @param gameServer
	 *            the game to aller if player is disconnected
	 * @param serverPlayer
	 *            the player to check
	 */
	public ServerPingConnection(ServerGameSetting gameServer,
			ServerPlayer serverPlayer) {

		this.serverPlayer = serverPlayer;
		this.serverGameSet = gameServer;
		// in this way in the test i can change the value of timeout
		this.timeout = ServerConnConstants.PING_MS;
	}

	/**
	 * an infinite loop with a Sleep and a ping
	 */
	public void run() {
		boolean goOn = true;
		while (goOn) {
			try {
				Thread.sleep(this.timeout);
				if (!this.serverPlayer.getClientRemote().ping()) {
					throw new RemoteException();
				}
			} catch (RemoteException e) {
				// if send fail it's go here
				goOn = false;
				Log.debug("ServerPingConnection RemoteException", e);
			} catch (InterruptedException e) {
				// if thread stop go here
				goOn = false;
				Log.debug("ServerPingConnection InterruptedException", e);
			}
		}
		// and send the "message" to the game
		this.serverGameSet.alertPlayerDisconnected(this.serverPlayer);
	}
}
