package client.view.clc;

import java.util.Scanner;

import com.esotericsoftware.minlog.Log;

/**
 * only class that handle the input (texted input) from the user
 * 
 * @author mirko conti
 * 
 */
public class ClientCLCInput {

	private Scanner scan;

	/**
	 * constructor of ClientCLCInput, where get the access to the input for the
	 * user
	 */
	public ClientCLCInput() {
		this.scan = new Scanner(System.in);
	}

	/**
	 * close the scanner
	 */
	public void destroy() {
		this.scan.close();
	}

	/**
	 * return an int that is between the possible answers (belong to the array)
	 * 
	 * @param optionToShow
	 * @return
	 */
	public int getInput(int[] optionToShow) {
		while (true) {
			try {
				int result = this.scan.nextInt();
				for (int i : optionToShow) {
					if (i == result) {
						return result;
					}
				}
			} catch (Exception e) {
				Log.debug("getInput", e);
				this.scan = new Scanner(System.in);
			}
			Log.error("Insert an int between options!");
		}
	}

	/**
	 * return needs a string value
	 * 
	 * @return
	 */
	public String getInputString() {
		while (true) {
			try {
				return this.scan.next();
			} catch (Exception e) {
				Log.debug("getInputString", e);
				this.scan = new Scanner(System.in);
			}
			Log.error("Insert a valid string!");
		}
	}

	/**
	 * return needs an int value
	 * 
	 * @return
	 */
	public int getInputInt() {
		while (true) {
			try {
				return this.scan.nextInt();
			} catch (Exception e) {
				Log.debug("getInputInt", e);
				this.scan = new Scanner(System.in);
			}
			Log.error("Insert a valid int!");
		}
	}
}
