package share.game.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestDeck {

	private Deck cards;

	@Before
	public void TestAllCard() {
		this.cards = new Deck();
		initCard();
	}

	private void initCard() {
		this.cards.addCard(new FieldCard(0, TypeField.DESERT));
		this.cards.addCard(new FieldCard(1, TypeField.HAY));
		this.cards.addCard(new FieldCard(2, TypeField.HILL));
		this.cards.addCard(new FieldCard(3, TypeField.MOUNTAIN));
		this.cards.addCard(new FieldCard(4, TypeField.SWAMP));
		this.cards.addCard(new FieldCard(5, TypeField.WOOD));
	}
	
	@Test
	public void testSetDeck(){
		Deck deck2 = new Deck();
		deck2.setDeck();
		assertEquals(deck2.getSizeOFCards(),30);
	}

	@Test
	public void TestGetCopyOFCardByType() {
		assertEquals(this.cards.getCopyOFCardByType(TypeField.DESERT).getId(),
				0);
		assertEquals(this.cards.getCopyOFCardByType(TypeField.HAY).getId(), 1);
		assertEquals(this.cards.getCopyOFCardByType(TypeField.HILL).getId(), 2);
		assertEquals(
				this.cards.getCopyOFCardByType(TypeField.MOUNTAIN).getId(), 3);
		assertEquals(this.cards.getCopyOFCardByType(TypeField.SWAMP).getId(), 4);
	}

	@Test
	public void TestRemoveCardByIdGetSize() {
		this.cards.removeCardById(new FieldCard(2, TypeField.HILL));
		assertEquals(this.cards.getSizeOFCards(), 5);
	}

	@Test
	public void TestCheckIfIsIn() {
		assertEquals(this.cards.checkIfIsIn(TypeField.DESERT), true);
		assertEquals(this.cards.checkIfIsIn(TypeField.WOOD), true);
	}
	
	@Test
	public void canBoughtTrue(){
		ArrayList<Field> listToCheck = new ArrayList<Field>();
		listToCheck.add(new Field(0,TypeField.HAY));
		listToCheck.add(new Field(0, TypeField.HILL));
		
		assertEquals(this.cards.canBought(listToCheck, 0),false);
		assertEquals(this.cards.canBought(listToCheck, 5),true);
	}
	
	@Test
	public void particularCase(){
		Deck deck2 = new Deck();
		deck2.setDeck();
		
		deck2.removeCardById(deck2.getCopyOFCardByType(TypeField.DESERT));
		assertEquals(deck2.getSizeOFCards(),29);
		
		deck2.removeCardById(deck2.getCopyOFCardByType(TypeField.DESERT));
		assertEquals(deck2.getSizeOFCards(),28);
		
		deck2.removeCardById(deck2.getCopyOFCardByType(TypeField.DESERT));
		assertEquals(deck2.getSizeOFCards(),27);
		
		deck2.removeCardById(deck2.getCopyOFCardByType(TypeField.DESERT));
		assertEquals(deck2.getSizeOFCards(),26);
		
		deck2.removeCardById(deck2.getCopyOFCardByType(TypeField.DESERT));
		assertEquals(deck2.getSizeOFCards(),25);
		
		deck2.removeCardById(deck2.getCopyOFCardByType(TypeField.DESERT));
		assertEquals(deck2.getSizeOFCards(),25);
	}

}
