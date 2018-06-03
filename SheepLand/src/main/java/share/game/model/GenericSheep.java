package share.game.model;

import java.io.Serializable;

/**
 * This class is about a sheep, the sheep can be RAM, SHEEP or LAMB.
 * 
 * @author andrea
 * 
 */
public class GenericSheep implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int age;
	private SheepType sheepType;

	private static final int MAX_AGE = 3;
	private static final int MIN_AGE = 0;
	private static int ID_PROGRESS = 0;

	/**
	 * constructor
	 * 
	 * @param id
	 * @param sheepType
	 */
	public GenericSheep(SheepType sheepType) {
		this.id = ID_PROGRESS;
		this.sheepType = sheepType;
		setAge();
		ID_PROGRESS++;
	}

	private void setAge() {
		if (this.sheepType == SheepType.LAMB) {
			this.age = MIN_AGE;
		} else {
			this.age = MAX_AGE;
		}

	}

	/**
	 * Return the sheep type of this one, could be RAM, SHEEP or RAM
	 * 
	 * @return
	 */
	public SheepType getSheepType() {
		return this.sheepType;
	}

	/**
	 * Return the age of this sheep
	 * 
	 * @return
	 */
	public int getAge() {
		return this.age;
	}

	/**
	 * Sum 1 to the age of this sheep
	 */
	public void incAge() {
		if (this.age < MAX_AGE) {
			this.age++;
		}
	}

	/**
	 * Return the id of this sheep
	 * 
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Set the id of this sheep
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Check if this sheep can upgrade, LAM can upgrade to SHEEP or LAMB when
	 * they are at least 1 years old
	 * 
	 * @return
	 */
	public boolean canUpgradeToSheep() {
		return this.age > 1 ? true : false;
	}

	/**
	 * Upgrade this LAM to RAM or SHEEP random
	 */
	public void upgradeToSheep() {
		this.sheepType = SheepType.getRandomTypeToUpgrade();
	}

}
