package share.game.model;

public enum SheepType {
	SHEEP, LAMB, RAM;

	/**
	 * Return a random type of sheep
	 * 
	 * @return, SHEEP, RAM, LAMB
	 */
	public static SheepType getRandomType() {

		int number = (int) (Math.random() * 4);

		if (number == 0) {
			return SHEEP;
		} else if (number == 1) {
			return LAMB;
		} else if (number == 2) {
			return RAM;
		} else {
			return SHEEP;
		}
	}

	/**
	 * Return one from SHEEP or RAM random
	 */
	public static SheepType getRandomTypeToUpgrade() {
		int number = (int) (Math.random() * 2);

		if (number == 0) {
			return SHEEP;
		} else if (number == 1) {
			return RAM;
		} else
			return SHEEP;

	}
}
