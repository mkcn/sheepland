package client.view.clc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import share.game.model.Field;
import share.game.model.FieldCard;
import share.game.model.Node;
import share.game.model.NumberedSpace;
import share.game.model.Player;
import share.game.model.Shepherd;
import share.game.move.MoveOption;
import share.game.move.ReturnMoveChoice;
import client.view.ClientGameAbstract;
import client.view.ClientGraphicConstants;
import client.view.ClientOpenFile;

/**
 * class to implemntet the graphic with CLC
 * 
 * @author mirko conti
 * 
 */
public class ClientCLCGameImpl extends ClientGameAbstract {

	private ClientCLCOutput output;
	private ClientCLCMapViewer mapData;
	private boolean askInfo = true;

	/**
	 * constructor of ClientCLCGameImpl, initialize the output
	 */
	public ClientCLCGameImpl() {
		super();
		this.output = new ClientCLCOutput();
		this.mapData = new ClientCLCMapViewer();
	}

	/**
	 * {@inheritDoc}
	 */
	public void showMsg(String title, String msg) {
		this.output.showMsg(title, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showMsgErr(String title, String msg) {
		this.output.showMsgErr(title, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void syncMap(List<Node> nodes, List<Player> players) {
		int numberFence = NUMBER_FENCE_TOT;
		// sync all the node
		for (Node i : nodes) {
			if (i instanceof NumberedSpace) {
				this.mapData.updatePaths((NumberedSpace) i);
				if (((NumberedSpace) i).isFence()
						|| ((NumberedSpace) i).isFinalFence()) {
					numberFence--;
				}
			} else if (i instanceof Field) {
				this.mapData.updateField((Field) i);
			}
		}
		// all the info about player
		for (Player i : players) {
			this.mapData.updatePlayer(i);
		}
		this.mapData.setNumberOfFence(numberFence);
		showQuestionInfo();
	}

	@Override
	public void syncFieldCard(List<FieldCard> listCardsCheaper,
			List<Integer> numberOfCards) {
		for (int i = 0; i < listCardsCheaper.size(); i++) {
			this.mapData.updateFieldCards(listCardsCheaper.get(i),
					numberOfCards.get(i));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void askIfMoveBlackSheep(String question) {
		boolean inputYesNo = this.output.getInputYesNo(question);
		super.askIfMoveBlackSheepReturn(inputYesNo);
	}

	/**
	 * do nothing only gui
	 * 
	 */
	@Override
	public void playerDisconnected(Player player) {
		this.mapData.updatePlayer(player);
	}

	/**
	 * do nothing only gui
	 */
	@Override
	public void playerComeBack(Player player) {
		this.mapData.updatePlayer(player);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveBlack(Field fromWhere, Field toWhere) {
		this.output.showMsg(
				"Black sheep",
				"moved from field " + fromWhere.getId() + " to "
						+ toWhere.getId());
		this.mapData.updateField(fromWhere);
		this.mapData.updateField(toWhere);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showGame(boolean show, String name) {
		if (show) {
			this.mapData.setNameUser(name);
		}
		// else not care , here there isn't different between login and game
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveShepherdOfAPlayer(Shepherd who, NumberedSpace from,
			NumberedSpace where, Player player, int numberFence) {

		this.output.showMsg(player.getUsrname(),
				"has move his shepherd from path " + from.getId() + " to path "
						+ where.getId());
		if (from.getId() != 0) {
			this.mapData.updatePaths(from);
		}
		this.mapData.updatePaths(where);
		this.mapData.updatePlayer(player);
		this.mapData.setNumberOfFence(NUMBER_FENCE_TOT - numberFence);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveSheepOfAPlayer(Field fromWhere, Field toWhere, Player player) {
		this.output.showMsg(player.getUsrname(),
				" has move a sheep from field " + fromWhere.getId()
						+ " to field " + toWhere.getId());
		this.mapData.updateField(fromWhere);
		this.mapData.updateField(toWhere);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionMove(String title, List<MoveOption> list) {
		if (this.askInfo) {
			showQuestionInfo();
		} else {
			this.askInfo = true;
		}
		int returnValue = this.output.showQuestionAndOptions(
				"Choose your move", list);
		sendChoiceAndHideOptions(ReturnMoveChoice.MOVE_TYPE, returnValue);
	}

	/**
	 * Simply show the map and your card
	 */
	private void showQuestionInfo() {
		List<String> optionInfo = new ArrayList<String>(Arrays.asList("no",
				"all info about game", "open image of game board",
				"open pdf with rules"));

		int returnValue = this.output.showQuestionAndOptions(
				"Do you want show info about game?", optionInfo);
		if (returnValue == 1) {
			this.output.showMsg(" - - - Map - -", this.mapData.toString());
			showQuestionInfo();
		} else if (returnValue == 2) {
			openAFileWithDefaultProgram(ClientGraphicConstants.GAME_BOARD_NUMERED);
			showQuestionInfo();
		} else if (returnValue == 3) {
			openAFileWithDefaultProgram(ClientGraphicConstants.RULES_PDF);
			showQuestionInfo();
		}
		// go on and ask for the choice of move
	}

	private void openAFileWithDefaultProgram(String file) {
		new ClientOpenFile(file).openWithDefaultProgram();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionField(String title, List<Field> list) {
		int returnValue;
		returnValue = this.output.showQuestionAndOptions(title, list);
		sendChoiceAndHideOptions(ReturnMoveChoice.FIELD, returnValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionNumberedSpace(String title, List<NumberedSpace> list) {
		int returnValue;
		returnValue = this.output.showQuestionAndOptions(title, list);
		sendChoiceAndHideOptions(ReturnMoveChoice.NUMERED_SPACE, returnValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionFieldCard(String title, List<FieldCard> list) {
		int returnValue;
		// if two cardField are the same show just one of them
		if (list.size() == 2
				&& list.get(0).getCardType() == list.get(1).getCardType()) {
			list.remove(1);
		}
		returnValue = this.output.showQuestionAndOptions(title, list);
		sendChoiceAndHideOptions(ReturnMoveChoice.FIELD_CARD, returnValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showOptionShepherd(String title, List<Shepherd> shepherds) {
		int returnValue;
		showQuestionInfo();
		this.askInfo = false;
		List<NumberedSpace> listNumeredSpaces = new ArrayList<NumberedSpace>();
		listNumeredSpaces.add(((Shepherd) shepherds.get(0)).getPositioOnMapn());
		listNumeredSpaces.add(((Shepherd) shepherds.get(1)).getPositioOnMapn());

		returnValue = this.output.showQuestionAndOptions(title,
				listNumeredSpaces);
		// differetly from GUI pass the shepherd used to save in data of CLC
		if (shepherds.get(0).getPositioOnMapn().getId() == returnValue) {
			setShepherdUsed(shepherds.get(0));
		} else {
			setShepherdUsed(shepherds.get(1));
		}
		sendChoiceAndHideOptions(ReturnMoveChoice.NUMERED_SPACE, returnValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendChoiceAndHideOptions(char type, int index) {
		super.showOptionsReturn(type, index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveWolf(Field fromWhere, Field toWhere) {
		this.output.showMsg("Wolf", "moved from field " + fromWhere.getId()
				+ " to " + toWhere.getId());
		this.mapData.updateField(fromWhere);
		this.mapData.updateField(toWhere);
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
	public void notifyPlayerPlaying(Player player) {
		String generateSeparateLine = this.mapData.generateSeparateLine('-');
		this.output.showMsg("", generateSeparateLine);
		this.output.showMsg(player.getUsrname(), "is playing");
		// reset choisen shepherd
		this.mapData.setChosenShepherd(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeNumberOfSheep(Field where, boolean coupling) {
		this.mapData.updateField(where);
	}

	@Override
	public void changeFieldCard(FieldCard card, Player player,
			String ActualMoney, int numCardBuyed, boolean initialCard) {
		if (initialCard) {
			this.mapData.setMyInitialFieldCard(card);
		} else {
			this.mapData.updateFieldCards(card, numCardBuyed);
		}
		this.mapData.updatePlayer(player);
	}

	/**
	 * save the shepherd chosen
	 */
	@Override
	protected void setShepherdUsed(Shepherd shepherd) {
		this.mapData.setChosenShepherd(shepherd);
	}
}
