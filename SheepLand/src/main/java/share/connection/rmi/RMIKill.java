package share.connection.rmi;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import server.view.ServerCLC;
import share.connection.ConnectionConstants;

import com.esotericsoftware.minlog.Log;

/**
 * class to close RMI
 * 
 * @author mirko conti
 * 
 */
public class RMIKill {

	/**
	 * unbind and close the registry of RMI
	 * 
	 * @param registry
	 */
	public RMIKill(Registry registry) {
		try {
			// Unregister ourself
			registry.unbind(ConnectionConstants.RMI_ID);
			// Unexport; this will also remove us from the RMI runtime
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (Exception e) {
			Log.debug("RMIKill", e);
			ServerCLC.showMsg("RMI", " killed");
		}
	}
}
