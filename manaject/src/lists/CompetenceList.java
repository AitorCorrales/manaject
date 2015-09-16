package lists;

import java.util.HashMap;
import java.util.Vector;

import essentials.Competence;

public class CompetenceList {
	
	private static HashMap <String, Competence> comp;
	public CompetenceList(){
		comp = new HashMap <String, Competence> ();
	}
	
	public static HashMap<String, Competence> getComp() {
		return comp;
	}

	public static void setComp(HashMap<String, Competence> comp) {
		CompetenceList.comp = comp;
	}

	public CompetenceList(Vector <Competence> v){

	}
}
