package share.connection.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.esotericsoftware.minlog.Log;

/**
 * class to handle the input stream of socket
 * 
 * @author mirko conti
 * 
 */
public class SocketInput {

	private Socket socket;
	private ObjectInputStream ois;

	/**
	 * initialize the input of stream
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public SocketInput(Socket socket) throws IOException {
		this.socket = socket;
		// to call only after the SocketOutput!
		this.ois = new ObjectInputStream(socket.getInputStream());
	}

	/**
	 * constructor empty for test
	 */
	public SocketInput() {
	}

	/**
	 * close the input stream
	 */
	public void closeInput() {
		try {
			this.ois.close();
		} catch (IOException e1) {
			Log.debug("closeInput", e1);
		}
	}

	/**
	 * check if the socket is close and close it if not
	 */
	public void closeSocket() {
		try {
			if (!this.socket.isClosed()) {
				this.socket.close();
			}
		} catch (Exception e) {
			Log.debug("closeSocker", e);
		}
	}

	/**
	 * receive object and accept only if it's a sting
	 * 
	 * @return a string
	 */
	public String receiveString() {
		try {
			return this.ois.readObject().toString();
		} catch (ClassNotFoundException e) {
			Log.debug("ClassNotFoundException", e);
		} catch (IOException e) {
			Log.debug("IOException", e);
		}
		return null;
	}

	/**
	 * receive an object
	 * 
	 * @return serialized object
	 */
	public Object receiveObj() {
		try {
			return this.ois.readObject();
		} catch (Exception e) {
			Log.debug("receiveObj", e);
			return null;
		}
	}
}
