package server.game;

import java.util.ArrayList;
import java.util.List;

import share.game.model.Field;
import share.game.model.Player;
import share.game.model.TypeField;

/**
 * This class has the task to calculate the final score for every player in this
 * game
 * 
 * @author andrea
 * 
 */
public class ServerGameFinalScore {

	private List<Field> fileds;
	private List<ServerPlayer> player;
	private int[] sheepPerField;
	private int max;

	/**
	 * Constructor: receive the game status but get only the player and the
	 * field
	 * 
	 * @param statusGame
	 */
	public ServerGameFinalScore(ServerGameStatus statusGame) {
		this.fileds = statusGame.getGameGraph()
				.deleteNumberedSpaceFromNearNode(null);
		this.player = statusGame.getPlayers();
		this.sheepPerField = new int[6];
		this.max = Integer.MIN_VALUE;
	}

	/**
	 * Return a a list, in which there will be in position 0 the first, in
	 * position the second and so on
	 * 
	 * @return
	 */
	public void calculateScore() {
		fillSheepPerField();
		finalScoreForEveryPlayer();
	}

	/**
	 * For every player calculate his final score
	 */
	private void finalScoreForEveryPlayer() {
		for (ServerPlayer x : this.player) {
			if (x.isAlive()) {
				for (TypeField y : TypeField.getListWithType()) {
					x.setFinalScore(x.getFinalScore()
							+ (x.getNumberCard(y) * this.sheepPerField[y
									.ordinal()]));
				}
				x.setFinalScore(x.getFinalScore() + x.getMoney());
			} else {
				x.setFinalScore(0);
			}

			max = x.getFinalScore() > max ? x.getFinalScore() : max;
		}

	}

	/**
	 * in sheep per field array ,in position i (i corresponds to the ordinal
	 * value of field type) there is the number of the sheep for that field type
	 */
	private void fillSheepPerField() {
		for (Field x : this.fileds) {
			if (x.getType() != TypeField.SHEEPSBURG) {
				sheepPerField[x.getType().ordinal()] = sheepPerField[x
						.getType().ordinal()]
						+ x.numberOfSheepWithOutBlackSheep();
				if (x.isBlackSheepHere() != -1) {
					sheepPerField[x.getType().ordinal()] = sheepPerField[x
							.getType().ordinal()] + 1;
				}
			}
		}
	}

	/**
	 * Return the player who won the game, actually return a list in case two
	 * player have the same final score
	 * 
	 * @return
	 */
	public List<Player> getFirst() {
		List<Player> listPlayer = new ArrayList<Player>();
		for (ServerPlayer x : this.player) {
			if (x.getFinalScore() == max) {
				listPlayer.add(x.getAsPlayer());
			}
		}
		return listPlayer;
	}

}
