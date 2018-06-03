package client.view.clc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import share.connection.ConnectionConstants;
import client.connection.ClienConnectionAbstract;
import client.connection.ClientStartConn;
import client.view.ClientLoginInterface;

import com.esotericsoftware.minlog.Log;

/**
 * class that implement with the CLC the stuff about graphic login
 * 
 * @author mirko conti
 * 
 */
public class ClientCLCLoginImpl implements ClientLoginInterface {

	private ClientCLCOutput output;
	private ClientStartConn clientStartConn;

	/**
	 * constructor of ClientCLCLoginImpl, where initialize the class to handle
	 * the output
	 */
	public ClientCLCLoginImpl(ClientStartConn clientStartConn) {
		this.output = new ClientCLCOutput();
		this.clientStartConn = clientStartConn;
	}

	/**
	 * this is the only return direct to the caller (clientStarter)
	 * 
	 * @return
	 */
	public boolean askIfactivateGui() {
		return this.output.getInputYesNo("Do you want to activate the GUI?");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param clientGame
	 */
	public void chooseConnection() {
		List<String> arrayList = new ArrayList<String>(Arrays.asList("RMI",
				"Socket"));
		try {
			String ip = null;
			int port = 0;
			boolean activeRMI;
			if (Log.DEBUG) {
				ip = ConnectionConstants.IPLOCAL;
				port = 12500;
				activeRMI = false;
			} else {
				ip = this.output.getInputString("Insert IP");
				port = this.output.getInputInt("Insert PORT");
				activeRMI = this.output.showQuestionAndOptions(
						"Which type of comunication you want?", arrayList) == 0;
			}
			this.output.showMsg("", "wait..");
			if (activeRMI) {
				this.clientStartConn.initializeRmi(ip, port, this);
			} else {
				this.clientStartConn.initializeSocket(ip, port, this);
			}

		} catch (Exception e) {
			Log.debug("chooseConnection", e);
			this.output.showMsgErr("Client chooseConnection",
					"error initialization connection!");
			chooseConnection();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertUsrnameAndPassword(
			ClienConnectionAbstract initializeConnection) {
		try {
			String usr = this.output.getInputString("Insert usrname");
			String pass = this.output.getInputString("Insert password");
			initializeConnection.setUsrnameAndPassword(usr, pass);
		} catch (Exception e) {
			Log.debug("insertUsrnameAndPassword", e);
			this.output.showMsgErr("insert usrname and password",
					"error send data!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void showMsg(String title, String msg) {
		this.output.showMsg(title, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	public void showGui(boolean show) {
		if (show) {
			this.output.showMsg("reiserisc", "dati");
		}
	}
}
