package client.connection.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;

import share.connection.ConnectionConstants;
import share.connection.RemoteSendInterface;
import share.connection.rmi.RMIKill;
import share.connection.rmi.RMIRemoteInitInterface;
import share.game.comunication.ConnectionProtocol;
import client.connection.ClienConnectionAbstract;
import client.connection.ConnectionExceptionGeneric;
import client.game.ClientGameSetting;

import com.esotericsoftware.minlog.Log;

/**
 * class that start and lookup the registry of RMI
 * 
 * @author mirko conti
 * 
 */
public class ClientRMI extends ClienConnectionAbstract implements Runnable {

	private Registry registry;
	private RMIRemoteInitInterface remoteServer;
	private ExecutorService executeTimeout;
	private String ip;
	private int port;

	/**
	 * constructor of ClientRMI
	 * 
	 * @param clientGame
	 *            the game to start after the connection
	 * @param graphicLogin
	 *            the object to handle for input and output
	 */
	public ClientRMI(ClientGameSetting clientGameSet, String ip, int port,
			ExecutorService executeTimeout) {
		super(clientGameSet);
		this.ip = ip;
		this.port = port;
		this.executeTimeout = executeTimeout;
	}

	/**
	 * initialize RMI and sent the RemoteCallInterface to the server so the
	 * server can send msg to the client
	 * 
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void run() {
		try {
			System.setProperty("sun.rmi.transport.tcp.responseTimeout", "2000");
			System.setProperty("sun.rmi.transport.tcp.readTimeout", "2000");
			this.registry = LocateRegistry.getRegistry(this.ip, this.port);
			// get the remote class
			this.remoteServer = (RMIRemoteInitInterface) this.registry
					.lookup(ConnectionConstants.RMI_ID);
			// the lookup is successful so stop the timer to check the
			// connection
			this.executeTimeout.shutdownNow();
			// create the class implemetation where the server will call the
			// methods
			super.askUsrPass();
		} catch (RemoteException e) {
			killClientRMI();
			Log.debug("startClientRMI RemoteException", e);
		} catch (NotBoundException e1) {
			killClientRMI();
			Log.debug("startClientRMI NotBoundException", e1);
		}
	}

	/**
	 * sent to server a tmp ursname e pass and the server replay in different
	 * ways
	 * 
	 * @param socketMethod
	 * @throws RemoteException
	 * 
	 */
	@Override
	public void setUsrnameAndPassword(String usrname, String pass)
			throws ConnectionExceptionGeneric {
		try {
			RemoteSendInterface client = new ClientRMIImpl(super.clientGameSet);
			ConnectionProtocol answer = this.remoteServer
					.setClientConfiguration(client, usrname, pass);

			if (answer.getReply().equals(ConnectionProtocol.NEW_GAMER)) {
				this.remoteServer.askConnectionSettingNewPlayer();
			} else if (answer.getReply().equals(
					ConnectionProtocol.OLD_GAME_RESUME)) {
				this.remoteServer.ackConnectionSettingOldGameResume();
			}

			// handle the different answer
			if (super.handleAnswer(answer.getReply(), usrname)) {
				createSenderReceiverInterfacesAndPing(answer.getReply().equals(
						ConnectionProtocol.OLD_GAME_RESUME));
			}

		} catch (Exception e) {
			Log.debug("setUsrnameAndPassword", e);
		}
	}

	/**
	 * get the interface to send msg and ping and then send to the game
	 * 
	 * @throws RemoteException
	 */
	private void createSenderReceiverInterfacesAndPing(boolean comeBack)
			throws RemoteException {
		RemoteSendInterface remoteServerInterface = this.remoteServer
				.getServerSendInterface(comeBack);
		// set the interface used to call
		this.clientGameSet.createGame(remoteServerInterface);
		// THE THREAD OF PING
		super.startThreadPing(remoteServerInterface);
	}

	/**
	 * if you need to stop RMI
	 */
	public void killClientRMI() {
		new RMIKill(this.registry);
	}

}
