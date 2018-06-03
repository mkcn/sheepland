package client.game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import share.connection.RemoteSendInterface;
import share.game.comunication.Information;
import share.game.comunication.InformationType;
import share.game.comunication.Message;
import share.game.comunication.Request;
import share.game.comunication.RequestType;
import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.MapHandler;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.Player;
import share.game.model.Shepherd;
import share.game.move.MoveOption;
import share.game.move.ReturnMoveChoice;
import client.view.ClientGameAbstract;

import com.esotericsoftware.minlog.Log;

/**
 * The main task of this class is to receive the message and switch them. Once
 * recived the message get the information and call the view to interact with
 * the user. Plus send message to the server
 * 
 * @author andrea bertarini
 * 
 */
public class ClientGame {

	private RemoteSendInterface remoteServer;
	private ClientGameAbstract graphicGame;
	private ClientMove actualMove = null;
	private MapHandler map = null;
	private boolean shepIndexRequest = false;
	private boolean shepSync = true;
	private boolean comeBack = false;

	/**
	 * initialize with the interface where call the send and the ping
	 * 
	 * @param remoteServerInterface
	 */
	public ClientGame(RemoteSendInterface remoteServerInterface) {
		this.remoteServer = remoteServerInterface;
	}

	/**
	 * Return the graphic interface chosen by this player
	 * 
	 * @return
	 */
	public ClientGameAbstract getGrafic() {
		return this.graphicGame;
	}

	public void setGrafic(ClientGameAbstract graficGame) {
		this.graphicGame = graficGame;
	}

	/**
	 * called by RemoteClient to receive msg from server
	 * 
	 * @param obj
	 */
	public void serverToClient(Object obj) {
		this.graphicGame.showMsg("From server", obj.toString());
		if (obj.getClass() == Message.class) {
			this.handleMessage((Message) obj);
		}
	}

	/**
	 * Receive a message and according to the message dynamics class send this
	 * message to handle information or request
	 * 
	 * @param newMessage
	 * @throws RemoteException
	 * @throws CardFieldException
	 */
	public void handleMessage(Message newMessage) {

		try {
			if (newMessage.getClass() == Request.class) {
				handleRequest((Request) newMessage);
			} else if (newMessage.getClass() == Information.class) {
				handleInformation((Information) newMessage);
			}
		} catch (Exception e) {
			Log.debug("handleMessage", e);
		}
	}

	/**
	 * Received a Information message and decide what to do, decide which method
	 * of the grafic class call
	 * 
	 * @param newMessage
	 * @throws RemoteException
	 */
	@SuppressWarnings("unchecked")
	private void handleInformation(Information newMessage)
			throws RemoteException {
		// if the information message contain info about the move call make your
		// choice
		if ((!this.comeBack || this.shepSync)
				&& (newMessage.getInfoType() == InformationType.INFOMOVE || newMessage
						.getInfoType() == InformationType.MOVEBLACKSHEEP)) {
			if (this.shepSync) {
				this.actualMove = new ClientMoveShepherd(this.remoteServer,
						this.graphicGame);
			}
			this.actualMove.makeYourChoiche(newMessage);
		} else if (newMessage.getInfoType() == InformationType.MOVEOPTION) {
			this.graphicGame
					.showQuestionAndOptions((ArrayList<MoveOption>) newMessage
							.getInformation());

		} else if (newMessage.getInfoType() == InformationType.OTHERPLAYERBUY) {
			this.graphicGame.buyFieldCard(
					(FieldCard) newMessage.getInformation(),
					newMessage.getPlayer(),
					((Integer) newMessage.getSecondInformation()).intValue(),
					false);

		} else if (newMessage.getInfoType() == InformationType.OTHERPLAYERMOVESHEEP) {
			List<Field> infoMove = (ArrayList<Field>) newMessage
					.getInformation();
			this.graphicGame.moveSheepOfAPlayer(infoMove.get(0),
					infoMove.get(1), newMessage.getPlayer());

		} else if (newMessage.getInfoType() == InformationType.OTHERPLAYERSHPHERD) {
			List<NumberedSpace> infoMove = (ArrayList<NumberedSpace>) newMessage
					.getInformation();
			Shepherd shepherdMoved = (Shepherd) newMessage.getActualShepherd();
			this.graphicGame.moveShepherdOfAPlayer(shepherdMoved,
					infoMove.get(0), infoMove.get(1), newMessage.getPlayer(),
					(Integer) newMessage.getSecondInformation());

		} else if (newMessage.getInfoType() == InformationType.SYNC) {
			// in this case receive a message containing
			// the player list
			List<Player> players = (ArrayList<Player>) newMessage
					.getSecondInformation();
			this.map = (MapHandler) newMessage.getInformation();

			this.graphicGame.syncMap((List<Node>) this.map.getMapNode(),
					players);

		} else if (newMessage.getInfoType() == InformationType.YOUCARD) {
			this.graphicGame.syncFieldCard(
					(ArrayList<FieldCard>) newMessage.getInformation(),
					(ArrayList<Integer>) newMessage.getSecondInformation());

		} else if (newMessage.getInfoType() == InformationType.NEWPLAYER) {
			this.graphicGame.notifyAddPlayers((Player) newMessage
					.getInformation());

		} else if (newMessage.getInfoType() == InformationType.PLAYERCOMEBACK) {
			this.comeBack = true;
			this.actualMove.setComeBack(this.comeBack || this.shepSync);
			this.graphicGame.notifyPlayerComeBack(newMessage.getPlayer());

		} else if (newMessage.getInfoType() == InformationType.PLAYERDISCONNECT) {
			this.graphicGame.notifyPlayerDisconnected(newMessage.getPlayer());
		} else if (newMessage.getInfoType() == InformationType.ENDMOVE
				|| newMessage.getInfoType() == InformationType.ENDMOVEPAY) {

			this.actualMove.showInfoRmationReceived(newMessage);
			sendAck();

		} else if (newMessage.getInfoType() == InformationType.OTHERPLAYERCOUPLING) {
			Field field = (Field) newMessage.getInformation();
			if (newMessage.getInformation() != null) {
				this.graphicGame.notifyChangeNumberOfSheep(field,
						newMessage.getPlayer(), true);
			} else {
				Field field2 = (Field) newMessage.getSecondInformation();
				this.graphicGame.showMsg("Coupling failed ",
						"in field of type '" + field2.getType() + "'");
			}
		} else if (newMessage.getInfoType() == InformationType.TURNSHEPHERD) {
			this.shepIndexRequest = true;
			this.graphicGame
					.showQuestionAndOptions(newMessage.getInformation());
		} else if (newMessage.getInfoType() == InformationType.INITIALCARD) {
			this.graphicGame.buyFieldCard(
					(FieldCard) newMessage.getInformation(),
					newMessage.getPlayer(),
					(Integer) newMessage.getSecondInformation(), true);
		} else if (newMessage.getInfoType() == InformationType.OTHERPLAYKILL) {
			Field field = (Field) newMessage.getInformation();
			if (newMessage.getInformation() != null) {
				this.graphicGame.notifyChangeNumberOfSheep(field,
						newMessage.getPlayer(), false);
			} else {
				Field field2 = (Field) newMessage.getSecondInformation();
				this.graphicGame.showMsg("Killing failed ",
						"in field of type '" + field2.getType() + "'");
			}
		} else if (newMessage.getInfoType() == InformationType.BLACKRNDMMOVE) {
			if (newMessage.getInformation() != null) {
				List<Field> blackMove = (ArrayList<Field>) newMessage
						.getInformation();
				Field toField = blackMove.get(0);
				Field fromField = blackMove.get(1);
				this.graphicGame.moveBlack(fromField, toField);
			}
		} else if (newMessage.getInfoType() == InformationType.WOLFMOVE) {
			List<Field> wolfMove = (List<Field>) newMessage.getInformation();
			this.graphicGame.moveWolf(wolfMove.get(1), wolfMove.get(0));
		} else if (newMessage.getInfoType() == InformationType.WOLFEAT) {
			List<Field> wolfMove = (List<Field>) newMessage.getInformation();
			this.graphicGame.moveWolf(wolfMove.get(1), wolfMove.get(0));
			this.graphicGame.showMsg("The wolf eats a sheep", "");
		} else if (newMessage.getInfoType() == InformationType.ENDOPERATIONBLACK) {
			List<Field> blackMove = (ArrayList<Field>) newMessage
					.getInformation();
			Field toField = blackMove.get(0);
			Field fromField = blackMove.get(1);
			this.graphicGame.moveBlack(fromField, toField,
					newMessage.getPlayer());
		} else if (newMessage.getInfoType() == InformationType.SCORE) {
			showScore(newMessage);
		}
	}

	@SuppressWarnings("unchecked")
	private void showScore(Information newMessage) {
		String title = new String();
		String message = new String("");
		List<Player> listFirst = (List<Player>) newMessage.getInformation();
		List<Player> listPlayers = (List<Player>) newMessage
				.getSecondInformation();
		if (listFirst.size() == 1) {
			title = "The winner is:";
		} else {
			title = "The winners are:";
		}

		for (Player x : listFirst) {
			title = title.concat(x.getUsrname());
		}

		for (Player x : listPlayers) {
			message = message.concat("(" + x.getUsrname() + ","
					+ Integer.toString(x.getFinalScore()) + ")" + " ");
		}

		this.graphicGame.showMsg(title, "Point:" + message);

	}

	/**
	 * Send an ack message
	 */
	private void sendAck() {
		try {
			this.remoteServer.sendObj(new Request(-1, RequestType.ACK, null));
		} catch (RemoteException e) {
			Log.debug("sendAck", e);
		}
	}

	/**
	 * Receive a request message and according to status of the client game
	 * decide what to do
	 * 
	 * @param newMessage
	 * @throws RemoteException
	 */
	private void handleRequest(Request newMessage) throws RemoteException {

		if (newMessage.getReqType() == RequestType.STARTGAME) {
			this.graphicGame.showMsg("", RequestType.STARTGAME.toString());
			this.actualMove = new ClientMoveShepherd(this.remoteServer,
					this.graphicGame);
		} else if (newMessage.getReqType() == RequestType.STARTSYNC) {
			this.shepSync = true;
		} else if (newMessage.getReqType() == RequestType.ENDSYNC) {
			this.shepSync = false;
			if (this.actualMove != null) {
				this.actualMove.setComeBack(comeBack || shepSync);
			}
		} else if (newMessage.getReqType() == RequestType.TURNOF) {
			this.graphicGame.notifyPlayerPlaying(newMessage.getPlayer());
		} else if (newMessage.getReqType() == RequestType.STOPCOMEBACK) {
			this.comeBack = false;
			this.actualMove.setComeBack(this.comeBack || this.shepSync);
		}

	}

	/**
	 * it is called by client game graphic interface, receive the choice of the
	 * player and call a method, send choice, in client game, that send the
	 * choice to the server
	 * 
	 * @param type
	 * 
	 * @param choice
	 * 
	 * @throws RemoteException
	 */
	public void handleInterfaceEvent(char type, int choice)
			throws RemoteException {

		if (type == ReturnMoveChoice.MOVE_TYPE) {
			this.handleInterfaceEvent(MoveOption.returnOptionByIndex(choice));
		} else {
			if (this.shepIndexRequest) {
				sendShepherdId(choice);
				this.shepIndexRequest = false;
			} else if (!this.actualMove.isEnd()) {
				this.actualMove.sendChoiche(choice);
			}
		}
	}

	/**
	 * Called by graphic interface, receive an integer value, that is the choice
	 * of the player
	 * 
	 * @param choice
	 */
	public void handleInterfaceEvent(boolean choice) {
		if (this.actualMove.getClass() == ClientMoveSheep.class && choice) {
			// if the actual move is a sheep move and the choice is true
			// then the player has decide to move the black sheep
			Request req = new Request(-1, RequestType.MOVEBLACKSHEEP, null);
			try {
				this.remoteServer.sendObj(req);
				this.actualMove.setEnd();
			} catch (RemoteException e) {
				Log.debug("handleInterface", e);
			}
		} else if (this.actualMove.getClass() == ClientMoveSheep.class
				&& !choice) {
			ClientMoveSheep move = (ClientMoveSheep) this.actualMove;
			move.makeYourChoiceSecond();
		}
	}

	/**
	 * Receive a value from the graphic interface, and decide what to do
	 * 
	 * @param choice
	 * @throws RemoteException
	 */
	private void handleInterfaceEvent(MoveOption choice) throws RemoteException {

		// if the this function is called when the move i finished or null, such
		// as during the first
		if (this.actualMove == null || this.actualMove.isEnd()) {
			// the choice is about the move option
			Request reqMove = null;
			if (choice == MoveOption.MOVESHEEP) {
				this.actualMove = new ClientMoveSheep(this.remoteServer,
						this.graphicGame);
				reqMove = new Request(-1, RequestType.MOVESHEEP, null);
			} else if (choice == MoveOption.MOVESHEPHERD) {
				this.actualMove = new ClientMoveShepherd(this.remoteServer,
						this.graphicGame);
				reqMove = new Request(-1, RequestType.MOVESHEPHERD, null);
				this.actualMove = new ClientMoveShepherd(this.remoteServer,
						this.graphicGame);
			} else if (choice == MoveOption.BUYFIELD) {
				this.actualMove = new ClientMoveBuyField(this.remoteServer,
						this.graphicGame);
				reqMove = new Request(-1, RequestType.MOVEBUYFIELD, null);
			} else if (choice == MoveOption.COUPLING) {
				this.actualMove = new ClientMoveCoupling(this.remoteServer,
						this.graphicGame);
				reqMove = new Request(-1, RequestType.COUPLING, null);
			} else if (choice == MoveOption.KILL) {
				this.actualMove = new ClientMoveKilling(this.remoteServer,
						this.graphicGame);
				reqMove = new Request(-1, RequestType.KILL, null);
			}

			this.remoteServer.sendObj(reqMove);
		}
	}

	/**
	 * Send a message with the shepherd choosen
	 */
	private void sendShepherdId(int choice) {
		Information info = new Information(-1, InformationType.SHEPHERDID, null);
		info.setInformation(choice);
		try {
			this.remoteServer.sendObj(info);
		} catch (RemoteException e) {
			Log.debug("sendShepherdId", e);
		}

	}

}
