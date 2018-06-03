package client.game;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.model.Field;
import share.game.model.Node;
import share.game.model.TypeField;
import client.view.ClientGameAbstract;

public class TestClientMoveSheep {

	RemoteSendInterface sender = Mockito.mock(RemoteSendInterface.class);
	ClientGameAbstract gI = Mockito.mock(ClientGameAbstract.class);
	ClientMoveSheep testMove = new ClientMoveSheep(sender, gI);
	ArrayList<Node> nodeTest = new ArrayList<Node>();
	
	
	
	@Before
	public void setUp(){
		nodeTest.add(0, new Field(0, TypeField.DESERT));
		nodeTest.add(1, new Field(1, TypeField.HAY));	
	}

	@Test
	public void testMakeChoiceBlack() {

		Information info = new Information(-1, InformationType.MOVEBLACKSHEEP,
				null);
		info.setInformation(nodeTest);
		info.setSecondInformation("");
		testMove.makeYourChoiche(info);

	}

	@Test
	public void testMakeYourChoiceForced() {
		Information info = new Information(-1, null, null);
		info.setInformation(this.nodeTest);
		info.setSecondInformation("ForcedSheep");
		testMove.makeYourChoiche(info);
	}
	
	@Test
	public void testMakeYourChoice(){
		Information info = new Information(-1, null, null);
		info.setInformation(this.nodeTest);
		info.setSecondInformation("");
		testMove.makeYourChoiche(info);
	}
	

	@Test
	public void testSendChoice() {
		Whitebox.setInternalState(testMove, "infoReceived", this.nodeTest);
		testMove.sendChoiche(0);
	}

	@Test
	public void testBlackSheep() {
		Information info = new Information(-1, InformationType.MOVEBLACKSHEEP,
				null);
		testMove.makeYourChoiche(info);
	}

	@Test
	public void testShowInfoReceived() {
		Information info = new Information(-1, null, null);
		info.setInformation(nodeTest);
		this.testMove.showInfoRmationReceived(info);
	}
}
