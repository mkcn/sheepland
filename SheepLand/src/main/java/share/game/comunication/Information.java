package share.game.comunication;

import share.game.model.Player;
import share.game.model.Shepherd;

public class Information extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InformationType infoType;
	private Object information;
	private Object information2;
	private Shepherd actualShepherd;

	/**
	 * Return the shepherd of this turn if set, else null
	 * 
	 * @return shepherd
	 */
	public Shepherd getActualShepherd() {
		return this.actualShepherd;
	}

	/**
	 * ????
	 * 
	 * @param actualShepherd
	 */
	public void setActualShepherd(Shepherd actualShepherd) {
		this.actualShepherd = actualShepherd;
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param infoType
	 * @param player
	 */
	public Information(int id, InformationType infoType, Player player) {
		super(id, player);
		this.infoType = infoType;
	}

	/**
	 * Set to the second information field the given object
	 * 
	 * @param information2
	 */
	public void setSecondInformation(Object information2) {
		this.information2 = information2;
	}

	/**
	 * Return the informatin store in the second information field
	 * 
	 * @return
	 */
	public Object getSecondInformation() {
		return this.information2;
	}

	/**
	 * Set an object as information in first field information
	 * 
	 * @param newInfo
	 */
	public void setInformation(Object newInfo) {
		this.information = newInfo;
	}

	/**
	 * Return the informtion stored in this message
	 * 
	 * @return
	 */
	public Object getInformation() {
		return this.information;
	}

	/**
	 * Return the type of this information message
	 * 
	 * @return
	 */
	public InformationType getInfoType() {
		return this.infoType;
	}

}
