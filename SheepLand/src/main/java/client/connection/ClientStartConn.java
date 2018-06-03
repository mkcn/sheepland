package client.connection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import share.connection.ConnectionConstants;
import client.connection.rmi.ClientRMI;
import client.connection.socket.ClientSockets;
import client.game.ClientGameSetting;
import client.view.ClientLoginInterface;

import com.esotericsoftware.minlog.Log;

/**
 * class to handle the wrong ip or port in setting of connection
 * 
 * @author mirko conti
 * 
 */
public class ClientStartConn {
	private ExecutorService executeConn, executeTimeout;
	private ClientGameSetting clientGameSet;

	/**
	 * 
	 * @param clientGameSet
	 *            main class to pass to the RMI or Socket
	 */
	public ClientStartConn(ClientGameSetting clientGameSet) {
		this.clientGameSet = clientGameSet;
	}

	/**
	 * check if port is enter the range of RMI ports and then try to start the
	 * RMI
	 * 
	 */
	public void initializeRmi(String ip, int port, ClientLoginInterface login) {
		if (port < ConnectionConstants.RMI_PORT
				|| port > ConnectionConstants.RMI_PORT
						+ ConnectionConstants.RANGE_PORT) {
			login.showMsg(
					"RMI range",
					ConnectionConstants.RMI_PORT
							+ "-"
							+ (ConnectionConstants.RMI_PORT + ConnectionConstants.RANGE_PORT));
			login.chooseConnection();
		} else {
			startConnectionTimeOut(login);
			this.executeConn.execute(new ClientRMI(this.clientGameSet, ip,
					port, this.executeTimeout));
		}
	}

	/**
	 * check if port is enter the range of socket ports and then try to start
	 * socket
	 * 
	 */
	public void initializeSocket(String ip, int port, ClientLoginInterface login) {

		if (port < ConnectionConstants.SOCKET_PORT
				|| port > ConnectionConstants.SOCKET_PORT
						+ ConnectionConstants.RANGE_PORT) {
			login.showMsg(
					"socket range",
					ConnectionConstants.SOCKET_PORT
							+ "-"
							+ (ConnectionConstants.SOCKET_PORT + ConnectionConstants.RANGE_PORT));
			login.chooseConnection();
		} else {
			startConnectionTimeOut(login);
			this.executeConn.execute(new ClientSockets(this.clientGameSet, ip,
					port, this.executeTimeout));
		}
	}

	/**
	 * start the timeout and give to it the interface to call for a new attempt
	 * 
	 * @param login
	 */
	private void startConnectionTimeOut(ClientLoginInterface login) {
		if (this.executeConn != null) {
			this.executeConn.shutdownNow();
			this.executeTimeout.shutdownNow();
		}
		this.executeConn = Executors.newCachedThreadPool();
		this.executeTimeout = Executors.newSingleThreadExecutor();
		TimeoutConnection timeoutConnection = new TimeoutConnection(login);
		this.executeTimeout.execute(timeoutConnection);
	}

	/**
	 * class to hadle the threaded timeout
	 * 
	 * @author mirko conti
	 * 
	 */
	class TimeoutConnection implements Runnable {
		private static final int TIMEOUT_CONNECTION = 5000;
		private ClientLoginInterface login;

		/**
		 * get the login where retry to get the credential
		 * 
		 * @param login
		 * @param startConn
		 */
		public TimeoutConnection(ClientLoginInterface login) {
			this.login = login;
		}

		/**
		 * stay in wait and if it's not stoped call again the login
		 */
		public void run() {
			try {
				Thread.sleep(TIMEOUT_CONNECTION);
				this.login.showMsg("Time out", "wrong ip or port");
				this.login.chooseConnection();
			} catch (InterruptedException e) {
				Log.debug("TimeoutConnection", e);
				this.login.showMsg("Connection", "established!");
			}

		}

	}

}
