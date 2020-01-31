import java.util.ArrayList;
import java.util.Collections;

public class Users {
	
	//ArrayList di user per la memorizzazione dati
	private ArrayList <User> users = new ArrayList <User>();
	
	//Metodo per aggiungere un utente alla collezione
	public void add (User user) {
		if (!this.contains(user.getName()))
			this.users.add((user));
	}	
	
	public User get (int i) {
		return this.users.get(i);
	}
	
	public boolean contains (String user) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(user)) return true;
		}
		return false;
	}
	
	public int addFriends (String user, String user2) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(user)) {
				if (users.get(i).getFriends().contains(user2)) {
					return 1;
				}else {
					users.get(i).Add_Friend(user2);
				}
			}
			if (users.get(i).getName().equals(user2)) {
				if (users.get(i).getFriends().contains(user)) {
					return 1;
				}else {
					users.get(i).Add_Friend(user);
				}
			}
		}
		return 0;
	}
	
	public User getByName (String user) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(user)) return users.get(i);
		}
		return null;
	}
	
	public void userSort () {
		Collections.sort(users);
	}
	
	public int size () {
		return users.size();
	}
	
}
