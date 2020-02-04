import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;


public class Client {
	
	private static int commandCode;
	private static BufferedWriter out;
    private static BufferedReader in_user;
    private static BufferedReader in_server;
    private Thread window = new  Thread (new Word_Quizzle_Client());
    
    public static void main(String[] args) {
        
    	//Inizializzo commandcode e connetto al server
    	commandCode = 9997;
    	try {
			Socket clientSocket = new Socket("localhost", 9997);
			out = new BufferedWriter(new OutputStreamWriter (clientSocket.getOutputStream()));
	        in_user = new BufferedReader(new InputStreamReader(System.in));
	        in_server = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    Thread window = new  Thread (new Word_Quizzle_Client());
		    window.start();
		} catch (IOException e) {
			System.out.println("Couldn't connect to server or create window, shutting down..");
			System.exit(1);
		}
		
    }

	public static String register(String user, String password){
		RMIInterface serverObj;
	    Remote remoteObj;
	    Registry r;
		try {
			// Connetto al server RMI per la registrazione
			r = LocateRegistry.getRegistry(999);
			remoteObj = r.lookup("SERVER_TEST");
		    serverObj = (RMIInterface) remoteObj;
		    int response = serverObj.registra_utente(user, password);
		    
			if (response == 0) {
				return new String ("User Registered");
			} else {
				return new String ("User already exists");
			}
		} catch (RemoteException e) {
			System.out.println("Couldn't connect to remote server, shutting down..");
			System.exit(1);
		} catch (NotBoundException e) {
			System.out.println("Couldn't connect to remote server, shutting down..");
			System.exit(1);
		}
		return new String ("Register failed");
	    
	}

	public static String login(String user, String pass, int port) {
		try {
			out.write("login " + user + " " + pass + " " + port + " " + commandCode);
			out.newLine();
			out.flush();
			String response = in_server.readLine().toString();
			if (!response.equals(null)) System.out.println(response);
			return response;
		} catch (IOException e) {
			return new String ("Operation failed");			
		}
	}

	public static String addfriend(String user, String friend){
		try {
			out.write("aggiungi_amico " + user + " " + friend + " " + commandCode);
			out.newLine();
			out.flush();
			String response = in_server.readLine().toString();
			return response;
		} catch (IOException e) {
			return new String ("Operation failed");			
		}
		
	}

	public static String show_match_notification(String msg){
		boolean i = showConfirmDialogWithTimeout("You've been challenged by " + msg, "warning", 10 * 1000);	
		try {
			if (i == true) {
				// Accetto la sfida
				out.write("accetta" + " " + commandCode);
				out.newLine();
				out.flush();
			} else {
				// Rifiuto la sfida
				out.write("rifiuta" + " " + commandCode);
				out.newLine();
				out.flush();
			}
			String response = in_server.readLine();
		    return  response;
		} catch (IOException e) {
			return new String ("Operation failed");			
		}
	}

	public static String challenge(String user, String friend) {
		try {
			out.write("sfida " + user + " " + friend + " " + commandCode);
			out.newLine();
			out.flush();
		} catch (IOException e1) {
			System.out.println("Failed to comunicate with server"); 
			System.exit (1);
		}
		String response = null;
		try {
			response = in_server.readLine().toString();
		} catch (IOException e) {
			return new String ("Operation failed");			
		}
		return response;
	}

	public static String writeword(String word){
		try {
			out.write(word);
			out.newLine();
			out.flush();
			String response = in_server.readLine().toString();
			return response;
		} catch (IOException e) {
			return new String ("Operation failed");			
		}
	}

	public static String showscore() {
		try {
			out.write("mostrapunteggio" + " " + commandCode);
			out.newLine();
			out.flush();
			String response = in_server.readLine().toString();
			return response;
		} catch (IOException e) {
			return new String ("Operation failed");			
		}
	}

	public static String logout() {
		try {
			out.write("logout" + " " + commandCode);
			out.newLine();
			out.flush();
			String response = in_server.readLine().toString();
			return response;
		} catch (IOException e) {
			return new String ("Operation failed");			
		}
	}

	public static String friendlist() {
		try {
			out.write("lista_amici" + " " + commandCode);
			out.newLine();
			out.flush();
			String response = in_server.readLine().toString();
			return response;
		} catch (IOException e) {
			return new String ("Operation failed");			
		}
	}

	public static String friendscore() {
		try {
			out.write("mostraclassifica" + " " + commandCode);
			out.newLine();
			out.flush();
			String response = in_server.readLine().toString();
			return response;
		} catch (IOException e) {
			return new String ("Operation failed");			
		}
	}
    
	public final static boolean showConfirmDialogWithTimeout(Object params, String title, int timeout_ms) {
	    final JOptionPane msg = new JOptionPane(params, JOptionPane.WARNING_MESSAGE, JOptionPane.CANCEL_OPTION);
	    final JDialog dlg = msg.createDialog(title);

	    msg.setInitialSelectionValue(JOptionPane.CANCEL_OPTION);
	    dlg.setAlwaysOnTop(true);
	    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    dlg.addComponentListener((ComponentListener) new ComponentAdapter() {
	        @Override
	        public void componentShown(ComponentEvent e) {
	            super.componentShown(e);
	            final Timer t = new Timer(timeout_ms, new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    dlg.setVisible(false);
	                }

	            });
	            t.start();
	        }
	    });
	    dlg.setVisible(true);

	    Object selectedvalue = msg.getValue();
	    if (selectedvalue.equals(JOptionPane.OK_OPTION)) {
	        return true;
	    } else {
	        return false;
	    }
	}


}