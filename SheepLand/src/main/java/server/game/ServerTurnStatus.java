package server.game;

import java.util.ArrayList;
import java.util.List;

import share.game.comunication.RequestType;
import share.game.model.MapHandler.NoReachableNode;
import share.game.model.Shepherd;
import share.game.move.MoveOption;

/**
 * Handle the turn, for example from here is possible to know if the turn is
 * end. Handle the moves, initialize them and return the actual move.
 * 
 * @author andrea
 * 
 */
public class ServerTurnStatus {

	private List<ServerMove> moves;
	private int moveIndex;
	private Shepherd actualShepherd;
	private ServerMoveOptionHandler optionHandler;
	private ServerGameStatus status;

	// max move really is 3, but i have to count also 0
	private static final int MAX_MOVE = 3;

	/**
	 * Constructor
	 * 
	 * @param gameStatus
	 */
	public ServerTurnStatus(ServerGameStatus gameStatus) {
		this.moveIndex = 0;
		this.moves = new ArrayList<ServerMove>();
		this.status = gameStatus;
		this.optionHandler = new ServerMoveOptionHandler(gameStatus);
	}

	/**
	 * set the default value the all the class attributes
	 */
	public void resetTurn() {
		this.moveIndex = 0;
		this.moves = new ArrayList<ServerMove>();
		this.actualShepherd = null;
		this.optionHandler.resetMoveOption();
	}

	/**
	 * Set the shepherd
	 * 
	 * @param actualShepherd
	 */
	public void setShepherd(Shepherd actualShepherd) {
		this.actualShepherd = actualShepherd;
	}

	/**
	 * Return a list with the possible move that the player can do
	 * 
	 * @return
	 * @throws NoReachableNode
	 */
	public List<MoveOption> getPossibleMove() {

		return this.optionHandler.getMoveOption(moveIndex, moves,
				actualShepherd);

	}

	/**
	 * Return the move in progress
	 * 
	 * @return
	 */
	public ServerMove getActualMove() {
		// sub -1 to move index because after init every move the move index is
		// incremented
		// so it is necessary to do this.
		// for example on first move: init so the move index go to 1 but actual
		// move is on
		// position number 0 in the array
		return this.moves.get(moveIndex - 1);
	}

	/**
	 * Check if the turn is end, or else check if the number of move made is the
	 * same as the max move
	 * 
	 * @return
	 */
	public boolean isTurnEnd() {
		// max move is set as a static attribute, and his value is 3, so if the
		// move already done is 3 the turn is end
		return this.moveIndex == MAX_MOVE;
	}

	/**
	 * Init a new move server object, that will be the next move according to
	 * the player choice, plus modify the possible move option for the next move
	 * 
	 * @param moveType
	 * @param status
	 */
	public void initNextMove(RequestType moveType, ServerGameSender sender) {
		if (moveType == RequestType.MOVEBUYFIELD) {
			this.moves.add(moveIndex, new ServerMoveBuyField(status,
					this.actualShepherd, sender));
		} else if (moveType == RequestType.MOVESHEEP) {
			this.moves.add(moveIndex, new ServerMoveSheep(status,
					this.actualShepherd, sender));
		} else if (moveType == RequestType.MOVESHEPHERD) {
			this.moves.add(moveIndex, new ServerMoveShepHerd(status,
					this.actualShepherd, sender));
		} else if (moveType == RequestType.KILL) {
			this.moves.add(moveIndex, new ServerMoveKilling(status,
					this.actualShepherd, sender));
		} else if (moveType == RequestType.COUPLING)
			this.moves.add(moveIndex, new ServerMoveCoupling(status,
					this.actualShepherd, sender));

		if (this.moveIndex <= 3) {
			this.moveIndex++;
		}
	}

	/**
	 * Return the shepherd chose from the player
	 * 
	 * @return
	 */
	public Shepherd getShepherd() {
		return this.actualShepherd;
	}

	/**
	 * Set end this turn, can be useful in many case such as when a player
	 * disconnects during his turn
	 */
	public void setEnd() {
		this.moveIndex = MAX_MOVE;

	}

	/**
	 * Return the index move of this turn
	 * 
	 * @return
	 */
	public int getMoveIndex() {
		return this.moveIndex;
	}

	/**
	 * Dec the move index if it is more than 0 else reset the turn
	 */
	public void decMoveIndex() {
		if (this.moveIndex > 1) {
			this.moveIndex = this.moveIndex - 1;
		}

	}

}
