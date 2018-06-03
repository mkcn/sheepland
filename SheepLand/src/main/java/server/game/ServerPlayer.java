package server.game;

import java.util.ArrayList;
import java.util.List;

import share.connection.RemoteSendInterface;
import share.game.model.FieldCard;
import share.game.model.Player;
import share.game.model.TypeField;

public class ServerPlayer extends Player {

	private static final long serialVersionUID = 1L;
	private RemoteSendInterface clientRemote;
	private boolean sync;
	private String password;
	private List<FieldCard> playerCards;

	private boolean canPlay;

	/**
	 * Return true if the player can play, for example a player can play untill
	 * has money or he can made a move
	 * 
	 * @return
	 */
	public boolean isCanPlay() {
		if (super.getMoney() > 0) {
			return this.canPlay;
		} else {
			return false;
		}
	}

	/**
	 * Set the value of can play, for exmple can be used when the player can not
	 * do any move
	 * 
	 * @param canPlay
	 */
	public void setCanPlay(boolean canPlay) {
		this.canPlay = canPlay;
	}

	/**
	 * Constructor, create a new server player
	 * 
	 * @param clientRemote
	 * @param usrname
	 * @param password
	 */
	public ServerPlayer(RemoteSendInterface clientRemote, String usrname,
			String password) {

		super(usrname);
		this.clientRemote = clientRemote;
		this.playerCards = new ArrayList<FieldCard>();
		this.sync = false;
		this.password = password;
		this.canPlay = true;
	}

	/**
	 * 
	 * Return the password of this player
	 * 
	 * @return
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * 
	 * Return the sync state of this player A player must be synchronized, when
	 * he has not chose where place his shepherd
	 * 
	 * @return
	 */
	public boolean isSync() {
		return this.sync;
	}

	/**
	 * Set to sync or not the state of this player. A player must be
	 * synchronized, when he has not chose where place his shepherd
	 * 
	 * @param sync
	 */
	public void setSync() {
		this.sync = true;
	}

	/**
	 * add a card to player' s deck
	 * 
	 * @param newCard
	 */
	public void addPlayerCard(FieldCard newCard) {
		this.playerCards.add(newCard);
	}

	/**
	 * Return the card owned by this player
	 * 
	 * @return
	 */
	public List<FieldCard> getCards() {
		return this.playerCards;
	}

	/**
	 * Set the remote interface use to send information to the client
	 * 
	 * @param clientRemote
	 */
	public void setClientRemoteAndAlive(RemoteSendInterface clientRemote) {
		this.clientRemote = clientRemote;
		super.setAlive(true);
	}

	/**
	 * Set dead this player (alive = false), should be used when the player
	 * disconnects, set to null the remote interface
	 */
	public void setDead() {
		super.setAlive(false);
		this.clientRemote = null;
	}

	/**
	 * Return the interface used to send information to client
	 * 
	 * @return
	 */
	public RemoteSendInterface getClientRemote() {
		return this.clientRemote;
	}

	/**
	 * Compare two player and if they are the same player return true, alse
	 * false
	 * 
	 * @param playerToCompare
	 * @return
	 */
	public boolean equalsServerPlayer(ServerPlayer playerToCompare) {
		return super.getUsrname().equals(playerToCompare.getUsrname())
				&& this.password.equals(playerToCompare.getPassword());
	}

	/**
	 * Create a player(super-class), from the attributes of this server player
	 */
	public Player getAsPlayer() {
		Player player = new Player(super.getUsrname());
		player.setAllShepherd(super.getAllShepher());
		player.setMoney(super.getMoney());
		player.setId(super.getId());
		player.setFinalScore(super.getFinalScore());
		return player;
	}

	/**
	 * Return the number of card of a specified type for this player
	 * 
	 * @param type
	 * @return
	 */
	public int getNumberCard(TypeField type) {
		int i = 0;
		for (FieldCard x : this.playerCards) {
			if (x.getCardType() == type) {
				i = i + 1;
			}
		}
		return i;
	}

}
