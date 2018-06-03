package client.view.gui.map;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import client.view.ClientGraphicConstants;

/**
 * class for the legend that explain each symbols and icon in the game
 * 
 * @author mirko conti
 * 
 */
public class GuiLegend {

	private static final int SIZE = 35;
	private GuiLoadImageResource resorce;
	private Container contentPane;
	private GridBagConstraints grid;

	/**
	 * generate the legend
	 */
	public GuiLegend() {
		String divide = "____________";
		JFrame jFrame = new JFrame("Legend sheepland");
		this.contentPane = jFrame.getContentPane();
		this.contentPane.setLayout(new GridBagLayout());
		this.grid = new GridBagConstraints();
		// setting grid
		this.grid.weightx = 1.0;
		this.grid.weighty = 1.0;
		this.grid.anchor = GridBagConstraints.WEST;
		this.grid.fill = GridBagConstraints.BOTH;
		this.grid.insets = new Insets(5, 5, 5, 5);
		// need to load the icon
		this.resorce = new GuiLoadImageResource();
		// load the choises
		this.grid.gridx = 1;
		loadSymbolsBuyField("Elements", divide);
		addLabelsFromString(ClientGraphicConstants.REGION_SHEEP);
		addLabelsFromString(ClientGraphicConstants.REGION_BLACK);
		addLabelsFromString(ClientGraphicConstants.REGION_WOLF);
		loadSelectable();

		this.grid.gridx = 2;
		loadSymbolsBuyField("Pawns", divide);
		addLabelsFromArray(ClientGraphicConstants.SHEEPHERD);
		this.grid.gridx = 3;
		loadSymbolsBuyField("Choices of move", divide);
		addLabelsFromArray(ClientGraphicConstants.CHOISES);
		this.grid.gridx = 4;
		loadSymbolsBuyField("CardField", divide);
		addLabelsFromArray(ClientGraphicConstants.FIELDCARDS);
		this.grid.gridx = 5;
		loadSymbolsBuyField("Others", divide);
		loadSymbolsBuyField(ClientGraphicConstants.SYMBOL_NUMBER.toUpperCase(),
				"number of");
		loadSymbolsBuyField(ClientGraphicConstants.SYMBOL_MONEY, "money");
		loadSymbolsBuyField(ClientGraphicConstants.SYMBOL_SOLD_OUT, "sold out");
		addLabelsFromString(ClientGraphicConstants.PATH_FENCE);
		addLabelsFromString(ClientGraphicConstants.PATH_FENCE_FINAL);
		addLabelsFromString(ClientGraphicConstants.PATH_COUNT_FENCE);

		loadInstructionMove();

		jFrame.setSize(700, 340);
		jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jFrame.setVisible(true);
	}

	/**
	 * load all symbols that GUI uses
	 */
	private void loadSymbolsBuyField(String symbol, String meaning) {
		JLabel symbols = new JLabel();
		symbols.setHorizontalAlignment(SwingConstants.LEFT);
		symbols.setText("<html>" + symbol + "<br>" + meaning + "</html>");
		this.contentPane.add(symbols, this.grid);
	}

	/**
	 * load exemple of selectable label
	 */
	private void loadSelectable() {
		// icon used only here to show an exemple of a selectable area
		String selec = "GAME.selectable.png";
		ImageIcon iconTmp = this.resorce.getSizedIcon(selec, SIZE, SIZE);
		JLabel labelTmp = new JLabel(iconTmp);
		labelTmp.setHorizontalAlignment(SwingConstants.LEFT);
		labelTmp.setText(getInfoFromIcon(selec));
		this.contentPane.add(labelTmp, this.grid);
	}

	/**
	 * gets an array and calls the generator of label
	 * 
	 * @param choises
	 */
	private void addLabelsFromArray(String[] choises) {
		for (String string : choises) {
			addLabelsFromString(string);
		}
	}

	/**
	 * generator of label, get the icon , the text from the name of icon and add
	 * it to the grid
	 * 
	 * @param string
	 * @param size
	 */
	private void addLabelsFromString(String string) {
		ImageIcon iconTmp = this.resorce.getSizedIcon(string, SIZE, SIZE);
		JLabel labelTmp = new JLabel(iconTmp);
		labelTmp.setHorizontalAlignment(SwingConstants.LEFT);
		labelTmp.setText(getInfoFromIcon(string));
		this.contentPane.add(labelTmp, this.grid);
	}

	/**
	 * 
	 * @param name
	 *            of icon : *.info.png || *.info.jpg
	 * @return the info
	 */
	private String getInfoFromIcon(String name) {
		// "." is a wildcard, "\\." matches a literal "."
		String[] arr = name.split("\\.");
		if (arr.length == 3) {
			return arr[1];
		}
		return "error read info";
	}

	private void loadInstructionMove() {
		JLabel instruction = new JLabel();
		instruction.setHorizontalAlignment(SwingConstants.LEFT);
		this.grid.gridx = 1;
		this.grid.gridy = 6;
		this.grid.gridwidth = 3;
		instruction
				.setText("<html>PS: Selectable fields indicate the destination of a move!</html>");
		this.contentPane.add(instruction, this.grid);
	}

}
