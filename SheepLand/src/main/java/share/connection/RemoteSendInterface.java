package share.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * interface to interact with the remote server or client
 * 
 * @author mirko conti
 * 
 */
public interface RemoteSendInterface extends Remote {

	/**
	 * send an object to the remote
	 * 
	 * @param obj
	 *            serializable object
	 * @throws RemoteException
	 */
	void sendObj(Object obj) throws RemoteException;

	/**
	 * check the connection
	 * 
	 * @return
	 * @throws RemoteException
	 */
	boolean ping() throws RemoteException;

}
