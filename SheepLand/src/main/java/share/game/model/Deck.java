package share.game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contain the information about he cards of the game, from here is
 * possible to know which card will be next to be draw, remove a card or know if
 * a card is in this deck
 * 
 * @author andrea bertarini
 * 
 */
public class Deck implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int MAX_PRICE = 5;

	private List<FieldCard> cardsRemaining;
	private List<FieldCard> initial;

	/**
	 * Constructor, create the deck of the game and an other for the initial
	 * card
	 */
	public Deck() {
		this.cardsRemaining = new ArrayList<FieldCard>();
		this.initial = new ArrayList<FieldCard>();
		setInitial();
	}

	/**
	 * Fill the deck with the card
	 */
	public void setDeck() {
		for (int i = 0; i < MAX_PRICE; i++) {
			for (TypeField x : TypeField.getListWithType()) {
				this.cardsRemaining.add(new FieldCard(i, x));
			}
		}

	}

	private void setInitial() {
		initial.add(new FieldCard(0, TypeField.DESERT));
		initial.add(new FieldCard(0, TypeField.HAY));
		initial.add(new FieldCard(0, TypeField.HILL));
		initial.add(new FieldCard(0, TypeField.MOUNTAIN));
		initial.add(new FieldCard(0, TypeField.SWAMP));
		initial.add(new FieldCard(0, TypeField.WOOD));
	}

	/**
	 * Return a random field card from the initial ones
	 * 
	 * @return
	 */
	public FieldCard getRandomInitalFieldCard() {
		int rand = (int) (Math.random() * this.initial.size());

		FieldCard returnCard = this.initial.get(rand);
		this.initial.remove(rand);

		return returnCard;
	}

	/**
	 * Add a card to the deck
	 * 
	 * @param newCard
	 */
	public void addCard(FieldCard newCard) {
		this.cardsRemaining.add(newCard);
	}

	/**
	 * Return a copy of the first card you can draw, that is ,for every type,
	 * the card with the lower price
	 * 
	 * @param typetoCopy
	 * @return FieldCard
	 */
	public FieldCard getCopyOFCardByType(TypeField typetoCopy) {
		int price = MAX_PRICE;
		FieldCard card = null;
		for (FieldCard x : this.cardsRemaining) {
			if (x.getCardType() == typetoCopy && x.getId() < price) {
				card = new FieldCard(x);
				price = card.getId();
			}
		}
		return card;
	}

	/**
	 * Remove a specified card from the deck
	 * 
	 * @param cardToRemove
	 */
	public void removeCardById(FieldCard cardToRemove) {
		// for every remaining card control if is equals to the card to remove
		if (cardToRemove != null) {
			for (int i = 0; i < this.cardsRemaining.size(); i++) {
				if (this.cardsRemaining.get(i).getId() == cardToRemove.getId()
						&& this.cardsRemaining.get(i).getCardType() == cardToRemove
								.getCardType()) {
					// if they are equals then remove the card
					this.cardsRemaining.remove(i);
				}
			}
		}
	}

	/**
	 * Control if still exist a card that has a particular type
	 * 
	 * @param typetoCheck
	 * @return
	 */
	public boolean checkIfIsIn(TypeField typetoCheck) {
		for (FieldCard x : this.cardsRemaining) {
			if (x.getCardType() == typetoCheck) {
				return true;
			}
		}
		return false;

	}

	/**
	 * Return the size of the deck
	 * 
	 * @return
	 */
	public int getSizeOFCards() {
		return this.cardsRemaining.size();
	}

	/**
	 * Receive a list of field and an integer(money), for every type of field in
	 * this list get the first that is possible to drawn and compare the the
	 * price of this card with the given money
	 * 
	 * @param list
	 * @param money
	 * @return
	 */
	public boolean canBought(List<Field> list, int money) {
		for (Field x : list) {
			TypeField fieldType = x.getType();
			FieldCard card = this.getCopyOFCardByType(fieldType);
			if (fieldType != TypeField.SHEEPSBURG && card != null) {
				if (card.getId() > money) {
					return false;
				}
			}
		}
		return true;
	}

}
