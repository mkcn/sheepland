package server.connection;

import java.util.ArrayList;
import java.util.List;

import server.game.ServerGameSetting;
import server.game.ServerPlayer;
import share.connection.RemoteSendInterface;
import share.game.comunication.ConnectionProtocol;

/**
 * class to handle the setting of a game and the new connection of a client
 * 
 * @author mirko conti
 * 
 */
public class ServerSettingConnection {
	private static ServerSettingConnection instance = null;

	private List<ServerGameSetting> gamesList = new ArrayList<ServerGameSetting>();

	private ServerPlayer tmpPlayer;
	private ServerGameSetting oldGameSetToResume;
	private ServerGameSetting tmpGameSet;

	/**
	 * constructor of ServerSettingConnection where create a new game and add it
	 * to the list of all games
	 */
	protected ServerSettingConnection() {
		this.tmpGameSet = new ServerGameSetting();
		this.gamesList.add(this.tmpGameSet);
	}

	/**
	 * get the instance of this class that is a singleton
	 * 
	 * @return
	 */
	public static synchronized ServerSettingConnection getInstance() {
		if (instance == null) {
			instance = new ServerSettingConnection();
		}
		return instance;
	}

	/**
	 * return the temp player
	 * 
	 * @return
	 */
	public ServerPlayer getTmpPlayer() {
		return this.tmpPlayer;
	}

	/**
	 * 
	 * @return the temp game setting
	 */
	public ServerGameSetting getTmpGameSet() {
		return this.tmpGameSet;
	}

	/**
	 * 
	 * @return the temp game to resume
	 */
	private ServerGameSetting getOldGameSetToResume() {
		return this.oldGameSetToResume;
	}

	/**
	 * return the temp game setting
	 * 
	 * @return
	 */
	public ServerGameSetting getGameSetting() {
		if (this.getOldGameSetToResume() != null) {
			return this.getOldGameSetToResume();
		}
		return this.getTmpGameSet();
	}

	/**
	 * check if usr and pass exits and if the player is playing or is
	 * disconnected or you need to create a new player
	 * 
	 * @param clientRemote
	 * @param usrname
	 * @param password
	 * @return
	 */
	public ConnectionProtocol checkAndSetCredentials(
			RemoteSendInterface clientRemote, String usrname, String password) {
		this.tmpPlayer = new ServerPlayer(clientRemote, usrname, password);
		ConnectionProtocol asw = new ConnectionProtocol(0, null);

		for (int i = 0; i < this.gamesList.size(); i++) {
			if (this.gamesList.get(i).getServerGameStatus().isAlive()) {
				this.oldGameSetToResume = this.gamesList.get(i);
				String out = checkClientUsrAndPass(usrname, password);
				if (out != null) {
					asw.setReply(out);
					return asw;
				}
			}
		}
		asw.setReply(ConnectionProtocol.NEW_GAMER);
		return asw;
	}

	/**
	 * after the send of asnwer of check information if he s a new player call
	 * this
	 */
	public void addPlayerToActualGame() {
		// CASE start ping
		this.oldGameSetToResume = null;
		this.tmpGameSet.addPlayer(this.tmpPlayer);
	}

	/**
	 * after the send of asnwer of check information if he s a
	 * "come back player" call this
	 */
	public void resumePlayerInOldGame() {
		this.oldGameSetToResume.playerComeBack(this.tmpPlayer);
	}

	/**
	 * if the game is alive check the credentials
	 * 
	 * @param usrname
	 * @param password
	 * @return
	 */
	private String checkClientUsrAndPass(String usrname, String password) {
		List<ServerPlayer> players = this.oldGameSetToResume
				.getServerGameStatus().getPlayers();
		for (int j = 0; j < players.size(); j++) {
			if (usrname.equals(players.get(j).getUsrname())) {
				if (password.equals(players.get(j).getPassword())) {
					return playerIsAlive(players.get(j));
				} else {
					return ConnectionProtocol.URS_EXISTING_PASS_WRONG;
				}
			}
		}
		return null;
	}

	/**
	 * if credentials are right check if the player is alive
	 * 
	 * @return
	 */
	private String playerIsAlive(ServerPlayer serverP) {
		if (!serverP.isAlive()) {
			return ConnectionProtocol.OLD_GAME_RESUME;
		} else {
			return ConnectionProtocol.GAMER_IS_PLAYING;
		}
	}

	/**
	 * if start a game,create a new one and add it to the list of games
	 */
	public void createNewGame() {
		this.tmpGameSet = new ServerGameSetting();
		this.gamesList.add(this.tmpGameSet);
	}

}
