package client.view.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.Player;
import share.game.model.Shepherd;
import share.game.move.MoveOption;
import share.game.move.ReturnMoveChoice;
import client.view.ClientConvertIndex;
import client.view.ClientGameAbstract;
import client.view.ClientGraphicConstants;
import client.view.gui.map.GuiGame;
import client.view.gui.map.GuiGameLabel;
import client.view.gui.map.GuiMap;
import client.view.gui.map.GuiMapLabel;
import client.view.gui.map.GuiMapMoverLabel;

import com.esotericsoftware.minlog.Log;

/**
 * implement to interact with the game GUI
 * 
 * @author mirko conti
 * 
 */
public class ClientGUIGameImpl extends ClientGameAbstract {
	private static final Color COLOR_PLAYER_GAMING = Color.orange,
			COLOR_PLAYER_DISCONNECTED = Color.gray;

	private GuiGame guiGame;
	private GuiMap guiMap;
	private JFrame jFrame;
	private ClientConvertIndex cci;
	private boolean shepherdToHide = false;
	private List<Shepherd> shepherdToDisable;

	/**
	 * start the game board but hidden
	 */
	public ClientGUIGameImpl() {
		super();
		this.jFrame = new JFrame();
		// generate all the stuff of the map in the main label
		this.guiMap = new GuiMap();
		this.guiMap.createGuiMap(this.jFrame, this);
		// generate all the stuff about the layout grid
		this.guiGame = new GuiGame();
		this.guiGame.createGuiGame(this.jFrame, this.guiMap, this);
		// load the converter of index
		this.cci = new ClientConvertIndex();
		this.guiGame.showForm(false);
	}

	/**
	 * hide and show the form
	 * 
	 * @param show
	 */
	@Override
	public void showGame(boolean show, String name) {
		this.guiGame.showForm(show);
		this.jFrame.setTitle("Sheepland - Player [" + name + "]");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyPlayerPlaying(Player player) {
		updatePlayer(player, true);
		if (this.shepherdToDisable != null) {
			this.guiMap.getListShepHerds()
					.get(this.cci.convertIndex(this.shepherdToDisable.get(0)))
					.getLabel().setEnabled(true);
			this.guiMap.getListShepHerds()
					.get(this.cci.convertIndex(this.shepherdToDisable.get(1)))
					.getLabel().setEnabled(true);
		}
	}

	// **************************************************************

	/**
	 * show a msg and the player have to click on it
	 */
	public void showMsg(String title, String msg) {
		GuiGameLabel dinLabelMsg = this.guiGame.getDinLabelMsg();
		dinLabelMsg.setVisible(true);
		dinLabelMsg.setSelectable(true);
		dinLabelMsg.setText(title, msg, true);
	}

	/**
	 * show a particular message when something go wrong
	 */
	@Override
	public void showMsgErr(String title, String msg) {
		GuiGameLabel dinLabelMsgErr = this.guiGame.getDinLabelMsg();
		dinLabelMsgErr.setVisible(true);
		dinLabelMsgErr.setSelectable(true);
		dinLabelMsgErr.setText(title, msg, true);
		dinLabelMsgErr.setColor(Color.red);
		disableAllPathsAndFields();
	}

	/**
	 * force the user to read the msg and click on it
	 */
	private void disableAllPathsAndFields() {
		for (GuiMapLabel pathToHide : this.guiMap.getListPaths()) {
			pathToHide.getLabel().setVisible(false);
		}
		for (GuiMapLabel fieldToHide : this.guiMap.getListFields()) {
			fieldToHide.getLabel().setVisible(false);
		}
	}

	// **************************************************************

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void syncFieldCard(List<FieldCard> listCardsCheaper,
			List<Integer> numberOfCards) {
		List<GuiGameLabel> listFieldCards = this.guiGame.getListFieldCards();
		for (int i = 0; i < listCardsCheaper.size(); i++) {
			listFieldCards.get(i)
					.setText(
							ClientGraphicConstants.SYMBOL_NUMBER
									+ numberOfCards.get(i),
							ClientGraphicConstants.SYMBOL_MONEY
									+ listCardsCheaper.get(i).getId(), false);
		}
	}

	/**
	 * get all the node (paths and fields)and the players, sync them and count
	 * the number of fence used and show the remain
	 */
	@Override
	public void syncMap(List<Node> nodes, List<Player> players) {
		List<GuiMapLabel> listPaths = this.guiMap.getListPaths();
		List<GuiMapLabel> listFields = this.guiMap.getListFields();
		List<GuiMapLabel> listShepHerds = this.guiMap.getListShepHerds();

		// only if the players on graphic arent loaded yet
		if (listShepHerds.isEmpty()) {
			// load players and theirs shepherds
			this.guiMap.loadAllShepherd(players.size());
			this.guiGame.loadPlayersAndBtns(players.size(), this.guiMap);
		}

		// SYNC all the players
		for (Player i : players) {
			GuiGameLabel updatePlayer = updatePlayer(i, false);
			updatePlayer.setVisible(true);
			if (!i.isAlive()) {
				updatePlayer.setColor(COLOR_PLAYER_DISCONNECTED);
			}
		}
		int numberFence = 0;
		for (Node i : nodes) {
			// SYNC all the paths
			if (i instanceof NumberedSpace) {
				NumberedSpace tmpPath = (NumberedSpace) i;
				// get the local dinamic path
				GuiMapLabel dinPath = listPaths.get(this.cci
						.convertIndex(tmpPath));
				Shepherd sHepherd = tmpPath.getSHepherd();
				if (tmpPath.isFence()) {
					numberFence++;
					dinPath.setFence(false);
				} else if (tmpPath.isFinalFence()) {
					dinPath.setFence(true);
				} else if (sHepherd != null) {
					GuiMapLabel shepHerdToMove = listShepHerds.get(this.cci
							.convertIndex(sHepherd));
					shepHerdToMove.fixPosition(
							ClientGraphicConstants.PATH_POSITION[this.cci
									.convertIndex(tmpPath)], true);
				}
			} else if (i instanceof Field) {
				// SYNC all the fields
				Field tmpField = (Field) i;
				// get the local dinamic field
				GuiMapLabel dinField = listFields.get(this.cci.convertIndex(i));
				if (tmpField.isWolf()) {
					this.guiMap
							.getDinamicWolf()
							.fixPosition(
									ClientGraphicConstants.FIELD_POSITION[this.cci
											.convertIndex(tmpField)], true);
				}
				// -1 means NO BLACK SHEEP
				if (tmpField.isBlackSheepHere() != -1) {
					this.guiMap
							.getDinamicSheepBlack()
							.fixPosition(
									ClientGraphicConstants.FIELD_POSITION[this.cci
											.convertIndex(tmpField)], true);
				}
				// change text with the num of sheep in the field
				dinField.setText(tmpField.numberOfSheepWithOutBlackSheep());
			}
		}
		//
		this.guiGame.getDinCountFence().setText(
				ClientGraphicConstants.SYMBOL_NUMBER
						+ (NUMBER_FENCE_TOT - numberFence), "", false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void playerDisconnected(Player player) {
		GuiGameLabel dinPlayerD = this.guiGame.getListPlayers().get(
				player.getId());
		dinPlayerD.setColor(COLOR_PLAYER_DISCONNECTED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void playerComeBack(Player player) {
		GuiGameLabel dinPlayerCB = this.guiGame.getListPlayers().get(
				player.getId());
		dinPlayerCB.setSelectable(false);
	}

	// **************************************************************

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void askIfMoveBlackSheep(String question) {
		// int answer
		int i = JOptionPane.showConfirmDialog(null, question, "Moving sheeps",
				JOptionPane.YES_NO_OPTION);
		askIfMoveBlackSheepReturn(i == 0);
	}

	// **************************************************************

	/**
	 * update the player after a move with money and color (if is gaming or is
	 * disconnected)
	 * 
	 * @param player
	 * @param playing
	 *            if the player is playing set the color and discolour the other
	 * @return
	 */
	private GuiGameLabel updatePlayer(Player player, boolean playing) {
		GuiGameLabel dinPlayer = this.guiGame.getListPlayers().get(
				player.getId());
		dinPlayer.setText(player.getUsrname(),
				ClientGraphicConstants.SYMBOL_MONEY + player.getMoney(), false);
		if (playing) {
			dinPlayer.setColor(COLOR_PLAYER_GAMING);
			for (GuiGameLabel i : this.guiGame.getListPlayers()) {
				if (!i.equals(dinPlayer)) {
					i.setSelectable(false);
				}
			}
		}
		return dinPlayer;
	}

	/*
	 * set to move the wolf, set the initial position,set the final one and
	 * start the graphic effect
	 */
	@Override
	public void moveWolf(Field fromWhere, Field toWhere) {
		GuiMapMoverLabel moveTimer = new GuiMapMoverLabel(this.jFrame,
				this.guiMap);
		moveTimer.setLabelToMove(this.guiMap.getDinamicWolf(),
				ClientGraphicConstants.FIELD_POSITION[this.cci
						.convertIndex(fromWhere)], false);
		moveTimer.startTimer(ClientGraphicConstants.FIELD_POSITION[this.cci
				.convertIndex(toWhere)]);
		// maybe reduce the number of sheep
		changeNumberOfSheep(toWhere, false);
	}

	/**
	 * set to move the black sheep, set the initial position,set the final one
	 * and start the graphic effect
	 */
	@Override
	public void moveBlack(Field fromWhere, Field toWhere) {
		// -1 because the id of first is 1 but the first of array is 0
		GuiMapMoverLabel moveTimer = new GuiMapMoverLabel(this.jFrame,
				this.guiMap);
		moveTimer.setLabelToMove(this.guiMap.getDinamicSheepBlack(),
				ClientGraphicConstants.FIELD_POSITION[this.cci
						.convertIndex(fromWhere)], false);
		moveTimer.startTimer(ClientGraphicConstants.FIELD_POSITION[this.cci
				.convertIndex(toWhere)]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveBlack(Field fromWhere, Field toWhere, Player player) {
		moveBlack(fromWhere, toWhere);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setShepherdUsed(Shepherd shepherd) {
		this.guiMap.getListShepHerds().get(this.cci.convertIndex(shepherd))
				.getLabel().setEnabled(false);
	}

	/**
	 * colored the label of player that is playing and move its shepherd
	 */
	@Override
	public void moveShepherdOfAPlayer(Shepherd who, NumberedSpace from,
			NumberedSpace where, Player player, int numberFence) {
		try {
			updatePlayer(player, true);
			// move shepherd
			GuiMapLabel shepherdToMove = this.guiMap.getListShepHerds().get(
					this.cci.convertIndex(who));
			GuiMapMoverLabel moveTimer = new GuiMapMoverLabel(this.jFrame,
					this.guiMap);
			if (from.getId() != 0) {
				// just to check if graphical the initial position of shepherd
				// is right
				moveTimer.setLabelToMove(shepherdToMove,
						ClientGraphicConstants.PATH_POSITION[this.cci
								.convertIndex(from)], false);
				this.guiMap.getListPaths().get(this.cci.convertIndex(from))
						.setFence(from.isFinalFence());
			}
			// if start from ship (initial position of shepherd)
			else {
				moveTimer.setLabelToMove(shepherdToMove,
						ClientGraphicConstants.INITIAL_POSITION, false);
			}
			moveTimer.startTimer(ClientGraphicConstants.PATH_POSITION[this.cci
					.convertIndex(where)]);

			GuiGameLabel dinCountFence = this.guiGame.getDinCountFence();
			dinCountFence.setText(ClientGraphicConstants.SYMBOL_NUMBER
					+ (NUMBER_FENCE_TOT - numberFence), "", false);

		} catch (Exception e) {
			Log.error("moveShepherdOfAPlayer", e);
		}
	}

	/**
	 * colored the label of player and move the label sheep move and then hide
	 * it
	 */
	@Override
	public void moveSheepOfAPlayer(Field fromWhere, Field toWhere, Player player) {
		// usually dont change nothing
		updatePlayer(player, true);
		GuiMapMoverLabel moveTimer = new GuiMapMoverLabel(this.jFrame,
				this.guiMap);
		moveTimer.setLabelToMove(this.guiMap.getDinamicSheep(),
				ClientGraphicConstants.FIELD_POSITION[this.cci
						.convertIndex(fromWhere)], true);
		moveTimer.startTimer(ClientGraphicConstants.FIELD_POSITION[this.cci
				.convertIndex(toWhere)]);
		// reduce the number of sheep
		changeNumberOfSheep(fromWhere, false);
		// increase the number of sheep
		changeNumberOfSheep(toWhere, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeFieldCard(FieldCard card, Player player,
			String actualMoney, int numCardBuyed, boolean initialCard) {
		// update money
		updatePlayer(player, false);
		List<GuiGameLabel> listFieldCards = this.guiGame.getListFieldCards();
		GuiGameLabel myCardToChange = listFieldCards.get(this.cci
				.convertIndex(card));
		// just update money not number if -1
		if (numCardBuyed == -1) {
			myCardToChange.setText(myCardToChange.getFirstLine(), actualMoney,
					false);
		} else {
			myCardToChange.setText(ClientGraphicConstants.SYMBOL_NUMBER
					+ numCardBuyed, actualMoney, false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeNumberOfSheep(Field where, boolean coupling) {
		// boolean not used maybe for future grphic effect
		GuiMapLabel dinField = this.guiMap.getListFields().get(
				this.cci.convertIndex(where));
		dinField.setText(where.numberOfSheepWithOutBlackSheep());
	}

	// **************************************************************

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionMove(String title, List<MoveOption> list) {
		List<GuiGameLabel> listChoices2 = this.guiGame.getListChoices();
		for (MoveOption i : list) {
			listChoices2.get(i.ordinal()).setSelectable(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionField(String title, List<Field> list) {
		List<GuiMapLabel> listR = this.guiMap.getListFields();
		for (Field i : list) {
			listR.get(this.cci.convertIndex(i)).setSelectable(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionNumberedSpace(String title, List<NumberedSpace> list) {
		List<GuiMapLabel> listP = this.guiMap.getListPaths();
		for (NumberedSpace i : list) {
			listP.get(this.cci.convertIndex(i)).setSelectable(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionFieldCard(String title, List<FieldCard> list) {
		List<GuiGameLabel> listFC = this.guiGame.getListFieldCards();
		for (FieldCard i : list) {
			listFC.get(this.cci.convertIndex(i)).setSelectable(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionShepherd(String title, List<Shepherd> shepherds) {
		// in gui this is helpful
		showMsg("", title);
		List<NumberedSpace> listPositions = new ArrayList<NumberedSpace>();
		listPositions.add(((Shepherd) shepherds.get(0)).getPositioOnMapn());
		listPositions.add(((Shepherd) shepherds.get(1)).getPositioOnMapn());
		showOptionNumberedSpace(title, listPositions);
		// set true so in the return disable the other shepherd
		this.shepherdToDisable = shepherds;
		// set true meaning on return of a path hide a shedherd
		this.shepherdToHide = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendChoiceAndHideOptions(char type, int index) {
		switch (type) {
		case ReturnMoveChoice.MOVE_TYPE:
			for (GuiGameLabel move : this.guiGame.getListChoices()) {
				move.setSelectable(false);
			}
			this.guiGame.getDinLabelMsg().setVisible(false);
			break;
		case ReturnMoveChoice.FIELD:
			for (GuiMapLabel field : this.guiMap.getListFields()) {
				field.setSelectable(false);
			}
			break;
		case ReturnMoveChoice.NUMERED_SPACE:
			for (GuiMapLabel path : this.guiMap.getListPaths()) {
				path.setSelectable(false);
			}
			if (this.shepherdToHide && this.shepherdToDisable != null) {
				if (this.shepherdToDisable.get(0).getPositioOnMapn().getId() != this.cci
						.convertIndexReturn(index, type)) {
					setShepherdUsed(this.shepherdToDisable.get(0));
				} else {
					setShepherdUsed(this.shepherdToDisable.get(1));
				}
				this.shepherdToHide = false;
			}
			break;
		case ReturnMoveChoice.FIELD_CARD:
			for (GuiGameLabel fc : this.guiGame.getListFieldCards()) {
				fc.setSelectable(false);
			}
			break;
		case ReturnMoveChoice.PLAYER:
			for (GuiGameLabel pl : this.guiGame.getListPlayers()) {
				pl.setSelectable(false);
			}
			break;
		case ReturnMoveChoice.INPUT_BOX:
			this.guiGame.getDinLabelMsg().setVisible(false);
			for (GuiMapLabel pathToShow : this.guiMap.getListPaths()) {
				pathToShow.getLabel().setVisible(true);
			}
			for (GuiMapLabel fieldToShow : this.guiMap.getListFields()) {
				fieldToShow.getLabel().setVisible(true);
			}
			break;
		default:
			Log.error("sendChoiceAndHideOptions", "error switch");
			break;
		}
		// -1 for the ok message box
		if (index != -1) {
			int indexConverted = this.cci.convertIndexReturn(index, type);
			Log.debug("convertIndexReturn coverted", "(" + indexConverted + ")");
			super.showOptionsReturn(type, indexConverted);
		}
	}
	// **************************************************************

}
