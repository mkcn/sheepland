package client.connection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import share.connection.RemoteSendInterface;
import share.game.comunication.ConnectionProtocol;
import client.game.ClientGameSetting;
import client.view.ClientLoginInterface;

/**
 * class to set the data about connection
 * 
 * @author mirko conti
 * 
 */
public abstract class ClienConnectionAbstract {

	protected ClientGameSetting clientGameSet;
	private ClientLoginInterface graphicLogin;

	/**
	 * constructor of InitializeConnection
	 * 
	 * @param clientGameSet
	 *            class of setting
	 */
	public ClienConnectionAbstract(ClientGameSetting clientGameSet) {
		this.clientGameSet = clientGameSet;
		this.graphicLogin = clientGameSet.getClientLogin();
	}

	/**
	 * the inferface replay here, with the a name and a password this is
	 * implemeted in socket or RMI and then sent to server and wait for the
	 * answer
	 * 
	 * 
	 * @param usr
	 *            username
	 * @param pass
	 *            password
	 * @throws Exception
	 *             connection error
	 */
	public abstract void setUsrnameAndPassword(String usr, String pass)
			throws ConnectionExceptionGeneric;

	/**
	 * @param serverImpl
	 * 
	 */
	protected void startThreadPing(RemoteSendInterface serverImpl) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new ClientPingConnection(this.clientGameSet,
				serverImpl));
	}

	/**
	 * ask to the CLC or to the GUI to insent a name and a password to keep in
	 * mind
	 * 
	 */
	protected void askUsrPass() {
		// this is the class where the graphic replays
		this.graphicLogin.insertUsrnameAndPassword(this);
	}

	/**
	 * server answer in 3 way , error user with pass or that user is playing or
	 * there is a game to resume or welcame new player.
	 */
	protected boolean handleAnswer(String answer, String usrname) {
		// show the result
		this.graphicLogin.showMsg("answer", answer);
		if (answer.equals(ConnectionProtocol.NEW_GAMER)
				|| answer.equals(ConnectionProtocol.OLD_GAME_RESUME)) {
			loginDoneSetUsrname(usrname);
			return true;
		} else {
			// fail so restart all the task
			askUsrPass();
			return false;
		}
	}

	/**
	 * the setting is done and you can go on
	 * 
	 * @param ursname
	 */
	protected void loginDoneSetUsrname(String ursname) {
		// save the name just for graphic effect
		this.clientGameSet.loginDoneOpenGame(ursname);
	}

}
