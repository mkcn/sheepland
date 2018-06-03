package share.game.move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Possible move that a player can do
 * 
 * @author andrea
 * 
 */
public enum MoveOption {
	MOVESHEEP, MOVESHEPHERD, BUYFIELD, KILL, COUPLING;

	/**
	 * Return a list with all the possible move opion
	 * 
	 * @return
	 */
	public static List<MoveOption> getAllOption() {
		return new ArrayList<MoveOption>(Arrays.asList(MOVESHEEP, MOVESHEPHERD,
				BUYFIELD, KILL, COUPLING));
	}

	public static MoveOption returnOptionByIndex(int index) {
		if (MOVESHEEP.ordinal() == index) {
			return MOVESHEEP;
		} else if (MOVESHEPHERD.ordinal() == index) {
			return MOVESHEPHERD;
		} else if (KILL.ordinal() == index) {
			return KILL;
		} else if (BUYFIELD.ordinal() == index) {
			return BUYFIELD;
		} else {
			return COUPLING;
		}
	}

}
