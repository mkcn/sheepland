package server.game;

import java.util.Random;

public class ServerRandomValue {

	private Random rand;
	private final static int MAX_VALUE = 5;
	private final static int ERROR = 1;

	public ServerRandomValue() {
		this.rand = new Random();
	}

	/**
	 * Return a random value between 1 and 6
	 * 
	 * @return
	 */
	public int getDiceValue() {
		return rand.nextInt(MAX_VALUE) + ERROR;
	}

	/**
	 * Return a random boolean value
	 * 
	 * @return
	 */
	public boolean getRandomBoolean() {
		return rand.nextBoolean();
	}
}
