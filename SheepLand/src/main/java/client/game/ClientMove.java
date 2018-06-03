package client.game;

import java.rmi.RemoteException;
import java.util.ArrayList;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.model.Field;
import share.game.move.Move;
import client.view.ClientGameAbstract;

import com.esotericsoftware.minlog.Log;

public abstract class ClientMove extends Move {

	// this client chosen graphic interface
	protected ClientGameAbstract gI;
	// this client sender interface
	protected RemoteSendInterface sender;
	protected Object infoReceived;
	protected Object infoToSend;
	private boolean comeBack;

	public ClientMove(RemoteSendInterface sender, ClientGameAbstract gI) {
		super();
		this.gI = gI;
		this.sender = sender;
		this.comeBack = false;
	}

	public void setComeBack(boolean comeBack) {
		this.comeBack = comeBack;
	}

	/**
	 * Once this object is initialized, calling this function with the message
	 * as parameter, show to the player found option so he can make his choice
	 * 
	 * @param infoMessage
	 */
	public void makeYourChoiche(Information infoMessage) {
		this.infoReceived = infoMessage.getInformation();

		this.gI.showQuestionAndOptions(this.infoReceived);
	}

	/**
	 * Receive as parameter, the player' s decision, and send it to server
	 * 
	 * @param nodeID
	 */
	public abstract void sendChoiche(int nodeID);

	/**
	 * Receive the message with the information about the move jut done, in
	 * order to show
	 * 
	 * @param newMessage
	 */
	public abstract void showInfoRmationReceived(Information newMessage);

	/**
	 * Create and send a message of end operation to the server, containing the
	 * information
	 */
	protected void sendInfo() {

		if (!this.comeBack) {
			Information newInfoMessage = new Information(-1,
					InformationType.ENDOPERATION, null);
			newInfoMessage.setInformation(this.infoToSend);

			try {
				this.sender.sendObj(newInfoMessage);
			} catch (RemoteException e) {
				Log.debug("sendInfo", e);
			}

			super.setEnd();
		}
	}

	@SuppressWarnings("unchecked")
	protected Field getFieldFromId(int id) {
		for (Field x : (ArrayList<Field>) this.infoReceived) {
			if (x.getId() == id) {
				return x;
			}
		}
		return null;
	}

}