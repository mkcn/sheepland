package share.game.comunication;

import share.game.model.Player;

/**
 * call to hadle all the message for the log in
 * 
 * @author mirko conti
 * 
 */
public class ConnectionProtocol extends Message {

	private static final long serialVersionUID = 1L;

	public static final String URS_EXISTING_PASS_WRONG = "Password wrong";
	public static final String OLD_GAME_RESUME = "Old game resume";
	public static final String NEW_GAMER = "Welcome new gamer";
	public static final String GAMER_IS_PLAYING = "This gamer is playing now";
	public static final String OK = "ok";
	public static final String EXIT = "exit";

	private String reply;
	private int objectInfo;

	/**
	 * 
	 * @param id
	 * @param player
	 *            that is trying to made the log in
	 */
	public ConnectionProtocol(int id, Player player) {
		super(id, player);
	}

	/**
	 * @return the reply
	 */
	public String getReply() {
		return this.reply;
	}

	/**
	 * @param reply
	 *            the reply to set
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}

	/**
	 * @return the objectInfo
	 */
	public int getObjectInfo() {
		return this.objectInfo;
	}

	/**
	 * @param objectInfo
	 *            the objectInfo to set
	 */
	public void setObjectInfo(int objectInfo) {
		this.objectInfo = objectInfo;
	}

}
