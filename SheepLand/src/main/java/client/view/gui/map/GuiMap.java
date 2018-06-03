package client.view.gui.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import share.connection.ConnectionConstants;
import share.game.move.ReturnMoveChoice;
import client.view.ClientGameAbstract;
import client.view.ClientGraphicConstants;

/**
 * class to generate all the stuff inside the main label, with dinamic effects
 * 
 * @author mirko conti
 * 
 */
public class GuiMap extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel mainLabel;
	// main label + path + regions
	private List<GuiMapLabel> listPaths = new ArrayList<GuiMapLabel>();
	private List<GuiMapLabel> listRegions = new ArrayList<GuiMapLabel>();
	private List<GuiMapLabel> listShepHerds = new ArrayList<GuiMapLabel>();

	private GuiMapLabel dinamicSheep, dinamicSheepBlack, dinamicWolf,
			dinamicMap;
	private GuiLoadImageResource resorce;
	// used to allow the zoom and rotate to dont have conflict with the timer
	// that move the label
	private boolean isMoving;

	/**
	 * create all the components inside the mainLabel
	 * 
	 * @param jFrame
	 *            need it for the refresh
	 */
	public void createGuiMap(JFrame jFrame, ClientGameAbstract callBack) {
		// need to load the icons
		this.resorce = new GuiLoadImageResource();
		this.mainLabel = new JLabel();
		this.mainLabel.setLayout(null);
		// expand the sea
		this.mainLabel.setOpaque(true);
		this.mainLabel.setBackground(new Color(38, 160, 246));
		this.mainLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.mainLabel.setVerticalAlignment(SwingConstants.TOP);
		// load resource with the 2 size of icon
		ImageIcon[] iconMap = this.resorce.getSizedIcons(ClientGraphicConstants.MAP,
				ClientGraphicConstants.DIM_MAP_X, ClientGraphicConstants.DIM_MAP_Y);
		// create the global dinamic map
		this.dinamicMap = new GuiMapLabel(this.mainLabel, iconMap, null, null,
				null);
		// generate the wolf
		loadWolf();
		// generate the sheep of moving
		loadSheepMove();
		// generate all the path where the shepherds move
		loadPaths(jFrame, callBack);
		// finally the regions
		loadFields(jFrame, callBack);
		// black sheep
		// the black sheep is behind the other label
		loadBlackSheep();

	}

	/**
	 * generate the sheep that you use to make the effect of moving from a field
	 * to another and set the dinamic public var
	 */
	public void loadSheepMove() {
		ImageIcon[] iconSheep = this.resorce.getSizedIcons(
				ClientGraphicConstants.REGION_SHEEP, ClientGraphicConstants.FIELD_DIM);
		JLabel labelSheep = new JLabel();
		labelSheep.setVisible(false);
		// set initial (dont care) position
		labelSheep.setBounds(ClientGraphicConstants.INITIAL_POSITION.x,
				ClientGraphicConstants.INITIAL_POSITION.y, ClientGraphicConstants.FIELD_DIM,
				ClientGraphicConstants.FIELD_DIM);

		this.dinamicSheep = new GuiMapLabel(labelSheep, iconSheep, null, null,
				null);
		this.mainLabel.add(labelSheep);
	}

	/**
	 * create the wolf, load the icon , set the position and set the dinamic
	 * public var
	 */
	public void loadWolf() {
		JLabel labelWolf = new JLabel();
		ImageIcon[] iconWolf = this.resorce.getSizedIcons(
				ClientGraphicConstants.REGION_WOLF, ClientGraphicConstants.FIELD_DIM);
		// REGION_POSITION[18] is sheepsburb
		labelWolf.setBounds(ClientGraphicConstants.FIELD_POSITION[18].x
				- ClientGraphicConstants.FIELD_DIM / 2, ClientGraphicConstants.FIELD_POSITION[18].y
				- ClientGraphicConstants.FIELD_DIM / 2, ClientGraphicConstants.FIELD_DIM,
				ClientGraphicConstants.FIELD_DIM);
		// this is unque and is called externally
		this.dinamicWolf = new GuiMapLabel(labelWolf, iconWolf, null, null,
				null);
		this.mainLabel.add(labelWolf);
	}

	/**
	 * create the black sheep , load the icons, set the position and set the
	 * dinamic public var
	 * 
	 * @return
	 */
	public void loadBlackSheep() {
		JLabel labelSheepBlack = new JLabel();
		ImageIcon[] iconBlack = this.resorce.getSizedIcons(
				ClientGraphicConstants.REGION_BLACK, ClientGraphicConstants.FIELD_DIM);
		// REGION_POSITION[18] is sheepsburb
		labelSheepBlack.setBounds(ClientGraphicConstants.FIELD_POSITION[18].x
				- ClientGraphicConstants.FIELD_DIM / 2, ClientGraphicConstants.FIELD_POSITION[18].y
				- ClientGraphicConstants.FIELD_DIM / 2, ClientGraphicConstants.FIELD_DIM,
				ClientGraphicConstants.FIELD_DIM);
		// this is unque and is called externally
		this.dinamicSheepBlack = new GuiMapLabel(labelSheepBlack, iconBlack,
				null, null, null);
		this.mainLabel.add(labelSheepBlack);
	}

	/**
	 * create the players after the creation of map , when the game start
	 * 
	 * @param numPlayers
	 * @param jFrame
	 *            to refresh
	 */
	public void loadAllShepherd(int numPlayers) {
		// load numPLayers° shepherds with this order, blu, red, green, yellow
		for (int i = 0; i < numPlayers; i++) {
			loadShepherd(i);
		}
		// load shepherds if player with two players
		if (numPlayers == ConnectionConstants.NUM_PLAYERS_MIN) {
			// load other 2 (blue and red) shepherds
			loadShepherd(0);
			loadShepherd(1);
		}
		// only after the shepherd you add the paths to the map, in this way the
		// shepherds stay over them
		for (GuiMapLabel i : this.listPaths) {
			this.mainLabel.add(i.getLabel());
		}
	}

	/**
	 * load each shepherd, choose the icon (different color) 1
	 * 
	 * @param mine
	 * @param i
	 * @param jFrame
	 */
	private void loadShepherd(int i) {

		JLabel labelShepherd = new JLabel();
		// set initial position and size (on the ship)
		labelShepherd.setBounds(ClientGraphicConstants.INITIAL_POSITION.x
				- ClientGraphicConstants.DIM_PAWN / 2, ClientGraphicConstants.INITIAL_POSITION.y
				- ClientGraphicConstants.DIM_PAWN / 2, ClientGraphicConstants.DIM_PAWN,
				ClientGraphicConstants.DIM_PAWN);
		ImageIcon[] iconShepherd = this.resorce.getSizedIcons(
				ClientGraphicConstants.SHEEPHERD[i], labelShepherd.getWidth(),
				labelShepherd.getHeight());

		GuiMapLabel dinamicShepherd = new GuiMapLabel(labelShepherd,
				iconShepherd, null, null, null);
		this.mainLabel.add(labelShepherd);
		// all the shepherd in a sorted list
		this.listShepHerds.add(dinamicShepherd);
	}

	/**
	 * create on the mainPanel and in the array of Dinamic elements all the
	 * paths of the maps
	 * 
	 * @param timerDedicate
	 *            which timer move a label if you click here
	 * @param labelToMove
	 *            label activate
	 */
	private void loadPaths(JFrame jFrame, ClientGameAbstract callBack) {
		// containers PATHs

		ImageIcon[] iconPath = this.resorce.getSizedIcons(
				ClientGraphicConstants.PATH_SELECTABLE, ClientGraphicConstants.PATH_DIM);
		ImageIcon[] iconPathMouseEnter = this.resorce.getSizedIcons(
				ClientGraphicConstants.PATH_MOUSE_ENTER, ClientGraphicConstants.PATH_DIM);

		for (int i = 0; i < ClientGraphicConstants.PATH_POSITION.length; i++) {

			JLabel labelTmp = new JLabel();
			// set initial position and dimension
			int x = ClientGraphicConstants.PATH_POSITION[i].x - ClientGraphicConstants.PATH_DIM / 2;
			int y = ClientGraphicConstants.PATH_POSITION[i].y - ClientGraphicConstants.PATH_DIM / 2;
			labelTmp.setBounds(x, y, ClientGraphicConstants.PATH_DIM,
					ClientGraphicConstants.PATH_DIM);
			GuiMapLabel dinLAbel = new GuiMapLabel(labelTmp, null, iconPath,
					iconPathMouseEnter, jFrame);
			labelTmp.addMouseListener(new GuiMapEventMouse(dinLAbel, callBack,
					ReturnMoveChoice.NUMERED_SPACE, i));
			this.listPaths.add(dinLAbel);
		}

	}

	/**
	 * create on the mainPanel and in the array of Dinamic elements all the
	 * regions of the maps
	 * 
	 * @param timerDedicate
	 *            which timer move a label if you click here
	 * @param labelToMove
	 */
	private void loadFields(JFrame jFrame, ClientGameAbstract callBack) {

		ImageIcon[] iconRegion = this.resorce.getSizedIcons(
				ClientGraphicConstants.REGION_SHEEP, ClientGraphicConstants.FIELD_DIM);
		ImageIcon[] iconRegionSelectable = this.resorce.getSizedIcons(
				ClientGraphicConstants.REGION_SELECTABLE, ClientGraphicConstants.FIELD_DIM);
		ImageIcon[] iconRegionMouseEnter = this.resorce.getSizedIcons(
				ClientGraphicConstants.REGION_MOUSE_ENTER, ClientGraphicConstants.FIELD_DIM);
		// containers REGIONs
		// -1 because the last is sheepsburg
		for (int i = 0; i < ClientGraphicConstants.FIELD_POSITION.length; i++) {
			JLabel labelTmp = new JLabel();
			// set initial position and dimension
			int x = Math.round(ClientGraphicConstants.FIELD_POSITION[i].x)
					- ClientGraphicConstants.FIELD_DIM / 2;
			int y = Math.round(ClientGraphicConstants.FIELD_POSITION[i].y)
					- ClientGraphicConstants.FIELD_DIM / 2;
			labelTmp.setBounds(x, y, ClientGraphicConstants.FIELD_DIM,
					ClientGraphicConstants.FIELD_DIM);
			// create the dinamic label with the different size
			GuiMapLabel dinLAbel = new GuiMapLabel(labelTmp, iconRegion,
					iconRegionSelectable, iconRegionMouseEnter, jFrame);
			labelTmp.addMouseListener(new GuiMapEventMouse(dinLAbel, callBack,
					ReturnMoveChoice.FIELD, i));
			this.listRegions.add(dinLAbel);
			this.mainLabel.add(labelTmp);
		}
		// only for sheepsburg
		this.listRegions.get(ClientGraphicConstants.FIELD_POSITION.length - 1).setText(0);
		this.listRegions.get(ClientGraphicConstants.FIELD_POSITION.length - 1)
				.setSelectable(false);
	}

	/**
	 * 
	 * @return the main label
	 */
	public JLabel getMainLabel() {
		return this.mainLabel;
	}

	/**
	 * @return the dinamicMap
	 */
	public GuiMapLabel getDinamicMap() {
		return this.dinamicMap;
	}

	/**
	 * 
	 * @return the list of all the paths
	 */
	public List<GuiMapLabel> getListPaths() {
		return this.listPaths;
	}

	/**
	 * 
	 * @return the list of all the regions
	 */
	public List<GuiMapLabel> getListFields() {
		return this.listRegions;
	}

	/**
	 * 
	 * @return the list of all shepherds
	 */
	public List<GuiMapLabel> getListShepHerds() {
		return this.listShepHerds;
	}

	/**
	 * @return the dinamicWolf
	 */
	public GuiMapLabel getDinamicWolf() {
		return this.dinamicWolf;
	}

	/**
	 * @return the dinamicSheepBlack
	 */
	public GuiMapLabel getDinamicSheepBlack() {
		return this.dinamicSheepBlack;
	}

	/**
	 * @return the dinamicSheep
	 */
	public GuiMapLabel getDinamicSheep() {
		return this.dinamicSheep;
	}

	/**
	 * @return the isMoving
	 */
	public boolean isMoving() {
		return this.isMoving;
	}

	/**
	 * @param isMoving
	 *            the isMoving to set
	 */
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
}
