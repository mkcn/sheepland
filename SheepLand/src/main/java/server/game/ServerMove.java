package server.game;

import java.rmi.RemoteException;
import java.util.List;

import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.comunication.Request;
import share.game.comunication.RequestType;
import share.game.model.MapHandler;
import share.game.model.Shepherd;
import share.game.move.Move;

/**
 * THis class receive message from the server game about the move chosen by the
 * player, then search for the information and send them to the player. Finally
 * it modify the map and notify all the player about the move made by the player
 * 
 * @author andrea bertarini
 * 
 */
public abstract class ServerMove extends Move {

	protected ServerMoveProgression progres;
	protected MapHandler gameGraph;
	protected ServerPlayer actualPlayer;
	protected Shepherd actualShepher;
	protected List<ServerPlayer> otherPlayerPlayer;
	protected ServerGameSender sender;
	protected boolean finalFence;
	protected ServerRandomValue randomValue;

	/**
	 * Constructor, receive game status and get the informatin needed, the game
	 * sender and the shepherd chose by the player
	 * 
	 * @param gameStatus
	 * @param actualShepher
	 * @param sender
	 */
	public ServerMove(ServerGameStatus gameStatus, Shepherd actualShepher,
			ServerGameSender sender) {
		this.gameGraph = gameStatus.getGameGraph();
		this.actualPlayer = gameStatus.getActualPlayer();
		this.actualShepher = actualShepher;
		this.otherPlayerPlayer = gameStatus.getOtherGamers();
		this.progres = ServerMoveProgression.INIT;
		this.sender = sender;
		this.finalFence = gameStatus.isGameEnd();
		this.randomValue = gameStatus.getRandomValue();
	}

	/**
	 * send an ERROR request if the move ends with error
	 * 
	 * @throws RemoteException
	 */
	public void sendErrorMove() throws RemoteException {
		// send error to actual player
		this.sender.serverToClient(this.actualPlayer, new Request(-1,
				RequestType.ERRORMOVE, this.actualPlayer.getAsPlayer()));
		setErrorStatus();
	}

	/**
	 * send an ENDMOVE request if the move ends without error
	 * 
	 * @param secondInfo
	 * 
	 * @throws RemoteException
	 */
	public void sendEndMove(boolean pay, Object optionToSend, Object secondInfo)
			throws RemoteException {
		Information info;

		if (pay) {
			info = new Information(-1, InformationType.ENDMOVEPAY,
					this.actualPlayer.getAsPlayer());
		} else {
			info = new Information(-1, InformationType.ENDMOVE,
					this.actualPlayer.getAsPlayer());
		}

		info.setInformation(optionToSend);
		info.setSecondInformation(secondInfo);
		info.setActualShepherd(actualShepher);
		this.sender.serverToClient(this.actualPlayer, info);
		setEndMoveStatus();
	}

	/**
	 * Send a message to actual player
	 * 
	 * @param messageToSend
	 * @throws RemoteException
	 */
	public void sendInfo(Information messageToSend) throws RemoteException {
		this.progres = ServerMoveProgression.FINDINFORMATION;
		messageToSend.setActualShepherd(actualShepher);
		this.sender.serverToClient(this.actualPlayer, messageToSend);
	}

	/**
	 * Receive a message and send it to all the player ,except for the actual
	 * player
	 * 
	 * @param newInfo
	 */
	public void sendToAll(Information newInfo) {
		newInfo.setActualShepherd(actualShepher);
		this.sender.serverToEveryClient(newInfo, this.actualPlayer, false);
	}

	/**
	 * Return the progress of the move
	 * 
	 * @return
	 */
	public ServerMoveProgression getProgres() {
		return this.progres;
	}

	/**
	 * Receive a message containing the information about the end of the move,
	 * change the status of the map, and notify all the player
	 */
	public abstract void endMove(Information infoMEssage)
			throws RemoteException;

	/**
	 * Find the information about the move, and send them to the player
	 */
	public abstract void findInformation() throws RemoteException;

	/**
	 * Set the class parameter to make the state of the move end
	 */
	public void setEndMoveStatus() {
		this.progres = ServerMoveProgression.ENDMOVE;
		this.setEnd();
	}

	/**
	 * Set the class parameter to make the state of the move error
	 */
	public void setErrorStatus() {
		this.progres = ServerMoveProgression.ERRORMOVE;
		this.setEnd();
	}
}
