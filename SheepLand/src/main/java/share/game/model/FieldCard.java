package share.game.model;

import java.io.Serializable;

/**
 * This class contain information about the card of the game, price and type of
 * this card the type is related to fyeldType(enum)
 * 
 * @author andrea
 * 
 */
public class FieldCard implements Serializable {

	private static final long serialVersionUID = 1L;
	private int price;
	private TypeField cardType;

	/**
	 * Constructor
	 * 
	 * @param price
	 *            , of the card
	 * @param cardType
	 *            , such as SWAMP, HAY, HILL, MOUNTAIN...
	 */
	public FieldCard(int price, TypeField cardType) {
		this.price = price;
		this.cardType = cardType;
	}

	/**
	 * Create a copy of a given card
	 * 
	 * @param card
	 */
	public FieldCard(FieldCard card) {
		this.price = card.getId();
		this.cardType = card.getCardType();
	}

	/**
	 * Return the price of this card
	 * 
	 * @return
	 */
	public int getId() {
		return this.price;
	}

	/**
	 * Return the type of this card
	 * 
	 * @return
	 */
	public TypeField getCardType() {
		return this.cardType;
	}

	/**
	 * Compare two field card and return true if they are equals else false
	 * 
	 * @param x
	 * @return
	 */
	public boolean equalsCard(FieldCard x) {
		return this.price == x.price && this.cardType == x.cardType;
	}

	/**
	 * Convert into string this card field
	 */
	public String toString() {
		return "type: " + this.cardType.toString() + " price: "
				+ Integer.toString(this.price);
	}
}
