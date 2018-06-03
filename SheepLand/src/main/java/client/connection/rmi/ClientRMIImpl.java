package client.connection.rmi;

import java.awt.EventQueue;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import share.connection.RemoteSendInterface;
import client.connection.ClientReceive;
import client.game.ClientGameSetting;

/**
 * class that implement the remote calls from the server
 * 
 * @author mirko conti
 * 
 */
public class ClientRMIImpl extends UnicastRemoteObject implements
		RemoteSendInterface, Serializable {

	private static final long serialVersionUID = 1L;
	private ClientGameSetting clientGameSet;

	/**
	 * class implemented where server call the methods
	 * 
	 * @param clientGame
	 * @throws RemoteException
	 */
	public ClientRMIImpl(ClientGameSetting clientGame) throws RemoteException {
		super();
		this.clientGameSet = clientGame;
	}

	/**
	 * receive an obj from server and pass to the game
	 */
	public void sendObj(Object obj) throws RemoteException {
		// call the game setting with a thread for the swing
		EventQueue.invokeLater(new ClientReceive(this.clientGameSet, obj));
	}

	/**
	 * called by server with RMI
	 */
	public boolean ping() throws RemoteException {
		return true;
	}
}
