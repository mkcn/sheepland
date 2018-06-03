package server.game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.model.Field;
import share.game.model.NumberedSpace;
import share.game.model.Shepherd;

/**
 * This class as the same function as server move, wich is its super-class, but
 * the move is about the move kill
 * 
 * @author andrea bertarin
 * 
 */
public class ServerMoveKilling extends ServerMove {

	List<Field> optionList;

	public ServerMoveKilling(ServerGameStatus gameStatus,
			Shepherd actualShepher, ServerGameSender sender) {
		super(gameStatus, actualShepher, sender);
		this.optionList = new ArrayList<Field>();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void findInformation() throws RemoteException {
		// get actual position on map of the shepherd
		NumberedSpace ns = this.actualShepher.getPositioOnMapn();
		// get near node with at least a sheep
		this.optionList = this.gameGraph.nearNodeCanKill(ns);

		// control how many field are ok to lamb and the decide what kind of
		// message send
		if (this.optionList.size() == 0) {
			super.sendErrorMove();
		} else {
			Information info;

			info = new Information(-1, InformationType.INFOMOVE,
					this.actualPlayer.getAsPlayer());
			// finally set and send the information
			info.setInformation(this.optionList);
			super.sendInfo(info);
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void endMove(Information infoMEssage) throws RemoteException {

		Field field = (Field) this.gameGraph.getNodeById(((Field) infoMEssage
				.getInformation()).getId());

		// get the numbered space value
		int value = this.actualShepher.getPositioOnMapn().getValue();

		int diceValue = super.randomValue.getDiceValue();

		Information infoToOther = new Information(-1,
				InformationType.OTHERPLAYKILL, this.actualPlayer.getAsPlayer());
		if (diceValue == value) {

			field.removeSheep();

			infoToOther.setInformation(field);
			infoToOther.setSecondInformation(field);
			super.sendEndMove(false, field, field);

		} else {
			infoToOther.setInformation(null);
			infoToOther.setSecondInformation(field);
			super.sendEndMove(false, null, field);
		}

		super.sendToAll(infoToOther);
	}
}
