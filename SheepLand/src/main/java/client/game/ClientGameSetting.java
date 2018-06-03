package client.game;

import share.connection.RemoteSendInterface;
import share.game.comunication.Message;
import client.view.ClientGameAbstract;
import client.view.ClientLoginInterface;
import client.view.clc.ClientCLCGameImpl;
import client.view.gui.ClientGUIGameImpl;

import com.esotericsoftware.minlog.Log;

/**
 * class to handle all the stuff about the connection and the interaction with
 * it and the game
 * 
 * @author mirko conti
 * 
 */
public class ClientGameSetting {

	private ClientGameAbstract graphicGame;
	private ClientLoginInterface graphicLogin;
	private ClientGame clientGame;

	/**
	 * hide the login graphic and try to show the gui but if it's null (not
	 * loaded yet) wait and retry
	 * 
	 * @param ursname
	 *            save the name of user , just to graphic effect
	 */
	public void loginDoneOpenGame(String ursname) {
		this.graphicLogin.showGui(false);
		if (this.graphicGame != null) {
			this.graphicGame.showGame(true, ursname);
		} else {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Log.debug("showForm", e);
			}
			loginDoneOpenGame(ursname);
		}
	}

	/**
	 * create the ClientGame and pass to it the inteface of server
	 * 
	 * @param remoteServerInterface
	 */
	public void createGame(RemoteSendInterface remoteServerInterface) {
		// fist start of client game
		this.clientGame = new ClientGame(remoteServerInterface);
		this.graphicGame.setClientGame(this.clientGame);
		this.clientGame.setGrafic(this.graphicGame);
	}

	/**
	 * all the message from the server arrive here
	 * 
	 * @param obj
	 */
	public void serverToClient(Object obj) {
		if (obj instanceof String) {
			Log.error("Receive string -", obj.toString());
		} else if (obj instanceof Message) {
			this.clientGame.handleMessage((Message) obj);
		} else {
			Log.error("receive other -" + obj.toString());
		}
	}

	/**
	 * if receive a null msg or the ping fail RMI || Socket call this method and
	 * then close theirselves
	 */
	public void notifyConnectionFail() {
		this.graphicLogin.showGui(true);
		this.graphicLogin.chooseConnection();
		this.graphicLogin.showMsg("connection ", "interrupted");
		this.graphicGame.showGame(false, null);
		if (this.graphicGame instanceof ClientGUIGameImpl) {
			// reset the graphic of GUI
			this.graphicGame = new ClientGUIGameImpl();
		} else {
			// reset the graphic of CLC
			this.graphicGame = new ClientCLCGameImpl();
		}
	}

	/**
	 * @return the clientGraphic
	 */
	public ClientGameAbstract getClientGraphic() {
		return this.graphicGame;
	}

	/**
	 * @param graphicGame
	 *            the clientGraphic to set
	 */
	public void setGraphicGame(ClientGameAbstract graphicGame) {
		this.graphicGame = graphicGame;
	}

	/**
	 * @return the clientLogin
	 */
	public ClientLoginInterface getClientLogin() {
		return this.graphicLogin;
	}

	/**
	 * @param graphicLogin
	 *            the clientLogin to set
	 */
	public void setGraphicLogin(ClientLoginInterface graphicLogin) {
		this.graphicLogin = graphicLogin;
	}

}
