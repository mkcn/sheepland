package client.view.gui;

import java.io.IOException;
import java.rmi.NotBoundException;

import javax.swing.SwingUtilities;

import client.connection.ClienConnectionAbstract;
import client.connection.ClientStartConn;
import client.view.ClientLoginInterface;

import com.esotericsoftware.minlog.Log;

/**
 * class to interact with the gui of login
 * 
 * @author mirko conti
 * 
 */
public class ClientGUILoginImpl implements ClientLoginInterface {

	private ClientStartConn clientStartConn;
	private ClientGUILogin clientGUILogin;
	private ClienConnectionAbstract initializeConnection;

	/**
	 * create the gui and show it
	 */
	public ClientGUILoginImpl(ClientStartConn clientStartConn) {
		this.clientStartConn = clientStartConn;
		this.clientGUILogin = new ClientGUILogin(this);
		SwingUtilities.invokeLater(this.clientGUILogin);
	}

	/**
	 * after show the gui if call this method set enable the choice of
	 * connection, exemple to reconnect after the connection fall
	 */
	public void chooseConnection() {
		this.clientGUILogin.resetConn();
	}

	/**
	 * called by graphic gui login
	 * 
	 * @param rmi
	 * @throws NotBoundException
	 * @throws IOException
	 */

	public void chooseConnectionReturn(boolean rmi, String ip, int port) {
		if (rmi) {
			this.clientStartConn.initializeRmi(ip, port, this);
		} else {
			this.clientStartConn.initializeSocket(ip, port, this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertUsrnameAndPassword(
			ClienConnectionAbstract initializeConnection) {
		this.initializeConnection = initializeConnection;
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertUsrnameAndPasswordReturn(String usr, String pass) {
		try {
			this.initializeConnection.setUsrnameAndPassword(usr, pass);
		} catch (Exception e) {
			Log.debug("insertUsrnameAndPasswordReturn", e);
			this.clientGUILogin.showMsg("error send data!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void showMsg(String title, String msg) {
		this.clientGUILogin.showMsg(title + " - " + msg);
		this.clientGUILogin.enableLoading(false);
	}

	/**
	 * {@inheritDoc}
	 */
	public void showGui(boolean show) {
		this.clientGUILogin.showForm(show);
	}

}
