package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class accountReplicaGUI extends JFrame{

	JTextArea area;
	JTextField field;
	
	JLabel peersLabel;
	JLabel balanceLabel;
	JLabel reqPeers;
	
	JButton addinterest;
	JButton rbalance;
	JButton withdraw;
	JButton deposit;
	JButton exit;
	
	public String peersLabtxt = "Total number of clients connected:";
	public String balanceLabtxt = "Current balance on the account:";
	public String reqPeerstxt = "Waiting for ";
	
	public accountReplicaGUI(String header) {
		super(header);
		/*
		 * ----------------------------------------------------------------------
		 * - description of graph. components
		 * ------------------------------------
		 * -----------------------------------
		 */
		peersLabel = new JLabel(peersLabtxt );
		balanceLabel = new JLabel(balanceLabtxt);
		reqPeers = new JLabel();
		Font font = new Font("Verdana", Font.ITALIC, 11);
		balanceLabel.setBackground(Color.RED);
		rbalance = new JButton("Refresh balance");
		rbalance.setEnabled(false);
		deposit = new JButton("Deposit");

		deposit.setEnabled(false);
		withdraw = new JButton("Withdraw");
		withdraw.setEnabled(false);
		addinterest = new JButton("Add interest");
		addinterest.setEnabled(false);
		exit = new JButton("Exit");
		JPanel actionsPanel = new JPanel();
		JPanel statusPanel = new JPanel();
		JPanel connStatusPanel = new JPanel();
		field = new JTextField(20);

		/*
		 * ----------------------------------------------------------------------
		 * - adding of graph. components
		 * ----------------------------------------
		 * -------------------------------
		 */
		area = new JTextArea();
		area.setForeground(Color.BLACK);
		area.append("Connecting...");

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		setLayout(gbl);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 50;
		c.insets = new Insets(20, 0, 0, 0);
		gbl.setConstraints(field, c);
		add(field);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 0, 0, 0);
		c.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(deposit, c);
		add(deposit);

		c.gridx = 0;
		c.gridy = 2;
		gbl.setConstraints(withdraw, c);
		add(withdraw);

		c.gridx = 0;
		c.gridy = 3;
		gbl.setConstraints(addinterest, c);
		add(addinterest);

		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 5, 0, 0);
		gbl.setConstraints(reqPeers, c);
		add(reqPeers);

		c.gridy = 1;
		gbl.setConstraints(peersLabel, c);
		add(peersLabel);

		c.gridy = 2;
		gbl.setConstraints(balanceLabel, c);
		add(balanceLabel);

		c.gridy = 3;
		gbl.setConstraints(rbalance, c);
		add(rbalance);

		c.gridy = 4;
		gbl.setConstraints(exit, c);
		add(exit);

		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty = 2.0;
		gbl.setConstraints(area, c);
		add(area);
	
}
}