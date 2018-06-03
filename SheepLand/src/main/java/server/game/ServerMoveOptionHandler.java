package server.game;

import java.util.ArrayList;
import java.util.List;

import share.game.model.Field;
import share.game.model.MapHandler.NoReachableNode;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;
import share.game.move.MoveOption;

import com.esotericsoftware.minlog.Log;

/**
 * This class handle the move option, return the possible move for the player
 * that is playing according to the stastus, the position of the chosen shepherd
 * and the previous moves.
 * 
 * @author andrea bertarini
 * 
 */
public class ServerMoveOptionHandler {

	ServerGameStatus gameStatus;
	private List<MoveOption> possibleMove = MoveOption.getAllOption();

	/**
	 * Constructor
	 * 
	 * @param gameStatus
	 */
	public ServerMoveOptionHandler(ServerGameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	/**
	 * Return all the possible option for the next move
	 * 
	 * @param moveIndex
	 * @param moves
	 * @param actualShepherd
	 * @return
	 */
	public List<MoveOption> getMoveOption(int moveIndex,
			List<ServerMove> moves, Shepherd actualShepherd) {
		setOptionMove(moveIndex, moves, actualShepherd);
		return this.possibleMove;
	}

	/**
	 * Reset the option, re-insert all the possible option into the list
	 */
	public void resetMoveOption() {
		possibleMove = MoveOption.getAllOption();
	}

	/**
	 * Decide what move will be available in the next turn
	 * 
	 * @throws NoReachableNode
	 */
	private void setOptionMove(int moveIndex, List<ServerMove> moves,
			Shepherd actualShepherd) {

		this.resetMoveOption();

		if (moveIndex == 2 && !wasMoveaShepherd(moves)) {
			// it is only possible move the shepherd
			this.onlyMoveShepherd();

		} else if (moveIndex == 0
				|| (moveIndex > 0 && moves.get(moveIndex - 1).getClass() == ServerMoveShepHerd.class)) {
			// or if it is the first move
			// or move at least a move is already done and the move maked before
			// is a shepherd move

			this.possibleMove = MoveOption.getAllOption();

		} else if ((moveIndex > 0 && moves.get(moveIndex - 1).getClass() == ServerMoveBuyField.class)) {
			this.deleteOption(MoveOption.BUYFIELD);

		} else if ((moveIndex > 0 && moves.get(moveIndex - 1).getClass() == ServerMoveSheep.class)) {
			this.deleteOption(MoveOption.MOVESHEEP);

		} else if ((moveIndex > 0 && moves.get(moveIndex - 1).getClass() == ServerMoveCoupling.class)) {
			this.deleteOption(MoveOption.COUPLING);

		} else if ((moveIndex > 0 && moves.get(moveIndex - 1).getClass() == ServerMoveKilling.class)) {
			this.deleteOption(MoveOption.KILL);
		}

		// plus will be deleted the move that can be done because of the state
		// of
		// the map, monay of actual player or the number of the sheep
		NumberedSpace position = actualShepherd.getPositioOnMapn();
		ServerPlayer player = this.gameStatus.getActualPlayer();
		List<Field> list = this.gameStatus.getGameGraph()
				.deleteNumberedSpaceFromNearNode(position);

		if (!this.gameStatus.getCards().canBought(list, player.getMoney())) {
			this.deleteOption(MoveOption.BUYFIELD);
		}
		if ((gameStatus.getGameGraph().fieldCanLamb(position).size() == 0)) {
			this.deleteOption(MoveOption.COUPLING);
		}

		if (gameStatus.getGameGraph().nearNodeWithSheep(position).size() == 0) {
			this.deleteOption(MoveOption.MOVESHEEP);
		}

		if ((gameStatus.getGameGraph().nearNodeCanKill(position).size() == 0)) {
			this.deleteOption(MoveOption.KILL);
		}

		try {
			if (gameStatus.getGameGraph().getAllReachableNode().size() == 0) {
				this.deleteOption(MoveOption.MOVESHEPHERD);

			}
		} catch (NoReachableNode e) {
			Log.debug("setMoveOption", e);
			this.deleteOption(MoveOption.MOVESHEPHERD);
		}

	}

	/**
	 * Return true if the shepherd was move at least once
	 * 
	 * @param moves
	 * @return
	 */
	private boolean wasMoveaShepherd(List<ServerMove> moves) {
		return moves.get(0).getClass() == ServerMoveShepHerd.class
				|| moves.get(1).getClass() == ServerMoveShepHerd.class;
	}

	/**
	 * In some case is allowed to move only the shepherd so the list of option
	 * move is fill only with a move shepherd option
	 * 
	 * @return
	 */
	private List<MoveOption> onlyMoveShepherd() {
		this.possibleMove = new ArrayList<MoveOption>();
		this.possibleMove.add(MoveOption.MOVESHEPHERD);
		return this.possibleMove;
	}

	/**
	 * Delete an option from the option list
	 * 
	 * @param optionToRemove
	 */
	private void deleteOption(MoveOption optionToRemove) {
		// for every element check if the element is the same
		// as the option to remove
		for (int i = 0; i < this.possibleMove.size(); i++) {
			if (this.possibleMove.get(i) == optionToRemove) {
				// if it is then will be removed
				this.possibleMove.remove(i);
			}
		}

	}

}
