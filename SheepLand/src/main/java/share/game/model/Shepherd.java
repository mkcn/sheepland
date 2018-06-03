package share.game.model;

import java.io.Serializable;

/**
 * Contain the information about a shepherd such as the position or the code
 * 
 * @author andrea bertarini
 * 
 */
public class Shepherd implements Serializable {

	private static final long serialVersionUID = 1L;
	private NumberedSpace positionOnMap;
	private int code;
	private boolean canPlay;

	/**
	 * Constructor
	 * 
	 * @param positionOnMap
	 */
	public Shepherd(NumberedSpace positionOnMap) {
		this.positionOnMap = positionOnMap;
		canPlay = true;
	}

	/**
	 * Return the space where is placed this shepherd
	 * 
	 * @return
	 */
	public NumberedSpace getPositioOnMapn() {
		return this.positionOnMap;
	}

	/**
	 * Set the position of this shepherd
	 * 
	 * @param newposition
	 */
	public void setPositionOnMap(NumberedSpace newposition) {
		this.positionOnMap = newposition;
	}

	/**
	 * Set the identification code of this shepherd
	 * 
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Return the code of this shepherd
	 * 
	 * @return
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * Set the value of can play of this shepherd
	 * 
	 * @param canPlay
	 */
	public void setCanPlay(boolean canPlay) {
		this.canPlay = canPlay;
	}

	/**
	 * Return true if this shepherd can do something
	 * 
	 * @return
	 */
	public boolean isCanPlay() {
		return this.canPlay;
	}

}
