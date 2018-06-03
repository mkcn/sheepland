package server.game;

import java.rmi.RemoteException;
import java.util.List;

import server.view.ServerCLC;
import share.game.comunication.Information;
import share.game.comunication.Request;
import share.game.comunication.RequestType;

import com.esotericsoftware.minlog.Log;

/**
 * class to handle the send of message to client
 * 
 * @author mirko conti
 * 
 */
public class ServerGameSender {

	private ServerGameStatus statusGame;

	/**
	 * get the info about game
	 * 
	 * @param statusGame
	 */
	public ServerGameSender(ServerGameStatus statusGame) {
		this.statusGame = statusGame;
	}

	/**
	 * print on server all the msg that sent to everyOne helpful for test and to
	 * keep logs
	 * 
	 * @param obj
	 */
	private void printOnServer(Object obj) {
		if (obj.getClass() == Information.class) {
			ServerCLC.showMsg("", ((Information) obj).getInfoType().toString());
		} else if (obj.getClass() == Request.class) {
			ServerCLC.showMsg("", ((Request) obj).getReqType().toString());
		}
	}

	/**
	 * Send a message to every player to let them know ..
	 * 
	 * @param player
	 */
	public void SendWaitTurnMessage() {
		Request req = new Request(-1, RequestType.TURNOF, this.statusGame
				.getActualPlayer().getAsPlayer());
		serverToEveryClient(req, null, true);
	}

	/**
	 * send a message to every one and print on server
	 * 
	 * @param msg
	 *            obj
	 * @param sender
	 *            dont send to this
	 * @param printToTheServer
	 *            print on server too
	 */
	public void serverToEveryClient(Object msg, ServerPlayer sender,
			boolean printToTheServer) {

		if (printToTheServer) {
			printOnServer(msg);
		}

		List<ServerPlayer> gamers = this.statusGame.getPlayers();
		for (int i = 0; i < gamers.size(); i++) {
			if (sender == null
					|| gamers.get(i).getUsrname() != sender.getUsrname()) {
				serverToClient(gamers.get(i), msg);
			}
		}
	}

	/**
	 * send a message to a single client
	 * 
	 * @param receiver
	 * @param obj
	 */
	public void serverToClient(ServerPlayer receiver, Object obj) {
		try {
			if (receiver.isAlive()) {
				receiver.getClientRemote().sendObj(obj);
			}
		} catch (RemoteException e) {
			Log.debug("serverToClient", e);
		}
	}

	/**
	 * Send to every client a message to let them know what player is banned
	 * 
	 * @param bannedPlayer
	 */
	public void sendBanPlayer(ServerPlayer bannedPlayer) {
		Request req = new Request(-1, RequestType.PLAYERBANNED,
				bannedPlayer.getAsPlayer());
		this.serverToEveryClient(req, null, false);
	}

}
