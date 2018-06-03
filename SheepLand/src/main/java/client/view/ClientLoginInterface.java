package client.view;

import client.connection.ClienConnectionAbstract;

/**
 * interface to interact with user and setting the game
 * 
 * @author mirko conti
 * 
 */
public interface ClientLoginInterface {

	/**
	 * ask to the graphic if use RMI or Socket
	 * 
	 * @param clientStarter
	 */
	void chooseConnection();

	/**
	 * ask to the graphic to insert usrname and password
	 * 
	 * @param initializeConnection
	 */
	void insertUsrnameAndPassword(
			ClienConnectionAbstract initializeConnection);

	/**
	 * use to show all the kind if message
	 * 
	 * @param title
	 * @param msg
	 */
	void showMsg(String title, String msg);

	/**
	 * all the setting is done and the game have to start so it hide the gui
	 * 
	 * @param show
	 */
	void showGui(boolean show);
}
