package client.connection.socket;

import java.awt.EventQueue;

import share.connection.socket.SocketInput;
import client.connection.ClientReceive;
import client.game.ClientGameSetting;

import com.esotericsoftware.minlog.Log;

/**
 * class to handle all the socket messages that came from the server
 * 
 * @author mirko conti
 * 
 */
public class ClientSocketsReceive implements Runnable {

	private ClientGameSetting clientGameSet;
	private SocketInput socketInput;

	/**
	 * constructor with
	 * 
	 * @param game
	 *            is what allert
	 * @param socket
	 *            from what receive
	 */
	public ClientSocketsReceive(ClientGameSetting clientGameSet,
			SocketInput socketInput) {
		this.clientGameSet = clientGameSet;
		this.socketInput = socketInput;
	}

	/**
	 * thread that read msgs from server and then notify at the game
	 */
	public void run() {
		while (true) {
			// stay in wait until a msg arrives
			Object data = this.socketInput.receiveObj();

			// if start to receive null msg so it have to quit
			if (data == null) {
				Log.error("*null receive*");
				break;
			}
			// else call method in game setting

			EventQueue.invokeLater(new ClientReceive(this.clientGameSet, data));
		}
		// close all sockets , because if you want to reopen it will fails
		this.socketInput.closeInput();
		this.socketInput.closeSocket();
		this.clientGameSet.notifyConnectionFail();
	}
}
