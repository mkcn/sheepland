package server.connection;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import server.game.ServerGameSetting;
import server.game.ServerPlayer;
import share.connection.RemoteSendInterface;

import com.esotericsoftware.minlog.Log;

public class Test_ServerPingConnection {

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
	}

	@Test(timeout = 1000)
	public void pingFalse() throws RemoteException {
		ServerGameSetting gameServer = Mockito.mock(ServerGameSetting.class);
		ServerPlayer serverPlayer = Mockito.mock(ServerPlayer.class);

		RemoteSendInterface clientRemote = Mockito
				.mock(RemoteSendInterface.class);

		Mockito.when(serverPlayer.getClientRemote()).thenReturn(clientRemote);
		Mockito.when(clientRemote.ping()).thenReturn(false);

		ServerPingConnection serverPingConnection = new ServerPingConnection(
				gameServer, serverPlayer);
		Whitebox.setInternalState(serverPingConnection, "timeout", 1);
		serverPingConnection.run();
	}

	@Test(timeout = 1000)
	public void pingTrue() throws RemoteException, InterruptedException {
		ServerGameSetting gameServer = Mockito.mock(ServerGameSetting.class);
		ServerPlayer serverPlayer = Mockito.mock(ServerPlayer.class);

		RemoteSendInterface clientRemote = Mockito
				.mock(RemoteSendInterface.class);

		Mockito.when(serverPlayer.getClientRemote()).thenReturn(clientRemote);
		Mockito.when(clientRemote.ping()).thenReturn(true);

		ServerPingConnection serverPingConnection = new ServerPingConnection(
				gameServer, serverPlayer);
		Whitebox.setInternalState(serverPingConnection, "timeout", 1);

		ExecutorService ex = Executors.newSingleThreadExecutor();
		ex.execute(serverPingConnection);
		Thread.sleep(10);
		Mockito.when(clientRemote.ping()).thenReturn(false);
	}
}
