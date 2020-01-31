import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;

public class Handler implements Runnable {
	
	// Variabili
	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	private Users userlist;
	private User user;
	private ArrayList <Sfida> sfide;
	
	//Costruttore	
	public Handler(Socket s, Users userlist, ArrayList<Sfida> sfide) {
        this.socket = s;
        this.userlist = userlist;
        this.sfide = sfide;
        try {
        	//Buffer per la letture
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Could not open data connection");
		} 
        try {
        	//Buffer per la scrittura
			this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Could not open data connection");
		}  
	}
	
	//esecuzione
	public void run() {     
		
		while (true) {
				try {
					String s = null;
					if (in.ready()) {
						s = in.readLine();
						if (!(s==null) && s.contains ("9997")) { 
							System.out.println(s);
							int result = handle (s);
							if (result == 1) return;
						}
					}
				} catch (IOException e) {
					System.out.println("Something went wrong");
					user.disconnect();
					System.exit(1);
				}
				catch (InterruptedException e) {
					System.out.println("Client disconnected");
				}
		}
    }

	private int handle(String finalmsg) throws InterruptedException {
		
		try {
			//divido il comando nelle parole
			String[] c_command = finalmsg.split(" ");
			
			//Seleziono handler specifico per la funzione richiesta
			switch (c_command[0]) {
			
				/*login
				 * l'utente inserisce nome utente e password
				 * una volta controllata l'esistenza e la correttezza delle
				 * credenziali, il sistema inserisce l'utente
				 * nella lista dei giocatori online
				 */
				case "timeout":
					
				break;
				case "login":
					user = new User (c_command[1], c_command [2]);
					if (userlist.contains((user.getName()))){
						if (userlist.getByName(user.getName()).getPassword().equals(user.getPassword())) {
							if (userlist.getByName(user.getName()).IsConnected()) {
								out.write("User already connected");
								out.newLine();
								out.flush();
								user = null;
							} else {
								userlist.getByName(user.getName()).Connect();
								userlist.getByName(user.getName()).setPort(Integer.valueOf(c_command[3]));;
								out.write("You logged in!");
								out.newLine();
								out.flush();
								
							}
						}else {
							out.write("Login failed, wrong password");
							out.newLine();
							out.flush();
							user = null;
							System.out.println("Login failed, wrong password");
						}
	
					} else {
						out.write("Login failed, user not found. Please register first");
						out.newLine();
						out.flush();
						System.out.println("Login failed, user not found. Please register first");
					}
				break;
				
				/* logout
				 * l'utente si disconnette dal server
				 * il sistema rimuove l'utente dalla lista dei 
				 * giocatori online
				 */
				case "logout":
					if (user != null) {
						if (userlist.contains((user.getName()))){
							userlist.getByName(user.getName()).disconnect();
						}else {
							System.out.println("Logout failed, user not found");
						}
					}
					System.out.println("Connection closed");  
					try {
						out.write("Logout done!");
						out.newLine();
						out.flush();
						out.close();
					} catch (IOException e1) {
						System.out.println("Data connection closing failed");
					}
			        try {
						in.close();
					} catch (IOException e) {
						System.out.println("Data connection closing failed");
					}
			        try {
						socket.close();
					} catch (IOException e) {
						System.out.println("Data connection closing failed");
					}
			        return 1;
			        
			    /* aggiungi amico   
			     * il sistema prende in input il nome 
			     * utente e il nome dell'amico da aggiungere e,
			     * se presenti nel sistema, li aggiunge
			     * come amici
			     */
				case "aggiungi_amico":
					int result = addFriends(c_command[1], c_command [2]);
					if (c_command[1].equals(c_command[2])) {
						out.write ("You can't add yourself as a friend");
					}else if (result == 0) {
						out.write ("Friend added");
					}else {
						out.write ("Operation invalid, check the nicknames");
					}
					out.newLine();
					out.flush();
				break;
				
					//mostra classifica
					case "mostraclassifica":
					User realuser = userlist.getByName(user.getName());
					Users list = new Users();
					ArrayList <String> stringfriendlist = new ArrayList<String>();
					for (int i = 0; i < realuser.getFriends().size(); i++) {
						list.add(userlist.getByName(realuser.getFriends().get(i)));
					}
					list.add(realuser);
					list.userSort();
					for (int i = 0; i < realuser.getFriends().size() + 1; i++) {
						stringfriendlist.add (list.get(i).getName());
					}
					System.out.println(stringfriendlist);
					out.write(stringfriendlist.toString());
					out.newLine();
					out.flush();
				break;
				
				//addfriend
				case "lista_amici":
					try {
						Gson gson = new Gson();
						System.out.println(userlist.getByName(user.getName()).getFriends());
						out.write(gson.toJson(userlist.getByName(user.getName()).getFriends()));
						out.newLine();
						out.flush();
					} catch (IOException e1) {
						System.out.println("Data connection with client unexpectedly closed");
					}
				break;
				
				//sfida
				case "sfida":
					if (c_command.length > 3) {
						System.out.println (finalmsg);
						if (userlist.contains(c_command [2]) && (userlist.getByName(c_command[1]).IsConnected()) && (userlist.getByName(c_command[2]).IsConnected()) &&(userlist.getByName(user.getName()).hasFriend(c_command[2]))) {
							launch_match (c_command[1], c_command [2], 1);
						} else {
							out.write("Can't challenge " + c_command[2] + " now (check if he's online or in your friend list).");
							out.newLine();
							out.flush();
						}
					} else {
						out.write("Please insert a valid name.");
						out.newLine();
						out.flush();
					}
	//				out.write("sfida lanciata");
	//				out.newLine();
	//				out.flush();
				break;
				
				//mostra punteggio
				case "mostrapunteggio":
					try {
						out.write (String.valueOf(userlist.getByName(user.getName()).getScore()));
						out.newLine();
						out.flush();
					} catch (IOException e1) {
						System.out.println("Data connection with client unexpectedly closed");
					}
				break;
				
				case "accetta":
					for (int i = 0; i < sfide.size(); i++) {
						if (sfide.get(i).hasPendingInvite(user.getName())) {
							sfide.get(i).setPending(0);
							launch_match(sfide.get(i).getUser1(), sfide.get(i).getUser2(), 0);
						}
					}
				break;
				
				case "rifiuta":
					for (int i = 0; i < sfide.size(); i++) {
						if (sfide.get(i).hasPendingInvite(user.getName())) {
							sfide.get(i).setRefused(1);
						}
					}
					out.write("Challenge refused");
					out.newLine();
					out.flush();				
				break;
				
				//others
				default:
					try {
						out.write("invalid");
						out.newLine();
						out.flush();
					} catch (IOException e) {
						System.out.println("Data connection with client unexpectedly closed");
					}
				
			}
		} catch (IOException e) {
			System.out.println("Something went wrong, disconnecting..");
			user.disconnect();
			System.exit(1);
		}
		return 0;
	}
	

	private synchronized void launch_match(String user, String friend, int accepted) {		
		
		if (accepted == 1) {
			//creo oggetto sfida per user 1 e 2
			
			//Creo arraylist di parole e relative traduzioni
			ArrayList <Word_Traduction> words = new ArrayList <Word_Traduction>();
			//invio richiesta per la traduzione delle parole
			for (int i = 0; i < 4; i++) {
				
				//genero casualmente parola italiana e la aggiungo alla lista
				String word = null;
				try {
					word = new String (GetItWord());
				} catch (IOException e) {
					System.out.print("Could not get word from server");
				}
				Word_Traduction trial = new Word_Traduction (word);
				words.add(trial);
				
				words.get(i).addword(word);
				//invio richiesta e aggiungo la traduzione alla lista
				System.out.println(words.get(i).getword());
				try {
					words.get(i).Add_Traductions(getalltraductions(word));
				} catch (IOException e) {
					System.out.print("Could not get traductions from web server");
				}		
				
			}

			Sfida sfida = new Sfida (user, friend);
			sfida.setWord_traductions(words);
			sfide.add(sfida);
			
			int friendport = userlist.getByName(friend).getPort();
			DatagramSocket dgs = null;
			try {
				dgs = new DatagramSocket();
			} catch (SocketException e) {
				System.out.print("Could not create datagram socket");
			}
			
			//Creo byte buffer per la rcv
			byte [] arbuff = new byte [20];
			ByteBuffer buff = ByteBuffer.allocate(20);
			String msg = new String(user);
			buff.put(msg.getBytes());
			arbuff = buff.array();
			
			//Creo dp per la ricezione
			DatagramPacket sndmsg;
			try {
				sndmsg = new DatagramPacket(arbuff, 20, InetAddress.getByName("localhost"), friendport);
				dgs.send (sndmsg);
				dgs.close();
			} catch (UnknownHostException e) {
				System.out.print("Failed to get server address");
			} catch (IOException e) {
				System.out.print("Failed to send datagram");				
			}
		}
		
		for (int i = 0; i < sfide.size(); i++) {
			Sfida sfida = sfide.get(i);
			if (sfide.get(i).hasPendingInvite(user)) {
				while (sfide.get(i).getPending() != 0) {
					try {
						if (sfide.get(i).getRefused() == 1) {
							sfide.get(i).clean();
							out.write("Your friend refused the challenge!");
							out.newLine();
							out.flush();
							return;
						}
						Thread.sleep(200);
					} catch (InterruptedException e) {
						System.out.print("Interruption failed while waiting");
					} catch (IOException e) {
						System.out.print("Failed to comunicate with client");
					}
				}
				String [] responses = new String [4];
				try {
					// traduzioni
					long init = System.currentTimeMillis();
					socket.setSoTimeout(60 * 1000);
					int j = 0;
					while (j < 4) {
						if (j == 0) {
							out.write("sfida iniziata: traduci la parola: " +  sfida.getWord_traductions().get(j).getword());
						}else {
							out.write(sfida.getWord_traductions().get(j).getword());
						}
						out.newLine();
						out.flush();					
						responses [j] = in.readLine();
						socket.setSoTimeout((int) ((60 * 1000) - (System.currentTimeMillis() - init)));	
						j++;
					}
					
					int points = evaluate_response (responses, sfide.get(i));
					if (accepted == 1) {
						sfide.get(i).setUser1done(true);
						sfide.get(i).setUser1points(points);
						System.out.println ("Waiting for opponent to finish");
						while (sfide.get(i).isUser2done() == false) {
							Thread.sleep(400);
							System.out.println (".");
						}
					} else {
						sfide.get(i).setUser2done(true);
						sfide.get(i).setUser2points(points);
						System.out.println ("Waiting for opponent to finish");
						while (sfide.get(i).isUser1done() == false) {
							Thread.sleep(400);
							System.out.println (".");
						}
						sfide.get(i).setMatchended(true);
					}
					
					end_match(i, points);
					if (accepted == 1) {
						while (sfide.get(i).isMatchended() == false) {
							Thread.sleep(400);
						}
						remove_match(i);
					}
				} catch (SocketTimeoutException e) {
					System.out.println("Client timed out!");
					int points = evaluate_response (responses, sfide.get(i));
					if (accepted == 1){
						//userlist.getByName(user).disconnect();
						sfide.get(i).setUser1done(true);
						sfide.get(i).setUser1points(-10);
						while (sfide.get(i).isUser2done() == false) {
							try {
								Thread.sleep(400);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							System.out.println (".");
						}
					} else {
						//userlist.getByName(friend).disconnect();
						sfide.get(i).setUser2done(true);
						sfide.get(i).setUser2points(-10);
						while (sfide.get(i).isUser1done() == false) {
							try {
								Thread.sleep(400);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						sfide.get(i).setMatchended(true);
					}
					try {
						end_match(i, points);
					} catch (IOException e1) {
						System.out.println("Match ended for timeout");
					}
					if (accepted == 1) {
						while (sfide.get(i).isMatchended() == false) {
							try {
								Thread.sleep(400);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						remove_match(i);
					}
				} catch (IOException e) {
					System.out.println("Client disconnected from the match");
					int points = evaluate_response (responses, sfide.get(i));
					if (accepted == 1){
						userlist.getByName(user).disconnect();
						sfide.get(i).setUser1done(true);
						sfide.get(i).setUser1points(-10);
						while (sfide.get(i).isUser2done() == false) {
							try {
								Thread.sleep(400);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							System.out.println (".");
						}
					} else {
						userlist.getByName(friend).disconnect();
						sfide.get(i).setUser2done(true);
						sfide.get(i).setUser2points(-10);
						while (sfide.get(i).isUser1done() == false) {
							try {
								Thread.sleep(400);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						sfide.get(i).setMatchended(true);
					}
					try {
						end_match(i, points);
					} catch (IOException e1) {
						System.out.println("Match ended for disconnection");
					}
					if (accepted == 1) {
						while (sfide.get(i).isMatchended() == false) {
							try {
								Thread.sleep(400);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						remove_match(i);
					}
				} catch (InterruptedException e) {
					System.out.println("Could not interrupt thread while waiting");
				}
			}
		}
	}

	private void remove_match(int i) {
		sfide.get(i).clean();		
	}

	private synchronized void end_match(int i, int points) throws IOException {
		String winner = calculate_result (sfide.get(i));
		if (winner.equalsIgnoreCase("pareggio")) {
			userlist.getByName(sfide.get(i).getUser1()).setScore(sfide.get(i).getUser1points());
			userlist.getByName(sfide.get(i).getUser2()).setScore(sfide.get(i).getUser2points());
			out.write("Match ended with a tie!");
			out.newLine();
			out.flush();
		}else {	
			if (sfide.get(i).getUser1().equals(winner)) {
				userlist.getByName(sfide.get(i).getUser1()).setScore(sfide.get(i).getUser1points() + 1);
				userlist.getByName(sfide.get(i).getUser2()).setScore(sfide.get(i).getUser2points() -1);
			}else {
				userlist.getByName(sfide.get(i).getUser2()).setScore(sfide.get(i).getUser2points() + 1);
				userlist.getByName(sfide.get(i).getUser1()).setScore(sfide.get(i).getUser1points() -1);
			}
			//Calcolo il risultato e mando la risposta
			out.write("Match ended, winner is " + winner);
			out.newLine();
			out.flush();	
		}
		update (userlist);
		updatedata (sfide);
	}

	private String calculate_result(Sfida sfida) {
		if (sfida.getUser1points() > sfida.getUser2points()) {
			return sfida.getUser1();
		}
		if (sfida.getUser1points() < sfida.getUser2points()) {
			return sfida.getUser2();
		}
		if (sfida.getUser1points() == sfida.getUser2points()) {
			return "pareggio";
		}
		return null;
	}

	private int evaluate_response(String[] responses, Sfida sfida) {
		int points = 0;
		for (int i = 0; i < 4; i++) {
			if (sfida.getWord_traductions().get(i).gettranslations().contains(responses [i])){
				points ++;
			} else {
				points --;
			}
		}
		return points;
	}

	public int addFriends (String nickUtente, String nickAmico) {
		
		int result = 1;
		if (nickUtente.contentEquals(nickAmico)) return 1;
		if (userlist.contains(nickUtente) && userlist.contains(nickAmico)) {
			result = userlist.addFriends (nickUtente, nickAmico);
			update (userlist);
			return 0;
		}
		
		return result;
		
	}
		
	private synchronized void update(Users userlist) {
		File userfile = new File ("./user list/userfile.txt");
		userfile.delete();
		//////////////////////////////
		Gson gson = new Gson ();
		String juser = gson.toJson(userlist);
	    try {
			FileWriter fw = new FileWriter(userfile,false);
			fw.write(juser);
			fw.close();
		} catch (IOException e) {
			System.out.println ("Could not update the server data");
		}
	}

	private synchronized void updatedata (ArrayList <Sfida> sfide) {
		File serverdata = new File ("./user list/serverdata.txt");
		serverdata.delete();
		//////////////////////////////
		int matches = sfide.size();
		int totalpoints = 0;
		for (int i = 0; i < sfide.size(); i++) {
			totalpoints += (sfide.get(i).getUser1points() + sfide.get(i).getUser2points());
		}
		int avgpoints = totalpoints/(2 * matches);
	    try {
			FileWriter fw = new FileWriter(serverdata,false);
			fw.write("Total matches : " + matches + ", average user points : " + avgpoints + ".");
			fw.close();
		} catch (IOException e) {
			System.out.println ("Could not update the server data");
		}
	}

	private static String sendGET(String word) throws IOException {
		//creazione request URL
		String request_url = URL_builder (word);
		StringBuilder result = new StringBuilder();
		
		//invio request url al sito
        URL url = new URL(request_url);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        
        //creo BufferedReader per la ricezione della risposta
        BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null){
            result.append(line);
        }
        rd.close();
        
        //Uso la risposta
        return (result.toString());
	}
	
	
	
	
	private static String URL_builder (String word) throws IOException {
		
		//creazione stringa url per la request
		String traduction = "it|en";
		String GET_URL = "https://api.mymemory.translated.net/get?q=" + word + "&langpair=" + traduction ;
		return GET_URL;
	}
	
	
	
	
	private static ArrayList <String> getalltraductions (String word) throws IOException {
		String result = new String (sendGET(word));
		ArrayList <String> trad = new ArrayList <String>();
		///////////////////
		Gson gson = new Gson();
		Response founderArray = gson.fromJson(result, Response.class); 
		int number = (founderArray.matches.length);
		for (int i = 0; i < number; i++) {
			//controllo che la traduzione non sia un esempio, ma la parola originaria
			if (founderArray.matches[i].segment.equalsIgnoreCase(word)) {
				trad.add(founderArray.matches[i].translation);
			}
		}
		////////////////////
		return trad;
	}
	
	
	
	
	private static String GetItWord() throws IOException {
		File file = new File("./words file/parole.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
        Random rnd = new Random();
        int l = rnd.nextInt(1000);
        for(int i = 0; i < l; ++i)
        	  br.readLine();
        String line = br.readLine();
        br.close();
        return line;
	}
	
		
	
}
