package client.game;

import java.util.ArrayList;
import java.util.List;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.model.NumberedSpace;
import client.view.ClientGameAbstract;

public class ClientMoveShepherd extends ClientMove {

	public ClientMoveShepherd(RemoteSendInterface sender, ClientGameAbstract gI) {
		super(sender, gI);
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendChoiche(int nodeID) {
		// get the field chosen
		this.infoToSend = (NumberedSpace) getNumberedSpaceFromId(nodeID);

		super.sendInfo();
	}

	@SuppressWarnings("unchecked")
	private NumberedSpace getNumberedSpaceFromId(int id) {
		for (NumberedSpace x : (ArrayList<NumberedSpace>) this.infoReceived) {
			if (x.getId() == id) {
				return x;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * {@inheritDoc}}
	 */
	public void showInfoRmationReceived(Information newMessage) {
		List<NumberedSpace> infoToShow = (ArrayList<NumberedSpace>) newMessage
				.getInformation();
		this.gI.moveShepherdOfAPlayer(newMessage.getActualShepherd(),
				infoToShow.get(0), infoToShow.get(1), newMessage.getPlayer(),
				(Integer) newMessage.getSecondInformation());

		this.setEnd();
	}

}