package client.view.gui.map;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client.view.ClientGameAbstract;

/**
 * class to handle the click on a label, the mouse enter and exit
 * 
 * @author mirko conti
 * 
 */
public class GuiMapEventMouse implements MouseListener {

	private GuiMapLabel dinLabel;
	private ClientGameAbstract callBack;
	private char id;
	private int choice;

	/**
	 * constructor of GuiEventLabelClickEnterExit
	 * 
	 * @param dinLabel
	 *            where move the object
	 * @param labelSheep
	 *            the object to move
	 * @param timerMove
	 *            the timer that move the object
	 */
	public GuiMapEventMouse(GuiMapLabel dinLabel, ClientGameAbstract callBack,
			char id, int choice) {
		this.dinLabel = dinLabel;
		this.callBack = callBack;
		this.id = id;
		this.choice = choice;
	}

	/**
	 * override of mouseClick that call the timer move label
	 */
	public void mouseClicked(MouseEvent arg0) {
		if (this.dinLabel.getSelectable()) {
			this.callBack.sendChoiceAndHideOptions(this.id, this.choice);
		}
	}

	/**
	 * it's change the icon
	 */
	public void mouseEntered(MouseEvent arg0) {
		// only if it's selectable change icon and only in this case repaint the
		// frame
		if (this.dinLabel.getSelectable()) {
			this.dinLabel.mouseEnter();
		}
	}

	/**
	 * it's reset the icon
	 */
	public void mouseExited(MouseEvent arg0) {
		// reset the icon
		this.dinLabel.mouseExit();
	}

	/**
	 * not used but needed by implements
	 */
	public void mousePressed(MouseEvent arg0) {
		// not used
	}

	/**
	 * not used needed by implements
	 */
	public void mouseReleased(MouseEvent arg0) {
		// not used
	}
}
