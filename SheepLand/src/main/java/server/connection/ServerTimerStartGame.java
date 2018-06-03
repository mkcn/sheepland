package server.connection;

import com.esotericsoftware.minlog.Log;

/**
 * class to handle the timeout and start the game with less of 4 players
 * 
 * @author mirko conti
 * 
 */
public class ServerTimerStartGame implements Runnable {

	private int milliSeconds;
	private ServerControllerGame serverControllerGame;

	/**
	 * constructor of ServerTimerStartGame
	 * 
	 * @param serverSetConnection
	 *            the class with the handle of setting game
	 */
	public ServerTimerStartGame(ServerControllerGame serverSetConnection) {
		this.serverControllerGame = serverSetConnection;
		this.milliSeconds = ServerConnConstants.SEC_TIMEOUT_START_GAME * 1000;
	}

	/**
	 * start a sleep and if it isn't closed start the game
	 */
	public void run() {
		try {
			// start timer
			Thread.sleep(this.milliSeconds);
			this.serverControllerGame.startGame();
		} catch (InterruptedException e) {
			// Timer Stoped
			Log.debug("server timer stoped", e);
		}
	}
}
