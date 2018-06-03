package client.view.gui.map;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * class that intercepts event of button "zoom"
 * 
 * @author mirko conti
 * 
 */
public class GuiMapEventZoom implements ActionListener {
	private boolean zoom = false;
	private GuiMap guiMap;

	/**
	 * save in local all the component to change
	 * 
	 * @param listDinamic
	 *            list of all jLabel in the main one
	 * @param mainLabel
	 */
	public GuiMapEventZoom(GuiMap guiMap) {

		this.guiMap = guiMap;
	}

	/**
	 * Override of click of the mouse on the button "zoom"
	 */
	public void actionPerformed(ActionEvent e) {
		// avoid conflict with timer move label
		if (!this.guiMap.isMoving()) {
			this.zoom = !this.zoom;
			// change size of all the paths, all the regions and all the
			// shepherd
			resizeList(this.guiMap.getListPaths());
			resizeList(this.guiMap.getListFields());
			resizeList(this.guiMap.getListShepHerds());
			// change size of map, sheep, black sheep and wolf
			GuiMapLabel dinamicMap = this.guiMap.getDinamicMap();

			dinamicMap.setSize(this.zoom);
			// set the preferred size of main label so the scrollable pane
			// changes
			// the scrolbars
			dinamicMap.getLabel()
					.setPreferredSize(
							new Dimension(dinamicMap.getIconX(), dinamicMap
									.getIconY()));
			this.guiMap.getDinamicSheep().setSize(this.zoom);
			this.guiMap.getDinamicSheepBlack().setSize(this.zoom);
			this.guiMap.getDinamicWolf().setSize(this.zoom);
		}
	}

	/**
	 * method to resize all the element of a list
	 * 
	 * @param list
	 * @param wid
	 */
	private void resizeList(List<GuiMapLabel> list) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSize(this.zoom);
		}
	}
}
