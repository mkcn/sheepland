package server.game;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.connection.ServerConnConstants;
import server.view.ServerCLC;
import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.comunication.Message;
import share.game.comunication.Request;
import share.game.comunication.RequestType;

import com.esotericsoftware.minlog.Log;

/**
 * class between the logic of connection and the game
 * 
 * @author mirko conti
 * 
 */
public class ServerGameSetting {

	private ServerGame serverGame;
	private ServerGameStatus serverGameStatus;
	private ServerGameSender serverGameSender;
	private ExecutorService executeTimer;

	/**
	 * class called only by connection classes, create and handle the gameStatus
	 * and serverGame
	 */
	public ServerGameSetting() {
		this.serverGameStatus = new ServerGameStatus();
		this.serverGameSender = new ServerGameSender(this.serverGameStatus);
		this.serverGame = new ServerGame(this.serverGameStatus,
				this.serverGameSender);
	}

	/**
	 * return the status of the game with all the details
	 * 
	 * @return
	 */
	public ServerGameStatus getServerGameStatus() {
		return this.serverGameStatus;
	}

	/**
	 * return the game
	 * 
	 * @return
	 */
	public ServerGame getServerGame() {
		return this.serverGame;
	}

	/**
	 * ok, there is enogh players or time out
	 * 
	 * @param indexGame
	 * @throws RemoteException
	 */
	public void startGame() {
		// create a notificate msg
		try {
			Request msgStartGame = new Request(-1, RequestType.STARTGAME, null);
			this.serverGameSender.serverToEveryClient(msgStartGame, null, true);
			this.serverGame.firstOfAll();
		} catch (Exception e) {
			Log.error("startGame", e);
		}
	}

	/**
	 * a new player is added to this game and an info is sent to every one
	 * 
	 * @param player
	 * @return
	 * 
	 */
	public synchronized boolean addPlayer(ServerPlayer player) {
		this.serverGameStatus.addPlayer(player);
		Information info = new Information(-1, InformationType.NEWPLAYER, null);
		info.setInformation(player.getAsPlayer());
		// to every one the name of new player
		this.serverGameSender.serverToEveryClient(info, null, true);
		return true;
	}

	/**
	 * called by thread ping if client disconnect set the gamer not alive and
	 * then send msg to everyone to notificate it
	 * 
	 * @param player
	 */
	public void alertPlayerDisconnected(ServerPlayer player) {
		player.setDead();
		List<ServerPlayer> players = this.serverGameStatus.getPlayers();
		for (int i = 0; i < players.size(); i++) {
			if (player.getUsrname().equals(players.get(i).getUsrname())) {
				players.get(i).setDead();
			}
		}
		// only if game is still alive
		if (this.serverGameStatus.isAlive()) {
			this.serverGameStatus.setPaused(true);
			Information info = new Information(0,
					InformationType.PLAYERDISCONNECT, player.getAsPlayer());
			this.serverGameSender.serverToEveryClient(info, player, true);
			startTimeoutPlayerDisconnected();
		}
	}

	/**
	 * check if a player that was disconnected, is now come back to the game
	 * 
	 * @param player
	 * @return
	 */
	public synchronized boolean playerComeBack(ServerPlayer player) {
		List<ServerPlayer> players = this.serverGameStatus.getPlayers();
		// double check for resume a player
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getUsrname().equals(player.getUsrname())
					&& !players.get(i).isAlive()) {

				players.get(i)
						.setClientRemoteAndAlive(player.getClientRemote());
				players.get(i).setAlive(true);
				players.get(i).setCanPlay(true);
				player.setId(players.get(i).getId());

				// notify every one that
				Information info = new Information(0,
						InformationType.PLAYERCOMEBACK, players.get(i)
								.getAsPlayer());
				this.serverGameSender.serverToEveryClient(info, players.get(i),
						true);

				// stop the timer that start the game
				if (this.executeTimer != null
						&& !this.executeTimer.isShutdown()) {
					this.executeTimer.shutdownNow();
				}
				if (this.serverGameStatus.isPaused()
						&& this.serverGameStatus.checkAllAlive()) {
					// PLAYER CAME BACK WHEN GAME IS IN PAUSE
					this.serverGameStatus.setPaused(false);
				}
				// PLAYER CAME BACK WHEN GAME ISN'T IN PAUSE
				this.serverGame.canIplay(players.get(i));
				return true;
			}
		}
		// return to the connection part "no, it's a new player"
		return false;
	}

	/**
	 * timer to restart the game if the player disconnected don't come back
	 * before of n sec
	 */
	public void startTimeoutPlayerDisconnected() {
		if (this.executeTimer != null && !this.executeTimer.isShutdown()) {
			this.executeTimer.shutdownNow();
		}
		this.executeTimer = Executors.newCachedThreadPool();
		this.executeTimer.execute(new Runnable() {
			public void run() {
				try {
					// timer
					Thread.sleep(ServerConnConstants.SEC_TIMEOUT_COME_BACK * 1000);
					// next move call by timer
					// PLAYER CAME BACK WHEN GAME IS IN PAUSE
					ServerGameSetting.this.serverGameStatus.setPaused(false);
					ServerGameSetting.this.serverGame.whenTimerStop();
				} catch (InterruptedException e) {
					// Timer Stoped
					Log.debug("server timer stoped", e);
				}
			}
		});
	}

	/**
	 * it's arrived a message from the client
	 * 
	 */
	public void clientToServer(ServerPlayer gamer, Object obj) {
		ServerCLC.showMsg(gamer.getUsrname(), obj.toString());
		this.serverGame.handleMessage((Message) obj);
	}

}
