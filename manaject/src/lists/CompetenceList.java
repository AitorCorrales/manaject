package lists;

import java.util.Vector;
import essentials.Competence;

public class CompetenceList {
	
	public static Vector <Competence>comp;
	public CompetenceList(){
		comp = new Vector<Competence>(20, 5);
	}
	
	public CompetenceList(Vector <Competence> v){
		comp.addAll(v);
	}
}
