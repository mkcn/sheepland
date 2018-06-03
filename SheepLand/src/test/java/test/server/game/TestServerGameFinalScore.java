package test.server.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import server.game.ServerGameFinalScore;
import server.game.ServerGameStatus;
import server.game.ServerPlayer;
import share.game.model.BlackSheep;
import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.MapCreator;
import share.game.model.MapHandler;
import share.game.model.TypeField;

public class TestServerGameFinalScore {

	@Test
	public void testCaso1() {
		ServerGameStatus status = new ServerGameStatus();

		ServerPlayer pla = new ServerPlayer(null, "a", "a");

		pla.addPlayerCard(new FieldCard(0, TypeField.HAY));
		pla.addPlayerCard(new FieldCard(1, TypeField.HAY));
		pla.addPlayerCard(new FieldCard(2, TypeField.HAY));

		pla.addPlayerCard(new FieldCard(0, TypeField.SWAMP));
		pla.addPlayerCard(new FieldCard(1, TypeField.SWAMP));

		MapHandler map = new MapHandler();
		MapCreator mapCreator = new MapCreator();
		mapCreator.addNodeToMap();
		mapCreator.addSheepWolf(true);
		mapCreator.connect();

		map.setMap(mapCreator.getCreatedMap());

		status.setGameGraph(map);
		((Field) status.getGameGraph().getNodeById(2))
				.addShep(new BlackSheep());
		status.addPlayer(pla);

		ServerGameFinalScore score = new ServerGameFinalScore(status);

		score.calculateScore();

		assertEquals(pla.getFinalScore(), 18);

	}

}
