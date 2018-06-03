package client.view.clc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import share.game.model.FieldCard;
import share.game.model.Node;
import share.game.model.Shepherd;
import share.game.move.MoveOption;

import com.esotericsoftware.minlog.Log;

/**
 * only class in client that handle the output
 * 
 * @author mirko conti
 * 
 */
public class ClientCLCOutput {

	private static final String NEXT_LINE = "\n";
	private String prefix = "";
	private ClientCLCInput input;

	/**
	 * constructor of ClientCLCOutput, initialize the class where get the input
	 */
	public ClientCLCOutput() {
		this.input = new ClientCLCInput();
	}

	/**
	 * show the title and after the input of user return a string value
	 * 
	 * @param title
	 * @return
	 */
	public String getInputString(String title) {
		Log.info(this.prefix + title);
		return this.input.getInputString();
	}

	/**
	 * show the title and after the input of user return a int value
	 * 
	 * @param title
	 * @return
	 */
	public int getInputInt(String title) {
		Log.info(this.prefix + title + " (int value)");
		return this.input.getInputInt();
	}

	/**
	 * middle method that create the list of possible answers (yes || no)
	 * 
	 * @param title
	 * @return
	 * @throws IOException
	 */
	public boolean getInputYesNo(String title) {
		List<String> yesNo = new ArrayList<String>(Arrays.asList("yes", "no"));
		return showQuestionAndOptions(title, yesNo) == 0;
	}

	/**
	 * show a message with a title, if you need to show just a msg leave the
	 * title = ""
	 * 
	 * @param title
	 * @param msg
	 */
	public void showMsg(String title, String msg) {
		if (!"".equals(title)) {
			Log.info(this.prefix + title + " - " + msg);
		} else {
			Log.info(this.prefix + msg);
		}
	}

	/**
	 * show an error with a title,if you need to show just a msg leave the title
	 * = ""
	 * 
	 * @param title
	 * @param msg
	 */
	public void showMsgErr(String title, String msg) {
		if (!"".equals(title)) {
			Log.error(this.prefix + title + " - " + msg);
		} else {
			Log.error(this.prefix + msg);
		}
	}

	/**
	 * return one of the int in the arraylist || the id of the node ||the index
	 * of object
	 * 
	 * @param title
	 * @param listViewGenericWithoptionsToShow
	 * @return
	 */
	public synchronized int showQuestionAndOptions(String title,
			Object listViewGenericWithoptionsToShow) {

		@SuppressWarnings("unchecked")
		List<Object> optionsToShow = (ArrayList<Object>) listViewGenericWithoptionsToShow;

		String outp = this.prefix + title + NEXT_LINE + "[";
		int countElementsOnARow = 0;
		int[] options = new int[optionsToShow.size()];
		for (int i = 0; i < optionsToShow.size(); i++) {
			countElementsOnARow++;
			if (countElementsOnARow == 10) {
				outp += NEXT_LINE;
				countElementsOnARow = 0;
			}
			if (optionsToShow.get(i) instanceof Integer) {
				outp += " (" + optionsToShow.get(i) + ")";
				options[i] = (Integer) optionsToShow.get(i);
			} else if (optionsToShow.get(i) instanceof Node) {
				options[i] = ((Node) optionsToShow.get(i)).getId();
				outp += " (" + options[i] + ")";
				// change array with only the id of nodes
			} else if (optionsToShow.get(i) instanceof Shepherd) {
				outp += " (" + optionsToShow.get(i) + ")";
				// change array with only the id of nodes
				options[i] = ((Shepherd) optionsToShow.get(i)).getCode();
			} else if (optionsToShow.get(i) instanceof FieldCard) {
				FieldCard cardType = (FieldCard) optionsToShow.get(i);
				options[i] = cardType.getCardType().ordinal();
				outp += " (" + options[i] + ") " + cardType.toString();
			} else if (optionsToShow.get(i) instanceof MoveOption) {
				options[i] = ((MoveOption) optionsToShow.get(i)).ordinal();
				outp += " (" + options[i] + ") "
						+ (MoveOption) optionsToShow.get(i);
			} else {
				// if options aren't i show numerated options
				outp += " (" + i + ") " + optionsToShow.get(i);
				// change array with only the number
				options[i] = i;
			}
		}
		outp += "]";
		Log.info(outp);

		return this.input.getInput(options);
	}
}
