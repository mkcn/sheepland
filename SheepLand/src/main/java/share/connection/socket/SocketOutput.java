package share.connection.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.esotericsoftware.minlog.Log;

/**
 * class to handle the output stream of socket
 * 
 * @author mirko conti
 * 
 */
public class SocketOutput {

	private ObjectOutputStream oos;

	/**
	 * initialize the output of stream
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public SocketOutput(Socket socket) throws IOException {

		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.oos.flush();
	}

	/**
	 * constructor empty for test
	 */
	public SocketOutput() {
	}

	/**
	 * close the output stream
	 */
	public void closeOutput() {
		try {
			this.oos.close();
		} catch (IOException e1) {
			Log.debug("closeAll", e1);
		}
	}

	/**
	 * send an object with the stream
	 * 
	 * @param date
	 *            the serialized object
	 * @return the result of the operation
	 */
	public boolean sendObj(Object date) {
		try {
			// needed for update serializable obj
			this.oos.reset();
			this.oos.writeObject(date);
			return true;
		} catch (IOException e) {
			Log.debug("sendObj", e);
			return false;
		}
	}
}
