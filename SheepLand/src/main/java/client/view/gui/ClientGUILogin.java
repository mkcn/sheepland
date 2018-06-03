package client.view.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import share.connection.ConnectionConstants;
import client.view.gui.map.GuiLoadImageResource;

import com.esotericsoftware.minlog.Log;

/**
 * class to generate and show the gui of login
 * 
 * @author mirko conti
 * 
 */
public class ClientGUILogin implements Runnable {

	private ClientGUILoginImpl clientGUILoginImlp;

	private JTextField textUsrname, textIp, textPortSocket, textPortRmi;
	private JPasswordField textPassword;
	private int width = 200;
	private int height = 210;
	private int gap = 20;

	private JRadioButton jRatioRMI, jRatioSocket;

	private JLabel outputMsg;
	private JLabel loading;
	private JFrame jframe;

	/**
	 * save the class where to answer
	 * 
	 * @param clientGUILoginImlp
	 */
	public ClientGUILogin(ClientGUILoginImpl clientGUILoginImlp) {
		this.clientGUILoginImlp = clientGUILoginImlp;
	}

	/**
	 * start GUI login with swing invoke latery
	 */
	public void run() {
		startGUI();
	}

	/**
	 * create the gui
	 */
	public void startGUI() {
		// frame set
		this.jframe = new JFrame("Login sheepland");
		this.jframe.setLayout(null);

		this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jframe.setSize(this.width + 50, this.height + 50);

		GuiLoadImageResource guiLoadImageResource = new GuiLoadImageResource();

		ImageIcon iconShepherd = guiLoadImageResource.getSizedIcon(
				"GAME.login.jpg", this.jframe.getWidth(), this.height);

		JLabel backLabel = new JLabel();
		backLabel.setIcon(iconShepherd);
		backLabel.setBounds(0, 0, this.jframe.getWidth(), this.height);
		// 1 LABEL contenitor
		JLabel contenitorPortIpAndRatio = new JLabel();
		contenitorPortIpAndRatio.setBounds(0, this.gap + 15, this.width + 50,
				80);
		backLabel.add(contenitorPortIpAndRatio);
		// 2 label contenitor
		JLabel contenitorCredential = new JLabel();
		contenitorCredential.setBounds(0, contenitorPortIpAndRatio.getHeight()
				+ this.gap * 2, this.width + 50, 80);
		backLabel.add(contenitorCredential);

		// load the single parts
		loadLoading();
		loadMsgLabel();
		loadIpPort(contenitorPortIpAndRatio);
		loadRatio(contenitorPortIpAndRatio, contenitorCredential);
		loadUsrAndPass(contenitorCredential);

		this.jframe.add(backLabel);
		this.jframe.setResizable(false);
		this.jframe.setLocation(10, 100);
		this.jframe.setVisible(true);
	}

	private void loadMsgLabel() {
		// initial message
		this.outputMsg = new JLabel("Select a type of connection");
		this.outputMsg.setBackground(Color.cyan);
		this.outputMsg.setOpaque(true);

		this.outputMsg.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		this.outputMsg.setBounds(0, this.height, this.width + 50, 20);
		this.outputMsg.setAlignmentX(JFrame.CENTER_ALIGNMENT);
		this.jframe.add(this.outputMsg);
	}

	private void loadLoading() {
		this.loading = new JLabel();
		GuiLoadImageResource resource = new GuiLoadImageResource();
		Image iconLoad = resource.getGifAnimation("GAME.loading.gif", 20);
		this.loading.setIcon(new ImageIcon(iconLoad));
		this.loading.setVisible(false);
		this.loading.setBounds(this.width + 25, this.height, 20, 20);
		this.jframe.add(this.loading);
	}

	private void loadUsrAndPass(Container contenitorCredential) {
		// password and username
		this.textUsrname = new JTextField(20);
		if (Log.DEBUG) {
			this.textUsrname.setText("usr." + new Random().nextInt(1000));
		} else {
			this.textUsrname.setText("username");
		}
		this.textUsrname.setBounds(this.gap, 20, this.width / 2, 20);
		this.textPassword = new JPasswordField(10);
		if (Log.DEBUG) {
			this.textPassword.setText("pass");
		} else {
			this.textPassword.setText("password");
		}
		this.textPassword.setBounds(this.gap + this.width / 2, 20,
				this.width / 2, 20);
		contenitorCredential.add(this.textUsrname);
		contenitorCredential.add(this.textPassword);

		// button1
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(this.gap, 40, this.width, 20);
		btnLogin.addActionListener(new EventClickBtnLogin());
		contenitorCredential.add(btnLogin);
	}

	private void loadIpPort(Container contenitorPortIpAndRatio) {
		// IP and port
		this.textIp = new JTextField(20);
		// use this boolean for auto compilation
		this.textIp.setText(ConnectionConstants.IPLOCAL);
		this.textIp.setBounds(this.gap, 10, this.width, 20);
		contenitorPortIpAndRatio.add(this.textIp);

		this.textPortRmi = new JTextField(5);
		this.textPortRmi
				.setText(Integer.toString(ConnectionConstants.RMI_PORT));
		this.textPortRmi.setBounds(this.gap, 30, this.width / 2, 20);
		contenitorPortIpAndRatio.add(this.textPortRmi);

		this.textPortSocket = new JTextField(5);
		this.textPortSocket.setText(Integer
				.toString(ConnectionConstants.SOCKET_PORT));
		this.textPortSocket.setBounds(this.gap + this.width / 2, 30,
				this.width / 2, 20);
		contenitorPortIpAndRatio.add(this.textPortSocket);
	}

	private void loadRatio(Container contenitorPortIpAndRatio,
			JLabel contenitorCredential) {
		// RATIO BUTTON
		this.jRatioRMI = new JRadioButton("RMI");
		this.jRatioRMI.setBounds(this.gap, 50, this.width / 2, 20);
		this.jRatioSocket = new JRadioButton("Socket");
		this.jRatioSocket.setBounds(this.gap + this.width / 2, 50,
				this.width / 2, 20);
		ClientGUILoginEventRatioBtn eventClickButtonRmi = new ClientGUILoginEventRatioBtn(
				this.jRatioRMI, this.jRatioSocket, this.textIp,
				this.textPortRmi, this.clientGUILoginImlp,
				contenitorCredential, this.loading);
		this.jRatioRMI.addActionListener(eventClickButtonRmi);
		ClientGUILoginEventRatioBtn eventClickButtonSock = new ClientGUILoginEventRatioBtn(
				this.jRatioRMI, this.jRatioSocket, this.textIp,
				this.textPortSocket, this.clientGUILoginImlp,
				contenitorCredential, this.loading);
		this.jRatioSocket.addActionListener(eventClickButtonSock);
		contenitorPortIpAndRatio.add(this.jRatioRMI);
		contenitorPortIpAndRatio.add(this.jRatioSocket);
	}

	/**
	 * show message in the jlabel outputMsg
	 * 
	 * @param msg
	 *            text
	 */
	public void showMsg(String msg) {
		this.outputMsg.setText(msg);
	}

	/**
	 * active or disactive the gif of connection wait
	 * 
	 * @param active
	 */
	public void enableLoading(boolean active) {
		this.loading.setVisible(active);
	}

	/**
	 * show or hide the gui
	 * 
	 * @param show
	 */
	public void showForm(boolean show) {
		this.jframe.setVisible(show);
	}

	/**
	 * set to initial setting the component
	 */
	public void resetConn() {
		this.jRatioRMI.setSelected(!true);
		this.jRatioSocket.setSelected(!true);
		this.jRatioRMI.setEnabled(true);
		this.jRatioSocket.setEnabled(true);
	}

	/**
	 * class to catch the event of click about the credential of connetion
	 * 
	 * @author mirko conti
	 * 
	 */
	public class EventClickBtnLogin implements ActionListener {

		/**
		 * when the usr click on the button send the credential to the logic
		 */
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {
			if (!"".equals(ClientGUILogin.this.textUsrname.getText())) {
				ClientGUILogin.this.clientGUILoginImlp
						.insertUsrnameAndPasswordReturn(
								ClientGUILogin.this.textUsrname.getText(),
								ClientGUILogin.this.textPassword.getText());
			} else {
				ClientGUILogin.this.outputMsg
						.setText("Error : complete fields");
			}

		}

	}
}
