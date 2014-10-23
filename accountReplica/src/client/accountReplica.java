package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import spread.BasicMessageListener;
import spread.MembershipInfo;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

public class accountReplica {
	public static SpreadConnection connection;
	public String connName;
	public String peersLabtxt = "Total number of clients connected:";
	public String balanceLabtxt = "Current balance on the account:";
	public String reqPeerstxt = "Waiting for ";
	public static String accountName = "";
	private int port = 4333;
	private static int numberOfReplicas;
	private static int clients;
	static boolean connInited=false;

	private static double balance = 0.0;

	static JFrame frame;
	static JTextArea area;
	JLabel peersLabel;
	JLabel balanceLabel;
	JLabel reqPeers;
	JButton addinterest;
	JButton rbalance;
	JButton withdraw;
	JButton deposit;

	public void connect() {

	}

	public void checkPeersNumber() {
		peersLabel.setText(peersLabtxt + clients);
		reqPeers.setText(reqPeerstxt + (numberOfReplicas - clients)
				+ " more peer(s) to join");
		if (clients >= numberOfReplicas) {
			deposit.setEnabled(true);
			rbalance.setEnabled(true);
			addinterest.setEnabled(true);
			withdraw.setEnabled(true);
			reqPeers.setVisible(false);
		} else {
			deposit.setEnabled(false);
			rbalance.setEnabled(false);
			addinterest.setEnabled(false);
			withdraw.setEnabled(false);
			reqPeers.setVisible(true);
		}
	}

	public void createGUI() {

		/*
		 * ----------------------------------------------------------------------
		 * - description of graph. components
		 * ------------------------------------
		 * -----------------------------------
		 */
		peersLabel = new JLabel(peersLabtxt + clients);
		balanceLabel = new JLabel(balanceLabtxt + balance);
		reqPeers = new JLabel(reqPeerstxt + (numberOfReplicas - clients)
				+ " more peer(s) to join");
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
		JButton exit = new JButton("Exit");
		JPanel actionsPanel = new JPanel();
		JPanel statusPanel = new JPanel();
		JPanel connStatusPanel = new JPanel();
		JTextField field = new JTextField(20);

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

		frame = new JFrame("Distributed banking application");
		frame.setLayout(gbl);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 50;
		c.insets = new Insets(20, 0, 0, 0);
		gbl.setConstraints(field, c);
		frame.add(field);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 0, 0, 0);
		c.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(deposit, c);
		frame.add(deposit);

		c.gridx = 0;
		c.gridy = 2;
		gbl.setConstraints(withdraw, c);
		frame.add(withdraw);

		c.gridx = 0;
		c.gridy = 3;
		gbl.setConstraints(addinterest, c);
		frame.add(addinterest);

		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0, 5, 0, 0);
		gbl.setConstraints(reqPeers, c);
		frame.add(reqPeers);

		c.gridy = 1;
		gbl.setConstraints(peersLabel, c);
		frame.add(peersLabel);

		c.gridy = 2;
		gbl.setConstraints(balanceLabel, c);
		frame.add(balanceLabel);

		c.gridy = 3;
		gbl.setConstraints(rbalance, c);
		frame.add(rbalance);

		c.gridy = 4;
		gbl.setConstraints(exit, c);
		frame.add(exit);

		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty = 2.0;
		gbl.setConstraints(area, c);
		frame.add(area);

		/*
		 * ----------------------------------------------------------------------
		 * - LISTENERS FOR BUTTONS
		 * ----------------------------------------------
		 * ------------------------
		 */

		/*
		 * ----------------------------------------------------------------------
		 * - deposit
		 * ------------------------------------------------------------
		 * -----------
		 */
		deposit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int toAdd;
				try {
					toAdd = Integer.parseInt(field.getText());
					area.append("\nAdding" + toAdd + "NOK to account...");
					deposAcc(toAdd);

				} catch (NumberFormatException e1) {
					area.append("Wrong number format.");
				}

			}
		});
		/*
		 * ----------------------------------------------------------------------
		 * - withdraw
		 * ------------------------------------------------------------
		 * -----------
		 */
		withdraw.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int toWd;
				try {
					toWd = Integer.parseInt(field.getText());
					if (toWd <= balance) {
						area.append("\nWithdrawing" + toWd
								+ "NOK from account...");
						withdAcc(toWd);
					} else {
						int s = area.getText().length();
						area.append("\nTransaction denied. You are allowed to withdraw not more than "
								+ balance + " NOK");
						int en = area.getText().length();
						area.select(s, en);
						area.append("Selected text " + s + " to " + en);

						area.setSelectedTextColor(Color.RED);
						area.setSelectionColor(Color.RED);

					}
				} catch (NumberFormatException e1) {
					area.append("\nWrong number format.");
				}

			}
		});
		/*
		 * ----------------------------------------------------------------------
		 * - add interest
		 * --------------------------------------------------------
		 * ---------------
		 */
		addinterest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int toAdd;
				try {
					toAdd = Integer.parseInt(field.getText());
					area.append("\nAdding" + toAdd + "% to account...");
					addInt(toAdd);

				} catch (NumberFormatException e1) {
					area.append("Wrong number format.");
				}

			}
		});
		rbalance.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshBal();
			}
		});
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		// draw frame
		frame.setMinimumSize(new Dimension(300, 500));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	/*
	 * -----------------------------------------------------------------------
	 * IMPLEMENTATION OF BUTTONS FUNCTIONALITY
	 * -----------------------------------------------------------------------
	 */

	/*
	 * -----------------------------------------------------------------------
	 * deposit
	 * -----------------------------------------------------------------------
	 */
	public static void deposAcc(int toAdd) {
		balance += toAdd;

		SpreadMessage msg = new SpreadMessage();
		msg.setReliable();
		msg.addGroup(accountName);
		
		//byte mess[] = new String(Double.toString(balance)).getBytes();
//deposit-beta
		byte mess[] = new String(Double.toString(toAdd)).getBytes();
		msg.setData(mess);

		try {
			connection.multicast(msg);

		} catch (SpreadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * -----------------------------------------------------------------------
	 * add interest
	 * -----------------------------------------------------------------------
	 */

	public static void addInt(int toAdd) {
		balance = round(balance * (1 + (toAdd / 100.0)), 2);

		SpreadMessage msg = new SpreadMessage();
		/*
		 * msg.setSafe(); msg.setData(new
		 * String(Double.toString(balance)).getBytes());
		 */
		msg.setReliable();
		msg.addGroup(accountName);
		byte mess[] = new String(Double.toString(balance)).getBytes();

		msg.setData(mess);

		try {
			connection.multicast(msg);

		} catch (SpreadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * -----------------------------------------------------------------------
	 * withdraw
	 * -----------------------------------------------------------------------
	 */
	public static void withdAcc(int toWd) {
		balance -= toWd;

		SpreadMessage msg = new SpreadMessage();
		/*
		 * msg.setSafe(); msg.setData(new
		 * String(Double.toString(balance)).getBytes());
		 */
		msg.setReliable();
		msg.addGroup(accountName);
		byte mess[] = new String(Double.toString(balance)).getBytes();

		msg.setData(mess);

		try {
			connection.multicast(msg);

		} catch (SpreadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * -----------------------------------------------------------------------
	 * refresh balance --- button is not actually needed, balance is being
	 * updated automatically on permanent label: 1)after each transaction
	 * 2)after new connection
	 * -----------------------------------------------------------------------
	 */
	public void refreshBal() {
		balanceLabel.setText(balanceLabtxt + balance);
	}

	public void init(String serverName, String accountName) {

		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		connName = "CliNum-" + randomInt;

		connection = new SpreadConnection();
		try {

			connection.connect(InetAddress.getByName(serverName), port,
					connName, false, true);

			SpreadGroup group = new SpreadGroup();
			group.join(connection, accountName);
			area.append("\nConnected to " + accountName + " group on "
					+ serverName);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			area.setForeground(Color.RED);
			area.append("\nConnection error: unknown host name");
			e.printStackTrace();
		} catch (SpreadException e) {
			// TODO Auto-generated catch block
			area.setForeground(Color.RED);
			area.append("\nConnection error: unable to connect spread server");
			e.printStackTrace();
		}

		connection.add(new BasicMessageListener() {

			@Override
			public void messageReceived(SpreadMessage message) {

				System.out.println(message.getType());

				if (message.isRegular()) {

					area.append("\nBalance has been updated.");
					if(message.getSender().equals(connection.getPrivateGroup()))
						{
						area.append("\nI am doing smth: ");
						}
					else
					{
						area.append("\nSomeone else is doing smth: ");
					balance += Double.parseDouble((new String(message.getData())));
					refreshBal();
					}
				} else if (message.isMembership()) {
					MembershipInfo info = message.getMembershipInfo();

					if (info.isCausedByDisconnect()
							&& info.isRegularMembership()) {
						clients = info.getMembers().length;
						area.append("\nMember disconnected: "
								+ info.getDisconnected().toString());
						checkPeersNumber();
					}
					if (info.isCausedByJoin()) {
						clients = info.getMembers().length;

						// When someone new joins we should send balance message
						// to him!!!
						checkPeersNumber();
						if (!info.getJoined().equals(
								connection.getPrivateGroup())) // but not to
																// send zero
																// balance of
																// new group
																// member...
						{
							area.append("\nANOTHER Member connected: "
									+ info.getJoined().toString());

							SpreadMessage msg = new SpreadMessage();
							byte mess[] = new String(Double.toString(balance))
									.getBytes();

							msg.setData(mess);
							msg.setReliable();
							msg.addGroup(accountName);
							try {
								connection.multicast(msg);
								area.append("Sending account info to new member...");

							} catch (SpreadException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else connInited=true;
					}

				} else if (message.isReject()) {
					area.append("Received rejected message");
				}

			}

		});

	}

	public static void main(String[] args) {
		System.out.println("Creating new client...");
		String adress = "";

		String fileName;
		// parse args
		if (args.length > 2) {
			adress = args[0];
			accountName = args[1];
			numberOfReplicas = Integer.parseInt(args[2]);
			if (args.length > 3)
				fileName = args[3];

		}

		else {
			System.out
					.print("Usage: accountReplica "
							+ "\t<server address>  		    : address of the Spread server\n"
							+ "\t\t\t<account name>   		    : name of the account\n"
							+ "\t\t\t<number of replicas>                : number of clients that will be initially deployed for <account name>\n"
							+ "\t\t\t[file name]      		    : name and path to file that contains necessary commends(optional)\n");
			System.exit(0);
		}

		accountReplica client = new accountReplica();
		client.createGUI();

		client.init(adress, accountName);
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (args.length == 4) {
			if (connInited&&(clients >= numberOfReplicas)) {
				parseDocument(args[3]);

			} else {
				// wait some time for initialization then repeat checking
				
				
				area.append("\nCannot parse documect. Waiting for more clients to join.");
			}

			// frame.dispose();
		}

	}

	private static void parseDocument(String fileToReadFrom) {
		File file = new File(fileToReadFrom);
		Scanner scanner = null;

		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			area.append("\nCannot parse documect. Wrong file name");
			
		}
		while (scanner.hasNext()) {
			String command = scanner.next();
			int par = scanner.nextInt();
			if (command.equals("deposit")) {
				//System.out.println("DEOPSIT HERE");
				deposAcc(par);
			} else if (command.equals("withdraw")) {
				if (par<=balance)
				{withdAcc(par);
				
				}
			} else if (command.equals("addinterest")) {
			
				addInt(par);
			} else if (command.equals("sleep")) {
				try {
					Thread.currentThread().sleep(par);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * ------------------------------------------------------- This function
	 * helps to round balance with nec. precision
	 * -------------------------------------------------------
	 */
	private static double round(double number, int scale) {
		int pow = (int) Math.pow(10, scale);
		double tmp = number * pow;
		return (double) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
	}

}