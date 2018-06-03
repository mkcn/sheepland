package client.game;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.model.NumberedSpace;
import client.view.ClientGameAbstract;

public class TestClientMoveSepherd {

	RemoteSendInterface sender = Mockito.mock(RemoteSendInterface.class);
	ClientGameAbstract gI = Mockito.mock(ClientGameAbstract.class);
	ClientMoveShepherd testMove = new ClientMoveShepherd(sender, gI);
	Information infoTest;
	ArrayList<NumberedSpace> cardTest = new ArrayList<NumberedSpace>();

	@Before
	public void setUp() {
		infoTest = new Information(-1, null, null);
		cardTest.add(0, new NumberedSpace(0, 2));
		cardTest.add(1, new NumberedSpace(1, 3));
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
		info.setInformation(this.cardTest);
		info.setSecondInformation(1);

		testMove.showInfoRmationReceived(info);
	}

}
