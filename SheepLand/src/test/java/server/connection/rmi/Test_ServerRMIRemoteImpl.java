package server.connection.rmi;

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import server.game.ServerGameSetting;
import server.game.ServerPlayer;

import com.esotericsoftware.minlog.Log;

public class Test_ServerRMIRemoteImpl {

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
	}

	@Test
	public void constructor() throws RemoteException {
		ServerGameSetting serverGameSet = Mockito.mock(ServerGameSetting.class);
		ServerPlayer gamer = Mockito.mock(ServerPlayer.class);
		ServerRMIRemoteImpl test = new ServerRMIRemoteImpl(serverGameSet, gamer);
		test.sendObj("test");
		assertTrue(test.ping());
	}
}
