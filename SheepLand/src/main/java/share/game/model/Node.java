package share.game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author andrea
 *
 */
public abstract class Node implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private List<Node> nearNode;

	/**
	 * constructor
	 * 
	 * @param id
	 */
	public Node(int id) {
		this.id = id;
		this.nearNode = new ArrayList<Node>();
	}

	/**
	 * Return the id of this object
	 * 
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * connect this node to an other
	 * 
	 * @param newNearNode
	 */
	public void insertNewNearNode(Node newNearNode) {
		this.nearNode.add(newNearNode);
	}

	/**
	 * Convert into a string this node
	 */
	public String toString() {
		return "Code" + this.id + "\n";
	}

	/**
	 * Return an array list containing the near nodes
	 * 
	 * @return
	 */
	public List<Node> nearNode() {
		return this.nearNode;
	}

}
