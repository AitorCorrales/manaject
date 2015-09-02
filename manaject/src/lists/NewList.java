package lists;

import java.util.Vector;
import essentials.People;

public class NewList {
	
	private static Vector<People> newPeople;
	private static Vector<People> newCompetences;
	   private static NewList instance = null;
	   private NewList(){}
	   public static NewList getInstance() {
	      if(instance == null) {
	         instance = new NewList();
	         newPeople = new Vector<People>();
	         newCompetences = new Vector<People>();
	      }
	      return instance;
	   }
	public static Vector<People> getNewPeople() {
		return newPeople;
	}
	public static void setNewPeople(Vector<People> newPeople) {
		NewList.newPeople = newPeople;
	}
	public static Vector<People> getNewCompetences() {
		return newCompetences;
	}
	public static void setNewCompetences(Vector<People> newCompetences) {
		NewList.newCompetences = newCompetences;
	}
	   
}

//this new list is used for storage the people and the competences requested by the project