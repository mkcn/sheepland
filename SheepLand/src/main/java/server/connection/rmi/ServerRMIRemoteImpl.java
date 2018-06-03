package server.connection.rmi;

import java.awt.EventQueue;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.connection.ServerReceiver;
import server.game.ServerGameSetting;
import server.game.ServerPlayer;
import share.connection.RemoteSendInterface;

/**
 * class to handle the message from client to server on RMI
 * 
 * @author mirko conti
 * 
 */
public class ServerRMIRemoteImpl extends UnicastRemoteObject implements
		RemoteSendInterface {

	private static final long serialVersionUID = 1L;
	private ServerGameSetting serverGameSet;
	private ServerPlayer gamer;

	/**
	 * 
	 * @param gameServer
	 * @param ID
	 * @throws RemoteException
	 */
	protected ServerRMIRemoteImpl(ServerGameSetting serverGameSet,
			ServerPlayer gamer) throws RemoteException {
		super();
		this.gamer = gamer;
		this.serverGameSet = serverGameSet;
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendObj(Object obj) throws RemoteException {
		// call a thread on receive so the called (client) don't blocks
		EventQueue.invokeLater(new ServerReceiver(this.serverGameSet,
				this.gamer, obj));
	}

	/**
	 * ping called by client with RMI
	 */
	public boolean ping() throws RemoteException {
		return true;
	}
}
