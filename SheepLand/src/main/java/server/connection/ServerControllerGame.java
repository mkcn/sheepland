package server.connection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.game.ServerGameSetting;
import server.game.ServerPlayer;
import server.view.ServerCLC;
import share.connection.ConnectionConstants;

import com.esotericsoftware.minlog.Log;

/**
 * class to start game, start timer, start ping
 * 
 * @author mirko conti
 * 
 */
public class ServerControllerGame {

	private ExecutorService executor, executorTimer;
	private ServerGameSetting gameSet;
	private ServerPlayer serverPlayer;
	ServerSettingConnection serverSetConnection;

	/**
	 * constructor where from ServerSettingConnection you take all the class you
	 * need
	 * 
	 */
	public ServerControllerGame(ServerSettingConnection serverSetConnection) {
		this.executor = Executors.newSingleThreadExecutor();
		this.executorTimer = Executors.newSingleThreadExecutor();
		this.serverSetConnection = serverSetConnection;
		this.gameSet = serverSetConnection.getGameSetting();
		this.serverPlayer = serverSetConnection.getTmpPlayer();
	}

	/**
	 * start a timer for each game that start game after n seconds
	 */
	public void startTimerStartGame() {
		try {
			if (this.gameSet.getServerGameStatus().getPlayers().size() == ConnectionConstants.NUM_PLAYERS_MIN) {
				this.executorTimer.submit(new ServerTimerStartGame(this));
			}
		} catch (Exception e) {
			Log.debug("startTimerStartGame", e);
			ServerCLC.showMsg("StartTimerStartGame", e.toString());
		}
	}

	/**
	 * start thread that each x second send method ping and check if client is
	 * alive not exit ref to this class but with ping if client disconnet it's
	 */
	public void startPingConnection() {
		ServerPingConnection threadPing = new ServerPingConnection(
				this.gameSet, this.serverPlayer);
		this.executor.submit(threadPing);
	}

	/**
	 * if we reach 4° player the timer have to stop
	 */
	public void stopTimer() {
		// it's check that the timer is already dead
		if (!this.executorTimer.isShutdown()) {
			this.executorTimer.shutdownNow();
			this.executorTimer = Executors.newSingleThreadExecutor();
		}
	}

	/**
	 * all the condition are satisfied so the game start
	 */
	public void startGame() {
		this.gameSet.startGame();
		this.serverSetConnection.createNewGame();
	}

	/**
	 * 
	 * where time a player is added check if start the game
	 * 
	 * @return if the game is started
	 */
	public boolean checkIfStartGame() {
		if (this.gameSet.getServerGameStatus().getPlayers().size() == ConnectionConstants.NUM_PLAYERS) {
			stopTimer();
			// if the player is come back not have need to restart the game
			startGame();
			return true;
		}
		return false;
	}

}
