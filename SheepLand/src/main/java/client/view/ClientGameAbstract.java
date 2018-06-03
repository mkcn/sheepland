package client.view;

import java.rmi.RemoteException;
import java.util.List;

import server.connection.ServerConnConstants;
import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.Player;
import share.game.model.Shepherd;
import share.game.move.MoveOption;
import client.game.ClientGame;

import com.esotericsoftware.minlog.Log;

/**
 * class between the login and the graphic
 * 
 * @author mirko conti
 * 
 */
public abstract class ClientGameAbstract {

	protected final static int NUMBER_FENCE_TOT = 20;
	private ClientGame clientGame;

	/**
	 * have to call this before use the class
	 * 
	 * @param clientGame
	 */
	public void setClientGame(ClientGame clientGame) {
		this.clientGame = clientGame;
	}

	/**
	 * set the graphic of game visible or not ,in CLC just tell it
	 * 
	 * @param show
	 */
	public abstract void showGame(boolean show, String name);

	/**
	 * called every turn by server to notify which player is playing and reset
	 * the data about shepherd choisen
	 * 
	 * @param show
	 * @param name
	 */
	public abstract void notifyPlayerPlaying(Player player);

	// **************************************************************
	/**
	 * notify new player or all the list of them
	 * 
	 * @param players
	 */
	public void notifyAddPlayers(Player player) {
		String title = "Number of players (" + (player.getId() + 1) + ")";
		if (player.getId() > 0) {
			showMsg(title, "The game will start within "
					+ ServerConnConstants.SEC_TIMEOUT_START_GAME + " sec");
		} else {
			showMsg("", title);
		}

	}

	/**
	 * show a msg if player is disconnected
	 * 
	 * @param player
	 */
	public void notifyPlayerDisconnected(Player player) {
		showMsg("Player (" + player.getUsrname() + ") disconnected",
				"the game will restart within "
						+ ServerConnConstants.SEC_TIMEOUT_COME_BACK + " sec");
		playerDisconnected(player);
	}

	/**
	 * 
	 * @param player
	 */
	public abstract void playerDisconnected(Player player);

	/**
	 * notify to the user that a player is disconnected
	 * 
	 * @param player
	 *            disconnected
	 */
	public void notifyPlayerComeBack(Player player) {
		showMsg("Player comes back", player.getUsrname());
		playerComeBack(player);
	}

	/**
	 * only for gui, change graphic whem a player come back
	 * 
	 * @param player
	 */
	public abstract void playerComeBack(Player player);

	// **************************************************************

	/**
	 * show a public message to notify something to the user, if you need to
	 * show just a msg leave the title = ""
	 * 
	 * @param title
	 * @param msg
	 */
	public abstract void showMsg(String title, String msg);

	/**
	 * show message of error to the user,if you need to show just a msg leave
	 * the title = ""
	 * 
	 * @param title
	 * @param msg
	 */
	public abstract void showMsgErr(String title, String msg);

	// **************************************************************
	/**
	 * 
	 * @param who
	 *            shepherd to move
	 * @param from
	 *            field of start
	 * @param where
	 *            field of destination
	 * @param player
	 *            player that move the shepherd
	 * @param numberFence
	 *            updated number of fence
	 */
	public abstract void moveShepherdOfAPlayer(Shepherd who,
			NumberedSpace from, NumberedSpace where, Player player,
			int numberFence);

	/**
	 * when a move is done send this to everyone and move the sheep
	 * 
	 * @param fromWhere
	 * @param toWhere
	 * @param player
	 *            that move the motion
	 */
	public abstract void moveSheepOfAPlayer(Field fromWhere, Field toWhere,
			Player player);

	/**
	 * change the number of sheep after a copling or a killing
	 * 
	 * @param where
	 * @param player
	 */
	public void notifyChangeNumberOfSheep(Field where, Player player,
			boolean coupling) {
		String title = null;
		if (coupling) {
			title = "Coupling successfully";
		} else {
			title = "Killing successfully";
		}
		showMsg(title, "in a field " + where.getType());
		changeNumberOfSheep(where, coupling);
	}

	/**
	 * change graphic only in GUI
	 * 
	 * @param where
	 *            the field to change
	 * @param coupling
	 *            is coupling or not
	 */
	public abstract void changeNumberOfSheep(Field where, boolean coupling);

	// **************************************************************

	/**
	 * called by server where a turn start
	 * 
	 * @param fromWhere
	 * @param toWhere
	 */
	public abstract void moveBlack(Field fromWhere, Field toWhere);

	/**
	 * black sheep move by a player
	 * 
	 * @param fromWhere
	 * @param toWhere
	 * @param player
	 */
	public abstract void moveBlack(Field fromWhere, Field toWhere, Player player);

	/**
	 * 
	 * @param fromWhere
	 * @param toWhere
	 */
	public abstract void moveWolf(Field fromWhere, Field toWhere);

	/**
	 * notify the type of card buyed or the intial one
	 * 
	 * @param card
	 * @param numCardBuyed
	 * @param initialCard
	 * 
	 */
	public void buyFieldCard(FieldCard card, Player player, int numCardBuyed,
			boolean initialCard) {
		String buyCard = "FieldCard", money;
		if (card.getId() != 4) {
			money = ClientGraphicConstants.SYMBOL_MONEY + card.getId();
		} else {
			// if = 5 means that the cards are sold out
			money = ClientGraphicConstants.SYMBOL_SOLD_OUT;
		}

		// msg to all
		if (numCardBuyed == -1) {
			showMsg(buyCard, player.getUsrname() + " has bought the card "
					+ card.getCardType());
		} else {
			// msg to buyer of card
			showMsg(buyCard,
					player.getUsrname() + " gets a card " + card.getCardType());
		}
		changeFieldCard(card, player, money, numCardBuyed, initialCard);
	}

	/**
	 * change the graphic of player that buy the card and the list of cards
	 * 
	 * @param card
	 * @param player
	 * @param actualMoney
	 * @param numCardBuyed
	 * @param initialCard
	 *            is the initial one?
	 */
	public abstract void changeFieldCard(FieldCard card, Player player,
			String actualMoney, int numCardBuyed, boolean initialCard);

	// -------------------------------------------------
	/**
	 * if choce to move the sheep but there is the black ask to the user if move
	 * it
	 * 
	 * @param question
	 *            to show
	 */
	public abstract void askIfMoveBlackSheep(String question);

	/**
	 * return the choce of askIfMoveBlackSheep
	 * 
	 * @param
	 */
	public void askIfMoveBlackSheepReturn(boolean yes) {
		this.clientGame.handleInterfaceEvent(yes);
	}

	/**
	 * when game start or restart the game have to sync with the server
	 * 
	 * @param nodes
	 * @param players
	 */
	public abstract void syncMap(List<Node> nodes, List<Player> players);

	/**
	 * when player come back need to sync the cards after the map
	 */
	public abstract void syncFieldCard(List<FieldCard> listCardsCheaper,
			List<Integer> numberOfCards);

	// **************************************************************

	/**
	 * must be called with a generic Arraylist of field|| NumberedSpace||
	 * FieldCard || Shepherd
	 * 
	 * @param listViewGenericWithoptionsToShow
	 */
	@SuppressWarnings("unchecked")
	public void showQuestionAndOptions(Object listViewGenericWithoptionsToShow) {
		List<Object> listObj = (List<Object>) listViewGenericWithoptionsToShow;
		if (listObj.get(0) instanceof Field) {
			// Three case: move a sheep , coupling two sheep and kill a sheep
			showOptionField("Choose a field",
					(List<Field>) listViewGenericWithoptionsToShow);
		} else if (listObj.get(0) instanceof NumberedSpace) {
			showOptionNumberedSpace("Where do you want to move your shepherd?",
					(List<NumberedSpace>) listViewGenericWithoptionsToShow);
		} else if (listObj.get(0) instanceof FieldCard) {
			// Two case buy and sell
			showOptionFieldCard("Choose a cardfield",
					(List<FieldCard>) listViewGenericWithoptionsToShow);
		} else if (listObj.get(0) instanceof Shepherd) {
			// Chose shepherd to move
			showOptionShepherd("Choose the shepherd",
					(List<Shepherd>) listViewGenericWithoptionsToShow);
		} else if (listObj.get(0) instanceof MoveOption) {
			// choose a move when it's your turn
			showOptionMove("Choose a move",
					(List<MoveOption>) listViewGenericWithoptionsToShow);
		} else {
			Log.error("showQuestionAndOptions", "error switch");
		}
	}

	/**
	 * emphasize a shepherd when the player have to choose one of 2
	 * 
	 * @param shepherd
	 */
	protected abstract void setShepherdUsed(Shepherd shepherd);

	/**
	 * ask to the user to chose between a list of Move
	 * 
	 * @param title
	 * @param list
	 */
	public abstract void showOptionMove(String title, List<MoveOption> list);

	/**
	 * ask to the user to chose between a list of Field
	 * 
	 * @param title
	 * @param list
	 */
	public abstract void showOptionField(String title, List<Field> list);

	/**
	 * ask to the user to chose between a list of NumberedSpace
	 * 
	 * @param title
	 * @param list
	 */
	public abstract void showOptionNumberedSpace(String title,
			List<NumberedSpace> list);

	/**
	 * ask to the user to chose between a list of FieldCard
	 * 
	 * @param title
	 * @param list
	 */
	public abstract void showOptionFieldCard(String title, List<FieldCard> list);

	/**
	 * ask to the user to chose between a list of Shepherd, called only if
	 * number of players == 2
	 * 
	 * @param title
	 * @param shepherds
	 */
	public abstract void showOptionShepherd(String title,
			List<Shepherd> shepherds);

	/**
	 * called only from gui after a choice to hide the options and so disable
	 * the click
	 * 
	 * @param type
	 */
	public abstract void sendChoiceAndHideOptions(char type, int index);

	/**
	 * all the showOption return here
	 * 
	 * @param index
	 *            get the integer choisen
	 */
	public void showOptionsReturn(char type, int index) {
		try {
			this.clientGame.handleInterfaceEvent(type, index);
		} catch (RemoteException e) {
			Log.debug("showQuestionAndOptionsReturn", e);
			showMsgErr("question and options return",
					"RemoteException send choice");
		}
	}

}