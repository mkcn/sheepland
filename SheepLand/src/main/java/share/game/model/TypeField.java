package share.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This enum is about the type of the field in the game
 * 
 * @author andrea
 * 
 */
public enum TypeField {
	WOOD, MOUNTAIN, DESERT, HILL, HAY, SWAMP, SHEEPSBURG;

	/**
	 * Return a list with all the possible field type
	 * 
	 * @return
	 */
	public static List<TypeField> getListWithType() {
		return new ArrayList<TypeField>(Arrays.asList(WOOD, MOUNTAIN, DESERT,
				HILL, HAY, SWAMP));

	}
}
