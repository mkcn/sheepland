package client.connection.socket;

import java.rmi.RemoteException;

import share.connection.RemoteSendInterface;
import share.connection.socket.SocketOutput;

/**
 * {@inheritDoc}
 * 
 * @author mirko conti
 * 
 */
public class ClientSocketsSend implements RemoteSendInterface {

	private SocketOutput socketOutput;

	/**
	 * {@inheritDoc}
	 * 
	 * @param socketOutput
	 */
	public ClientSocketsSend(SocketOutput socketOutput) {
		this.socketOutput = socketOutput;
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendObj(Object obj) throws RemoteException {
		this.socketOutput.sendObj(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean ping() throws RemoteException {
		// this.socketOutput.sendObj(date)
		return true;
	}

}
