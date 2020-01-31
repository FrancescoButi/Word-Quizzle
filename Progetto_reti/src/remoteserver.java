import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;

import com.google.gson.Gson;

public class remoteserver extends RemoteServer implements RMIInterface{
	
	private static Users userlist;
	private static final long serialVersionUID = 1L;
	public remoteserver (Users userlist) throws IOException {
		this.userlist = userlist;
		
		//add some test users
		registra_utente("a", "b");
		registra_utente("b", "b");
		registra_utente("c", "b");
	}
	
	
	public int registra_utente(String name, String password) throws RemoteException {
		File userfile = new File ("./user list/userfile.txt");
		////////////////////////////////
		User user = new User (name, password);
		if (userlist.contains(name)) return 1;
		userlist.add(user);
		//////////////////////////////
		Gson gson = new Gson ();
		String juser = gson.toJson(userlist);
	    System.out.println("New user registered");
	    try {
			FileWriter fw = new FileWriter(userfile,false);
			fw.write(juser);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

}