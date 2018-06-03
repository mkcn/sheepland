package share.game.move;

public abstract class Move {

	private boolean isEnd;

	/**
	 * Constructor
	 */
	public Move() {
		this.isEnd = false;
	}

	/**
	 * Return a boolean value, true if this move is end, else false
	 * 
	 * @return
	 */
	public boolean isEnd() {
		return this.isEnd;
	}

	/**
	 * Set end to true
	 */
	public void setEnd() {
		this.isEnd = true;
	}

}
