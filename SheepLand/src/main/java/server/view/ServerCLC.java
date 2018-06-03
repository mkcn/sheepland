package server.view;

import com.esotericsoftware.minlog.Log;

/**
 * class to hadle the output of server
 * 
 * @author mirko conti
 * 
 */
public class ServerCLC {

	private static final String PREFIX = "SERVER -> ";
	private static final String MARK = " ## ";

	/**
	 * private constructor to override the public one
	 */
	private ServerCLC() {
	}

	/**
	 * if the server needs to print some message call this
	 * 
	 * @param title
	 * @param msg
	 */
	public static void showMsg(String title, String msg) {
		if (!"".equals(title)) {
			Log.info(MARK + PREFIX + title + ": " + msg);
		} else {
			Log.info(MARK + PREFIX + msg);
		}
	}

	/**
	 * if the server needs to print some error call this
	 * 
	 * @param title
	 * @param msg
	 */
	public static void showMsgErr(String title, String msg) {
		Log.error(MARK + PREFIX + title + ":" + msg + MARK);
	}
}
