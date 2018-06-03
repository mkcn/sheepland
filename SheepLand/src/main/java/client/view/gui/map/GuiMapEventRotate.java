package client.view.gui.map;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;

/**
 * class to handle the rotation of the map
 * 
 * @author mirko conti
 * 
 */
public class GuiMapEventRotate implements ActionListener {

	private GuiMap guiMainLabel;

	/**
	 * constructor of GuiDinamicEventRotate
	 * 
	 * @param listDinamic
	 * @param mainLabel
	 * @param scrollPane
	 * @param frame
	 */
	public GuiMapEventRotate(GuiMap guiMainLabel) {

		this.guiMainLabel = guiMainLabel;
	}

	/**
	 * override of click on the button "rotate"
	 */
	public void actionPerformed(ActionEvent e) {

		JLabel labelMainTmp = this.guiMainLabel.getDinamicMap().getLabel();

		int wid = labelMainTmp.getPreferredSize().height;
		int hei = labelMainTmp.getPreferredSize().width;

		labelMainTmp.setPreferredSize(new Dimension(wid, hei));

		// this is the only icon that rotate
		this.guiMainLabel.getDinamicMap().rotateIconAndSize();

		this.guiMainLabel.getDinamicSheep().rotateXY(wid, hei);
		this.guiMainLabel.getDinamicSheepBlack().rotateXY(wid, hei);
		this.guiMainLabel.getDinamicWolf().rotateXY(wid, hei);

		rotateList(this.guiMainLabel.getListPaths(), wid, hei);
		rotateList(this.guiMainLabel.getListFields(), wid, hei);
		rotateList(this.guiMainLabel.getListShepHerds(), wid, hei);
	}

	/**
	 * method to rotate all the element of a list
	 * 
	 * @param list
	 * @param wid
	 */
	private void rotateList(List<GuiMapLabel> list, int wid, int hei) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).rotateXY(wid, hei);
		}
	}
}
