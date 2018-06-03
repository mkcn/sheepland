package client.game;

import java.util.List;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.model.FieldCard;
import share.game.model.Player;
import client.view.ClientGameAbstract;

public class ClientMoveBuyField extends ClientMove {

	public ClientMoveBuyField(RemoteSendInterface sender, ClientGameAbstract gI) {
		super(sender, gI);
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendChoiche(int nodeID) {

		FieldCard card= getCardByType(nodeID);
		super.infoToSend = card;
		super.sendInfo();
	}

	@Override
	/**
	 * {@inheritDoc}}
	 */
	public void showInfoRmationReceived(Information newMessage) {
		FieldCard cardbought = (FieldCard) newMessage.getInformation();
		Player pl = newMessage.getPlayer();
		int numberOfcard = (Integer) newMessage.getSecondInformation();
		this.gI.buyFieldCard(cardbought, pl, numberOfcard, false);
		this.setEnd();
	}

	private FieldCard getCardByType(int index) {
		@SuppressWarnings("unchecked")
		List<FieldCard> cards = (List<FieldCard>) super.infoReceived;
		if (cards.size() == 1) {
			return cards.get(0);
		} else {
			for (FieldCard x : cards) {
				if (x.getCardType().ordinal() == index) {
					return x;
				}
			}
			return null;
		}
	}
}
