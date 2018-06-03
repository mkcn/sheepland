package share.game.move;

/**
 * class with all the type of retur from the graphic
 * 
 * @author mirko conti
 * 
 */
public class ReturnMoveChoice {
	public static final char NUMERED_SPACE = 'S', FIELD = 'F', PLAYER = 'P',
			MOVE_TYPE = 'M', FIELD_CARD = 'C', INPUT_BOX = 'I';

	// the INPUT_BOX usually dont return because it's a ok msg
	// but can return if the message have different choices

	/**
	 * private constructor ReturnMoveChoice to override the public one
	 */
	private ReturnMoveChoice() {
	}
}
