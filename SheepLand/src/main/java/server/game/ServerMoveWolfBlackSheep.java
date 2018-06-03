package server.game;

import java.util.ArrayList;
import java.util.List;

import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.model.Field;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;

/**
 * This is a special move, about the move of the wolf, or about the move of the
 * black sheep
 * 
 * @author andrea bertarini
 * 
 */
public class ServerMoveWolfBlackSheep {

	ServerGameStatus statusGame;

	public ServerMoveWolfBlackSheep(ServerGameStatus statusGame) {
		this.statusGame = statusGame;
	}

	/**
	 * Find the black sheep, get a random number(dice value) and decide if the
	 * black sheep can move or not, in the end send a message to every player
	 */
	public Information moveRandomBlackSheep() {
		List<Field> optionList = new ArrayList<Field>();
		Information newInfo = new Information(-1,
				InformationType.BLACKRNDMMOVE, null);

		// rolling dice
		int diceValue = this.statusGame.getRandomValue().getDiceValue();

		// get the field where the black sheep is now
		Field fromField = this.statusGame.getGameGraph().fieldWithBlackSheep();
		// get the space near the actual position of the sheep
		List<NumberedSpace> nearSpace = this.statusGame.getGameGraph()
				.deleteFieldFroNearNode(fromField);
		// Check if there is the right numbered space
		NumberedSpace middleSpace = canBlackSheepMove(nearSpace, diceValue);

		if (middleSpace != null) {
			// if there is, find the toField
			Field toField = findOtherField(fromField, middleSpace);
			// then move the black sheep
			this.moveBlackSheep(fromField, toField);

			// put information into the message

			optionList.add(0, toField);
			optionList.add(1, fromField);
		}
		newInfo.setInformation(optionList);
		return newInfo;

	}

	/**
	 * 
	 * @param fromField
	 * @param middleSpace
	 * @return
	 */
	private Field findOtherField(Field fromField, NumberedSpace middleSpace) {
		for (Field x : this.statusGame.getGameGraph()
				.deleteNumberedSpaceFromNearNode(middleSpace)) {
			if (!x.equalsField(fromField)) {
				return x;
			}
		}
		return null;
	}

	/**
	 * Check if one of the list node has the same value as dice value and it is
	 * not fence
	 * 
	 * @param nearSpace
	 * @param diceValue
	 * @return
	 */
	private NumberedSpace canBlackSheepMove(List<NumberedSpace> nearSpace,
			int diceValue) {
		for (NumberedSpace x : nearSpace) {
			if (x.getValue() == diceValue && !x.isFence()
					&& x.getSHepherd() == null) {
				return x;
			}
		}
		return null;
	}

	/**
	 * Get all the information to move the black sheep from his field to a new
	 * field passing through the position of tha actual player
	 */
	public Information playerMoveBlackSheep(Shepherd actualShep) {
		// get the type where the black sheep is now
		Field fromField = this.statusGame.getGameGraph().fieldWithBlackSheep();
		// get the destination of the black sheep
		Field toField = getToNodePlayerMoveBlackSheep(fromField, actualShep);

		moveBlackSheep(fromField, toField);

		// put the information into an array list
		List<Field> requestNode = new ArrayList<Field>();
		requestNode.add(0, toField);
		requestNode.add(1, fromField);

		// create an information message
		Information newInfo = new Information(-1,
				InformationType.ENDOPERATIONBLACK, null);
		// with the request info
		newInfo.setInformation(requestNode);

		return newInfo;

	}

	/**
	 * Take the black sheep from his field and replace her into a new field
	 * 
	 * @param fromField
	 * @param toField
	 */
	private void moveBlackSheep(Field fromField, Field toField) {
		toField.addShep(fromField.removeBlackSheep());

	}

	/**
	 * Return the new position of the black sheep, knowing the actual position
	 * 
	 * @param fromField
	 * @return
	 */
	private Field getToNodePlayerMoveBlackSheep(Field fromField,
			Shepherd actualShep) {
		List<NumberedSpace> nearSpace = this.statusGame.getGameGraph()
				.deleteFieldFroNearNode(fromField);
		NumberedSpace actualPosition = actualShep.getPositioOnMapn();

		for (NumberedSpace x : nearSpace) {
			if (x.equalsNumberedSpace(actualPosition)) {
				for (Field y : this.statusGame.getGameGraph()
						.deleteNumberedSpaceFromNearNode(x)) {
					if (!y.equalsField(fromField)) {
						return y;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Make move the wolf and maybe eat a sheep. Get the position and according
	 * to the rules, check if he can move to a near field and eat a sheep
	 */
	public Information moveWolf() {
		Information newInfo = new Information(-1, InformationType.WOLFMOVE,
				null);
		List<Node> optionList = new ArrayList<Node>();

		// get the field containing the wolf
		Field fromField = this.statusGame.getGameGraph().fieldWithWolf();

		// rolling dice
		int diceValue = this.statusGame.getRandomValue().getDiceValue();

		// find the space from which the wolf will pass
		NumberedSpace middleSpace = canWolfMove(fromField, diceValue);

		if (middleSpace != null) {
			// find the destination
			Field toField = findOtherField(fromField, middleSpace);

			// move the wolf from fromField to toField
			fromField.setWolf(false);
			toField.setWolf(true);
			// send to all player where the wolf moved

			// if the wolf can eat a sheep remove it from toField
			if (canEat(toField)) {
				toField.removeSheep();
				newInfo = new Information(-1, InformationType.WOLFEAT, null);
			}

			optionList.add(0, toField);
			optionList.add(1, fromField);

		}
		newInfo.setInformation(optionList);
		return newInfo;
	}

	private boolean canEat(Field toField) {
		return this.statusGame.getRandomValue().getRandomBoolean()
				&&  toField.canKill();
	}

	/**
	 * 
	 * @param fromField
	 * @param diceValue
	 * @return
	 */
	private NumberedSpace canWolfMove(Field fromField, int diceValue) {
		// get the near numbered space number
		int nearSpaceNumber = this.statusGame.getGameGraph()
				.deleteFieldFroNearNode(fromField).size();
		// get the number of the near numbered space with a fence
		int nearFenceNumber = this.statusGame.getGameGraph().getFenceNumber(
				fromField);

		if (nearSpaceNumber == nearFenceNumber) {
			// if the two numbers are equals every near space is fence so the
			// wolf can jump over the fence
			// in this case i have to control only if the dice value corresponds
			// to the value of the near space
			for (NumberedSpace x : this.statusGame.getGameGraph()
					.deleteFieldFroNearNode(fromField)) {
				if (x.getValue() == diceValue) {
					return x;
				}
			}
		} else {
			// else not all the space are fence
			// in this case i have to check also if on the space there is not a
			// fence
			for (NumberedSpace x : this.statusGame.getGameGraph()
					.deleteFieldFroNearNode(fromField)) {
				if (x.getValue() == diceValue && !x.isFence()) {
					return x;
				}
			}
		}

		return null;
	}

}
