package client;

import client.connection.ClientStartConn;
import client.game.ClientGameSetting;
import client.view.ClientLoginInterface;
import client.view.clc.ClientCLCGameImpl;
import client.view.clc.ClientCLCLoginImpl;
import client.view.gui.ClientGUIGameImpl;
import client.view.gui.ClientGUILoginImpl;

/**
 * class starter of client
 * 
 * @author mirko conti
 * 
 */
public class ClientStarter {

	private ClientGameSetting clientGameSet;
	private ClientCLCLoginImpl login;
	private ClientStartConn clientStartConn;

	/**
	 * start the game and show input chooses initialize graphic e connection
	 */
	public void start() {
		this.clientGameSet = new ClientGameSetting();
		this.clientStartConn = new ClientStartConn(this.clientGameSet);
		this.login = new ClientCLCLoginImpl(this.clientStartConn);
		this.login.showMsg("", "--------Welcome to sheepland-------");
		chooseGraphic();
	}

	/**
	 * initialize with CLC and then ask if want start the GUI
	 * 
	 * @param gameClient
	 * @param id
	 */
	public void chooseGraphic() {
		if (!this.login.askIfactivateGui()) {
			this.clientGameSet.setGraphicLogin(this.login);
			this.clientGameSet.setGraphicGame(new ClientCLCGameImpl());
			this.login.chooseConnection();
		} else {
			ClientLoginInterface clientLogin = new ClientGUILoginImpl(
					this.clientStartConn);
			this.clientGameSet.setGraphicLogin(clientLogin);
			// load after the login show so it look like faster
			this.clientGameSet.setGraphicGame(new ClientGUIGameImpl());
		}
	}

}
