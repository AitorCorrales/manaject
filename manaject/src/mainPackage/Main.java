package mainPackage;

import java.util.HashMap;
import java.util.Vector;

import essentials.Competence;
import essentials.DatabaseManagement;
import essentials.Person;
import essentials.Recommended;
import utils.UtilFunctions;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		DatabaseManagement dbm = new DatabaseManagement();
		UtilFunctions func = new UtilFunctions();
		Vector<Competence> comp = new Vector<Competence>();
		Vector<Person> p = new Vector<Person>();
		Vector<Recommended> rec = new Vector<Recommended>();
		HashMap<String, String> hash = new HashMap<String, String>();

		comp.add(new Competence("Hedor", 0.9));
		comp.add(new Competence("Telecomunicaciones", 0.8));
		comp.add(new Competence("Diseñador", 0.6));
		comp.add(new Competence("Animador", 1.0));
		comp.add(new Competence("Experto Web", 0.8));
		comp.add(new Competence("Pintor", 0.8));
		comp.add(new Competence("Paleto", 0.93));
		try {

			// dbm.insertPersonCompetences("Kevin Vegas", comp);
			// dbm.findPeopleByEachCompetence(dbm.connectToStardog("myDb"),
			// comp);
			// System.out.println(func.prepareStatementSelect(comp));
			// hash = dbm.findPeopleByCompetences(dbm.connectToStardog("myDb"),
			// comp);
			// p = func.separateIntoLists(dbm.findPeopleByCompetences(
			// dbm.connectToStardog("myDb"), comp));
			rec = func.getEstablishedCompetencePunctuation(
					func.separateIntoLists(dbm.findPeopleByCompetences(
							dbm.connectToStardog("myDb"), comp)), comp);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
