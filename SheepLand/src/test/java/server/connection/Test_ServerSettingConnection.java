package server.connection;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import share.connection.RemoteSendInterface;

import com.esotericsoftware.minlog.Log;

public class Test_ServerSettingConnection {
	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
	}

	@Test
	public void addTwoPlayers() {

		ServerSettingConnection serverSettingConnection = new ServerSettingConnection();
		RemoteSendInterface clientRemote = Mockito
				.mock(RemoteSendInterface.class);
		serverSettingConnection.checkAndSetCredentials(clientRemote, "test",
				"test");
		// second equal request
		serverSettingConnection.checkAndSetCredentials(clientRemote, "test",
				"test");

		assertFalse(serverSettingConnection.getGameSetting() == null);
		assertFalse(serverSettingConnection.getTmpGameSet() == null);
		assertFalse(serverSettingConnection.getTmpPlayer() == null);

		serverSettingConnection.createNewGame();
	}

}
