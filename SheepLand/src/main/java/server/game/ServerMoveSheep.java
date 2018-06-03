package server.game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.model.Field;
import share.game.model.GenericSheep;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;

/**
 * This class as the same function as server move, wich is his super-class, but
 * the move is about the move move sheep
 * 
 * @author andrea bertarin
 * 
 */
public class ServerMoveSheep extends ServerMove {

	private List<Field> optionList;
	NumberedSpace ns;

	public ServerMoveSheep(ServerGameStatus gameStatus, Shepherd actualShepher,
			ServerGameSender sender) {
		super(gameStatus, actualShepher, sender);
		this.optionList = new ArrayList<Field>();
		this.ns = this.actualShepher.getPositioOnMapn();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void findInformation() throws RemoteException {

		// get the node near the position without numbered space
		this.optionList = this.gameGraph.nearNodeWithSheep(ns);
		// create message to send
		Information infoMessage;
		InformationType infoType = null;
		// checking how many of the near nodes are empty

		if (this.optionList.size() == 0) {
			super.sendErrorMove();
		} else {
			infoType = getMessageType(this.optionList.size());
			infoMessage = new Information(-1, infoType,
					this.actualPlayer.getAsPlayer());
			infoMessage.setInformation(this.optionList);

			if (this.optionList.size() == 1) {
				infoMessage.setSecondInformation("ForcedSheep");
			} else {
				infoMessage.setSecondInformation("");
			}

			super.sendInfo(infoMessage);
		}
	}

	/**
	 * check if the black sheep is near
	 * 
	 * @return
	 */
	private Field checkBlackSheep() {
		for (Field x : this.optionList) {
			if (x.isBlackSheepHere() != -1) {
				return x;
			}
		}
		return null;
	}

	/**
	 * Return the message type according to the situation: BLACKSHEEPFORCED: if
	 * is present the black sheep and in one field there are not sheep
	 * MOVEBLACKSHEEP : if is present the black sheep and both the near field
	 * has at least one sheep FORCEDMOVESHEEPINFO : no black sheep and one field
	 * has no sheep MOVESHEEPINFO : no black sheep and both the near field has
	 * at least one sheep
	 * 
	 * @param count
	 * @param fence
	 * @return
	 */
	private InformationType getMessageType(int count) {

		if (checkBlackSheep() != null) {
			return InformationType.MOVEBLACKSHEEP;
		} else if (checkBlackSheep() == null) {
			return InformationType.INFOMOVE;
		}
		return null;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void endMove(Information infoMessage) throws RemoteException {

		// get to Node and put it into the list to send
		Field toNode = (Field) this.gameGraph.getNodeById(((Field) infoMessage
				.getInformation()).getId());

		Field fromNode = (Field) this.gameGraph.getNodeById(getFromNode(toNode)
				.getId());

		if (this.optionList.size() == 1) {
			this.moveAndSend(toNode, fromNode);
		} else {
			this.moveAndSend(fromNode, toNode);
		}
	}

	private void moveAndSend(Field fromNode, Field toNode)
			throws RemoteException {
		GenericSheep sheepToMove;
		// move the sheep from fromfield to tofield
		if (fromNode.numberOfSheepWithOutBlackSheep() == 0
				&& toNode.numberOfSheepWithOutBlackSheep() == 0
				&& fromNode.isBlackSheepHere() != -1) {
			// if in the
			sheepToMove = fromNode.removeBlackSheep();
		} else {
			sheepToMove = fromNode.removeSheep();
		}
		if (sheepToMove != null) {
			toNode.addShep(sheepToMove);
		}

		List<Field> optionToSend = new ArrayList<Field>();
		optionToSend.add(0, fromNode);
		optionToSend.add(1, toNode);

		// send to actual client an end move message
		super.sendEndMove(false, optionToSend, null);

		// create a messsage to send to other player with the information
		Information newInfo = new Information(-1,
				InformationType.OTHERPLAYERMOVESHEEP,
				this.actualPlayer.getAsPlayer());
		newInfo.setInformation(optionToSend);
		super.sendToAll(newInfo);
	}

	private Field getFromNode(Field toNode) {
		for (Field x : this.gameGraph.deleteNumberedSpaceFromNearNode(this.ns)) {
			if (x.getId() != toNode.getId()) {
				return x;
			}
		}
		return null;

	}

}
