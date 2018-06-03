package client.view;

import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.NumberedSpace;
import share.game.model.Player;
import share.game.model.Shepherd;
import share.game.move.ReturnMoveChoice;

import com.esotericsoftware.minlog.Log;

/**
 * class to convert the index from Logic to GUI and reconvert it from GUI to
 * Logic
 * 
 * @author mirko conti
 * 
 */
public class ClientConvertIndex {

	/**
	 * convert the index from server to the correspondenced index of array in
	 * the gui
	 * 
	 * @param index
	 * @param type
	 * @return
	 */
	public int convertIndex(Object type) {
		if (type instanceof Field) {
			// index >= 1 && index <= 19
			return ((Field) type).getId() - 1;
		} else if (type instanceof NumberedSpace) {
			// index >= 101 && index <= 142
			return ((NumberedSpace) type).getId() - 101;
		} else if (type instanceof Shepherd) {
			return ((Shepherd) type).getCode();
		} else if (type instanceof FieldCard) {
			return ((FieldCard) type).getCardType().ordinal();
		} else if (type instanceof Player) {
			return ((Player) type).getId();
		} else {
			Log.error("convertIndex");
			return 0;
		}
	}

	/**
	 * convert the return of a choice of gui to the index used by login on
	 * server
	 * 
	 * @param index
	 * @param type
	 * @return
	 */
	public int convertIndexReturn(int index, Object type) {
		Log.debug("convertIndexReturn", "(" + index + ")");
		if (type.equals(ReturnMoveChoice.FIELD)) {
			// index >= 0 && index <= 18
			return index + 1;
		} else if (type.equals(ReturnMoveChoice.NUMERED_SPACE)) {
			// index >= 0 && index <= 41
			return index + 101;
		} else {
			return index;
		}
	}

}
