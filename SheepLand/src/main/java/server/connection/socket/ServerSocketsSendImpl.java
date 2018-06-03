package server.connection.socket;

import java.io.Serializable;

import share.connection.RemoteSendInterface;
import share.connection.socket.SocketOutput;

/**
 * class that with socket implement the remoteSendInterface
 * 
 * @author mirko conti
 * 
 */
public class ServerSocketsSendImpl implements RemoteSendInterface, Serializable {

	private static final long serialVersionUID = 1L;
	private SocketOutput socketOutput;

	/**
	 * 
	 * @param socketOutput
	 *            receive the output stream towards which send msgs to the
	 *            client
	 */
	public ServerSocketsSendImpl(SocketOutput socketOutput) {
		this.socketOutput = socketOutput;
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendObj(Object obj) {
		this.socketOutput.sendObj(obj);
	}

	/**
	 * not used by socket, but can will be
	 */
	public boolean ping() {
		return true;
	}
}
