package server.connection;

import server.game.ServerGameSetting;
import server.game.ServerPlayer;

/**
 * middle class to send a msg frm client to server and don't block the called
 * 
 * @author mirko conti
 * 
 */
public class ServerReceiver implements Runnable {

	private Object obj;
	private ServerGameSetting serverGameSet;
	private ServerPlayer gamer;

	/**
	 * 
	 * @param serverGameSet
	 *            who call when receive something
	 * @param gamer
	 *            from who come from the msg
	 * @param obj
	 *            the msg receive from client
	 */
	public ServerReceiver(ServerGameSetting serverGameSet, ServerPlayer gamer,
			Object obj) {
		this.serverGameSet = serverGameSet;
		this.gamer = gamer;
		this.obj = obj;
	}

	/**
	 * override of runnable where call the game setting that handle the msg
	 * received
	 */
	public void run() {
		this.serverGameSet.clientToServer(this.gamer, this.obj);
	}

}
