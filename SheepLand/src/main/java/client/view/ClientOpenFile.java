package client.view;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;

import com.esotericsoftware.minlog.Log;

/**
 * call to open a file on the client with the default program on the OS
 * 
 * @author mirko conti
 * 
 */
public class ClientOpenFile {

	private String pathFile;

	/**
	 * 
	 * @param pathFile
	 *            the file to open
	 */
	public ClientOpenFile(String pathFile) {
		this.pathFile = pathFile;
	}

	/**
	 * call this to open the file , if fail show M
	 */
	public void openWithDefaultProgram() {
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			URL resource = classLoader.getResource(this.pathFile);

			File file = new File(resource.getFile());
			Desktop.getDesktop().open(file);
		} catch (Exception e) {
			Log.debug("openWithDefaultProgram", e);
			try {
				String[] split = this.pathFile.split(".");
				Log.error("Error open file of type " + split[2]);
			} catch (Exception e1) {
				Log.debug("openWithDefaultProgram split", e1);
				Log.error("Error open file");

			}
		}
	}
}
