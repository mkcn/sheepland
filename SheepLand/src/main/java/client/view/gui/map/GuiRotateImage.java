package client.view.gui.map;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * class to rotate an image
 * 
 * @author mirko conti
 * 
 */
public class GuiRotateImage {

	/**
	 * 
	 * @param image
	 *            the image you want rotate
	 * @param angle
	 *            between -90° and 90°
	 * @return an image
	 */
	public Image rotateImage(Image image, float angle) {

		BufferedImage bi = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(image, 0, 0, null);
		bg.dispose();
		bi = rotate(bi, angle);
		return (Image) bi;
	}

	/**
	 * 
	 * @param bi
	 *            get a BufferedImage
	 * @param angle
	 * @return return it rotate of angle°
	 */
	private BufferedImage rotate(BufferedImage bi, float angle) {

		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(angle), bi.getWidth() / 2.0,
				bi.getHeight() / 2.0);
		at.preConcatenate(findTranslation(at, bi, angle));

		BufferedImageOp op = new AffineTransformOp(at,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(bi, null);
	}

	private AffineTransform findTranslation(AffineTransform at,
			BufferedImage bi, float angle) {
		Point2D p2din = null, p2dout = null;

		if (angle > 0 && angle <= 90) {
			p2din = new Point2D.Double(0, 0);
		} else if (angle < 0 && angle >= -90) {
			p2din = new Point2D.Double(bi.getWidth(), 0);
		}
		p2dout = at.transform(p2din, null);
		double ytrans = p2dout.getY();

		if (angle > 0 && angle <= 90) {
			p2din = new Point2D.Double(0, bi.getHeight());
		} else if (angle < 0 && angle >= -90) {
			p2din = new Point2D.Double(0, 0);
		}
		p2dout = at.transform(p2din, null);
		double xtrans = p2dout.getX();

		AffineTransform tat = new AffineTransform();
		tat.translate(-xtrans, -ytrans);
		return tat;
	}
}
