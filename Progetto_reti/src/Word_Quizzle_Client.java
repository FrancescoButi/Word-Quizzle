import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.JTextPane;

public class Word_Quizzle_Client implements Runnable {

	private JFrame frame;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private String user = null;

	/**
	 * Launch the application.
	 */

			public void run() {
				try {
					Word_Quizzle_Client window = new Word_Quizzle_Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.err.println("Failed to create window");
				}
			}

	/**
	 * Create the application.
	 */
	public Word_Quizzle_Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setForeground(SystemColor.activeCaption);
		frame.getContentPane().setForeground(SystemColor.activeCaption);
		frame.setBounds(0, 0, 798, 477);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Azione da performare se la finestra viene chiusa preventivamente
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        JOptionPane.showMessageDialog(null, "You have disconnected from the server");
		        Client.logout();
		       // }
            System.exit(0);
		    }
		});
		
		JButton btnRegistrati = new JButton("Register");
		btnRegistrati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//implementa la registrazione
				String response;
				user = txtUsername.getText();
				user = user.replaceAll("\\s+","");
				String password = txtPassword.getText();
				password = password.replaceAll("\\s+","");
				if (user.isBlank() || password.isBlank()) {
					JOptionPane.showMessageDialog(null, "Please fill the empty fields");
				} else {
					response = Client.register (user, password);
					JOptionPane.showMessageDialog(null, response);
				}
			}
		});
		btnRegistrati.setBackground(Color.WHITE);
		btnRegistrati.setBounds(369, 330, 89, 23);
		frame.getContentPane().add(btnRegistrati);
		
		JLabel lblGiRegistrato = new JLabel("Still not registered?");
		lblGiRegistrato.setBounds(362, 305, 128, 14);
		frame.getContentPane().add(lblGiRegistrato);
		
		txtUsername = new JTextField();
		txtUsername.setToolTipText("");
		txtUsername.setBackground(Color.WHITE);
		txtUsername.setBounds(443, 189, 86, 20);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBackground(Color.WHITE);
		txtPassword.setBounds(443, 220, 86, 20);
		frame.getContentPane().add(txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblWordQuizzle = new JLabel("Word Quizzle");
		lblWordQuizzle.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 35));
		lblWordQuizzle.setBounds(306, 128, 289, 50);
		frame.getContentPane().add(lblWordQuizzle);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblUsername.setBounds(306, 192, 89, 14);
		frame.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPassword.setBounds(306, 223, 89, 14);
		frame.getContentPane().add(lblPassword);
	
		JButton btnLogout = new JButton("Logout");		
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String response = Client.logout();
					JOptionPane.showMessageDialog(null, response);
					if (response.contains("Logout")) {System.exit (0);}
				} catch (HeadlessException e) {
					JOptionPane.showMessageDialog(null, "Something went wrong");
				}
			}
		});
		btnLogout.setBackground(Color.WHITE);
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnLogout.setBounds(607, 202, 151, 38);
		frame.getContentPane().add(btnLogout);
		btnLogout.setVisible(false);
		
		//ImageIcon image = new ImageIcon("Image_sources/challenge.bmp");
		JButton btnNewButton = new JButton("CHALLENGE A FRIEND");
		
		btnNewButton.setBounds(255, 154, 264, 86);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.setVisible(false);
		
		JLabel lblWordQuizzle_2 = new JLabel("Word Quizzle");
		lblWordQuizzle_2.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 35));
		lblWordQuizzle_2.setBounds(230, 11, 289, 50);
		frame.getContentPane().add(lblWordQuizzle_2);
		lblWordQuizzle_2.setVisible(false);
		
		JLabel lblBentornatoInWord = new JLabel("Welcome back to Word Quizzle! What will you do?");
		lblBentornatoInWord.setBounds(209, 73, 459, 44);
		frame.getContentPane().add(lblBentornatoInWord);
		lblBentornatoInWord.setVisible(false);
		
		JButton btnMostraLaClassifica = new JButton("Show \r\nfriend scoreboard\r\n");
		btnMostraLaClassifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(null, "List in decreasing order : " + Client.friendscore());
				} catch (HeadlessException e2) {
					JOptionPane.showMessageDialog(null, "Something went wrong");
				}
			}
		});
		btnMostraLaClassifica.setBackground(Color.WHITE);
		btnMostraLaClassifica.setBounds(30, 191, 151, 44);
		frame.getContentPane().add(btnMostraLaClassifica);
		btnMostraLaClassifica.setVisible(false);
		
		JButton btnMostraPunteggio = new JButton("Show score");
		btnMostraPunteggio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JOptionPane.showMessageDialog(null, "Your score is: " + Client.showscore());
				} catch (HeadlessException e) {
					JOptionPane.showMessageDialog(null, "Something went wrong");
				}
			}
		});
		btnMostraPunteggio.setBackground(Color.WHITE);
		btnMostraPunteggio.setBounds(607, 142, 151, 37);
		frame.getContentPane().add(btnMostraPunteggio);
		btnMostraPunteggio.setVisible(false);
		
		JButton btnAggiungiUnAmico_1 = new JButton("Add a friend");
		btnAggiungiUnAmico_1.setBackground(Color.WHITE);
		btnAggiungiUnAmico_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String friend = JOptionPane.showInputDialog("What's your friend's name?");
				try {
					JOptionPane.showMessageDialog(null, Client.addfriend (user , friend));
				} catch (HeadlessException e1) {
					JOptionPane.showMessageDialog(null, "Something went wrong");
				}
			}
		});
		btnAggiungiUnAmico_1.setBounds(30, 142, 151, 38);
		frame.getContentPane().add(btnAggiungiUnAmico_1);
		btnAggiungiUnAmico_1.setVisible(false);
		
		
		JButton btnListaAmici = new JButton("Show friend list");
		btnListaAmici.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(null, "Your friend list " + Client.friendlist());
				} catch (HeadlessException e1) {
					JOptionPane.showMessageDialog(null, "Something went wrong");
				}
			}
		});
		btnListaAmici.setBackground(Color.WHITE);
		btnListaAmici.setBounds(30, 249, 151, 38);
		frame.getContentPane().add(btnListaAmici);
		btnListaAmici.setVisible(false);
		
		JLabel lblTraduciLeParole = new JLabel("CHALLENGE!");
		lblTraduciLeParole.setFont(new Font("MV Boli", Font.BOLD, 15));
		lblTraduciLeParole.setBounds(191, 248, 254, 67);
		frame.getContentPane().add(lblTraduciLeParole);
		lblTraduciLeParole.setVisible(false);
		
		JTextField textPane = new JTextField();
		textPane.setBounds(191, 344, 237, 30);
		frame.getContentPane().add(textPane);
		textPane.setVisible(false);
		
		JLabel wordLabel = new JLabel();
		wordLabel.setBounds(218, 307, 583, 25);
		wordLabel.setEnabled(true);
		frame.getContentPane().add(wordLabel);
		wordLabel.setVisible(false);
		
		JButton btnBackToMenu = new JButton("Back to menu");
		btnBackToMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//reset della gui al menù principale
				lblWordQuizzle_2.setVisible(true);
				btnNewButton.setVisible(true);
				btnAggiungiUnAmico_1.setVisible(true);
				btnLogout.setVisible(true);
				btnNewButton.setVisible(true);
				lblWordQuizzle_2.setVisible(true);
				lblBentornatoInWord.setVisible(true);
				btnMostraLaClassifica.setVisible(true);
				btnMostraPunteggio.setVisible(true);
				btnMostraPunteggio.setVisible(true);
				btnAggiungiUnAmico_1.setVisible(true);
				btnListaAmici.setVisible(true);
				//tolgo residui della vecchia gui
				btnBackToMenu.setVisible(false);
				lblTraduciLeParole.setVisible(false);
				lblTraduciLeParole.setText("CHALLENGE!");
			}
		});
		btnBackToMenu.setBounds(191, 154, 252, 50);
		frame.getContentPane().add(btnBackToMenu);
		btnBackToMenu.setVisible(false);
		
		JButton btnAccedi = new JButton("Login");
		btnAccedi.setBackground(Color.WHITE);
		btnAccedi.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//implementa il pulsante login
				try {
					
					user = txtUsername.getText();
					user = user.replaceAll("\\s+","");
					String password = txtPassword.getText();
					password = password.replaceAll("\\s+","");
					if (user.isBlank() || password.isBlank()) {
						JOptionPane.showMessageDialog(null, "Please fill the empty fields");
					} else {
						DatagramSocket datags = new DatagramSocket();
						int port = datags.getLocalPort();
						new Thread(){
	
							@Override
							public void run() {
								try {
									//dgs.setSoTimeout(2000);
									while (true) {
										//Creo byte buffer per la rcv
										byte [] rcvbuff = new byte [20];
										//Creo dp per la ricezione
										DatagramPacket rcvmsg = new DatagramPacket(rcvbuff, 20);
										datags.receive(rcvmsg);
										String msg = new String(rcvbuff).toString();
										String result = Client.show_match_notification (msg);
										System.out.println(result);
										if (result.contains("iniziata")){
											lblWordQuizzle_2.setVisible(false);
											btnNewButton.setVisible(false);
											btnAggiungiUnAmico_1.setVisible(false);
											btnLogout.setVisible(false);
											btnNewButton.setVisible(false);
											lblWordQuizzle_2.setVisible(false);
											lblBentornatoInWord.setVisible(false);
											btnMostraLaClassifica.setVisible(false);
											btnMostraPunteggio.setVisible(false);
											btnMostraPunteggio.setVisible(false);
											btnAggiungiUnAmico_1.setVisible(false);
											btnListaAmici.setVisible(false);
											btnBackToMenu.setVisible(false);
											///////////////////////////////////////////////////////////////////spawno finestra sfida
											lblTraduciLeParole.setVisible(true);
											lblTraduciLeParole.setText("CHALLENGE!");
											textPane.setVisible(true);
											wordLabel.setVisible(true);									
											wordLabel.setText(result);
											
											JButton btnSendTraduction = new JButton("Submit traduction");
											btnSendTraduction.setBounds(380, 397, 238, 30);
											frame.getContentPane().add(btnSendTraduction);
											btnSendTraduction.addActionListener(new ActionListener() {
												public void actionPerformed(ActionEvent e) {
													String word = textPane.getText();
													String serverresponse = Client.writeword(word);
													if (serverresponse.contains("Match ended") ) {
														//tolgo elementi della sfida
														lblTraduciLeParole.setText(serverresponse);
														textPane.setVisible(false);
														wordLabel.setVisible(false);
														btnSendTraduction.setVisible(false);
														btnBackToMenu.setVisible(true);
													}
													wordLabel.setText(serverresponse);
													
													
												}
											});
										}else {
											JOptionPane.showMessageDialog(null, result);
										}
									}
								} catch (IOException e1) {
									System.err.println("Failed to create or operate on socket:");
								}
							}
	
						}.start();
						String response = Client.login (user, password, port);
						JOptionPane.showMessageDialog(null, response);
						if (response.equalsIgnoreCase("You logged in!")) {
							btnRegistrati.setVisible(false);;
							btnAccedi.setVisible(false);
							lblWordQuizzle.setVisible(false);
							lblPassword.setVisible(false);
							lblUsername.setVisible(false);
							lblGiRegistrato.setVisible(false);
							txtUsername.setVisible(false);
							txtPassword.setVisible(false);
							//
							//
							lblWordQuizzle_2.setVisible(true);
							btnNewButton.setVisible(true);
							btnAggiungiUnAmico_1.setVisible(true);
							btnLogout.setVisible(true);
							btnNewButton.setVisible(true);
							lblWordQuizzle_2.setVisible(true);
							lblBentornatoInWord.setVisible(true);
							btnMostraLaClassifica.setVisible(true);
							btnMostraPunteggio.setVisible(true);
							btnMostraPunteggio.setVisible(true);
							btnAggiungiUnAmico_1.setVisible(true);
							btnListaAmici.setVisible(true);
						}
					}
				} catch (IOException e1) {
					System.err.println("Failed to create or operate on socket:");
				}
			}
		});
		btnAccedi.setBounds(369, 264, 89, 23);
		frame.getContentPane().add(btnAccedi);

		
		
		///////////////////////////////////////////////////////////////////////sfida
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String friend = JOptionPane.showInputDialog("Who do you want to challenge?");
				if (friend == null || friend.isEmpty()){
					JOptionPane.showMessageDialog(null, "Please insert a name");
				}else if (friend.contentEquals(user)) {
					JOptionPane.showMessageDialog(null, "you can't challenge yourself");
				}else {
					String response = Client.challenge (user, friend);
					if (response.contains("iniziata")){
						lblWordQuizzle_2.setVisible(false);
						btnNewButton.setVisible(false);
						btnAggiungiUnAmico_1.setVisible(false);
						btnLogout.setVisible(false);
						btnNewButton.setVisible(false);
						lblWordQuizzle_2.setVisible(false);
						lblBentornatoInWord.setVisible(false);
						btnMostraLaClassifica.setVisible(false);
						btnMostraPunteggio.setVisible(false);
						btnMostraPunteggio.setVisible(false);
						btnAggiungiUnAmico_1.setVisible(false);
						btnListaAmici.setVisible(false);
						btnBackToMenu.setVisible(false);
						///////////////////////////////////////////////////////////////////spawno finestra sfida
						lblTraduciLeParole.setText("CHALLENGE!");
						lblTraduciLeParole.setVisible(true);
						textPane.setVisible(true);
						wordLabel.setVisible(true);										
						wordLabel.setText(response);								
	
						JButton btnSendTraduction = new JButton("Submit traduction");
						btnSendTraduction.setBounds(380, 397, 238, 30);
						frame.getContentPane().add(btnSendTraduction);
						btnSendTraduction.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String word = textPane.getText();
								String serverresponse = Client.writeword(word);
								if (serverresponse.contains("Match ended") ) {
									//tolgo elementi della sfida
									lblTraduciLeParole.setText(serverresponse);
									textPane.setVisible(false);
									wordLabel.setVisible(false);
									btnSendTraduction.setVisible(false);
									btnBackToMenu.setVisible(true);
								}
								wordLabel.setText(serverresponse);	
							}
						});
					} else {
						JOptionPane.showMessageDialog(null, response);
					}
				}
			}
		});	
	}
}
