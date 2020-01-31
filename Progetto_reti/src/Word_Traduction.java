import java.util.ArrayList;

public class Word_Traduction {
	private String word;
	private ArrayList <String> traductions;
	
	public Word_Traduction (String word) {
		this.word = word;
	}
	
	public void addword (String word) {
		this.word = word;
	}
	
	public String getword (){
		return this.word;
	}
	
	public ArrayList<String> gettranslations (){
		return this.traductions;
	}
	
	
	public void Add_Traductions (ArrayList <String> Traductions) {
		this.traductions = Traductions;
	}
}
