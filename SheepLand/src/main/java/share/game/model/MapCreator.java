package share.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class create a map (field and space) and connect them in the right way
 * 
 * @author andrea bertarini
 * 
 */
public class MapCreator {

	private List<Node> map;
	private NumberedSpace startPos;

	/**
	 * Constructor, create a list where the nodes will be placed, but create
	 * also a numbered Space called start position, this space is starting point
	 * for very player
	 */
	public MapCreator() {
		this.map = new ArrayList<Node>();
		this.startPos = new NumberedSpace(0, 0);
	}

	/**
	 * Return the a list containing the map
	 * 
	 * @return
	 */
	public List<Node> getCreatedMap() {
		return this.map;
	}

	/**
	 * Return the space where every shepherd will be placed at the beginning of
	 * the game
	 * 
	 * @return
	 */
	public NumberedSpace getStartPos() {
		return this.startPos;
	}

	/**
	 * Connect the field and numbered space in the map
	 */
	public void connect() {
		this.connectNode(103, 104);
		this.connectNode(103, 110);

		this.connectNode(109, 110);
		this.connectNode(109, 116);

		this.connectNode(101, 105);
		this.connectNode(101, 106);

		this.connectNode(102, 106);
		this.connectNode(102, 107);

		this.connectNode(108, 113);
		this.connectNode(108, 114);

		this.connectNode(115, 114);
		this.connectNode(115, 120);

		this.connectNode(128, 125);
		this.connectNode(128, 133);

		this.connectNode(138, 133);
		this.connectNode(138, 137);

		this.connectNode(142, 136);
		this.connectNode(142, 140);

		this.connectNode(141, 140);
		this.connectNode(141, 145);

		this.connectNode(139, 134);
		this.connectNode(139, 130);

		this.connectNode(129, 130);
		this.connectNode(129, 126);

		this.connectNode(126, 121);
		this.connectNode(126, 116);

		this.connectNode(116, 110);

		this.connectNode(110, 104);

		this.connectNode(104, 111);
		this.connectNode(104, 105);

		this.connectNode(105, 106);

		this.connectNode(106, 107);

		this.connectNode(107, 112);
		this.connectNode(107, 113);

		this.connectNode(113, 114);

		this.connectNode(114, 120);

		this.connectNode(120, 124);
		this.connectNode(120, 125);

		this.connectNode(125, 133);

		this.connectNode(133, 137);

		this.connectNode(137, 132);
		this.connectNode(137, 136);

		this.connectNode(136, 140);

		this.connectNode(140, 135);

		this.connectNode(135, 131);
		this.connectNode(135, 134);

		this.connectNode(134, 130);

		this.connectNode(130, 126);

		this.connectNode(1221, 122);
		this.connectNode(121, 117);
		this.connectNode(121, 116);

		this.connectNode(111, 117);
		this.connectNode(111, 118);
		this.connectNode(111, 105);

		this.connectNode(112, 118);
		this.connectNode(112, 119);
		this.connectNode(112, 113);

		this.connectNode(124, 119);
		this.connectNode(124, 123);
		this.connectNode(124, 125);

		this.connectNode(132, 123);
		this.connectNode(132, 127);
		this.connectNode(132, 136);

		this.connectNode(131, 127);
		this.connectNode(131, 122);
		this.connectNode(131, 134);

		this.connectNode(117, 118);

		this.connectNode(118, 119);

		this.connectNode(119, 123);

		this.connectNode(123, 127);

		this.connectNode(127, 122);

		this.connectNode(122, 117);

		this.connectNode(1, 103);
		this.connectNode(1, 109);
		this.connectNode(1, 110);

		this.connectNode(2, 103);
		this.connectNode(2, 104);
		this.connectNode(2, 105);
		this.connectNode(2, 101);

		this.connectNode(3, 101);
		this.connectNode(3, 106);
		this.connectNode(3, 102);

		this.connectNode(4, 102);
		this.connectNode(4, 107);
		this.connectNode(4, 113);
		this.connectNode(4, 108);

		this.connectNode(5, 108);
		this.connectNode(5, 114);
		this.connectNode(5, 115);

		this.connectNode(6, 105);
		this.connectNode(6, 106);
		this.connectNode(6, 107);
		this.connectNode(6, 112);
		this.connectNode(6, 118);
		this.connectNode(6, 111);

		this.connectNode(7, 104);
		this.connectNode(7, 111);
		this.connectNode(7, 117);
		this.connectNode(7, 121);
		this.connectNode(7, 116);
		this.connectNode(7, 110);

		this.connectNode(8, 109);
		this.connectNode(8, 116);
		this.connectNode(8, 126);
		this.connectNode(8, 129);

		this.connectNode(9, 129);
		this.connectNode(9, 130);
		this.connectNode(9, 139);

		this.connectNode(10, 139);
		this.connectNode(10, 134);
		this.connectNode(10, 135);
		this.connectNode(10, 141);

		this.connectNode(11, 141);
		this.connectNode(11, 140);
		this.connectNode(11, 142);

		this.connectNode(12, 142);
		this.connectNode(12, 136);
		this.connectNode(12, 137);
		this.connectNode(12, 138);

		this.connectNode(13, 138);
		this.connectNode(13, 133);
		this.connectNode(13, 128);

		this.connectNode(14, 140);
		this.connectNode(14, 135);
		this.connectNode(14, 131);
		this.connectNode(14, 127);
		this.connectNode(14, 132);
		this.connectNode(14, 136);

		this.connectNode(15, 134);
		this.connectNode(15, 130);
		this.connectNode(15, 126);
		this.connectNode(15, 121);
		this.connectNode(15, 122);
		this.connectNode(15, 131);

		this.connectNode(16, 137);
		this.connectNode(16, 132);
		this.connectNode(16, 123);
		this.connectNode(16, 124);
		this.connectNode(16, 125);
		this.connectNode(16, 133);

		this.connectNode(17, 128);
		this.connectNode(17, 125);
		this.connectNode(17, 120);
		this.connectNode(17, 115);

		this.connectNode(18, 124);
		this.connectNode(18, 119);
		this.connectNode(18, 112);
		this.connectNode(18, 113);
		this.connectNode(18, 114);
		this.connectNode(18, 120);

		this.connectNode(19, 127);
		this.connectNode(19, 122);
		this.connectNode(19, 117);
		this.connectNode(19, 118);
		this.connectNode(19, 119);
		this.connectNode(19, 123);

	}

	/**
	 * Place a sheep in every field of the map, except for sheepsburg where are
	 * placed both the wolf and the blacksheep. If traditional is false the
	 * sheep type can be also lamb or ram, else only sheep
	 * 
	 * @param traditional
	 */
	public void addSheepWolf(boolean traditional) {

		// the add a sheep for every other field
		if (traditional) {
			for (Node x : this.map) {
				if (x.getClass() == Field.class) {
					Field f = (Field) x;
					finallyAdd(f);
				}
			}
		}
	}

	private void finallyAdd(Field f) {
		if (f.getType() == TypeField.SHEEPSBURG) {
			f.addShep(new BlackSheep());
			f.setWolf(true);
		} else {
			f.addShep(new GenericSheep(SheepType.SHEEP));
		}

	}

	/**
	 * Create a add to map field and numbered space
	 */
	public void addNodeToMap() {

		addField();
		addNumberedSpace();
		connectStartPosition();

	}

	/**
	 * Take the numbered space in the list and make them near node of start
	 * position.
	 * 
	 */
	private void connectStartPosition() {
		for (Node x : this.map) {
			if (x.getClass() == NumberedSpace.class) {
				this.startPos.insertNewNearNode(x);
			}
		}
	}

	/**
	 * Connect 2 given node if they exist
	 * 
	 * @param firstId
	 * @param secondId
	 */
	private void connectNode(int firstId, int secondId) {
		Node firstNode = getNodeById(firstId);
		Node secondNode = getNodeById(secondId);

		if (secondNode != null && firstNode != null) {
			firstNode.insertNewNearNode(secondNode);
			secondNode.insertNewNearNode(firstNode);
		}
	}

	/**
	 * Check if exist a node with the given id, if exist return the node else
	 * return null
	 * 
	 * @param id
	 * @return
	 */
	private Node getNodeById(int id) {
		for (Node x : this.map) {
			if (x.getId() == id) {
				return x;
			}
		}

		return null;
	}

	/**
	 * add to map all the field
	 */
	private void addField() {
		// type field: hill
		this.map.add(new Field(1, TypeField.HILL));
		this.map.add(new Field(8, TypeField.HILL));
		this.map.add(new Field(15, TypeField.HILL));

		// type field: hay
		this.map.add(new Field(2, TypeField.HAY));
		this.map.add(new Field(3, TypeField.HAY));
		this.map.add(new Field(7, TypeField.HAY));

		// type field: mountain
		this.map.add(new Field(4, TypeField.MOUNTAIN));
		this.map.add(new Field(5, TypeField.MOUNTAIN));
		this.map.add(new Field(6, TypeField.MOUNTAIN));

		// type field: wood
		this.map.add(new Field(9, TypeField.WOOD));
		this.map.add(new Field(10, TypeField.WOOD));
		this.map.add(new Field(14, TypeField.WOOD));

		// type field: swamp
		this.map.add(new Field(11, TypeField.SWAMP));
		this.map.add(new Field(12, TypeField.SWAMP));
		this.map.add(new Field(16, TypeField.SWAMP));

		// type field:desert
		this.map.add(new Field(13, TypeField.DESERT));
		this.map.add(new Field(17, TypeField.DESERT));
		this.map.add(new Field(18, TypeField.DESERT));

		// type field: sheepsburg
		this.map.add(new Field(19, TypeField.SHEEPSBURG));
	}

	/**
	 * add to map all the numbered space
	 */
	private void addNumberedSpace() {
		this.map.add(new NumberedSpace(101, 3));
		this.map.add(new NumberedSpace(102, 1));
		this.map.add(new NumberedSpace(103, 2));
		this.map.add(new NumberedSpace(104, 4));
		this.map.add(new NumberedSpace(105, 6));
		this.map.add(new NumberedSpace(106, 2));
		this.map.add(new NumberedSpace(107, 3));
		this.map.add(new NumberedSpace(108, 2));
		this.map.add(new NumberedSpace(109, 9));
		this.map.add(new NumberedSpace(110, 3));
		this.map.add(new NumberedSpace(111, 5));
		this.map.add(new NumberedSpace(112, 4));
		this.map.add(new NumberedSpace(113, 5));
		this.map.add(new NumberedSpace(114, 3));
		this.map.add(new NumberedSpace(115, 1));
		this.map.add(new NumberedSpace(116, 2));
		this.map.add(new NumberedSpace(117, 6));
		this.map.add(new NumberedSpace(118, 1));
		this.map.add(new NumberedSpace(119, 2));
		this.map.add(new NumberedSpace(120, 6));
		this.map.add(new NumberedSpace(121, 1));
		this.map.add(new NumberedSpace(122, 5));
		this.map.add(new NumberedSpace(123, 3));
		this.map.add(new NumberedSpace(124, 1));
		this.map.add(new NumberedSpace(125, 2));
		this.map.add(new NumberedSpace(126, 3));
		this.map.add(new NumberedSpace(127, 4));
		this.map.add(new NumberedSpace(128, 5));
		this.map.add(new NumberedSpace(129, 4));
		this.map.add(new NumberedSpace(130, 2));
		this.map.add(new NumberedSpace(131, 6));
		this.map.add(new NumberedSpace(132, 5));
		this.map.add(new NumberedSpace(133, 4));
		this.map.add(new NumberedSpace(134, 4));
		this.map.add(new NumberedSpace(135, 3));
		this.map.add(new NumberedSpace(136, 2));
		this.map.add(new NumberedSpace(137, 6));
		this.map.add(new NumberedSpace(138, 1));
		this.map.add(new NumberedSpace(139, 1));
		this.map.add(new NumberedSpace(140, 1));
		this.map.add(new NumberedSpace(141, 2));
		this.map.add(new NumberedSpace(142, 5));
	}

}
