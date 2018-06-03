package server.game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.comunication.Message;
import share.game.comunication.Request;
import share.game.comunication.RequestType;
import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.MapCreator;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;
import share.game.model.TypeField;
import share.game.move.MoveOption;

import com.esotericsoftware.minlog.Log;

/**
 * this class controll the game, receive messages from the client and process
 * them, call the class server move, return information to client, ask the
 * client information
 * 
 * @author andrea bertarini
 * 
 */
public class ServerGame {

	private ServerGameStatus statusGame;
	private ServerTurnStatus move;
	private ServerMoveWolfBlackSheep specialMove;
	private ServerGameSender serverGameSender;
	private int shepherdIndex;
	NumberedSpace startPos;

	/**
	 * Constructor
	 * 
	 * @param statusGame
	 *            ,the status of this gsme
	 * @param serverGameSender
	 *            , is class that send the messages
	 */
	public ServerGame(ServerGameStatus statusGame,
			ServerGameSender serverGameSender) {
		this.statusGame = statusGame;
		this.specialMove = new ServerMoveWolfBlackSheep(statusGame);
		this.move = new ServerTurnStatus(statusGame);
		this.shepherdIndex = 0;
		this.serverGameSender = serverGameSender;
	}

	/**
	 * Actually it start the game if it is not paused
	 * 
	 */
	public void firstOfAll() {

		if (!this.statusGame.isPaused()) {

			MapCreator mapCreator = new MapCreator();
			// add the node
			mapCreator.addNodeToMap();
			// connect the node
			mapCreator.connect();
			// add the sheep to map
			mapCreator.addSheepWolf(true);
			// finally add the new map to game status
			this.statusGame.getGameGraph().setMap(mapCreator.getCreatedMap());
			// give to every client 20 of sheepland' s money
			giveMoney();
			// send map
			sendMapAndPlayer();
			// create the deck
			this.statusGame.getCards().setDeck();
			// send initial field card
			sendInitialFieldCard();
			// send a card sync message
			sendCard();
			// send to every client a shepherd sync request
			this.serverGameSender.serverToEveryClient(new Request(-1,
					RequestType.STARTSYNC, null), null, false);
			// get the start position
			this.startPos = mapCreator.getStartPos();
			// init the phase of synchronization
			startShepherdPositioning();
		}
	}

	/**
	 * 
	 */
	private void sendCard() {

		List<FieldCard> listCard = new ArrayList<FieldCard>();
		for (TypeField x : TypeField.getListWithType()) {
			listCard.add(this.statusGame.getCards().getCopyOFCardByType(x));
		}
		for (ServerPlayer x : this.statusGame.getPlayers()) {
			Information info = new Information(-1, InformationType.YOUCARD,
					x.getAsPlayer());

			List<Integer> listNumber = new ArrayList<Integer>();
			for (FieldCard y : listCard) {

				listNumber.add(new Integer(x.getNumberCard(y.getCardType())));
			}

			info.setInformation(listCard);
			info.setSecondInformation(listNumber);
			this.serverGameSender.serverToClient(x, info);
		}

	}

	/**
	 * For every player in this game, get a random card from the initials ones,
	 * then send this card to the player
	 */
	private void sendInitialFieldCard() {
		for (ServerPlayer x : this.statusGame.getPlayers()) {
			Information info = new Information(-1, InformationType.INITIALCARD,
					x.getAsPlayer());
			FieldCard firstCard = this.statusGame.getCards()
					.getRandomInitalFieldCard();
			x.addPlayerCard(firstCard);
			info.setInformation(firstCard);
			info.setSecondInformation(1);
			this.serverGameSender.serverToClient(x, info);
		}

	}

	/**
	 * Create a server move shepherd, it is the first move, with it the player
	 * can placed the shepherd
	 */
	private void startShepherdPositioning() {
		if (!this.statusGame.isPaused()) {
			this.statusGame.setIsPlaying(true);
			ServerPlayer pl = this.statusGame.getActualPlayer();
			if (pl.isAlive()) {

				this.serverGameSender.SendWaitTurnMessage();
				// first reset this turn, then create the shepherd
				this.move.resetTurn();
				Shepherd shep = new Shepherd(this.startPos);
				shep.setCode(getCode());

				pl.addShepherd(shep, this.shepherdIndex);

				this.move.setShepherd(shep);
				this.move.initNextMove(RequestType.MOVESHEPHERD,
						this.serverGameSender);
			}
			try {
				this.move.getActualMove().findInformation();
			} catch (RemoteException e) {
				Log.debug("startShepherdPositioning", e);
			}
		}
	}

	/**
	 * Return the code of the shepherd. if there are more than 2 player the code
	 * is the same as the player index else return a value in function of the
	 * player index and the shepherd index
	 * 
	 * @return
	 */
	private int getCode() {

		return this.statusGame.getPlayerIndex() * 1 + this.shepherdIndex * 2;
	}

	/**
	 * Give 20 or 30 sheepland' s money to every player, according to player
	 * number
	 */
	private void giveMoney() {
		int money = 0;
		if (this.statusGame.getNumberPlayer() == 2) {
			money = 30;
		} else {
			money = 20;
		}

		for (ServerPlayer x : this.statusGame.getPlayers()) {
			x.setMoney(money);
		}

	}

	/**
	 * Send the map and a list of all player to everyone
	 */
	private void sendMapAndPlayer() {
		Information infoMap = new Information(-1, InformationType.SYNC, null);
		infoMap.setInformation(this.statusGame.getGameGraph());
		infoMap.setSecondInformation(this.statusGame.getPlayerForClient());

		this.serverGameSender.serverToEveryClient(infoMap, null, false);
	}

	/**
	 * When a player resurrect, this function should be called In particular
	 * there are 2 case: -player resurected is sync set true the send of the map
	 * -the player is not sync, find 1 for 3 or 4 player, or 2 for 2 player
	 * positions where placed the player' s shepherd if it is possible else the
	 * player will be banned In the end call next move where, if it is necesary
	 * will be snt the map
	 * 
	 * @param playerResurrected
	 */
	public void canIplay(ServerPlayer playerResurrected) {

		this.serverGameSender.serverToEveryClient(new Request(-1,
				RequestType.STOPCOMEBACK, null), playerResurrected, false);

		if (!this.statusGame.isPaused()) {
			// if the player has alredy positioned its shepherd and the game is
			// not paused
			if (this.statusGame.isPlaying()) {

				if (playerResurrected.isSync()) {

					this.serverGameSender.serverToEveryClient(new Request(-1,
							RequestType.ENDSYNC, null), null, false);
					// Check if the map has to be sent
					// send the map
					this.sendMapAndPlayer();
					this.sendCard();
					// and set send map to false
					sleep(2);

					// at the end call next move so the game can go on
					reStartGame();

				} else if (!playerResurrected.isSync()) {

					// Check if the map has to be sent
					// send the map
					this.sendMapAndPlayer();
					this.sendCard();
					// and set send map to false
					sleep(2);

					// this.statusGame.setPlayerIndex(playerResurrected.getId());
					if (playerResurrected.getId() == this.statusGame
							.getActualPlayer().getId()) {
						this.startShepherdPositioning();
					} else {
						this.startShepherdPositioning();
					}
				}
			} else {
				this.firstOfAll();

			}
		}
	}

	private void reStartGame() {

		if (this.move.getMoveIndex() == 0) {
			shepherd();
		} else {
			ServerMove actualMove = this.move.getActualMove();
			if (this.move.isTurnEnd()
					|| actualMove.getProgres() == ServerMoveProgression.ENDMOVE) {
				nextMove();
			} else if (actualMove.getProgres() == ServerMoveProgression.FINDINFORMATION) {
				try {
					actualMove.findInformation();
				} catch (RemoteException e) {
					Log.debug("restartGame", e);
				}
			}
		}
	}

	/**
	 * This function should be called when the timer stop. In this case if the
	 * game has more than 2 player the game will be restarted else the game is
	 * over because there is only one player in the game
	 */
	public void whenTimerStop() {
		if (this.statusGame.numberPlayerAlive() == 1) {
			lastOfAll();
		} else {
			reStartGame();
		}
	}

	/**
	 * Choose which is the next move according to game status, such as send e
	 * move request or a shepherd id request
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void nextMove() {

		if (!this.statusGame.isPaused()) {

			// maybe the game is stil alive but can exist a player that is not
			// alive anymore or he has not money so he can play, in this case
			// is needed to skip this player
			this.untillNextPlayerisAliveAndRich();

			// if the turn is end
			if (this.statusGame.getNextPlayer().isAlive()) {
				if (this.move.isTurnEnd()) {

					if (this.statusGame.isGameEnd()
							&& this.statusGame.lastPlayer()) {
						sleep(4);
						// if this game is in end phase and the last player has
						// made his
						// move
						lastOfAll();
					} else {

						// move randomly the wolf
						sleep(1);
						Information infoWolf = this.specialMove.moveWolf();
						if (((ArrayList<Field>) infoWolf.getInformation())
								.size() == 2) {
							this.serverGameSender.serverToEveryClient(infoWolf,
									null, false);
						}
						// reset the turn
						this.move.resetTurn();

						// /**************************************************************************old
						// turn^
						// at this point increment the player index
						// in order to make the next player the actual player
						this.statusGame.incPlayerIndex();
						// and send to every one a turn of message
						sleep(2);
						this.serverGameSender.SendWaitTurnMessage();
						// move randomly the black sheep
						sleep(2);
						Information infoBlack = this.specialMove
								.moveRandomBlackSheep();
						if (((ArrayList<Field>) infoBlack.getInformation())
								.size() == 2) {
							this.serverGameSender.serverToEveryClient(
									infoBlack, null, false);
						}

						shepherd();
					}

				} else {

					// if the turn is not finished send possible move to
					// player
					this.sendMoveOption();

				}
			}
		}
	}

	private void shepherd() {
		if (this.statusGame.getNumberPlayer() == 2) {
			// if the turn is finished and is a 2 player game
			// ask to player to chose the shepherd
			this.sendShepherIdRequest();
		} else if (this.statusGame.getNumberPlayer() > 2) {
			// if the turn is finished and is a 3 or 4 player
			// game
			Shepherd actuaShep = this.statusGame.getActualPlayer().getShepherd(
					this.statusGame.getActualPlayer().getId());
			this.move.setShepherd(actuaShep);
			// send a move request
			this.sendMoveOption();
		}
	}

	private void sleep(int times) {
		try {
			Thread.sleep(times * 500);
		} catch (InterruptedException e) {
			Log.debug("sleep", e);
		}

	}

	/**
	 * Search the next player that is alive
	 */
	private void untillNextPlayerisAliveAndRich() {
		while (!this.statusGame.getNextPlayer().isAlive()
				|| !this.statusGame.getNextPlayer().isCanPlay()) {
			this.statusGame.incPlayerIndex();
		}

	}

	/**
	 * Get all the information and calculates for every player his final score,
	 * then send it
	 */
	private void lastOfAll() {
		this.statusGame.setAlive(false);
		// calculate the final score for every player
		ServerGameFinalScore finalScore = new ServerGameFinalScore(statusGame);
		finalScore.calculateScore();
         
		// send to every player information about his score
		// the score of the other player and the winner
		for (ServerPlayer x : this.statusGame.getPlayers()) {
			Information info = new Information(-1, InformationType.SCORE,
					x.getAsPlayer());
			info.setInformation(finalScore.getFirst());
			info.setSecondInformation(this.statusGame.getPlayerForClient());
			this.serverGameSender.serverToClient(x, info);
		}
	}

	/**
	 * Send move option to actual player
	 */
	private void sendMoveOption() {

		// create a new Information message with the possible move that the
		// player can do
		Information newInfo = new Information(-1, InformationType.MOVEOPTION,
				this.statusGame.getActualPlayer().getAsPlayer());
		List<MoveOption> listMove = this.move.getPossibleMove();
		if (listMove.size() > 0) {
			newInfo.setInformation(listMove);
			// send the message to the actual player
			this.serverGameSender.serverToClient(
					this.statusGame.getActualPlayer(), newInfo);
		} else {
			this.statusGame.getActualPlayer().setCanPlay(false);
			this.move.setEnd();
			nextMove();
		}

	}

	/**
	 * Send shepherd id request to actual player
	 */
	private void sendShepherIdRequest() {
		Information infoShep = new Information(-1,
				InformationType.TURNSHEPHERD, this.statusGame.getActualPlayer()
						.getAsPlayer());
		infoShep.setInformation(this.statusGame.getActualPlayer()
				.getAllShepher());
		this.serverGameSender.serverToClient(this.statusGame.getActualPlayer(),
				infoShep);
	}

	/**
	 * Receive a message and according to the message dynamics class decide what
	 * to do with the message
	 * 
	 * @param newMessage
	 * @throws RemoteException
	 * @throws CardFieldException
	 */
	public void handleMessage(Message newMessage) {

		if (newMessage.getClass() == Request.class) {
			try {
				handleRequest((Request) newMessage);
			} catch (RemoteException e) {
				Log.debug("handleMessage", e);
			}
		}
		if (newMessage.getClass() == Information.class) {
			try {
				handleInformation((Information) newMessage);
			} catch (RemoteException e) {
				Log.debug("handleMessage", e);
			}
		}
	}

	/**
	 * Receive an information message and handle it depending on the game status
	 * and information type
	 * 
	 * @param newMessage
	 * @throws RemoteException
	 */
	public void handleInformation(Information newMessage)
			throws RemoteException {

		if (newMessage.getInfoType() == InformationType.SHEPHERDID) {
			// if the message contain an id shepherd
			NumberedSpace chooseSpace = (NumberedSpace) this.statusGame
					.getGameGraph().getNodeById(
							(Integer) newMessage.getInformation());
			int shepCode = chooseSpace.getSHepherd().getCode();
			this.move.setShepherd(this.statusGame.getActualPlayer()
					.getShepherd(shepCode));
			// id will be saved
			// the client will received a request to choose the move
			sendMoveOption();
		} else if (newMessage.getInfoType() == InformationType.ENDOPERATION) {
			// & the information type s end operation call end move on
			// actual move
			this.move.getActualMove().endMove(newMessage);
		}

	}

	/**
	 * According to request type, game status and move progress, decide what to
	 * do
	 * 
	 * @param newMessage
	 * @throws RemoteException
	 * @throws CardFieldException
	 */
	public void handleRequest(Request newMessage) throws RemoteException {
		if (newMessage.getReqType() == RequestType.ACK) {

			if (this.statusGame.getSync()) {
				continuePositioning();
			} else if (isOKToNextMove()) {
				nextMove();
			}

		} else if (!this.move.isTurnEnd()
				&& !this.statusGame.getSync()
				&& (newMessage.getReqType() == RequestType.MOVEBUYFIELD
						|| newMessage.getReqType() == RequestType.MOVESHEEP
						|| newMessage.getReqType() == RequestType.MOVESHEPHERD
						|| newMessage.getReqType() == RequestType.COUPLING || newMessage
						.getReqType() == RequestType.KILL)) {
			// if the request type is an answer to a request move and turn is
			// not end
			// init a new move
			this.move.initNextMove(newMessage.getReqType(),
					this.serverGameSender);
			if (newMessage.getReqType() == RequestType.MOVESHEPHERD) {
				// if the player choose to move the shepherd, the fence number
				// will be
				// increase, because is sure that he can move his shepherd
				this.statusGame.incUsedFence();
			}
			// and start the find information phase
			ServerMove actualMove = this.move.getActualMove();
			actualMove.findInformation();

		} else if (!this.move.isTurnEnd() && !this.statusGame.getSync()
				&& newMessage.getReqType() == RequestType.MOVEBLACKSHEEP) {
			// if the request type is move black sheep
			// get the information into an information message
			Information newInfo = this.specialMove
					.playerMoveBlackSheep(this.move.getShepherd());
			this.serverGameSender.serverToEveryClient(newInfo, null, false);
			// in the end set to end actual move
			this.move.getActualMove().setEndMoveStatus();
			nextMove();
		}
	}

	/**
	 * When an ack is receive or a player is positioned randomly and the game is
	 * not yet sync this function will so the positioning can go on!!!!
	 */
	private void continuePositioning() {
		// check if is it possible to set sync the player
		// the player will be synchronized if is a 3 or 4 player game
		// or if it is a 2 player game but if the player index is 1
		if (this.statusGame.getNumberPlayer() > 2) {
			this.statusGame.syncActualPlayer();
		} else if (this.shepherdIndex == 1) {
			this.statusGame.syncActualPlayer();
		}

		if (!this.statusGame.getSync()) {
			this.serverGameSender.serverToEveryClient(new Request(-1,
					RequestType.ENDSYNC, null), null, false);
			this.move.setEnd();
			nextMove();
		} else if (this.statusGame.getNumberPlayer() == 2) {
			if (this.statusGame.getPlayerIndex() == 1) {
				this.shepherdIndex++;
			}
			this.statusGame.incPlayerIndex();
			this.startShepherdPositioning();
		} else if (this.statusGame.getNumberPlayer() > 2) {
			this.statusGame.incPlayerIndex();
			this.startShepherdPositioning();
		}

	}

	private boolean isOKToNextMove() {
		return this.move.getActualMove().getProgres() == ServerMoveProgression.ERRORMOVE
				|| this.move.getActualMove().getProgres() == ServerMoveProgression.ENDMOVE;

	}

}
