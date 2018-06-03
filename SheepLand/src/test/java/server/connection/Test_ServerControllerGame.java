package server.connection;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import server.game.ServerGameSetting;
import server.game.ServerGameStatus;
import server.game.ServerPlayer;

import com.esotericsoftware.minlog.Log;

public class Test_ServerControllerGame {

	@Test
	public void checlAllServerControllerGame() {
		Log.INFO = false;
		Log.ERROR = false;
		ServerSettingConnection serverSetConnection = Mockito
				.mock(ServerSettingConnection.class);
		ServerGameSetting gameSetting = Mockito.mock(ServerGameSetting.class);
		ServerGameStatus gameStatus = Mockito.mock(ServerGameStatus.class);

		Mockito.when(serverSetConnection.getGameSetting()).thenReturn(
				gameSetting);
		Mockito.when(gameSetting.getServerGameStatus()).thenReturn(gameStatus);

		List<ServerPlayer> listPlayers = new ArrayList<ServerPlayer>();
		listPlayers.add(null);
		listPlayers.add(null);

		Mockito.when(gameStatus.getPlayers()).thenReturn(listPlayers);

		ServerPlayer player = Mockito.mock(ServerPlayer.class);
		Mockito.when(serverSetConnection.getTmpPlayer()).thenReturn(player);

		ServerControllerGame serverControllerGame = new ServerControllerGame(
				serverSetConnection);
		// num player = 2
		serverControllerGame.startTimerStartGame();
		listPlayers.add(null);
		listPlayers.add(null);
		// num player = 4
		serverControllerGame.checkIfStartGame();
		serverControllerGame.startPingConnection();
	}
}
