package it.polimi.deib.sheepland.sheepland;

import server.ServerStarter;
import client.ClientStarter;

import com.esotericsoftware.minlog.Log;

/**
 * 
 * @author mirko conti
 * 
 */
public class App {

	/**
	 * private constructor of App to override the public one
	 */
	private App() {
	}

	/**
	 * Start a client or a server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length == 1
				&& ("-d".equals(args[0]) || "-debug".equals(args[0]))) {
			Log.DEBUG = true;
			Log.debug("Debug mode active");
			new ServerStarter().start();
			new ClientStarter().start();
		} else if (args.length == 1
				&& ("-s".equals(args[0]) || "-server".equals(args[0]))) {
			new ServerStarter().start();

		} else if (args.length == 1
				&& ("-c".equals(args[0]) || "-client".equals(args[0]))) {
			new ClientStarter().start();
		} else {
			Log.error("wrong args!",
					"Insert '-client' or '-server' to start game like client or server");
		}
	}
}
