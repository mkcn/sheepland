package share.game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * MapHandler contai and handle the information about the map of the game
 * 
 * @author andrea
 * 
 */
public class MapHandler implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Node> mapNode;

	/**
	 * Constructor, crwate a list where will be stored the nodes of the map
	 */
	public MapHandler() {
		this.mapNode = new ArrayList<Node>();
	}

	/**
	 * return the node in the map with the same id as the givan id else return
	 * null
	 * 
	 * @param id
	 * @return
	 */
	public Node getNodeById(int id) {
		for (Node x : this.mapNode) {
			if (x.getId() == id) {
				return x;
			}
		}
		return null;
	}

	/**
	 * Return the list containing the node
	 * 
	 * @return
	 */
	public List<Node> getMapNode() {
		return this.mapNode;
	}

	/**
	 * Set the map
	 * 
	 * @param mapNode
	 */
	public void setMap(List<Node> mapNode) {
		this.mapNode = mapNode;

	}

	/**
	 * Return the size of the map
	 * 
	 * @return
	 */
	public int getMapSize() {
		return this.mapNode.size();
	}

	/**
	 * Return the node near the position, with at least a sheep
	 * 
	 * @return
	 */
	public List<Field> nearNodeWithSheep(NumberedSpace position) {
		List<Field> list = new ArrayList<Field>();
		for (Field x : this.deleteNumberedSpaceFromNearNode(position)) {
			if (x.isThereASheep()) {
				list.add(x);
			}
		}

		return list;
	}

	/**
	 * Return the list of field where is possible create a new sheep
	 * 
	 * @param position
	 * @return
	 */
	public List<Field> fieldCanLamb(NumberedSpace position) {
		List<Field> list = new ArrayList<Field>();
		for (Field x : this.deleteNumberedSpaceFromNearNode(position)) {
			if (x.canLamb1()) {
				list.add(x);
			}
		}
		return list;
	}

	/**
	 * return the field where is locate the blacksheep
	 * 
	 * @return
	 */
	public Field fieldWithBlackSheep() {
		for (Field x : this.deleteNumberedSpaceFromNearNode(null)) {
			if (x.isBlackSheepHere() != -1) {
				return x;
			}
		}
		return null;
	}

	/**
	 * return an ArrayList of the node near the givenNode
	 * 
	 * @param givenNode
	 * @return
	 */
	public List<Node> getNearNode(int id) {
		for (Node x : this.mapNode) {
			if (x.getId() == id) {
				return x.nearNode();
			}
		}

		return null;
	}

	/**
	 * Return all the Field of a given node, but if the value of the given node
	 * is null return all the Field of the map
	 * 
	 * @param n
	 * @return
	 */
	public List<Field> deleteNumberedSpaceFromNearNode(Node n) {
		List<Field> returnList = new ArrayList<Field>();

		if (n == null) {
			for (Node x : this.mapNode) {
				if (x.getClass() == Field.class) {
					returnList.add((Field) x);
				}
			}

		} else {
			for (Node x : n.nearNode()) {
				if (x.getClass() == Field.class) {
					returnList.add((Field) x);
				}
			}
		}

		return returnList;
	}

	/**
	 * Return all the Numbered Space of a given node, but if the value of the
	 * given node is null return all the Numbered Space in this map
	 * 
	 * @param n
	 * @return
	 */
	public List<NumberedSpace> deleteFieldFroNearNode(Node n) {

		List<NumberedSpace> returnList = new ArrayList<NumberedSpace>();

		if (n == null) {
			for (Node x : this.mapNode) {
				if (x.getClass() == NumberedSpace.class) {
					returnList.add((NumberedSpace) x);
				}
			}

		} else {

			for (Node x : n.nearNode()) {
				if (x.getClass() == NumberedSpace.class) {
					returnList.add((NumberedSpace) x);
				}
			}
		}

		return returnList;
	}

	/**
	 * Return a list of node that can be reached, for example numbered space
	 * without fence or not taken
	 * 
	 * @param takenSpace
	 * @return
	 * @throws NoReachableNode
	 */
	public List<NumberedSpace> getAllReachableNode() throws NoReachableNode {

		List<NumberedSpace> reachableNode = new ArrayList<NumberedSpace>();
		for (NumberedSpace x : this.deleteFieldFroNearNode(null)) {
			if (isReachable(x)) {
				reachableNode.add((NumberedSpace) x);
			}
		}

		if (reachableNode.size() == 0) {
			throw new NoReachableNode();
		} else {
			return reachableNode;
		}

	}

	/**
	 * Check if the node is a numbered space, if there is not a fence and if is
	 * not taken
	 * 
	 * @param givenNode
	 * @return
	 */
	private boolean isReachable(NumberedSpace givenNode) {

		return givenNode.getClass() == NumberedSpace.class
				&& !givenNode.isFence() && givenNode.getSHepherd() == null;

	}

	/**
	 * Return the fence number, but it does not count the final fence
	 * 
	 * @return
	 */
	public int getFenceNumber() {

		int fenceNumber = 0;

		for (NumberedSpace x : deleteFieldFroNearNode(null)) {
			if (x.isFence() && !x.isFinalFence()) {
				fenceNumber++;
			}
		}

		return fenceNumber;
	}

	/**
	 * return the number of fence around a field
	 * 
	 * @param fieldToCheck
	 * @return
	 */
	public int getFenceNumber(Field fieldToCheck) {

		int fenceNumber = 0;

		for (NumberedSpace x : this.deleteFieldFroNearNode(fieldToCheck)) {
			if (x.isFence()) {
				fenceNumber++;
			}
		}
		return fenceNumber;
	}

	/**
	 * return the field where is loated the wolf
	 * 
	 * @return
	 */
	public Field fieldWithWolf() {
		for (Field x : this.deleteNumberedSpaceFromNearNode(null)) {
			if (x.isWolf()) {
				return x;
			}
		}
		return null;
	}

	/**
	 * Return a boolean value, true if the given position is near sheepsburg
	 * else return false
	 * 
	 * @param positioOnMapn
	 * @return
	 */
	public boolean isNearShepsBurg(NumberedSpace positioOnMapn) {
		for (Field x : this.deleteNumberedSpaceFromNearNode(positioOnMapn)) {
			if (x.getType() == TypeField.SHEEPSBURG) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the first position without shepherd or fence
	 * 
	 * @return
	 */
	public NumberedSpace getandomFreePosition() {
		for (NumberedSpace x : this.deleteFieldFroNearNode(null)) {
			if (!x.isFence() && x.getSHepherd() == null) {
				return x;
			}
		}
		return null;
	}

	/**
	 * Extend exception, should be used when there are no more numbered space to
	 * reach
	 */
	public class NoReachableNode extends Exception implements Serializable {

		private static final long serialVersionUID = 1L;

		public NoReachableNode() {
			super();
		}

		public NoReachableNode(String s) {
			super(s);
		}

	}

	/**
	 * Return a list of field near a position where it is possible to kill
	 * 
	 * @param position
	 * @return
	 */
	public List<Field> nearNodeCanKill(NumberedSpace position) {
		List<Field> listCanKill = new ArrayList<Field>();
		for (Field x : this.deleteNumberedSpaceFromNearNode(position)) {
			if (x.canKill()) {
				listCanKill.add(x);
			}
		}
		return listCanKill;
	}

}
