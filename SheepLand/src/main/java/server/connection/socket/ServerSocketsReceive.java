package server.connection.socket;

import server.game.ServerGameSetting;
import server.game.ServerPlayer;
import share.connection.socket.SocketInput;

/**
 * class start like a thread that receive with a loop the message from a client
 * 
 * @author mirko conti
 * 
 */
public class ServerSocketsReceive implements Runnable {

	private ServerGameSetting gameSet;
	private ServerPlayer gamer;
	private SocketInput socketInput;

	/**
	 * constructor
	 * 
	 * @param gameSet
	 *            , where to send the message
	 * @param gamer
	 *            , from who arrive a message
	 * @param socketMethod
	 *            , what receive e message
	 */
	public ServerSocketsReceive(ServerGameSetting gameSet, ServerPlayer gamer,
			SocketInput socketInput) {
		this.gamer = gamer;
		this.gameSet = gameSet;
		this.socketInput = socketInput;
	}

	/**
	 * stay in wait until a msg arrives and then loop
	 */
	public void run() {
		while (true) {
			Object data = this.socketInput.receiveObj();
			// receive null means that the connections is close or damage
			if (data == null) {
				this.gameSet.alertPlayerDisconnected(this.gamer);
				break;
			}
			this.gameSet.clientToServer(this.gamer, data);
		}
		this.socketInput.closeInput();
	}
}
