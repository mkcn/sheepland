package client.view.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.esotericsoftware.minlog.Log;

/**
 * class to hadle the events of the button ratio on login
 * 
 * @author mirko conti
 * 
 */
public class ClientGUILoginEventRatioBtn implements ActionListener {

	private JRadioButton jRatioRMI, jRatioSocket;
	private JTextField textIp;
	private JTextField textPort;
	private ClientGUILoginImpl clientGUILoginImlp;
	private JLabel outputMsg, loading;

	/**
	 * get all the information to check if the select of a ratio is avaiable
	 * 
	 * @param jRatioRMI
	 * @param jRatioSocket
	 * @param textIp
	 * @param textPort
	 * @param clientGUILoginImlp
	 * @param outputMsg
	 */
	public ClientGUILoginEventRatioBtn(JRadioButton jRatioRMI,
			JRadioButton jRatioSocket, JTextField textIp, JTextField textPort,
			ClientGUILoginImpl clientGUILoginImlp, JLabel outputMsg,
			JLabel loading) {
		this.jRatioRMI = jRatioRMI;
		this.jRatioSocket = jRatioSocket;
		this.textIp = textIp;
		this.textPort = textPort;
		this.clientGUILoginImlp = clientGUILoginImlp;
		this.outputMsg = outputMsg;
		this.loading = loading;
	}

	/**
	 * override of action where check if the text of ip and port arent empty and
	 * fulled with the right info
	 */
	public void actionPerformed(ActionEvent e) {
		if (!this.textIp.getText().isEmpty()
				&& checkIsAnInteger(this.textPort.getText())) {
			this.jRatioRMI.setEnabled(false);
			this.jRatioSocket.setEnabled(false);
			// show the gif
			this.loading.setVisible(true);
			if (this.jRatioRMI == e.getSource()) {
				this.jRatioRMI.setSelected(true);
				this.jRatioSocket.setSelected(!true);
				this.clientGUILoginImlp.chooseConnectionReturn(true,
						this.textIp.getText(),
						Integer.parseInt(this.textPort.getText()));
			}
			if (this.jRatioSocket == e.getSource()) {
				this.jRatioRMI.setSelected(!true);
				this.jRatioSocket.setSelected(true);
				this.clientGUILoginImlp.chooseConnectionReturn(!true,
						this.textIp.getText(),
						Integer.parseInt(this.textPort.getText()));
			}
		} else {
			this.outputMsg.setText("Complete port and ip (int value)!");
			this.jRatioRMI.setSelected(!true);
			this.jRatioSocket.setSelected(!true);
		}
	}

	/**
	 * get a string and check the cast
	 * 
	 * @param value
	 * @return
	 */
	private boolean checkIsAnInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			Log.debug("checkIsAnInteger", e);
			return false;
		}
	}
}
