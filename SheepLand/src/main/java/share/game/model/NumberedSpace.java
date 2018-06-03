package share.game.model;

/**
 * Extends node, contain information about a numbered space like its value or if
 * it is taken by a shepherd or there is a fence
 * 
 * @author andrea
 * 
 */
public class NumberedSpace extends Node {

	private static final long serialVersionUID = 1L;
	private int value;
	private boolean fence, finalFence;
	private Shepherd shepherd;

	/**
	 * Constructor, crate a numbered space with the given value and id
	 * 
	 * @param id
	 * @param valore
	 */
	public NumberedSpace(int id, int valore) {
		super(id);
		this.value = valore;
		this.fence = false;
		this.finalFence = false;
		this.shepherd = null;
	}

	/**
	 * place a shepherd on this numbered space
	 * 
	 * @param shepherd
	 */
	public void setShepherd(Shepherd shepherd) {
		this.shepherd = shepherd;
	}

	/**
	 * return the shepherd on this space
	 * 
	 * @return
	 */
	public Shepherd getSHepherd() {
		return this.shepherd;
	}

	/**
	 * check if a fence is placed here
	 * 
	 * @return
	 */
	public boolean isFence() {
		return this.fence;
	}

	/**
	 * place a fence on this node
	 * 
	 * @param finalFence
	 */
	public void setFence(boolean finalFence) {
		this.fence = true;
		this.finalFence = finalFence;
	}

	/**
	 * return the value of this space
	 * 
	 * @return
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Convert into string this space
	 */
	public String toString() {
		String isRecinto = "";

		if (this.fence) {
			isRecinto = "There is a fence";
		}

		return "Numbered space: \n" + super.toString() + "Value: "
				+ Integer.toString(this.value) + "\n" + isRecinto;
	}

	/**
	 * check if a node with an id like the given one, is near this space
	 * 
	 * @param id
	 * @return
	 */
	public boolean isNear(int id) {
		for (int i = 0; i < this.nearNode().size(); i++) {
			if (this.nearNode().get(i).getId() == id) {
				if (this.nearNode().get(i).getClass() == NumberedSpace.class) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Compare two numbered space, return true if they are equals
	 * 
	 * @param x
	 * @return
	 */
	public boolean equalsNumberedSpace(NumberedSpace x) {
		return super.getId() == x.getId() && this.value == x.value;
	}

	/**
	 * Return a boolean value, true if in this space is place a fence
	 * 
	 * @return
	 */
	public boolean isFinalFence() {
		return finalFence;
	}

}
