package share.game.comunication;

import share.game.model.Player;

/**
 * Request can be used to ask something, but can't be used to carry any kind of
 * information
 * 
 * @author andrea
 * 
 */
public class Request extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequestType reqType;

	/**
	 * Create request message
	 * 
	 * @param id
	 *            , of ths message
	 * @param reqType
	 *            , can be request type
	 * @param player
	 */
	public Request(int id, RequestType reqType, Player player) {
		super(id, player);
		this.reqType = reqType;
	}

	/**
	 * Retyrn the type of this request message
	 * 
	 * @return
	 */
	public RequestType getReqType() {
		return this.reqType;
	}

}
