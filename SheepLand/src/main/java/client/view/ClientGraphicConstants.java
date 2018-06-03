package client.view;

import java.awt.Point;

/**
 * class with all the constant used to build the GUI
 * 
 * @author mirko conti
 * 
 */
public class ClientGraphicConstants {

	// the name of the font of every text in the GUIs
	public static final String FONT = "Serif";
	public static final String SYMBOL_MONEY = "<font size=\"5\">$ </font>";
	public static final String SYMBOL_NUMBER = "<font size=\"5\">x </font>";;
	public static final String SYMBOL_SOLD_OUT = "<font size=\"5\" color=\"red\">S.O.</font>";

	// the PDF with all the rules of the game
	public static final String RULES_PDF = "GAME.rules.pdf";
	public static final String GAME_BOARD_NUMERED = "Game.board_numered.jpg";
	// cyan
	public static final String PATH_SELECTABLE = "PATH.path selectable.png";
	// yellow
	public static final String PATH_MOUSE_ENTER = "PATH.path mouse over.png";

	public static final String MAP = "GAME.board.jpg";

	// all the possible icons about regions
	public static final String REGION_SHEEP = "REGION.sheep.png";
	public static final String REGION_SELECTABLE = "REGION.region selectable.png";
	public static final String REGION_MOUSE_ENTER = "REGION.region mouse over.png";
	public static final String REGION_BLACK = "REGION.black sheep.png";
	public static final String REGION_WOLF = "REGION.wolf.png";

	public static final int DIM_PAWN = 20;
	// the icons of 4 players
	public static final String[] SHEEPHERD = { "SHEEPHERD.shepherd 1.png",
			"SHEEPHERD.shepherd 2.png", "SHEEPHERD.shepherd 3.png",
			"SHEEPHERD.shepherd 4.png" };

	// the icons of 5 choises
	public static final String[] CHOISES = { "CHOISES.move a sheep.png",
			"CHOISES.move a pawn.png", "CHOISES.buy a fieldcard.png",
			"CHOISES.kill a sheep.png", "CHOISES.coupling two sheep.png" };

	// the 6 fields
	public static final String[] FIELDCARDS = { "FIELD.wood.png",
			"FIELD.mountain.png", "FIELD.desert.png", "FIELD.hill.png",
			"FIELD.hay.jpg", "FIELD.swamp.png" };

	public static final int DIM_MAP_X = 450, DIM_MAP_Y = 648;

	public static final String PATH_FENCE = "FIELD.fence.png";
	public static final String PATH_FENCE_FINAL = "FIELD.final fence.png";
	public static final String PATH_COUNT_FENCE = "FIELD.count fence.png";

	public static final int PATH_DIM = 26;
	public static final Point[] PATH_POSITION = { new Point(470 / 2, 214 / 2),
			new Point(634 / 2, 224 / 2), new Point(233 / 2, 326 / 2),
			new Point(370 / 2, 379 / 2), new Point(460 / 2, 338 / 2),
			new Point(537 / 2, 313 / 2), new Point(595 / 2, 364 / 2),
			new Point(752 / 2, 321 / 2), new Point(152 / 2, 473 / 2),
			new Point(286 / 2, 417 / 2), new Point(439 / 2, 433 / 2),
			new Point(567 / 2, 453 / 2), new Point(652 / 2, 408 / 2),
			new Point(719 / 2, 440 / 2), new Point(809 / 2, 469 / 2),
			new Point(283 / 2, 525 / 2), new Point(405 / 2, 543 / 2),
			new Point(475 / 2, 493 / 2), new Point(543 / 2, 542 / 2),
			new Point(708 / 2, 534 / 2), new Point(342 / 2, 597 / 2),
			new Point(408 / 2, 640 / 2), new Point(552 / 2, 637 / 2),
			new Point(611 / 2, 598 / 2), new Point(708 / 2, 631 / 2),
			new Point(283 / 2, 645 / 2), new Point(487 / 2, 681 / 2),
			new Point(788 / 2, 709 / 2), new Point(216 / 2, 706 / 2),
			new Point(295 / 2, 770 / 2), new Point(426 / 2, 767 / 2),
			new Point(550 / 2, 744 / 2), new Point(676 / 2, 760 / 2),
			new Point(350 / 2, 855 / 2), new Point(417 / 2, 893 / 2),
			new Point(552 / 2, 881 / 2), new Point(605 / 2, 833 / 2),
			new Point(687 / 2, 916 / 2), new Point(230 / 2, 968 / 2),
			new Point(482 / 2, 933 / 2), new Point(350 / 2, 1072 / 2),
			new Point(547 / 2, 1026 / 2) };
	// is the ship , from where the shepherd come from
	public static final Point INITIAL_POSITION = new Point(367, 588);

	public static final int FIELD_DIM = 42;
	public static final Point[] FIELD_POSITION = { new Point(96, 196),
			new Point(179, 146), new Point(272, 110), new Point(341, 150),
			new Point(387, 195), new Point(254, 200), new Point(178, 244),
			new Point(114, 290), new Point(106, 416), new Point(159, 486),
			new Point(232, 520), new Point(308, 471), new Point(370, 404),
			new Point(241, 408), new Point(176, 363), new Point(314, 348),
			new Point(383, 294), new Point(316, 251), new Point(235, 297) };

	/**
	 * private constructor GuiConstants to override the public one
	 */
	private ClientGraphicConstants() {
		// not callable
	}
}
