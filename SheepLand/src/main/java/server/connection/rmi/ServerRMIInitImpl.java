package server.connection.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.connection.ServerConnConstants;
import server.connection.ServerControllerGame;
import server.connection.ServerSettingConnection;
import share.connection.RemoteSendInterface;
import share.connection.rmi.RMIRemoteInitInterface;
import share.game.comunication.ConnectionProtocol;

/**
 * class to halde the initialize the connection of a new client
 * 
 * @author mirko conti
 * 
 */
public class ServerRMIInitImpl extends UnicastRemoteObject implements
		RMIRemoteInitInterface {

	private static final long serialVersionUID = 1L;
	ServerSettingConnection commonPart = null;

	/**
	 * 
	 * @param commonPart
	 *            is the class that hadle the global list of games
	 * @throws RemoteException
	 */
	public ServerRMIInitImpl() throws RemoteException {
		super();
		this.commonPart = ServerSettingConnection.getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	public ConnectionProtocol setClientConfiguration(
			RemoteSendInterface remoteClient, String usr, String pass)
			throws RemoteException {

		synchronized (ServerConnConstants.MUTEX) {
			ConnectionProtocol checkAndSetCredentials = this.commonPart
					.checkAndSetCredentials(remoteClient, usr, pass);
			return checkAndSetCredentials;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void askConnectionSettingNewPlayer() {
		this.commonPart.addPlayerToActualGame();
	}

	/**
	 * {@inheritDoc}
	 */
	public void ackConnectionSettingOldGameResume() {
		this.commonPart.resumePlayerInOldGame();
	}

	/**
	 * {@inheritDoc}
	 */
	public RemoteSendInterface getServerSendInterface(boolean comeBack)
			throws RemoteException {
		ServerControllerGame serverControllerGame = new ServerControllerGame(
				this.commonPart);
		if (!comeBack && !serverControllerGame.checkIfStartGame()) {
			serverControllerGame.startTimerStartGame();
		}
		serverControllerGame.startPingConnection();
		return new ServerRMIRemoteImpl(this.commonPart.getGameSetting(),
				this.commonPart.getTmpPlayer());
	}
}
