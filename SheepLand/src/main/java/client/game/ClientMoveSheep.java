package client.game;

import java.util.ArrayList;
import java.util.List;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.model.Field;
import client.view.ClientGameAbstract;

public class ClientMoveSheep extends ClientMove {

	String secondInfo;

	/**
	 * Constructor
	 * 
	 * @param sender
	 * @param gI
	 */
	public ClientMoveSheep(RemoteSendInterface sender, ClientGameAbstract gI) {
		super(sender, gI);
		this.secondInfo = new String();
	}

	/**
	 * {@inheritDoc}
	 */
	public void makeYourChoiche(Information infoMessage) {
		this.infoReceived = infoMessage.getInformation();
		this.secondInfo = (String) infoMessage.getSecondInformation();

		if (infoMessage.getInfoType() == InformationType.MOVEBLACKSHEEP) {
			this.gI.askIfMoveBlackSheep("Do you want to move the black sheep?");
		} else {
			makeYourChoiceSecond();
		}
	}

	/**
	 * Show to the user the possible option, or let him know that the move will
	 * be do automatically if necessary
	 */
	@SuppressWarnings("unchecked")
	public void makeYourChoiceSecond() {

		if (this.secondInfo.equals("ForcedSheep")) {
			this.gI.showMsg("Sheep will be move automatically", "");
			sendChoiche(((ArrayList<Field>) this.infoReceived).get(0).getId());
		} else {
			makeYourChoiceThird();
		}

	}

	private void makeYourChoiceThird() {
		this.gI.showQuestionAndOptions(this.infoReceived);
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendChoiche(int nodeID) {
		// get the field chosen
		this.infoToSend = getFieldFromId(nodeID);
		// maybe a recall to make your choice if choice is null

		// call the super class method to send the choice
		super.sendInfo();

	}

	@Override
	/**
	 * {@inheritDoc}}
	 */
	public void showInfoRmationReceived(Information newMessage) {
		@SuppressWarnings("unchecked")
		List<Field> infoToShow = (List<Field>) newMessage.getInformation();
		this.gI.moveSheepOfAPlayer(infoToShow.get(0), infoToShow.get(1),
				newMessage.getPlayer());
		this.setEnd();
	}

}