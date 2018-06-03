package server.game.move;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import server.game.ServerGameSender;
import server.game.ServerGameStatus;
import server.game.ServerMove;
import server.game.ServerMoveBuyField;
import server.game.ServerPlayer;
import share.game.comunication.Information;
import share.game.model.Deck;
import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.MapHandler;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;
import share.game.model.TypeField;

public class TestMoveBuyField {

	ServerGameStatus testStatus = new ServerGameStatus();

	MapHandler testMap = new MapHandler();
	ArrayList<Node> nodeList = new ArrayList<Node>();
	ServerPlayer pl1 = new ServerPlayer(null, "a", "");
	ServerPlayer pl2 = new ServerPlayer(null, "b", "5");

	NumberedSpace space1 = new NumberedSpace(0, 1);
	NumberedSpace space2 = new NumberedSpace(5, 1);

	Field field1 = new Field(1, TypeField.DESERT);
	Field field2 = new Field(2, TypeField.HAY);
	Field field3 = new Field(3, TypeField.HILL);
	Field field4 = new Field(4, TypeField.HILL);

	Shepherd shpl2;
	Shepherd shpl1;
	
	ServerGameSender mockedSender = Mockito.mock(ServerGameSender.class);

	@Before
	public void setUpTest() {
		this.nodeList.add(field1);
		this.nodeList.add(field2);
		this.nodeList.add(field3);
		this.nodeList.add(field4);

		this.nodeList.add(space1);
		this.nodeList.add(space2);

		field1.insertNewNearNode(space1);
		space1.insertNewNearNode(field1);

		space1.insertNewNearNode(field2);
		field2.insertNewNearNode(space1);

		field3.insertNewNearNode(space2);
		space2.insertNewNearNode(field3);

		space2.insertNewNearNode(field4);
		field4.insertNewNearNode(space2);

		this.testStatus.setGameGraph(testMap);

		shpl1 = new Shepherd(this.space1);
		shpl2 = new Shepherd(this.space2);

		pl1.addShepherd(shpl1, 0);
		pl2.addShepherd(shpl2, 0);

		this.testStatus.addPlayer(pl1);
		this.testStatus.addPlayer(pl2);

	}

	@Test
	public void atLeastACard() throws RemoteException {
		Deck testDeck = new Deck();
		testDeck.addCard(new FieldCard(0, TypeField.DESERT));
		testDeck.addCard(new FieldCard(1, TypeField.HAY));

		this.testStatus.setCards(testDeck);
        
		ServerMove testMove = new ServerMoveBuyField(this.testStatus, shpl1,mockedSender);
		testMove.findInformation();

	}

	@Test
	public void noCard() throws RemoteException {
		Deck testDeck = new Deck();
		testDeck.addCard(new FieldCard(0, TypeField.DESERT));
		testDeck.addCard(new FieldCard(1, TypeField.HAY));

		this.testStatus.setCards(testDeck);

		ServerMove testMove = new ServerMoveBuyField(this.testStatus, shpl2,this.mockedSender);
		testMove.findInformation();

	}
	
	@Test
	public void testEndMove() throws RemoteException{
		ServerGameStatus status = new ServerGameStatus();
		
		ServerPlayer pl = new ServerPlayer(null,"","");
		pl.setMoney(20);
		status.addPlayer(pl);
		
		FieldCard card1 = new FieldCard(0,TypeField.DESERT);
		FieldCard card2 = new FieldCard(1,TypeField.MOUNTAIN);
		Deck deck = new Deck();
		deck.addCard(card2);
		deck.addCard(card1);
		status.setCards(deck);
		
		ArrayList<FieldCard> optionList = new ArrayList<FieldCard>();
		optionList.add(card2);
		optionList.add(card1);

		ServerMove testMove = new ServerMoveBuyField(status, shpl2,this.mockedSender);
		Whitebox.setInternalState(testMove, "optionList", optionList);
		
		Information newInfo = new Information(-1,null,null);
		newInfo.setInformation(card2);
		testMove.endMove(newInfo);		
	}
	
	@Test
	public void testendMoveNoMoney() throws RemoteException{
		ServerGameStatus status = new ServerGameStatus();
		
		ServerPlayer pl = new ServerPlayer(null,"","");
		pl.setMoney(0);
		status.addPlayer(pl);
		
		FieldCard card1 = new FieldCard(0,TypeField.DESERT);
		FieldCard card2 = new FieldCard(1,TypeField.MOUNTAIN);
		Deck deck = new Deck();
		deck.addCard(card2);
		deck.addCard(card1);
		status.setCards(deck);
		
		ArrayList<FieldCard> optionList = new ArrayList<FieldCard>();
		optionList.add(card2);
		optionList.add(card1);

		ServerMove testMove = new ServerMoveBuyField(status, shpl2,this.mockedSender);
		Whitebox.setInternalState(testMove, "optionList", optionList);
		
		Information newInfo = new Information(-1,null,null);
		newInfo.setInformation(card2);
		testMove.endMove(newInfo);		
	}

}
