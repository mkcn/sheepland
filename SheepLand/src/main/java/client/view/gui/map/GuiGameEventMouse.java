package client.view.gui.map;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import client.view.ClientGameAbstract;

/**
 * catch three event of label on gui: click ,mouse enter and exit
 * 
 * @author mirko conti
 * 
 */
public class GuiGameEventMouse implements MouseListener {

	private static final Color COLOR_MOUSE_ENTER = Color.yellow;
	private static final Color COLOR_SELECTABLE = Color.cyan;
	private ClientGameAbstract callBack;
	private char id;
	private int choice;

	/**
	 * 
	 * @param callBack
	 *            to call if the pabel is clicked
	 * @param id
	 *            to give to the callBack
	 */
	public GuiGameEventMouse(ClientGameAbstract callBack, char id, int choice) {
		this.callBack = callBack;
		this.id = id;
		this.choice = choice;
	}

	/**
	 * return to the ClientGameInterface the id of choice
	 */
	public void mouseClicked(MouseEvent e) {
		if (((JLabel) e.getSource()).isOpaque()) {
			this.callBack.sendChoiceAndHideOptions(this.id, this.choice);
		}
	}

	/**
	 * when mouse enter change color of background
	 */
	public void mouseEntered(MouseEvent e) {
		((JLabel) e.getSource()).setBackground(COLOR_MOUSE_ENTER);
	}

	/**
	 * when mouse exit reset color of background
	 */
	public void mouseExited(MouseEvent e) {
		((JLabel) e.getSource()).setBackground(COLOR_SELECTABLE);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent e) {
		// not used
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent e) {
		// not used
	}

}
