package share.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends node, contain information about the sheep and the wolf A field has a
 * type, this type is related to TypeField (enum)
 * 
 */
public class Field extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TypeField type;
	private boolean wolf;

	private List<GenericSheep> sheep;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            , this will be the id of the field
	 * @param type
	 *            , this is the field type, such as wood, mountain...
	 */
	public Field(int id, TypeField type) {
		super(id);
		this.type = type;
		this.sheep = new ArrayList<GenericSheep>();

	}

	/**
	 * Return all the sheep in this field
	 * 
	 * @return
	 */
	public List<GenericSheep> getSheep() {
		return this.sheep;
	}

	/**
	 * Convert into a string
	 */
	public String toString() {
		String ishere = "";
		String ishere2 = "";

		if (isBlackSheepHere() != -1) {
			ishere = "Black sheep is here";
		}

		if (isWolf()) {
			ishere2 = "Wolf is here";
		}

		return "Field: \n" + super.toString() + "Type: " + this.type.toString()
				+ "\n" + "Sheep number: " + this.sheep.size() + "\n" + ishere
				+ "\n" + ishere2;
	}

	/**
	 * Add a sheep to the field
	 * 
	 * @param newSheep
	 *            , this will be the sheep added
	 */
	public void addShep(GenericSheep newSheep) {
		this.sheep.add(newSheep);
	}

	/**
	 * Return the number of the sheep in this field, without the black sheep
	 * 
	 * @return
	 */
	public int numberOfSheepWithOutBlackSheep() {
		int number = this.sheep.size();
		if (isBlackSheepHere() != -1) {
			number = number - 1;
		}
		return number;
	}

	/**
	 * Remove a non black sheep from this field
	 * 
	 * @return
	 */
	public GenericSheep removeSheep() {
		int i = 0;
		boolean find = false;
		for (; i < this.sheep.size(); i++) {
			if (this.sheep.get(i) instanceof GenericSheep) {
				find = true;
				break;
			}
		}

		if (find) {
			GenericSheep gs = this.sheep.get(i);
			this.sheep.remove(i);
			return gs;
		} else {
			return null;
		}
	}

	/**
	 * Remove the black sheep
	 * 
	 * @return
	 */
	public BlackSheep removeBlackSheep() {
		int i = 0;
		for (; i < this.sheep.size(); i++) {
			if (this.sheep.get(i) instanceof BlackSheep) {
				break;
			}
		}

		GenericSheep gs = this.sheep.get(i);
		this.sheep.remove(i);
		return (BlackSheep) gs;
	}

	/**
	 * Remove a given sheep from this field
	 * 
	 * @param id
	 *            , this is the sheep id
	 * @return
	 */
	public GenericSheep removeSheep(int id) {

		int i;
		GenericSheep sheepToRemove = null;
		for (i = 0; i < this.sheep.size(); i++) {
			if (this.sheep.get(i).getId() == id) {
				sheepToRemove = this.sheep.get(i);
				break;
			}
		}
		this.sheep.remove(i);
		return sheepToRemove;
	}

	/**
	 * return the type of the field
	 * 
	 * @return
	 */
	public TypeField getType() {
		return this.type;
	}

	/**
	 * Return the black sheep id, if it is in this field else return -1
	 * 
	 * @return
	 */
	public int isBlackSheepHere() {

		for (GenericSheep x : this.sheep) {
			if (x.getClass() == BlackSheep.class) {
				return x.getId();
			}
		}
		return -1;
	}

	/**
	 * Check if there are at least two sheep in this field
	 * 
	 * @return
	 */
	public boolean canLamb1() {
		int sheepNumber = 0;
		for (GenericSheep x : this.sheep) {
			if (x.getSheepType() == SheepType.SHEEP) {
				sheepNumber++;
			}
		}
		return sheepNumber > 1 ? true : false;
	}



	/**
	 * check if in this field there is at least a sheep
	 * 
	 * @return
	 */
	public boolean isThereASheep() {
		if (this.sheep.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * check if the given node id is one of the near node id
	 * 
	 * @param id
	 * @return
	 */
	public boolean isNear(int id) {

		for (Node x : nearNode()) {
			if (x.getId() == id && x instanceof Field) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if this field and an other are the same field, controlling the id
	 * and the type
	 * 
	 * @param x
	 * @return
	 */
	public boolean equalsField(Field x) {
		return super.getId() == x.getId() && this.type == x.type;
	}

	/**
	 * check if there is the wolf
	 * 
	 * @return
	 */
	public boolean isWolf() {
		return this.wolf;
	}

	/**
	 * Set the wolf: true, there is false, there is not
	 * 
	 * @param wolf
	 */
	public void setWolf(boolean wolf) {
		this.wolf = wolf;
	}

	public boolean canKill() {
		return (this.getSheep().size() == 1 && this.isBlackSheepHere() == -1) ||
				(this.getSheep().size() > 1);
	}
}
