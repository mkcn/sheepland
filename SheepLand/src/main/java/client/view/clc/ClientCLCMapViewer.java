package client.view.clc;

import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.NumberedSpace;
import share.game.model.Player;
import share.game.model.Shepherd;
import client.view.ClientConvertIndex;
import client.view.ClientGraphicConstants;

/**
 * This class contain a list of the map for the clc, his task is to show the map
 * to the player
 * 
 * @author andrea
 * 
 */
public class ClientCLCMapViewer {

	private final static String NEXT_LINE = "\n";

	private String nameUser;
	private int numberOfFence = -1;
	private Shepherd chosenShepherd;
	private FieldCard myInitialFieldCard;
	private NumberedSpace[] paths = new NumberedSpace[ClientGraphicConstants.PATH_POSITION.length];
	private Field[] fields = new Field[ClientGraphicConstants.FIELD_POSITION.length];
	private FieldCard[] fieldCard = new FieldCard[ClientGraphicConstants.FIELDCARDS.length];
	private int[] numberOfFieldCard = new int[ClientGraphicConstants.FIELDCARDS.length];
	private Player[] players = new Player[ClientGraphicConstants.SHEEPHERD.length];
	private ClientConvertIndex cci;

	private String mapString;
	private boolean update;

	/**
	 * constructor that load the converter of index
	 */
	public ClientCLCMapViewer() {
		this.cci = new ClientConvertIndex();
		this.update = false;
	}

	/**
	 * call this if a path change (shepherd , fence..)
	 * 
	 * @param numberedSpace
	 */
	public void updatePaths(NumberedSpace numberedSpace) {
		this.update = false;
		this.paths[this.cci.convertIndex(numberedSpace)] = numberedSpace;
	}

	/**
	 * call this if a player change (money, disconnected ecc..)
	 * 
	 * @param player
	 */
	public void updatePlayer(Player player) {
		this.update = false;
		this.players[this.cci.convertIndex(player)] = player;
	}

	/**
	 * call this if a FieldCard change (number, actual value)
	 * 
	 * @param fieldCard
	 * @param numberOfCardBuyed
	 */
	public void updateFieldCards(FieldCard fieldCard, int numberOfCardBuyed) {
		this.update = false;
		this.fieldCard[this.cci.convertIndex(fieldCard)] = fieldCard;
		if (numberOfCardBuyed != -1) {
			this.numberOfFieldCard[this.cci.convertIndex(fieldCard)] = numberOfCardBuyed;
		}
	}

	/**
	 * call this if a Field change (sheep, black sheep, wolf)
	 * 
	 * @param field
	 */
	public void updateField(Field field) {
		this.update = false;
		this.fields[this.cci.convertIndex(field)] = field;
	}

	/**
	 * print all the info about the game fields, paths,shepherds, fieldcards and
	 * players
	 */
	public String toString() {
		if (!this.update) {
			this.update = true;
			this.mapString = generateSeparateLine() + nextLine(1)
					+ "Actual player: " + this.nameUser + nextLine(2);

			this.mapString += getInfoFromFields() + nextLine(1)
					+ getInfoFromPaths() + nextLine(1) + getInfoFieldCards()
					+ nextLine(1);

			if (this.myInitialFieldCard != null) {
				this.mapString += "Your initial card is "
						+ this.myInitialFieldCard.getCardType();
			}

			if (this.players[0] != null) {
				for (int i = 0; i < 2; i++) {
					this.mapString += nextLine(1) + "The player '"
							+ this.players[i].getUsrname() + "' have "
							+ this.players[i].getMoney() + " money"
							+ nextLine(1);
					if (this.players[2] == null) {
						this.mapString += "He has the 'shepherd " + (1 + 2 * i)
								+ "' and 'the shepherd " + (2 + 2 * i) + "'"
								+ nextLine(1);
					}
				}
				this.mapString += nextLine(1);
			}

			if (this.chosenShepherd != null) {
				this.mapString += "You are playing with 'shepherd "
						+ (this.chosenShepherd.getCode() + 1) + "' "
						+ nextLine(1);
			}

			if (this.numberOfFence == 1) {
				this.mapString += "Remain just 1 fence" + nextLine(1);
			} else if (this.numberOfFence != -1) {
				this.mapString += "Remain " + this.numberOfFence + " fences"
						+ nextLine(1);
			}

			this.mapString += generateSeparateLine();
			this.update = true;
		}
		// finally return all the data in a string
		return this.mapString;
	}

	/**
	 * 
	 * @return a line made of *
	 */
	public String generateSeparateLine() {
		return generateSeparateLine('*');
	}

	/**
	 * 
	 * @param carat
	 *            the char with to made the line
	 * @return a line made of a char
	 */
	public String generateSeparateLine(char carat) {
		String line = NEXT_LINE;
		for (int i = 0; i < 80; i++) {
			line += carat;
		}
		return line + NEXT_LINE;
	}

	/**
	 * @param the
	 *            chosen Shepherd
	 */
	public void setChosenShepherd(Shepherd chosenShepherd) {
		this.chosenShepherd = chosenShepherd;
	}

	/**
	 * @param numberOfFence
	 *            the numberOfFence to set
	 */
	public void setNumberOfFence(int numberOfFence) {
		this.numberOfFence = numberOfFence;
	}

	/**
	 * @param nameUser
	 *            the nameUser to set
	 */
	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	/**
	 * 
	 * @param myInitialFieldCard
	 */
	public void setMyInitialFieldCard(FieldCard myInitialFieldCard) {
		this.myInitialFieldCard = myInitialFieldCard;
	}

	private String nextLine(int numberNextLine) {
		String nextLine = "";
		for (int i = 0; i < numberNextLine; i++) {
			nextLine += NEXT_LINE + "* ";
		}
		return nextLine;
	}

	private String getInfoFromFields() {
		int countElementsOnARow = 0;
		String blackWolfString = "";
		String fieldString = "Field: ";
		for (Field field : this.fields) {
			if (field.isBlackSheepHere() != -1) {
				blackWolfString = blackWolfString
						.concat("Black Sheep is in field " + field.getId()
								+ nextLine(1));
			}

			if (field.isWolf()) {
				blackWolfString = blackWolfString.concat("Wolf is in field "
						+ field.getId() + nextLine(1));
			}

			countElementsOnARow++;
			if (countElementsOnARow == 5) {
				fieldString += nextLine(1);
				countElementsOnARow = 0;
			}
			String sheep = "sheep";
			if (field.getSheep().size() != 1) {
				sheep += "s";
			}
			fieldString = fieldString.concat(" (id " + field.getId() + ","
					+ sheep + " " + field.getSheep().size() + ")");
		}
		return fieldString + nextLine(1) + blackWolfString;
	}

	private String getInfoFromPaths() {
		int countElementsOnRow = 0;
		String pathString = "Path: ";
		for (NumberedSpace path : this.paths) {
			if (path.isFence()) {
				countElementsOnRow++;
				pathString += " (id " + path.getId() + ", fenced)";
			}

			if (path.getSHepherd() != null) {
				countElementsOnRow++;
				pathString += " (id " + path.getId() + ", 'shepherd "
						+ (path.getSHepherd().getCode() + 1) + "' is here)";
			}
			if (countElementsOnRow == 3) {
				pathString += nextLine(1);
				countElementsOnRow = 0;
			}
		}
		return pathString;
	}

	private String getInfoFieldCards() {
		String fieldcardsString = "Fieldcards: ";
		for (int i = 0; i < this.fieldCard.length; i++) {
			if (this.fieldCard[i] != null) {
				fieldcardsString += " ( you have " + this.numberOfFieldCard[i]
						+ " of " + this.fieldCard[i].getCardType()
						+ " and now it costs " + this.fieldCard[i].getId()
						+ ")" + nextLine(1);
			}
		}
		return fieldcardsString;
	}

}
