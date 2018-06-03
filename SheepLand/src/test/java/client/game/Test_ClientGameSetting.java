package client.game;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.comunication.InformationType;
import client.view.ClientGameAbstract;
import client.view.ClientLoginInterface;

public class Test_ClientGameSetting {

	private ClientGameSetting clientGameSetting;

	@Before
	public void configure() {
		this.clientGameSetting = new ClientGameSetting();
		RemoteSendInterface remoteServerInterface = Mockito
				.mock(RemoteSendInterface.class);

		ClientGameAbstract graphicGame = Mockito.mock(ClientGameAbstract.class);
		this.clientGameSetting.setGraphicGame(graphicGame);
		ClientLoginInterface graphicLogin = Mockito
				.mock(ClientLoginInterface.class);
		this.clientGameSetting.setGraphicLogin(graphicLogin);

		this.clientGameSetting.createGame(remoteServerInterface);
	}

	@Test
	public void testSendObjs() {

		this.clientGameSetting.serverToClient("test");
		this.clientGameSetting.serverToClient(this.clientGameSetting);

		ClientGame clientGame = Mockito.mock(ClientGame.class);
		Whitebox.setInternalState(this.clientGameSetting, "clientGame",
				clientGame);

		Information information = new Information(0, InformationType.ACK, null);
		this.clientGameSetting.serverToClient(information);
	}

	@Test
	public void testGet() {
		assertTrue(this.clientGameSetting.getClientGraphic() != null);
		assertTrue(this.clientGameSetting.getClientLogin() != null);
	}

	@Test
	public void testOpenGame() {
		this.clientGameSetting.loginDoneOpenGame("test");
	}

	@After
	public void testDisconnection() {
		this.clientGameSetting.notifyConnectionFail();
	}

}
