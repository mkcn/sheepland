package client.connection.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import share.connection.RemoteSendInterface;
import share.connection.socket.SocketInput;
import share.connection.socket.SocketOutput;
import share.game.comunication.ConnectionProtocol;
import client.connection.ClienConnectionAbstract;
import client.connection.ConnectionExceptionGeneric;
import client.game.ClientGameSetting;

import com.esotericsoftware.minlog.Log;

/**
 * class to create a socket connection with the server
 * 
 * @author mirko conti
 * 
 */
public class ClientSockets extends ClienConnectionAbstract implements
		Runnable {

	private ExecutorService executeTimeout;
	private SocketInput socketInput;
	private SocketOutput socketOutput;
	private String ip;
	private int port;

	/**
	 * constructor of ClientSockets
	 * 
	 * @param clientGameSet
	 *            the setting of the game to start
	 */
	public ClientSockets(ClientGameSetting clientGameSet, String ip, int port,
			ExecutorService executeTimeout) {
		super(clientGameSet);
		this.ip = ip;
		this.port = port;
		this.executeTimeout = executeTimeout;
	}

	/**
	 * connect socket, handle stream with class SocketMethod and start thread
	 * that receive msg from server
	 */
	public void run() {
		Socket socket = null;
		try {
			socket = new Socket(this.ip, this.port);
			this.socketOutput = new SocketOutput(socket);
			this.socketInput = new SocketInput(socket);
			// the connection Socket is successful so stop the timer to check it
			this.executeTimeout.shutdownNow();
			super.askUsrPass();
		} catch (IOException e) {
			Log.debug("run socket", e);
		}
	}

	/**
	 * sent to the server a null id or old one, server answer with the final id
	 * 
	 * @param socketMethod
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void setUsrnameAndPassword(String usrname, String pass)
			throws ConnectionExceptionGeneric {

		this.socketOutput.sendObj(usrname);
		this.socketOutput.sendObj(pass);

		ConnectionProtocol answer = (ConnectionProtocol) this.socketInput
				.receiveObj();

		// stay in wait for the string of relay from server
		if (super.handleAnswer(answer.getReply(), usrname)) {
			createSenderReceiverInterfacesAndPing();
		}
	}

	/**
	 * create the interface to send msg, create obj that receive msg and then
	 * send to the game
	 */
	public void createSenderReceiverInterfacesAndPing() {
		// create obj that game use to send msg
		RemoteSendInterface socketToRemoteServer = new ClientSocketsSend(
				this.socketOutput);
		this.clientGameSet.createGame(socketToRemoteServer);

		// it's not necessay start as threat but like this nothing blocks in the
		// test
		ExecutorService executor = Executors.newCachedThreadPool();
		ClientSocketsReceive socketClientReceive = new ClientSocketsReceive(
				this.clientGameSet, this.socketInput);
		executor.execute(socketClientReceive);
	}
}
