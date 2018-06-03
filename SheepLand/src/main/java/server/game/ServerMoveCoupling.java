package server.game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.model.Field;
import share.game.model.GenericSheep;
import share.game.model.NumberedSpace;
import share.game.model.SheepType;
import share.game.model.Shepherd;

/**
 * This class as the same function as server move, wich is his super-class, but
 * the move is about the move coupling
 * 
 * @author andrea bertarin
 * 
 */
public class ServerMoveCoupling extends ServerMove {

	private List<Field> optionList;

	public ServerMoveCoupling(ServerGameStatus gameStatus,
			Shepherd actualShepher, ServerGameSender sender) {
		super(gameStatus, actualShepher, sender);
		this.optionList = new ArrayList<Field>();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void findInformation() throws RemoteException {
		// get the actual position
		NumberedSpace position = this.actualShepher.getPositioOnMapn();

		// get the field near actual position where is possibe to lamb
		this.optionList = this.gameGraph.fieldCanLamb(position);

		// control how many field are ok to lamb and the decide what kind of
		// message send
		if (this.optionList.size() == 0) {
			super.sendErrorMove();
		} else {
			Information info;

			info = new Information(-1, InformationType.INFOMOVE,
					this.actualPlayer.getAsPlayer());

			// finally set and send the information
			info.setInformation(optionList);
			super.sendInfo(info);
		}

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void endMove(Information infoMEssage) throws RemoteException {
		
		Field field = (Field) this.gameGraph
				.getNodeById(((Field) infoMEssage.getInformation()).getId());
		
		// get the numbered space value
		int value = this.actualShepher.getPositioOnMapn().getValue();

		int diceValue = randomValue.getDiceValue();
		Information infoToOther = new Information(-1,
				InformationType.OTHERPLAYERCOUPLING,
				this.actualPlayer.getAsPlayer());

		if (diceValue == value) {
			
			field.addShep(new GenericSheep(SheepType.SHEEP));

			infoToOther.setInformation(field);
			super.sendEndMove(false, field, field);
		} else {

			infoToOther.setInformation(null);
			super.sendEndMove(false, null, field);
		}

		super.sendToAll(infoToOther);
	}
}
