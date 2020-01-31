import java.util.ArrayList;

public class Sfida {
	
	private String user1;
	private String user2;
	private ArrayList <Word_Traduction> word_traductions;
	private int pending = 1;
	private int refused = 0;
	private int user1points;
	private int user2points;
	private boolean user1done = false;
	private boolean user2done = false;
	private boolean matchended = false;
	
	public Sfida(String user, String user_2) {
		this.user1 = user;
		this.user2 = user_2;
	}

	public String getUser2() {
		return user2;
	}
	
	public Boolean hasPendingInvite(String user) {
		if (user1.contentEquals(user)) return true;
		if (user2.contentEquals(user)) return true;
		return false;
	}

	public void setUser2(String user2) {
		this.user2 = user2;
	}

	public String getUser1() {
		return user1;
	}

	public void setUser1(String user1) {
		this.user1 = user1;
	}

	public ArrayList <Word_Traduction> getWord_traductions() {
		return word_traductions;
	}

	public void setWord_traductions(ArrayList <Word_Traduction> word_traductions) {
		this.word_traductions = word_traductions;
	}

	public int getPending() {
		return pending;
	}

	public void setPending(int pending) {
		this.pending = pending;
	}

	public int getUser1points() {
		return user1points;
	}

	public void setUser1points(int user1points) {
		this.user1points = user1points;
	}

	public int getUser2points() {
		return user2points;
	}

	public void setUser2points(int user2points) {
		this.user2points = user2points;
	}

	public boolean isUser1done() {
		return user1done;
	}

	public void setUser1done(boolean user1done) {
		this.user1done = user1done;
	}

	public boolean isUser2done() {
		return user2done;
	}

	public void setUser2done(boolean user2done) {
		this.user2done = user2done;
	}

	public boolean isMatchended() {
		return matchended;
	}

	public void setMatchended(boolean matchended) {
		this.matchended = matchended;
	}

	public void clean() {
		this.user1 = "";
		this.user2 = "";
	}

	public int getRefused() {
		return refused;
	}

	public void setRefused(int refused) {
		this.refused = refused;
	}

}
