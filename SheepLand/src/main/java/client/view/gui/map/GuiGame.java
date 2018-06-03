package client.view.gui.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import share.game.move.ReturnMoveChoice;
import client.view.ClientGameAbstract;
import client.view.ClientGraphicConstants;
import client.view.ClientOpenFile;

/**
 * class that generate the form of GAME
 * 
 * @author mirko conti
 * 
 */
public class GuiGame {

	private JFrame jFrame;
	private GuiGameLabel dinLabelMsg, dinCountFence;

	private Container pane;
	private GridBagConstraints gridBag;
	private GuiLoadImageResource resorce;
	private static final int WIDTH_BORD_PLAYERS = 90;
	private static final int HEIGHT_BORD_CHOICES = 10;

	// all the list to able the select
	private List<GuiGameLabel> listChoices, listFieldCards, listPlayers;

	/**
	 * hide or show the gui
	 * 
	 * @param show
	 */
	public void showForm(boolean show) {
		this.jFrame.setVisible(show);
	}

	/**
	 * load all the graphic with the layout grid
	 * 
	 * @param jFrame
	 * @param guiMainLabel
	 */
	public void createGuiGame(JFrame jFrame, GuiMap guiMainLabel,
			ClientGameAbstract callBack) {
		this.jFrame = jFrame;
		// this form contain the mainLabel
		JLabel mainLabel = guiMainLabel.getMainLabel();
		JScrollPane scrollPane = loadMainLabel(mainLabel);
		this.pane = this.jFrame.getContentPane();
		this.pane.setBackground(new Color(38, 160, 246));
		this.pane.setLayout(new GridBagLayout());
		this.gridBag = new GridBagConstraints();
		// need to load the icon
		this.resorce = new GuiLoadImageResource();
		// set to expand the grid on 100% of the form
		this.gridBag.weightx = 1.0;
		this.gridBag.weighty = 1.0;
		this.gridBag.fill = GridBagConstraints.BOTH;
		this.gridBag.ipadx = WIDTH_BORD_PLAYERS / 2;
		this.gridBag.insets = new Insets(2, 2, 2, 2);
		loadFieldCards(callBack);
		// load hidden label to show for messages
		this.gridBag.insets = new Insets(4, 20, 4, 20);
		loadMsgBox(callBack);
		this.gridBag.insets = new Insets(2, 2, 2, 2);
		// map
		loadMap(scrollPane);
		this.gridBag.weighty = 0.0;
		this.gridBag.ipadx = 0;
		this.gridBag.ipady = HEIGHT_BORD_CHOICES;
		this.gridBag.fill = GridBagConstraints.HORIZONTAL;
		this.gridBag.anchor = GridBagConstraints.PAGE_END;
		// load labels of choise
		loadChoices(callBack);
		loadCountFence();

		this.jFrame.setSize(ClientGraphicConstants.DIM_MAP_X
				+ WIDTH_BORD_PLAYERS * 3 + 55, ClientGraphicConstants.DIM_MAP_Y
				+ HEIGHT_BORD_CHOICES + 80);
		this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jFrame.setMinimumSize(new Dimension(
				ClientGraphicConstants.DIM_MAP_X + WIDTH_BORD_PLAYERS * 2,
				ClientGraphicConstants.DIM_MAP_Y * 2 / 3));
		this.jFrame.setLocation(200, 200);
		// not display the window until the login is done
		this.jFrame.setVisible(false);
	}

	/**
	 * create this part of GUI only with the sync of the map (when the game
	 * start)
	 * 
	 * @param guiMainLabel
	 */
	public void loadPlayersAndBtns(int numPlayer, GuiMap guiMainLabel) {
		// load the Jlabel
		this.gridBag.ipadx = WIDTH_BORD_PLAYERS / 2;
		loadPlayers(numPlayer);
		// set space between the single box of the grid
		this.gridBag.insets = new Insets(1, 10, 1, 10);
		this.gridBag.fill = GridBagConstraints.HORIZONTAL;
		this.gridBag.weighty = 1.0;
		// all the buttons are height only 1 grid
		this.gridBag.gridheight = 1;
		// load the four buttons (sorted)
		loadBtnLegend();
		loadBtnRules();
		loadBtnZoom(guiMainLabel);
		loadBtnRotate(guiMainLabel);
	}

	/**
	 * generate the btn that show the gui with the legend
	 */
	public void loadBtnLegend() {
		JButton btnLegend = new JButton("Legend");
		this.gridBag.gridx = 6;
		this.gridBag.gridy = 8;
		btnLegend.addActionListener(new ActionListener() {
			/**
			 * override of click on the button
			 */
			public void actionPerformed(ActionEvent arg0) {
				new GuiLegend();
			}
		});
		this.pane.add(btnLegend, this.gridBag);
	}

	/**
	 * generate the btn that show the PDF with the rules
	 */
	public void loadBtnRules() {
		JButton btnRules = new JButton("Rules");
		this.gridBag.gridx = 6;
		this.gridBag.gridy = 9;
		this.pane.add(btnRules, this.gridBag);
		btnRules.addActionListener(new ActionListener() {
			/**
			 * override of click on the button
			 */
			public void actionPerformed(ActionEvent arg0) {
				new ClientOpenFile(ClientGraphicConstants.RULES_PDF)
						.openWithDefaultProgram();
			}
		});
	}

	/**
	 * load the second btn with rotation option
	 * 
	 * @param scrollPane
	 * @param jFrame
	 * @param mainLabel
	 */
	private JButton loadBtnRotate(GuiMap guiMainLabel) {
		JButton btnRotate = new JButton("Rotate");
		btnRotate.addActionListener(new GuiMapEventRotate(guiMainLabel));
		this.gridBag.gridx = 6;
		this.gridBag.gridy = 10;
		this.pane.add(btnRotate, this.gridBag);
		return btnRotate;
	}

	/**
	 * load the first btn with zoom option in the top|left corner
	 * 
	 * @param guiMainLabel
	 * @param mainLabel
	 */
	private JButton loadBtnZoom(GuiMap guiMainLabel) {
		JButton btnZoom = new JButton("Zoom");
		btnZoom.addActionListener(new GuiMapEventZoom(guiMainLabel));
		this.gridBag.gridx = 6;
		this.gridBag.gridy = 11;
		this.pane.add(btnZoom, this.gridBag);
		return btnZoom;
	}

	/**
	 * load a label in the middle of maps that hide the panel of map and request
	 * to click to go on
	 */
	private void loadMsgBox(ClientGameAbstract callBack) {
		JLabel labelMessage = new JLabel();
		labelMessage.setOpaque(true);
		labelMessage.setVisible(false);
		GuiGameEventMouse even = new GuiGameEventMouse(callBack,
				ReturnMoveChoice.INPUT_BOX, -1);
		this.dinLabelMsg = new GuiGameLabel(labelMessage, even, this.jFrame);

		this.gridBag.ipadx = 0;
		this.gridBag.gridheight = 1;
		this.gridBag.gridwidth = 5;
		this.gridBag.gridx = 1;
		this.gridBag.gridy = 3;

		// reset the font
		labelMessage.setHorizontalAlignment(SwingConstants.CENTER);
		labelMessage.setFont(new Font(ClientGraphicConstants.FONT, Font.BOLD,
				14));
		this.pane.add(labelMessage, this.gridBag);
	}

	/**
	 * load the label that count the fences
	 */
	private void loadCountFence() {
		JLabel labelCountFence = new JLabel();
		labelCountFence.setIcon(this.resorce.getSizedIcon(
				ClientGraphicConstants.PATH_COUNT_FENCE,
				WIDTH_BORD_PLAYERS - 10, 30));
		labelCountFence.setAlignmentX(SwingUtilities.CENTER);
		this.gridBag.gridheight = 1;
		this.gridBag.gridwidth = 1;
		this.gridBag.gridx = 0;
		this.gridBag.gridy = 12;
		this.pane.add(labelCountFence, this.gridBag);
		this.dinCountFence = new GuiGameLabel(labelCountFence, null,
				this.jFrame);
	}

	/**
	 * load the part of map
	 * 
	 * @param scrollPane
	 */
	private void loadMap(JScrollPane scrollPane) {
		this.gridBag.ipadx = ClientGraphicConstants.DIM_MAP_X;
		this.gridBag.gridheight = 12;
		this.gridBag.gridwidth = 5;
		this.gridBag.gridx = 1;
		this.gridBag.gridy = 0;
		this.pane.add(scrollPane, this.gridBag);
	}

	/**
	 * load the different Jlabel with the options of move
	 */
	private void loadChoices(ClientGameAbstract callBack) {
		this.listChoices = new ArrayList<GuiGameLabel>();
		for (int i = 0; i < ClientGraphicConstants.CHOISES.length; i++) {
			JLabel labelChoiceTmp = new JLabel();
			labelChoiceTmp.setHorizontalAlignment(SwingConstants.CENTER);
			labelChoiceTmp.setIcon(this.resorce.getSizedIcon(
					ClientGraphicConstants.CHOISES[i], 30, 30));
			GuiGameEventMouse even = new GuiGameEventMouse(callBack,
					ReturnMoveChoice.MOVE_TYPE, i);
			GuiGameLabel dinLab = new GuiGameLabel(labelChoiceTmp, even,
					this.jFrame);
			this.gridBag.gridheight = 1;
			this.gridBag.gridwidth = 1;
			// +1 because the edge in south ovest is empty
			this.gridBag.gridx = 1 + i;
			this.gridBag.gridy = 12;
			this.pane.add(labelChoiceTmp, this.gridBag);
			this.listChoices.add(dinLab);
		}
	}

	/**
	 * load the 6 type of field
	 */
	private void loadFieldCards(ClientGameAbstract callBack) {
		this.listFieldCards = new ArrayList<GuiGameLabel>();
		for (int i = 0; i < 6; i++) {
			JLabel labelFieldTmp = new JLabel();
			labelFieldTmp.setIcon(this.resorce.getSizedIcon(
					ClientGraphicConstants.FIELDCARDS[i],
					WIDTH_BORD_PLAYERS - 10, WIDTH_BORD_PLAYERS - 10));
			GuiGameEventMouse even = new GuiGameEventMouse(callBack,
					ReturnMoveChoice.FIELD_CARD, i);
			GuiGameLabel dinLab = new GuiGameLabel(labelFieldTmp, even,
					this.jFrame);
			// initial set num 0 and $ 0
			dinLab.setText(ClientGraphicConstants.SYMBOL_NUMBER + "0",
					ClientGraphicConstants.SYMBOL_MONEY + "0", false);
			labelFieldTmp.setAlignmentX(Component.CENTER_ALIGNMENT);
			// every fieldcard is height 2 grids
			this.gridBag.gridheight = 2;
			this.gridBag.gridx = 0;
			this.gridBag.gridy = i * 2;
			this.pane.add(labelFieldTmp, this.gridBag);
			this.listFieldCards.add(dinLab);
		}
	}

	/**
	 * load the different jlabel with the players
	 * 
	 * @param players
	 *            num of players
	 */
	private void loadPlayers(int players) {
		this.listPlayers = new ArrayList<GuiGameLabel>();
		for (int i = 0; i < players; i++) {
			JLabel labelPlayerTmp = new JLabel();
			labelPlayerTmp.setIcon(this.resorce.getSizedIcon(
					ClientGraphicConstants.SHEEPHERD[i], 30, 50));
			this.gridBag.gridheight = 2;
			this.gridBag.gridx = 6;
			this.gridBag.gridy = i * 2;
			this.pane.add(labelPlayerTmp, this.gridBag);
			// null because the player labels not need of input
			GuiGameLabel dinLab = new GuiGameLabel(labelPlayerTmp, null,
					this.jFrame);
			dinLab.setText("Name", ClientGraphicConstants.SYMBOL_MONEY + "0",
					false);
			this.listPlayers.add(dinLab);
		}
	}

	/**
	 * load all the stuff to put the miaiLabel in a scroll pane
	 * 
	 * @param mainLabel
	 * @return
	 */
	private JScrollPane loadMainLabel(JLabel mainLabel) {
		mainLabel.setAutoscrolls(true);
		mainLabel.setBounds(0, 0, ClientGraphicConstants.DIM_MAP_X,
				ClientGraphicConstants.DIM_MAP_Y);
		mainLabel.setPreferredSize(new Dimension(
				ClientGraphicConstants.DIM_MAP_X,
				ClientGraphicConstants.DIM_MAP_Y));
		// scollable panel
		JScrollPane scrollPane = new JScrollPane(mainLabel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainLabel.setAutoscrolls(true);
		scrollPane.setBounds(0, 0, ClientGraphicConstants.DIM_MAP_X + 3,
				ClientGraphicConstants.DIM_MAP_Y + 3);
		scrollPane.setPreferredSize(new Dimension(
				ClientGraphicConstants.DIM_MAP_X,
				ClientGraphicConstants.DIM_MAP_Y));
		return scrollPane;
	}

	/**
	 * called by ClientGUIGameImpl to change the values of players
	 * 
	 * @return
	 */
	public List<GuiGameLabel> getListPlayers() {
		return this.listPlayers;
	}

	/**
	 * called by ClientGUIGameImpl to make selectable the fields
	 * 
	 * @return
	 */
	public List<GuiGameLabel> getListFieldCards() {
		return this.listFieldCards;
	}

	/**
	 * called by ClientGUIGameImpl to make selectable the choises
	 * 
	 * @return
	 */
	public List<GuiGameLabel> getListChoices() {
		return this.listChoices;
	}

	/**
	 * called by ClientGUIGameImpl to show a msg
	 * 
	 * @return
	 */
	public GuiGameLabel getDinLabelMsg() {
		return this.dinLabelMsg;
	}

	/**
	 * @return the dinCountFence
	 */
	public GuiGameLabel getDinCountFence() {
		return this.dinCountFence;
	}
}
