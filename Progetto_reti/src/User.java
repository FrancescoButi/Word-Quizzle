import java.util.ArrayList;

public class User implements Comparable <Object> {
	private String name;
	private String password;
	private int score = 0;
	private boolean connected;
	private int port;
	private ArrayList <String> friends = new ArrayList <String>();
	
	public User (String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public ArrayList <String> getFriends() {
		return this.friends;
	}
	
	public synchronized boolean hasFriend(String user) {
		return this.friends.contains(user);
	}
	
	public synchronized String getName() {
		return this.name;
	}
	
	public synchronized int getPort() {
		return this.port;
	}
	
	public synchronized String getPassword() {
		return this.password;
	}
	
	public synchronized boolean IsConnected () {
		return this.connected;
	}
	
	public synchronized void Connect () {
		if (this.IsConnected()) System.out.println ("already connected");
		this.connected = true;
	}
	
	public synchronized void setPort (int port) {
		this.port = port;
	}
	
	public synchronized void disconnect () {
		this.connected = false;
	}
	
	public synchronized int Add_Friend (String name) {
		if (!this.friends.contains(name)){
			this.friends.add(name);
		}
		return 0;
	}



	@Override
	public int compareTo(Object compareuser) {
		int comparescore = (((User) compareuser).getScore());
		return comparescore-this.getScore();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = this.score + score;
	}
}
