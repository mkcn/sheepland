package test.server.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

import server.game.ServerGameSender;
import server.game.ServerGameStatus;
import server.game.ServerMoveBuyField;
import server.game.ServerMoveCoupling;
import server.game.ServerMoveKilling;
import server.game.ServerMoveSheep;
import server.game.ServerMoveShepHerd;
import server.game.ServerTurnStatus;
import share.game.comunication.RequestType;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;

public class TestServerTurnStatus {

	ServerGameStatus status = Mockito.mock(ServerGameStatus.class);
	ServerGameSender sender = Mockito.mock(ServerGameSender.class);
	ServerTurnStatus statusTurn = new ServerTurnStatus(this.status);
	Shepherd shep = new Shepherd(new NumberedSpace(0, 0));

	@Test
	public void test1() {
		this.statusTurn.resetTurn();

		assertEquals(this.statusTurn.isTurnEnd(), false);
	}

	@Test
	public void test2() {
		this.statusTurn.resetTurn();

		this.statusTurn.setShepherd(this.shep);

		this.statusTurn.initNextMove(RequestType.COUPLING, this.sender);
		assertEquals(this.statusTurn.getActualMove().getClass(),
				ServerMoveCoupling.class);

		this.statusTurn.initNextMove(RequestType.KILL, this.sender);
		assertEquals(this.statusTurn.getActualMove().getClass(),
				ServerMoveKilling.class);

		this.statusTurn.initNextMove(RequestType.MOVESHEEP, this.sender);
		assertEquals(this.statusTurn.getActualMove().getClass(),
				ServerMoveSheep.class);

		assertEquals(this.statusTurn.isTurnEnd(), true);

	}

	@Test
	public void test3() {
		this.statusTurn.resetTurn();

		this.statusTurn.setShepherd(this.shep);

		this.statusTurn.initNextMove(RequestType.MOVESHEPHERD, this.sender);
		assertEquals(this.statusTurn.getActualMove().getClass(),
				ServerMoveShepHerd.class);

		this.statusTurn.initNextMove(RequestType.MOVEBUYFIELD, this.sender);
		assertEquals(this.statusTurn.getActualMove().getClass(),
				ServerMoveBuyField.class);

		this.statusTurn.initNextMove(RequestType.MOVESHEEP, this.sender);
		assertEquals(this.statusTurn.getActualMove().getClass(),
				ServerMoveSheep.class);

		assertEquals(this.statusTurn.isTurnEnd(), true);

	}

}
