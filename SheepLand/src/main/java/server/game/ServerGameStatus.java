package server.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import share.game.model.Deck;
import share.game.model.MapHandler;
import share.game.model.Player;

/**
 * Contain all the information about the game, such as the number of player, the
 * status of the game, which player has to move.
 * 
 * @author andrea bertarini
 * 
 */
public class ServerGameStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int MAX_FENCE = 20;
	private List<ServerPlayer> gamers;
	private MapHandler gameGraph;
	private Deck cards;
	private int usedFence, playerIndex;
	private boolean alive, paused, sendMap, sync;
	private boolean endGame;
	private int playerNumber;
	private ServerRandomValue randomValue;
	private boolean isPlaying;

	/**
	 * Constructor
	 */
	public ServerGameStatus() {
		this.gamers = new ArrayList<ServerPlayer>();
		this.gameGraph = new MapHandler();
		this.cards = new Deck();
		this.playerIndex = 0;
		this.usedFence = 0;
		this.alive = true;
		this.paused = false;
		this.sync = true;
		this.sendMap = false;
		this.playerNumber = 0;
		this.randomValue = new ServerRandomValue();
		this.isPlaying = false;
	}

	/**
	 * Return true if a player is playing
	 * 
	 * @return
	 */
	public synchronized boolean isPlaying() {
		return this.isPlaying;
	}

	/**
	 * Set isPlaying to true or false, true should be used when a player start
	 * his move or his turn, false when a playerend his move
	 * 
	 * @param isPlaying
	 */
	public synchronized void setIsPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	/**
	 * Return a server random value objrct
	 * 
	 * @return
	 */
	public ServerRandomValue getRandomValue() {
		return this.randomValue;
	}

	/**
	 * Return a boolean value, true if the game is end,else false. The game is
	 * end when all the normal fence are placed and the actual player is the
	 * last
	 * 
	 * @return
	 */
	public boolean isGameEnd() {

		return this.endGame;
	}

	/**
	 * Set to synchronized the actual player, and if all the player alive are
	 * synchronized, set the game status to sync
	 */
	public synchronized void syncActualPlayer() {
		this.getActualPlayer().setSync();
		setSync();
	}

	/**
	 * Set true to sync and isPlaying if all the player are sync
	 */
	private synchronized void setSync() {
		for (ServerPlayer x : this.gamers) {
			if (!x.isSync()) {
				this.sync = true;
				return;
			}
		}
		this.sync = false;
		this.isPlaying = true;
		return;
	}

	/**
	 * Retur true if exist a player that has not positioned his shepherd and is
	 * disconnected
	 * 
	 * @return
	 */
	public synchronized boolean existPlayerNotAliveNotSync() {
		for (ServerPlayer x : this.gamers) {
			if (!x.isAlive() && !x.isSync()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Set sync to false, is used when a player come back but he did not sync
	 * during the start of the game
	 */
	public synchronized void deSync() {
		this.sync = false;
	}

	/**
	 * Return the sync state of this game
	 * 
	 * @return
	 */
	public synchronized boolean getSync() {
		return this.sync;
	}

	/**
	 * Return the id of the player is playing now
	 * 
	 * @return
	 */
	public int getPlayerIndex() {
		return this.playerIndex % this.gamers.size();
	}

	/**
	 * Inc the player index
	 */
	public void incPlayerIndex() {
		this.playerIndex++;
	}

	/**
	 * Dec the player index
	 */
	public void decPlayerIndex() {
		this.playerIndex--;
	}

	/**
	 * Set the player index
	 * 
	 * @param newPlayerIndex
	 */
	public void setPlayerIndex(int newPlayerIndex) {
		this.playerIndex = newPlayerIndex;
	}

	/**
	 * return the value of send map
	 * 
	 * @return
	 */
	public synchronized boolean getSendMap() {
		return this.sendMap;
	}

	/**
	 * Set the value of send map. Send map is true if one or more player
	 * resurrect, else is false
	 * 
	 * @param sendMap
	 */
	public synchronized void setSendMap(boolean sendMap) {
		this.sendMap = sendMap;
	}

	/**
	 * Return the actual player
	 * 
	 * @return
	 */
	public ServerPlayer getActualPlayer() {
		return this.gamers.get(this.playerIndex % this.gamers.size());
	}

	public ServerPlayer getNextPlayer() {
		return this.gamers.get((this.playerIndex + 1) % this.gamers.size());
	}

	/**
	 * Add a new given player to this game and set his id
	 * 
	 * @param newpl
	 */
	public void addPlayer(ServerPlayer newpl) {
		this.gamers.add(newpl);
		newpl.setId(this.playerNumber);
		this.playerNumber = this.playerNumber + 1;
	}

	/**
	 * Increment the number of used fence, and if the fence are more than 20
	 * this function return a boo
	 * 
	 * @return
	 */
	public synchronized void incUsedFence() {
		this.usedFence++;
		if (this.usedFence == MAX_FENCE) {
			this.endGame = true;
			this.alive = true;
		}
	}

	/**
	 * Return a list with all the player in this game
	 * 
	 * @return
	 */
	public List<ServerPlayer> getPlayers() {
		return this.gamers;
	}

	/**
	 * Return the map handler for this game
	 * 
	 * @return
	 */
	public MapHandler getGameGraph() {
		return this.gameGraph;
	}

	/**
	 * Return the deck with the non-bought card
	 * 
	 * @return
	 */
	public Deck getCards() {
		return this.cards;
	}

	/**
	 * Return a boolean value, true if the player is connect to this game, else
	 * false
	 * 
	 * @return
	 */
	public synchronized boolean isAlive() {
		return this.alive;
	}

	/**
	 * Return the number of the player in this game
	 * 
	 * @return
	 */
	public int getNumberPlayer() {
		return this.gamers.size();
	}

	/**
	 * 
	 */
	public synchronized void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * Set the deck, with field card of this game
	 * 
	 * @param cards
	 */
	public void setCards(Deck cards) {
		this.cards = cards;
	}

	/**
	 * Set the map of this game
	 * 
	 * @param gamegGraph
	 */
	public void setGameGraph(MapHandler gamegGraph) {
		this.gameGraph = gamegGraph;
	}

	/**
	 * Return a list of server player with out the actual player
	 * 
	 * @return
	 */
	public List<ServerPlayer> getOtherGamers() {
		// creating a list where add the player
		List<ServerPlayer> returnList = new ArrayList<ServerPlayer>();

		// for every player in this game
		for (ServerPlayer x : this.gamers) {
			// check if is not equals to the actual player
			if (!x.equalsServerPlayer(this.getActualPlayer())) {
				// if is it true add the player to the list
				returnList.add(x);
			}
		}
		// finally return the list
		return returnList;
	}

	/**
	 * @return the paused
	 */
	public synchronized boolean isPaused() {
		return this.paused;
	}

	/**
	 * @param paused
	 *            the paused to set
	 */
	public synchronized void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * Return a list of player, that can be use by the client
	 * 
	 * @return
	 */
	public List<Player> getPlayerForClient() {

		List<Player> players = new ArrayList<Player>();
		for (ServerPlayer x : this.gamers) {
			players.add(x.getAsPlayer());

		}
		return players;
	}

	/**
	 * Return a list of player, create from the list of serverplayer in this
	 * game
	 * 
	 * @param pl
	 * @return
	 */
	public ServerPlayer getAsServerPlayer(Player pl) {
		for (ServerPlayer x : this.gamers) {
			if (x.getUsrname() == pl.getUsrname()) {
				return x;
			}
		}
		return null;
	}

	/**
	 * 
	 * @return true if all players are alive
	 */
	public boolean checkAllAlive() {
		for (ServerPlayer i : this.gamers) {
			if (!i.isAlive()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @return true the number of player alive
	 */
	public int numberPlayerAlive() {
		int number = 0;
		for (ServerPlayer i : this.gamers) {
			if (!i.isAlive()) {
				number = number + 1;
			}
		}
		return number;
	}

	/**
	 * Return true if the actual player is the last player else false
	 * 
	 * @return
	 */
	public boolean lastPlayer() {
		return this.getActualPlayer().getId() == this.playerNumber - 1;
	}
}
