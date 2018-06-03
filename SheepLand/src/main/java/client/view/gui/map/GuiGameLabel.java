package client.view.gui.map;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import client.view.ClientGraphicConstants;

/**
 * class to hadle all the labels on the gui but not the label on the map
 * 
 * @author mirko conti
 * 
 */
public class GuiGameLabel {

	private JLabel label;
	private JFrame jFrame;
	private String firstLine, secondLine;

	/**
	 * 
	 * @param label
	 * @param colorCanSelected
	 */
	public GuiGameLabel(JLabel label, GuiGameEventMouse event, JFrame jFrame) {
		this.label = label;
		this.jFrame = jFrame;
		// this make possible color the background
		this.label.setOpaque(false);
		this.label.setBackground(Color.cyan);
		if (event != null) {
			this.label.addMouseListener(event);
		}
		// set new font and size
		this.label.setFont(new Font(ClientGraphicConstants.FONT, Font.BOLD, 16));
	}

	/**
	 * if true the label start to catch the event and it color itself
	 * 
	 * @param selectable
	 */
	public void setSelectable(boolean selectable) {
		this.label.setOpaque(selectable);
		this.jFrame.repaint();
	}

	/**
	 * called for the players list
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.label.setBackground(color);
		this.label.setOpaque(true);
	}

	/**
	 * called for exemple by the message label
	 * 
	 * @return
	 */
	public void setVisible(boolean visible) {
		this.label.setVisible(visible);
	}

	/**
	 * chage the text with the possibility of write two line
	 * 
	 * @param firstLine
	 * @param secondLine
	 */
	public void setText(String firstLine, String secondLine, Boolean bigger) {
		String firstline = firstLine;
		String front = "<html>";
		String back = "</html>";
		if (bigger) {
			this.firstLine = "<font size=\"5\">" + firstline + " </font>";
		} else {
			this.firstLine = firstline;
		}
		if (secondLine.isEmpty()) {
			this.label.setText(front + firstLine + back);
		} else {
			this.label.setText(front + this.firstLine + "<br/> " + secondLine
					+ back);
			this.secondLine = secondLine;
		}

	}

	/**
	 * @return the firstLine
	 */
	public String getFirstLine() {
		return this.firstLine;
	}

	/**
	 * @return the secondLine
	 */
	public String getSecondLine() {
		return this.secondLine;
	}
}
