package essentials;

import java.util.Vector;

public class Person {
	private String fullname;
	private Vector <String> competences;

	public Person(String fullname, Vector <String> competences){
		this.fullname = fullname;
		this.competences = competences;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Vector<String> getCompetences() {
		return competences;
	}

	public void setCompetences(Vector<String> competences) {
		this.competences = competences;
	}

}
