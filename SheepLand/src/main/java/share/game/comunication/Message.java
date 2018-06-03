package share.game.comunication;

import java.io.Serializable;

import share.game.model.Player;

/**
 * Object of this class contain, information about the game, move request of
 * something ....
 * 
 * @author andrea bertarini
 * 
 */
public abstract class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int messageId;
	private Player player;

	/**
	 * Create a message with a given id
	 * 
	 * @param id
	 */
	public Message(int id, Player player) {
		this.messageId = id;
		this.player = player;
	}

	/**
	 * return the id of this message
	 * 
	 * @return
	 */
	public int getId() {
		return this.messageId;
	}

	/**
	 * Return the player associated to this message
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return this.player;
	}

}
