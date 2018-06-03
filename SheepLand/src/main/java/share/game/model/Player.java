package share.game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	private String usrname;
	private int money;
	private List<Shepherd> playerShepherd;
	private int id;
	private boolean alive;
	private int finalScore;

	/**
	 * Constructor
	 * 
	 * @param usrname
	 */
	public Player(String usrname) {
		this.playerShepherd = new ArrayList<Shepherd>();
		this.usrname = usrname;
		this.money = 0;
		this.alive = true;
	}

	/**
	 * Return the final score of this player
	 * 
	 * @return
	 */
	public int getFinalScore() {
		return finalScore;
	}

	/**
	 * Set the value of the fina score of this player
	 * 
	 * @param finalScore
	 */
	public void setFinalScore(int finalScore) {
		this.finalScore = finalScore;
	}

	/**
	 * Return the id of this player
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id of this player
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Return the user name of this player
	 * 
	 * @return
	 */
	public String getUsrname() {
		return this.usrname;
	}

	/**
	 * Return all the shepherd controlled by the player
	 * 
	 * @return
	 */
	public List<Shepherd> getAllShepher() {
		return this.playerShepherd;
	}

	/**
	 * Return a shepherd with given id
	 * 
	 * @param pos
	 * @return
	 */
	public Shepherd getShepherd(int id) {
		for (Shepherd x : this.playerShepherd) {
			if (x.getCode() == id) {
				return x;
			}
		}
		return null;
	}

	/**
	 * Add a given shepherd to controlled ones
	 * 
	 * @param newShep
	 * @param shepPosition
	 *            TODO
	 */
	public void addShepherd(Shepherd newShep, int shepPosition) {
		this.playerShepherd.add(shepPosition, newShep);
	}

	/**
	 * Receive a list of shepherd and set them as owned by this player
	 * 
	 * @param sheps
	 */
	public void setAllShepherd(List<Shepherd> sheps) {
		this.playerShepherd = sheps;
	}

	/**
	 * get actual amount of money
	 * 
	 * @return
	 */
	public int getMoney() {
		return this.money;
	}

	/**
	 * set money
	 * 
	 * @param money
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * 
	 * @param duty
	 * @throws NoMoreMoney
	 */
	public void decMoney(int duty) throws NoMoreMoney {
		if (this.money - duty > 0) {
			this.money = this.money - duty;
		} else {
			throw new NoMoreMoney();
		}
	}

	/**
	 * Check if this player is alive(not disconnected)
	 */
	public boolean isAlive() {
		return this.alive;
	}

	/**
	 * Set the state of this player: True alive False disconnected
	 * 
	 * @param alive
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * class to handle error no money
	 * 
	 * @author andrea bertarini
	 * 
	 */
	public class NoMoreMoney extends Exception implements Serializable {
		private static final long serialVersionUID = 1L;

		/**
		 * {@inheritDoc}
		 */
		public NoMoreMoney() {
			super();
		}

		/**
		 * {@inheritDoc}
		 */
		public NoMoreMoney(String s) {
			super(s);
		}
	}

}
