package client.game;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.model.FieldCard;
import share.game.model.TypeField;
import client.view.ClientGameAbstract;

public class TestClientMoveBuyField {

	RemoteSendInterface sender = Mockito.mock(RemoteSendInterface.class);
	ClientGameAbstract gI = Mockito.mock(ClientGameAbstract.class);
	ClientMoveBuyField testMove = new ClientMoveBuyField(sender, gI);
	Information infoTest;
	ArrayList<FieldCard> cardTest = new ArrayList<FieldCard>();

	@Before
	public void setUp() {
		infoTest = new Information(-1, null, null);
		cardTest.add(0, new FieldCard(0, TypeField.DESERT));
		cardTest.add(1, new FieldCard(1, TypeField.HAY));
		this.infoTest.setInformation(cardTest);
	}

	@Test
	public void testMakeChoice() {

		testMove.makeYourChoiche(infoTest);

	}

	@Test
	public void testSendChoice() {
		Whitebox.setInternalState(testMove, "infoReceived", this.cardTest);
		testMove.sendChoiche(0);
	}

	@Test
	public void testShowInfoReceived() {
		Information info = new Information(-1, null, null);
		info.setInformation(new FieldCard(0, null));
        info.setSecondInformation(0);
		this.testMove.showInfoRmationReceived(info);
	}

}
