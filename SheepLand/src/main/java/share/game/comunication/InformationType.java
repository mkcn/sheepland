package share.game.comunication;

public enum InformationType {
	// answer for credential in connection
	CONNECTION,
	// message with this type contain a map
	SYNC, ENDOPERATION, ACK,
	// used by message to inform the player about its possibility with move
	// sheep
	MOVEBLACKSHEEP, ENDMOVEBLACKSHEEP,
	// used by the client to specify the shepherd he want to use
	SHEPHERDID,
	// used by the server, message of this type contain the move that the player
	// can do
	MOVEOPTION,
	// black sheep random move at he end of turn
	BLACKRNDMMOVE,
	// wolf random move
	WOLF, WOLFEAT, WOLFMOVE,

	// used by the server to inform all the player about the actual player move
	OTHERPLAYERMOVESHEEP, OTHERPLAYERBUY, OTHERPLAYERSHPHERD, OTHERPLAYERNOTENOUGHMONEY, OTHERPLAYKILL, OTHERPLAYERCOUPLING,
	// used by server, message with this type are send to client to know
	// where the player want to place shepherd
	SHEPPOSITION,
	// first message send to every client from server, it contain the number of
	// player
	FIRSTSYNC, NEWPLAYER, PLAYERCOMEBACK, PLAYERDISCONNECT,
	// message use by server, notify the actual player about the end of the move
	ENDMOVE, ENDMOVEPAY,
	// used by server to communicate about coupling move
	COUPLINGEND, NOCOUPLINGEND,
	// used by server to communicate about killing move
	KILLINGEND, NOKILLINGEND, TURNSHEPHERD, INITIALCARD, INFOMOVE, ENDOPERATIONBLACK, YOUCARD, SCORE, RANDOMPOSITION;
}
