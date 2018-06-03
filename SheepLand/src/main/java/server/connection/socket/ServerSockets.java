package server.connection.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import server.connection.ServerPort;
import server.view.ServerCLC;
import share.connection.ConnectionConstants;

import com.esotericsoftware.minlog.Log;

/**
 * class that handle the accept of new connection of type socket
 * 
 * @author mirko
 * 
 */
public class ServerSockets implements Runnable {

	private ServerSocket serverSocket;
	private ExecutorService executor;

	/**
	 * initialize the thread-handler and the class for the handle of games
	 */
	public ServerSockets(ExecutorService executor) {
		this.executor = executor;
	}

	/**
	 * until the connection go down , accept clients and lunch a thread for each
	 * of them
	 */
	public void run() {
		this.serverSocket = initializeServer();
		ServerCLC.showMsg("socket",
				"started on port " + this.serverSocket.getLocalPort());
		while (true) {
			try {
				// stay in wait for a connection
				Socket socket = this.serverSocket.accept();
				// start thread that initialize client
				ServerSocketsEcho serverSocketsEcho = new ServerSocketsEcho();
				serverSocketsEcho.setSocket(socket);
				this.executor.execute(serverSocketsEcho);
			} catch (IOException e) {
				// go here if the serverSocket closes
				Log.debug("ServerSockets", e);
				break;
			}
		}
		killSocketWay();
	}

	/**
	 * to stop the receive of clients
	 */
	public void killSocketWay() {
		try {
			if (!this.serverSocket.isClosed()) {
				this.serverSocket.close();
			}
		} catch (IOException e) {
			Log.debug("killSocketWay", e);
		}
		this.executor.shutdown();
	}

	/**
	 * get a free port and return a serverSocket worked
	 * 
	 * @return
	 */
	private ServerSocket initializeServer() {
		ServerPort ipPort = new ServerPort(ConnectionConstants.SOCKET_PORT);
		// while a port is chosen
		while (true) {
			try {
				return new ServerSocket(ipPort.gerPort());
			} catch (IOException e) {
				// port isn't free
				ipPort.setOtherRandomPort();
				ServerCLC.showMsgErr("Initialize socket",
						"port " + ipPort.gerPort() + " not available");
				Log.debug("initializeServer", e);
			}
		}
	}
}
