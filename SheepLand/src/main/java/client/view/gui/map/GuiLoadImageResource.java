package client.view.gui.map;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.esotericsoftware.minlog.Log;

/**
 * class to interact with the file and load resource
 * 
 * @author mirko conti
 * 
 */
public class GuiLoadImageResource {

	public static final short ZOOM_RATIO = 2;

	/**
	 * get icons resized
	 * 
	 * @param name
	 *            of resource
	 * @param dimX
	 *            of label where to put it
	 * @param dimY
	 *            of label where to put it
	 * @return array with the small icon and the big one
	 */
	public ImageIcon[] getSizedIcons(String name, int dimX, int dimY) {
		ImageIcon[] iconsTmp = new ImageIcon[2];
		// load small icon
		iconsTmp[0] = getSizedIcon(name, dimX, dimY);
		// load big icon
		iconsTmp[1] = getSizedIcon(name, dimX * ZOOM_RATIO, dimY * ZOOM_RATIO);
		return iconsTmp;
	}

	/**
	 * get icons squared
	 * 
	 * @param name
	 * @param dimSquare
	 * @return
	 */
	public ImageIcon[] getSizedIcons(String name, int dimSquare) {
		return getSizedIcons(name, dimSquare, dimSquare);
	}

	/**
	 * get the resource as a stream and put it in a Image icon
	 * 
	 * @param name
	 * @param dimX
	 * @param dimY
	 * @return ImageIcon
	 */
	public ImageIcon getSizedIcon(String name, int dimX, int dimY) {
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			InputStream resourceAsStream;
			ImageIcon imageicon;
			resourceAsStream = classLoader.getResourceAsStream(name);

			imageicon = new ImageIcon(resizeImage(resourceAsStream, dimX, dimY));
			return imageicon;
		} catch (Exception e) {
			Log.debug("getSizedIcon", e);
			Log.info("getResources", "error load resource: " + name);
			return null;
		}
	}

	/**
	 * get a inputstream and resize it
	 * 
	 * @param path
	 * @param x
	 * @param y
	 * @return Image
	 */
	public Image resizeImage(InputStream path, int x, int y) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(path);
			return image.getScaledInstance(x, y, Image.SCALE_SMOOTH);
		} catch (Exception e) {
			Log.debug("resizeImage", e);
		}
		return null;
	}

	/**
	 * load a gif animation
	 * 
	 * @param pathGif
	 * @param dim
	 * @return Image
	 */
	public Image getGifAnimation(String pathGif, int dim) {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL resource = classLoader.getResource(pathGif);
		Image tmpImage = Toolkit.getDefaultToolkit().createImage(resource);
		tmpImage = tmpImage.getScaledInstance(dim, dim, Image.SCALE_DEFAULT);
		return tmpImage;
	}
}
