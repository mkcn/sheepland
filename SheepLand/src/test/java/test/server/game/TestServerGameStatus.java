package test.server.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import server.game.ServerGameStatus;
import server.game.ServerPlayer;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;

public class TestServerGameStatus {
	ServerPlayer pl1 = new ServerPlayer(null, "a", "a");
	ServerPlayer pl2 = new ServerPlayer(null, "b", "b");
	ServerPlayer pl3 = new ServerPlayer(null, "c", "c");
	ServerPlayer pl4 = new ServerPlayer(null, "d", "d");

	ServerGameStatus testStatus = new ServerGameStatus();

	@Before
	public void setUpFakeStatus() {
		testStatus.addPlayer(pl4);
		testStatus.addPlayer(pl3);
		testStatus.addPlayer(pl2);
		testStatus.addPlayer(pl1);
	}

	@Test
	public void playerStatusTest() {

		assertEquals(testStatus.getNumberPlayer(), 4);
		assertEquals(testStatus.getOtherGamers().size(), 3);

		assertEquals(this.testStatus.getActualPlayer().getUsrname(), "d");

		this.testStatus.incPlayerIndex();

		assertEquals(this.testStatus.getActualPlayer().getUsrname(), "c");

		assertEquals(this.testStatus.getNextPlayer().getUsrname(), "b");
	}

	@Test
	public void fenceNumber() {

		assertEquals(this.testStatus.isGameEnd(), false);

		for (int i = 0; i < 20; i++) {
			this.testStatus.incUsedFence();
		}

		Whitebox.setInternalState(testStatus, "playerIndex", 2);
		assertEquals(this.testStatus.isGameEnd(), true);

		Whitebox.setInternalState(testStatus, "playerIndex", 3);
		assertEquals(this.testStatus.isGameEnd(), true);
	}

	@Test
	public void playerShepherd() {
		NumberedSpace ns1 = new NumberedSpace(0, 1);
		Shepherd sh = new Shepherd(ns1);

		NumberedSpace ns2 = new NumberedSpace(1, 2);
		Shepherd sh1 = new Shepherd(ns2);

		this.pl1.addShepherd(sh, 0);
		this.pl1.addShepherd(sh1, 0);

		assertEquals(this.pl1.getAllShepher().size(), 2);
	}

	@Test
	public void testSync() {
		this.testStatus.syncActualPlayer();
		this.testStatus.incPlayerIndex();

		this.testStatus.syncActualPlayer();
		this.testStatus.incPlayerIndex();

		assertEquals(this.testStatus.getSync(), true);

		this.testStatus.syncActualPlayer();
		this.testStatus.incPlayerIndex();

		this.testStatus.syncActualPlayer();
		this.testStatus.incPlayerIndex();

		assertEquals(this.testStatus.getSync(), false);
	}

	@Test
	public void testPlayerIndex() {
		this.testStatus.addPlayer(new ServerPlayer(null, "", ""));
		assertEquals(this.testStatus.getActualPlayer().getId(), 0);
		this.testStatus.incPlayerIndex();
		this.testStatus.addPlayer(new ServerPlayer(null, "", ""));
		assertEquals(this.testStatus.getActualPlayer().getId(), 1);

	}

}
