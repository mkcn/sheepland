package client.game;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.model.Field;
import client.view.ClientGameAbstract;

public class TestClientMoveCoupling {

	RemoteSendInterface sender = Mockito.mock(RemoteSendInterface.class);
	ClientGameAbstract gI = Mockito.mock(ClientGameAbstract.class);
	ClientMoveCoupling move = new ClientMoveCoupling(sender, gI);
	
	@Test
	public void testSendChoiche(){
		List<Field> optionList = new ArrayList<Field>();
		optionList.add(new Field(0,null));
		optionList.add(new Field(1,null));
		Whitebox.setInternalState(move, "infoReceived", optionList);
		move.sendChoiche(0);
	}
	
	@Test
	public void testShowInfoReceived(){
		Information info = new Information(-1, null, null);
		info.setInformation(new Field(0,null));
		
		this.move.showInfoRmationReceived(info);
	}

	@Test
	public void testShowInfoReceivedNoCoupling(){
		Information info = new Information(-1, null, null);
		info.setInformation(null);
		
		info.setSecondInformation(new Field(0,null));
		
		this.move.showInfoRmationReceived(info);
	}
}
