package server.game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.comunication.Request;
import share.game.comunication.RequestType;
import share.game.model.Deck;
import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.NumberedSpace;
import share.game.model.Player.NoMoreMoney;
import share.game.model.Shepherd;

import com.esotericsoftware.minlog.Log;

/**
 * This class as the same function as server move, wich is his super-class, but
 * the move is about the move buy field
 * 
 * @author andrea bertarin
 * 
 */
public class ServerMoveBuyField extends ServerMove {

	private Deck cards;
	private List<FieldCard> optionList;

	public ServerMoveBuyField(ServerGameStatus gameStatus,
			Shepherd actualShepher, ServerGameSender sender) {
		super(gameStatus, actualShepher, sender);
		this.optionList = new ArrayList<FieldCard>();
		this.cards = gameStatus.getCards();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void findInformation() throws RemoteException {

		NumberedSpace ns = this.actualShepher.getPositioOnMapn();
		// getting near node without numbered space
		List<Field> requiredField = this.gameGraph
				.deleteNumberedSpaceFromNearNode(ns);

		// comparison between remaining field card and near node
		createOptionList(requiredField);

		if (this.optionList.size() == 0) {
			super.sendErrorMove();
		} else {

			Information infoMessage = new Information(-1,
					InformationType.INFOMOVE, this.actualPlayer.getAsPlayer());
			infoMessage.setInformation(this.optionList);

			super.sendInfo(infoMessage);

		}
	}

	/**
	 * Check if the near fields have a field type you can buy
	 * 
	 * @param requiredField
	 */
	private void createOptionList(List<Field> requiredField) {
		FieldCard tempcard;
		for (Field x : requiredField) {
			// if exist a card of x.getType
			if (this.cards.checkIfIsIn(x.getType())) {
				tempcard = this.cards.getCopyOFCardByType(x.getType());
				// then will be add to the option list
				this.optionList.add(tempcard);
			}
		}

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void endMove(Information infoMessage) throws RemoteException {
		// get the chosen card
		FieldCard choosenCard = (FieldCard) infoMessage.getInformation();
		// remove it

		try {
			this.cards.removeCardById(choosenCard);
			this.actualPlayer.decMoney(choosenCard.getId());
			this.actualPlayer.addPlayerCard(choosenCard);

			Information newInfo = new Information(-1,
					InformationType.OTHERPLAYERBUY,
					this.actualPlayer.getAsPlayer());

			FieldCard nextCard = this.cards.getCopyOFCardByType(choosenCard
					.getCardType());
			newInfo.setInformation(nextCard);
			newInfo.setSecondInformation(-1);
			super.sendToAll(newInfo);

			// send end move with false because already know he has to pay
			super.sendEndMove(false, nextCard,
					this.actualPlayer.getNumberCard(choosenCard.getCardType()));

		} catch (NoMoreMoney e) {
			Log.debug("endMove", e);
			// if the player has not enough money to buy it will be warning
			// with a request message
			Request req = new Request(-1, RequestType.NOTENOUGHMONEY,
					this.actualPlayer.getAsPlayer());
			super.sender.serverToClient(actualPlayer, req);
			// and send to all other player the information
			Information newInfo = new Information(-1,
					InformationType.OTHERPLAYERNOTENOUGHMONEY,
					this.actualPlayer);
			super.sendToAll(newInfo);
		}

	}

}
