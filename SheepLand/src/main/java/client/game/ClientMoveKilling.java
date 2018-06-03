package client.game;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.model.Field;
import client.view.ClientGameAbstract;

public class ClientMoveKilling extends ClientMove {

	public ClientMoveKilling(RemoteSendInterface sender, ClientGameAbstract gI) {
		super(sender, gI);
	}

	@Override
	/**
	 * {@inheritDoc}}
	 */
	public void sendChoiche(int nodeID) {
		// get the field chosen
		this.infoToSend = getFieldFromId(nodeID);
		// maybe a recall to make your choice if choice is null

		// call the super class method to send the choice
		super.sendInfo();

	}

	@Override
	public void showInfoRmationReceived(Information newMessage) {
		Field where = (Field) newMessage.getInformation();
		if (where != null) {
			this.gI.notifyChangeNumberOfSheep(where, newMessage.getPlayer(),
					false);
		} else {
			Field whereFailed = (Field) newMessage.getSecondInformation();
			this.gI.showMsg("Killing failed", "In field of type '"
					+ whereFailed.getType() + "'");
		}
		this.setEnd();
	}

}
