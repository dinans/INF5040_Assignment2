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

	accountReplicaGUI gUI = new accountReplicaGUI("Account replica");
	
	/*static JFrame frame;
	static JTextArea area;
	JLabel peersLabel;
	JLabel balanceLabel;
	JLabel reqPeers;
	JButton addinterest;
	JButton rbalance;
	JButton withdraw;
	JButton deposit;*/

	public void connect() {

	}

	public void checkPeersNumber() {
		/*peersLabel.setText(peersLabtxt + clients);
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
		}*/
		gUI.peersLabel.setText(gUI.peersLabtxt + clients);
		gUI.reqPeers.setText(gUI.reqPeerstxt + (numberOfReplicas - clients) + " more peer(s) to join");
		if (clients >= numberOfReplicas) {
			gUI.deposit.setEnabled(true);
			gUI.rbalance.setEnabled(true);
			gUI.addinterest.setEnabled(true);
			gUI.withdraw.setEnabled(true);
			gUI.reqPeers.setVisible(false);
		} else {
			gUI.deposit.setEnabled(false);
			gUI.rbalance.setEnabled(false);
			gUI.addinterest.setEnabled(false);
			gUI.withdraw.setEnabled(false);
			gUI.reqPeers.setVisible(true);
		}
	}

	//public void createGUI() {

	
		
		
		
		
	public void addListeners() {
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
		gUI.deposit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int toAdd;
				try {
					toAdd = Integer.parseInt(gUI.field.getText());
					gUI.area.append("\nAdding" + toAdd + "NOK to account...");
					deposAcc(toAdd);

				} catch (NumberFormatException e1) {
					gUI.area.append("Wrong number format.");
				}

			}
		});
		/*
		 * ----------------------------------------------------------------------
		 * - withdraw
		 * ------------------------------------------------------------
		 * -----------
		 */
		gUI.withdraw.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int toWd;
				try {
					toWd = Integer.parseInt(gUI.field.getText());
					if (toWd <= balance) {
						gUI.area.append("\nWithdrawing" + toWd
								+ "NOK from account...");
						withdAcc(toWd);
					} else {
						int s = gUI.area.getText().length();
						gUI.area.append("\nTransaction denied. You are allowed to withdraw not more than "
								+ balance + " NOK");
						int en = gUI.area.getText().length();
						gUI.area.select(s, en);
						gUI.area.append("Selected text " + s + " to " + en);

						gUI.area.setSelectedTextColor(Color.RED);
						gUI.area.setSelectionColor(Color.RED);

					}
				} catch (NumberFormatException e1) {
					gUI.area.append("\nWrong number format.");
				}

			}
		});
		/*
		 * ----------------------------------------------------------------------
		 * - add interest
		 * --------------------------------------------------------
		 * ---------------
		 */
		gUI.addinterest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int toAdd;
				try {
					toAdd = Integer.parseInt(gUI.field.getText());
					gUI.area.append("\nAdding" + toAdd + "% to account...");
					addInt(toAdd);

				} catch (NumberFormatException e1) {
					gUI.area.append("Wrong number format.");
				}

			}
		});
		gUI.rbalance.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshBal();
			}
		});
		gUI.exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gUI.dispose();
			}
		});
		gUI.setMinimumSize(new Dimension(300, 500));
		
		gUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gUI.pack();
		gUI.setVisible(true);
	}
		// draw frame
		

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
		//balance += toAdd;

		SpreadMessage msg = new SpreadMessage();
		msg.setSafe();
		msg.addGroup(accountName);
		
		
		byte mess[] = new String("a"+Double.toString(toAdd)).getBytes();
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
		double a = round((balance*toAdd / 100.0),2);
//		balance = round(balance * (1 + (toAdd / 100.0)), 2);

		SpreadMessage msg = new SpreadMessage();
		
		msg.setSafe();
		msg.addGroup(accountName);
		//byte mess[] = new String(Double.toString(balance)).getBytes();
		byte mess[] = new String("a"+Double.toString(a)).getBytes();
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
//		balance -= toWd;

		SpreadMessage msg = new SpreadMessage();
		
		msg.setSafe();
		msg.addGroup(accountName);
		byte mess[] = new String("a"+Double.toString(-toWd)).getBytes();

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
		gUI.balanceLabel.setText(gUI.balanceLabtxt + balance);
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
			gUI.area.append("\nConnected to " + accountName + " group on "
					+ serverName);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			gUI.area.setForeground(Color.RED);
			gUI.area.append("\nConnection error: unknown host name");
			e.printStackTrace();
		} catch (SpreadException e) {
			// TODO Auto-generated catch block
			gUI.area.setForeground(Color.RED);
			gUI.area.append("\nConnection error: unable to connect spread server");
			e.printStackTrace();
		}

		connection.add(new BasicMessageListener() {

			@Override
			public void messageReceived(SpreadMessage message) {

				System.out.println(message.getType());

				if (message.isRegular()) {
/*-
 * ---------------------------------------------------
 * Replica got new regular message. That means someone updated balance
 * ---------------------------------------------------
 * */
					gUI.area.append("\nBalance has been updated.");
					/*-
					 * ---------------------------------------------------
					 * If it is own message do not update balance. 
					 * ---------------------------------------------------
					 * */
					/*if(message.getSender().equals(connection.getPrivateGroup())) 
						{
						
						refreshBal();
						}
					else
					{*/
						/*-
						 * ---------------------------------------------------
						 * If it is message from other group members
						 * ---------------------------------------------------
						 * */
						
						String oper=(new String(message.getData())).substring(0, 1);
						double val = Double.parseDouble((new String(message.getData())).substring(1));
						
						// Here "a" stands for operations with account, while "i" is for "initialize balance message"
						if(oper.equals("a"))
						{
							balance += val;
							
							refreshBal();
						}
						else if(oper.equals("i"))
						{
							balance = val;
							refreshBal();
						}
					//balance += Double.parseDouble((new String(message.getData())));
					//refreshBal();
					//}
				} else if (message.isMembership()) {
					MembershipInfo info = message.getMembershipInfo();

					if (info.isCausedByDisconnect()
							&& info.isRegularMembership()) {
						clients = info.getMembers().length;
						gUI.area.append("\nMember disconnected: "
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
							gUI.area.append("\nANOTHER Member connected: "
									+ info.getJoined().toString());

							SpreadMessage msg = new SpreadMessage();
							byte mess[] = new String("i"+Double.toString(balance))
									.getBytes();

							msg.setData(mess);
							msg.setReliable();
							msg.addGroup(accountName);
							try {
								connection.multicast(msg);
								gUI.area.append("Sending account info to new member...");

							} catch (SpreadException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else connInited=true;
					}

				} else if (message.isReject()) {
					gUI.area.append("Received rejected message");
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
		client.addListeners();

		client.init(adress, accountName);
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (args.length == 4) {
			if (client.connInited&&(client.clients >= numberOfReplicas)) {
				client.parseDocument(args[3]);

			} else {
				// wait some time for initialization then repeat checking
				
				
				client.gUI.area.append("\nCannot parse documect. Waiting for more clients to join.");
			}

			// frame.dispose();
		}

	}

	private void parseDocument(String fileToReadFrom) {
		File file = new File(fileToReadFrom);
		Scanner scanner = null;

		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			gUI.area.append("\nCannot parse documect. Wrong file name");
			
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
	 * helps to round balance with desired precision
	 * -------------------------------------------------------
	 */
	private static double round(double number, int scale) {
		int pow = (int) Math.pow(10, scale);
		double tmp = number * pow;
		return (double) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
	}

}