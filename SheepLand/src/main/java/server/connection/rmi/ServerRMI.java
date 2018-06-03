package server.connection.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.connection.ServerPort;
import server.view.ServerCLC;
import share.connection.ConnectionConstants;
import share.connection.rmi.RMIKill;
import share.connection.rmi.RMIRemoteInitInterface;

import com.esotericsoftware.minlog.Log;

/**
 * class to start the RMI on server
 * 
 * @author mirko conti
 * 
 */
public class ServerRMI {

	private Registry registry;

	/**
	 * start RMI on the server
	 * 
	 * @param commonPart
	 *            the place where the games are started
	 */
	public ServerRMI() {
		this.registry = initializeRmi();
		try {
			RMIRemoteInitInterface remoteImpl = new ServerRMIInitImpl();

			this.registry.bind(ConnectionConstants.RMI_ID, remoteImpl);
			ServerCLC.showMsg("RMI   ", "started on port "
					+ ConnectionConstants.RMI_PORT);
		} catch (RemoteException e) {
			ServerCLC.showMsgErr("RMI RemoteException", "connection fail");
			Log.debug("RemoteException", e);
		} catch (AlreadyBoundException e) {
			ServerCLC.showMsgErr("RMI AlreadyBoundException", "bind fail");
			Log.debug("AlreadyBoundException", e);
		}
	}

	/**
	 * check the port and take other one if it isnt free
	 * 
	 * @return the registry
	 */
	private Registry initializeRmi() {
		ServerPort ipPort = new ServerPort(ConnectionConstants.RMI_PORT);
		// while a port is chosen
		while (true) {
			try {
				return LocateRegistry.createRegistry(ipPort.gerPort());
			} catch (RemoteException e) {
				// port isn't free
				ipPort.setOtherRandomPort();
				ServerCLC.showMsgErr("Initialize RMI   ",
						"port " + ipPort.gerPort() + " not available");
				Log.debug("initializeRmi", e);
			}
		}
	}

	/**
	 * to stop to receive clients
	 */
	public void killServerRMI() {
		new RMIKill(this.registry);
	}

}
