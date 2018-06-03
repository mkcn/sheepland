package server.connection.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.connection.ServerConnConstants;
import server.connection.ServerControllerGame;
import server.connection.ServerSettingConnection;
import server.view.ServerCLC;
import share.connection.RemoteSendInterface;
import share.connection.socket.SocketInput;
import share.connection.socket.SocketOutput;
import share.game.comunication.ConnectionProtocol;

import com.esotericsoftware.minlog.Log;

/**
 * class start like a thread and set the connection with between a client and
 * the server using socket
 * 
 * @author mirko conti
 * 
 */
public class ServerSocketsEcho implements Runnable {

	private ServerSettingConnection commonPart = null;
	private SocketInput socketInput;
	private SocketOutput socketOutput;

	/**
	 * constructor ServerSocketsEcho
	 * 
	 * @param sockMethod
	 *            class used to send and receive message
	 * @param commonPart
	 *            the class common to all the thread where you add the client
	 * @throws IOException
	 */
	public ServerSocketsEcho() {
		this.commonPart = ServerSettingConnection.getInstance();
	}

	/**
	 * set the socket of server echo and get the input and the output stream
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public void setSocket(Socket socket) throws IOException {
		this.socketOutput = new SocketOutput(socket);
		this.socketInput = new SocketInput(socket);
	}

	/**
	 * the runnable method to initialize a socket connection with the client
	 * 
	 */
	public void run() {
		try {
			// all stuff about username and password
			// initialize class that the game class use to send objs
			RemoteSendInterface remoteClient = new ServerSocketsSendImpl(
					this.socketOutput);
			waitForCredential(remoteClient);
		} catch (Exception e) {
			Log.debug("ServerSocketsEcho", e);
			ServerCLC.showMsgErr("connection!", "error initialize");
		}
	}

	/**
	 * stay in wait until string usr and pass arrived. it's use a mutex object
	 * to handle the case of concurrent connections if credialt are new then
	 * call on server the method of create new
	 * 
	 * @param remoteClient
	 */
	public void waitForCredential(RemoteSendInterface remoteClient) {
		String usr = this.socketInput.receiveString();
		String pass = this.socketInput.receiveString();
		boolean isCredentialOk = false;
		ConnectionProtocol checkAndSetCredentials;

		// common object
		synchronized (ServerConnConstants.MUTEX) {
			// if the asnwer is ok the client is already added to the game
			checkAndSetCredentials = this.commonPart.checkAndSetCredentials(
					remoteClient, usr, pass);

			this.socketOutput.sendObj(checkAndSetCredentials);

			if (checkAndSetCredentials.getReply().equals(
					ConnectionProtocol.NEW_GAMER)) {
				this.commonPart.addPlayerToActualGame();
				isCredentialOk = true;
				initializeSetClient(true);
			} else if (checkAndSetCredentials.getReply().equals(
					ConnectionProtocol.OLD_GAME_RESUME)) {
				this.commonPart.resumePlayerInOldGame();
				isCredentialOk = true;
				initializeSetClient(false);
			}
		}
		// while the anwer is positive
		if (!isCredentialOk) {
			waitForCredential(remoteClient);
		}
	}

	/**
	 * set all the stuff for connection and check if start the game
	 * 
	 */
	private void initializeSetClient(boolean checkStartGame) {

		ServerControllerGame serverControllerGame = new ServerControllerGame(
				this.commonPart);
		// check if start the game
		if (checkStartGame && !serverControllerGame.checkIfStartGame()) {
			serverControllerGame.startTimerStartGame();
		}
		// when receive msg form client then sent it to
		// the class game
		ExecutorService exsecutor = Executors.newSingleThreadExecutor();
		exsecutor.execute(new ServerSocketsReceive(this.commonPart
				.getGameSetting(), this.commonPart.getTmpPlayer(),
				this.socketInput));
	}
}