package share.connection.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import share.connection.RemoteSendInterface;
import share.game.comunication.ConnectionProtocol;

/**
 * interface use for setting the connection of RMI
 * 
 * @author mirko conti
 * 
 */
public interface RMIRemoteInitInterface extends Remote {

	/**
	 * the client sent to the server the interface where call the methods and
	 * the server replay with the confirm of credential
	 * 
	 * @param remoteClient
	 * @param id
	 * @param pass
	 * @return
	 * @throws RemoteException
	 */
	ConnectionProtocol setClientConfiguration(RemoteSendInterface remoteClient,
			String id, String pass) throws RemoteException;

	/**
	 * called by client if the aswer of credential is "new player"
	 */
	void askConnectionSettingNewPlayer() throws RemoteException;

	/**
	 * called by client if the aswer of credential is
	 * "resume a player in a old one"
	 */
	void ackConnectionSettingOldGameResume() throws RemoteException;

	/**
	 * used in connection of rmi , is called by client to get from server the
	 * remote interface where call the send and the ping. before it check if
	 * start game and start ping
	 * 
	 * @param comeBack
	 *            is true if the player is come back after a disconnection
	 * 
	 * @return
	 * @throws RemoteException
	 */
	RemoteSendInterface getServerSendInterface(boolean comeBack)
			throws RemoteException;
}
