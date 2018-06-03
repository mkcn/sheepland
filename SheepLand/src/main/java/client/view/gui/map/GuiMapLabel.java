package client.view.gui.map;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import client.view.ClientGraphicConstants;

/**
 * class to handle all the change of panel
 * 
 * @author mirko conti
 * 
 */
public class GuiMapLabel {

	private JLabel label;
	// to refresh it
	private JFrame jFrame;
	private ImageIcon[] iconDefault = null, iconSelectable = null,
			iconMouseEnter = null;
	// 0 = zoomed 100%, 1 = zoomed 200%
	private short zoom = 0;
	// number of rotation of label (0, 90, 180, 270)
	private short numRotation = 0;
	// the width of the container of the label
	private int widthConteiner;
	private int heightConteiner;

	private boolean selectable;
	private int[] sizeFont = { 10, 20 };

	// helpful if the num of icon is 0 to hide the icon of sheep
	private boolean hideIconDefault = true;

	/**
	 * constructor for Label that use event like mouse enter and click
	 * (*,array,array) and for Label static with icon that change only with zoom
	 * (array, null, null)
	 * 
	 * @param label
	 * @param iconDefault
	 * @param iconSelectable
	 * @param iconMouseEnter
	 */
	public GuiMapLabel(JLabel label, ImageIcon[] iconDefault,
			ImageIcon[] iconSelectable, ImageIcon[] iconMouseEnter,
			JFrame jFrame) {
		this.label = label;
		this.jFrame = jFrame;
		this.selectable = false;
		label.setHorizontalTextPosition(JTextField.CENTER);
		label.setVerticalTextPosition(JTextField.CENTER);

		// for fields and map
		if (iconDefault != null) {
			this.hideIconDefault = false;
			this.iconDefault = new ImageIcon[iconDefault.length];
			System.arraycopy(iconDefault, 0, this.iconDefault, 0,
					iconDefault.length);
			// load first image
			reDrawIcon();
		}
		// for fields and paths
		if (iconSelectable != null) {
			this.iconSelectable = new ImageIcon[iconSelectable.length];
			System.arraycopy(iconSelectable, 0, this.iconSelectable, 0,
					iconSelectable.length);

		}
		// for field and paths
		if (iconMouseEnter != null) {
			this.iconMouseEnter = new ImageIcon[iconMouseEnter.length];

			System.arraycopy(iconMouseEnter, 0, this.iconMouseEnter, 0,
					iconMouseEnter.length);
		}
	}

	/**
	 * set the central text on jlabel, es: set the number of sheep
	 * 
	 */
	public void setText(int i) {
		if (i == 0) {
			this.hideIconDefault = true;
			this.label.setText("");
		} else {
			this.hideIconDefault = false;
			this.label.setText(Integer.toString(i));
		}
		reDrawIcon();
	}

	/**
	 * fix the position with zoom and rotation parameters
	 * 
	 * @param pos
	 * @param moveLabel
	 *            move the label with in input the center of the destination
	 * @return
	 */
	public Point fixPosition(Point pos, boolean moveLabel) {
		Point posWithZoom = new Point(pos.x * (1 + 1 * getZoom()), pos.y
				* (1 + 1 * getZoom()));
		int xTmp;
		int yTmp;
		if (getNumRotation() == 0) {
			xTmp = posWithZoom.x;
			yTmp = posWithZoom.y;
		} else if (getNumRotation() == 1) {
			xTmp = getWidthConteiner() - posWithZoom.y;
			yTmp = posWithZoom.x;
		} else if (getNumRotation() == 2) {
			xTmp = getWidthConteiner() - posWithZoom.x;
			yTmp = getHeightConteiner() - posWithZoom.y;
		} else {
			xTmp = posWithZoom.y;
			yTmp = getHeightConteiner() - posWithZoom.x;
		}

		Point finalPos = new Point(xTmp - getIconX() / 2, yTmp - getIconX() / 2);

		if (moveLabel) {
			this.label.setLocation(finalPos);
		}
		return finalPos;
	}

	/**
	 * set dimensions of label : 100% or 200%
	 */
	public void setSize(boolean setZoom) {
		if (setZoom) {
			this.label.setBounds(this.label.getX()
					* GuiLoadImageResource.ZOOM_RATIO, this.label.getY()
					* GuiLoadImageResource.ZOOM_RATIO, this.label.getWidth()
					* GuiLoadImageResource.ZOOM_RATIO, this.label.getHeight()
					* GuiLoadImageResource.ZOOM_RATIO);
			this.heightConteiner *= GuiLoadImageResource.ZOOM_RATIO;
			this.widthConteiner *= GuiLoadImageResource.ZOOM_RATIO;
			this.zoom = 1;
		} else {
			this.label.setBounds(this.label.getX()
					/ GuiLoadImageResource.ZOOM_RATIO, this.label.getY()
					/ GuiLoadImageResource.ZOOM_RATIO, this.label.getWidth()
					/ GuiLoadImageResource.ZOOM_RATIO, this.label.getHeight()
					/ GuiLoadImageResource.ZOOM_RATIO);
			this.heightConteiner /= GuiLoadImageResource.ZOOM_RATIO;
			this.widthConteiner /= GuiLoadImageResource.ZOOM_RATIO;
			this.zoom = 0;
		}
		reDrawIcon();
	}

	/**
	 * change the icon , usully called after a change of zoom or mouse exit
	 */
	private void reDrawIcon() {
		if (this.selectable) {
			this.label.setIcon(this.iconSelectable[this.zoom]);
		} else if (!this.hideIconDefault) {
			this.label.setIcon(this.iconDefault[this.zoom]);
		} else {
			this.label.setIcon(null);
		}
		this.label.setFont(new Font("Serif", Font.BOLD,
				this.sizeFont[this.zoom]));
		if (this.jFrame != null) {
			this.jFrame.repaint();
		}
	}

	/**
	 * called by all label but not by mainLabel it rotate the x and y of
	 * position and not the width and height because it's a square
	 * 
	 * @param widthConteiner
	 *            the width of the container of label in the final position
	 */
	public void rotateXY(int widthConteiner, int heightConteiner) {
		this.label.setBounds(
				widthConteiner - this.label.getY() - this.label.getWidth(),
				this.label.getX(), this.label.getHeight(),
				this.label.getWidth());
		this.widthConteiner = widthConteiner;
		this.heightConteiner = heightConteiner;
		this.numRotation = (short) ((this.numRotation + 1) % 4);
	}

	/**
	 * called only by mainLabel, it's rotate the icon of 90 in clockwise
	 */
	public void rotateIconAndSize() {
		// invert width with height
		this.label.setBounds(this.label.getX(), this.label.getY(),
				this.label.getWidth(), this.label.getHeight());
		// change each icon because it can rotate on between -90° and 90°
		// in this way you keep the icons update
		for (int i = 0; i < this.iconDefault.length; i++) {
			Image image = this.iconDefault[i].getImage();
			GuiRotateImage test = new GuiRotateImage();
			image = test.rotateImage(image, 90);
			this.iconDefault[i] = new ImageIcon(image);
		}
		this.numRotation = (short) ((this.numRotation + 1) % 4);
		reDrawIcon();
	}

	/**
	 * called just for paths, it change the default icon with a fence if the
	 * game work fine the icon wont change any more
	 * 
	 * @param isFinalFence
	 *            at tha last round of game the field become final and the icon
	 *            change
	 */
	public void setFence(boolean isFinalFence) {
		if (this.hideIconDefault) {
			if (isFinalFence) {
				this.iconDefault = new GuiLoadImageResource().getSizedIcons(
						ClientGraphicConstants.PATH_FENCE_FINAL,
						ClientGraphicConstants.PATH_DIM);
			} else {
				this.iconDefault = new GuiLoadImageResource().getSizedIcons(
						ClientGraphicConstants.PATH_FENCE,
						ClientGraphicConstants.PATH_DIM);
			}
			this.hideIconDefault = false;
			reDrawIcon();
		}
	}

	/**
	 * event of paths and regions, it's change the icon to show feedback
	 */
	public void mouseEnter() {
		this.label.setIcon(this.iconMouseEnter[this.zoom]);
		this.jFrame.repaint();
	}

	/**
	 * evento of paths and regions, it's reset the normal icon
	 */
	public void mouseExit() {
		reDrawIcon();
	}

	/**
	 * set the label selectable and clickable
	 * 
	 * @param selectable
	 */
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
		reDrawIcon();
	}

	/**
	 * get if the label is selectable
	 * 
	 * @param selectable
	 */
	public boolean getSelectable() {
		return this.selectable;
	}

	/**
	 * 
	 * @return the x size of actual icon
	 */
	public int getIconX() {
		return this.iconDefault[this.zoom].getIconWidth();
	}

	/**
	 * 
	 * @return the y size of actual icon
	 */
	public int getIconY() {
		return this.iconDefault[this.zoom].getIconHeight();
	}

	/**
	 * called by timer
	 * 
	 * @return
	 */
	public short getZoom() {
		return this.zoom;
	}

	/**
	 * 
	 * @return the angle of label (between 0 and 360)
	 */
	public short getNumRotation() {
		return this.numRotation;
	}

	/**
	 * @return the orginal label used to set the location, set the visibility,
	 *         get the x and y and more
	 */
	public JLabel getLabel() {
		return this.label;
	}

	/**
	 * @return the width of Conteiner of label
	 */
	public int getWidthConteiner() {
		return this.widthConteiner;
	}

	/**
	 * @return the height of Conteiner of label
	 */
	public int getHeightConteiner() {
		return this.heightConteiner;
	}

}
