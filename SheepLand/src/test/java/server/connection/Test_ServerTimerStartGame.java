package server.connection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import com.esotericsoftware.minlog.Log;

public class Test_ServerTimerStartGame {

	@Before
	public void configure() {
		Log.INFO = false;
		Log.ERROR = false;
	}

	@Test
	public void testTimerStop() {

		ServerControllerGame test = Mockito.mock(ServerControllerGame.class);
		ServerTimerStartGame serverTimerStartGame = new ServerTimerStartGame(
				test);
		// reduce the timeout to 1 sec
		Whitebox.setInternalState(serverTimerStartGame, "milliSeconds", 1);
		serverTimerStartGame.run();
	}
}
