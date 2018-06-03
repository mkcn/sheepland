package server;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.connection.ServerIp;
import server.connection.rmi.ServerRMI;
import server.connection.socket.ServerSockets;
import server.view.ServerCLC;

/**
 * class to start the server
 * 
 * @author mirko conti
 * 
 */
public class ServerStarter {

	/**
	 * have to call this to start the server
	 */
	public void start() {
		showIpToSetOnClient();
		startAsyncRmiAndSocket();
	}

	/**
	 * show the local , the private and the public ip to set on the client
	 * 
	 * @throws UnknownHostException
	 */
	public void showIpToSetOnClient() {
		ServerIp serverIp = new ServerIp();
		String privateIp = serverIp.getMyPrivateIp();
		ServerCLC.showMsg("IP local  ", serverIp.getMyLocalIp());
		ServerCLC.showMsg("IP private", privateIp);
		ServerCLC.showMsg("IP public ", serverIp.getMyPublicIp());
		// set the needed properties to use RMI
		System.setProperty("java.rmi.server.hostname", privateIp);
		System.setProperty("sun.rmi.transport.tcp.responseTimeout", "2000");
		System.setProperty("sun.rmi.transport.tcp.readTimeout", "2000");
	}

	/**
	 * start both the type of connection so the server are able to handle a RMI
	 * player and a Socket player in the same game
	 */
	public void startAsyncRmiAndSocket() {
		ExecutorService executor = Executors.newCachedThreadPool();
		ServerSockets socketServer = new ServerSockets(executor);
		executor.submit(socketServer);
		new ServerRMI();
	}

}
