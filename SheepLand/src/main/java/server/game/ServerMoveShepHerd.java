package server.game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.comunication.Request;
import share.game.comunication.RequestType;
import share.game.model.MapHandler.NoReachableNode;
import share.game.model.NumberedSpace;
import share.game.model.Player.NoMoreMoney;
import share.game.model.Shepherd;

import com.esotericsoftware.minlog.Log;

/**
 * This class as the same function as server move, wich is his super-class, but
 * the move is about the move move shepherd
 * 
 * @author andrea bertarin
 * 
 */
public class ServerMoveShepHerd extends ServerMove {

	protected static final int DUTY_MOVEMENT = 1;
	private List<NumberedSpace> optionList;

	/**
	 * 
	 * @param gameStatus
	 */
	public ServerMoveShepHerd(ServerGameStatus gameStatus,
			Shepherd actualShepher, ServerGameSender sender) {
		super(gameStatus, actualShepher, sender);
		this.optionList = new ArrayList<NumberedSpace>();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void findInformation() throws RemoteException {

		try {
			// get all the reachable numbered space, except occupied by other
			// shepherd and

			if (this.actualPlayer.getMoney() > 1) {
				this.optionList = this.gameGraph.getAllReachableNode();
			} else {
				this.optionList = this.gameGraph
						.deleteFieldFroNearNode(this.actualShepher
								.getPositioOnMapn());
			}

			// create Message and set option
			Information infoMessage = new Information(-1,
					InformationType.INFOMOVE, this.actualPlayer.getAsPlayer());
			infoMessage.setInformation(this.optionList);
			super.sendInfo(infoMessage);

		} catch (NoReachableNode e) {
			Log.debug("findInformation", e);
			// if the shepherd can' t reach a node send an error move request
			super.sendErrorMove();
		}

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void endMove(Information infoMEssage) throws RemoteException {
		// get the new position
		NumberedSpace newPosition = (NumberedSpace) this.gameGraph
				.getNodeById(((NumberedSpace) infoMEssage.getInformation())
						.getId());
		// and the old position
		NumberedSpace oldPosition = this.actualShepher.getPositioOnMapn();

		// control if the player have to pay
		boolean duty = haveToPay(newPosition);

		// trying to pay
		try {
			if (duty) {
				this.actualPlayer.decMoney(DUTY_MOVEMENT);
			}

			// this array will contain the information to send
			List<NumberedSpace> information = new ArrayList<NumberedSpace>();

			// place a fence on old place
			oldPosition.setFence(super.finalFence);

			// set free the old position on map
			oldPosition.setShepherd(null);
			// move shepherd to new node
			this.actualShepher.setPositionOnMap(newPosition);
			newPosition.setShepherd(actualShepher);

			// and put the old position into the information to put in
			// the message
			information.add(0, oldPosition);

			// and put the old position into the information to put in
			// the message
			information.add(1, newPosition);

			// send end move to actual player
			super.sendEndMove(duty, information,
					this.gameGraph.getFenceNumber());

			Information newInfo = new Information(-1,
					InformationType.OTHERPLAYERSHPHERD,
					this.actualPlayer.getAsPlayer());

			newInfo.setInformation(information);
			newInfo.setSecondInformation(this.gameGraph.getFenceNumber());
			super.sendToAll(newInfo);

		} catch (NoMoreMoney e) {
			Log.debug("endMove", e);
			// if the player has not enough money to move the
			// shepherd it will be warning with a request message
			Request req = new Request(-1, RequestType.NOTENOUGHMONEY,
					this.actualPlayer.getAsPlayer());
			super.sender.serverToClient(actualPlayer, req);
			// and also send to all other player the information
			Information newInfo = new Information(-1,
					InformationType.OTHERPLAYERNOTENOUGHMONEY,
					this.actualPlayer);
			super.sendToAll(newInfo);
		}

	}

	/**
	 * Check if the player have to pay. The player pay only when move the
	 * shepherd onto a not near spaces
	 * 
	 * @param newPosition
	 * @return
	 */
	private boolean haveToPay(NumberedSpace newPosition) {
		// get the old position of the shepherd
		NumberedSpace oldPos = this.actualShepher.getPositioOnMapn();
		// get the node near that old position
		List<NumberedSpace> nearSpace = this.gameGraph
				.deleteFieldFroNearNode(oldPos);
		// for every node check if is equals to the new position
		for (NumberedSpace x : nearSpace) {
			// if the new position is equals to a near node return false
			// because the plae don' t have to pay
			if (x.equals(newPosition)) {
				return false;
			}
		}
		// else false
		return true;
	}

}
