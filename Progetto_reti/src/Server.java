import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;



public class Server{
	
	private static Users userlist = new Users();
	static File userfile = new File ("./user list/userfile.txt");
	private static ArrayList <Sfida> sfide = new ArrayList <Sfida>();
	
	public static void main(String[] args) {
		
		// Creo rmi per la registrazione utente
		import_users();
	 	try {
			remoteserver rm = new remoteserver(userlist);
			RMIInterface stub = (RMIInterface) UnicastRemoteObject.exportObject(rm, 0);
	        LocateRegistry.createRegistry(999);
	        Registry r = LocateRegistry.getRegistry(999);
	        r.rebind("SERVER_TEST", stub);
		} catch (IOException e) {
			System.out.println("Couldn't create server for registration, shutting down..");
			System.exit(1);
		}
        

		//apro il server per il gioco
        setUsersOffline (userlist);
		open_server();
	}

	
	
	
	private static void setUsersOffline(Users userlist2) {
		for (int i = 0; i < userlist2.size(); i++) {
			userlist2.get(i).disconnect();
		}
	}




	private static int open_server() {
		 // creo socket
        int port = 9997;
        ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("Server open on port : " + port);

        // ciclo infinito per l'atesa di client
        while (true) {

            // mi blocco in attesa di un client
            Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Thread gestore = new Thread (new Handler(clientSocket, userlist, sfide));
			gestore.start();
        }
	}
	
	
	private static void import_users() {
	// Importo dal file la lista utenti
			try {
				if (userfile.createNewFile()){	
				    System.out.println("File created, no data existing");
				} else {
					BufferedReader br = new BufferedReader(new FileReader(userfile)); 
					Gson gson = new GsonBuilder().create();
					String control = br.readLine();
					if (control != null) {
						userlist = gson.fromJson(control, Users.class);
					}
					br.close();
				}
			} catch (JsonSyntaxException | IOException e) {
				System.out.println("Couldn't import users, shutting down..");
				System.exit(1);
			}
	}

}
