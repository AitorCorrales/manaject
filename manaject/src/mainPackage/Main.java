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
		Vector<Competence> comp2 = new Vector<Competence>();
		Vector<Competence> comp3 = new Vector<Competence>();
		Vector<Competence> search = new Vector<Competence>();

		Vector<Person> p = new Vector<Person>();
		Vector<Recommended> rec = new Vector<Recommended>();
		HashMap<String, String> hash = new HashMap<String, String>();

		comp.add(new Competence("Hedor", 0.9));
		comp3.add(new Competence("Telecomunicaciones", 0.8));
		comp3.add(new Competence("Diseñador", 0.6));
		comp.add(new Competence("Animador", 1.0));
		comp2.add(new Competence("Experto Web", 0.8));
		comp.add(new Competence("Pintor", 0.8));
		comp3.add(new Competence("Paleto", 0.93));
		comp3.add(new Competence("Escultor", 0.68));
		comp2.add(new Competence("Costurero", 0.13));
		comp2.add(new Competence("Escudero", 0.5));
		comp.add(new Competence("Caballero", 0.69));
		comp2.add(new Competence("Chofer", 0.14));
		comp3.add(new Competence("Leñador", 0.78));
		comp.add(new Competence("Agricultor", 0.4));
		comp3.add(new Competence("Jugador de Futbol", 0.5));
		
		search.add(new Competence("Pintor", 0.8));
		search.add(new Competence("Paleto", 0.93));
		search.add(new Competence("Escultor", 0.68));
		search.add(new Competence("Costurero", 0.13));
		search.add(new Competence("Escudero", 0.5));
		search.add(new Competence("Caballero", 0.69));
		search.add(new Competence("Chofer", 0.14));
		search.add(new Competence("Leñador", 0.78));
		search.add(new Competence("Agricultor", 0.4));
		search.add(new Competence("Jugador de Futbol", 0.5));
		
		try {

			//dbm.insertPersonEmail(dbm.connectToStardog("myDb"), "motroco@gmail.com");
			//dbm.insertPersonFullName(dbm.connectToStardog("myDb"), "Babatunde Moraza");
			//dbm.insertPersonFullName(dbm.connectToStardog("myDb"), "William Wilson");
			//dbm.insertPersonFullName(dbm.connectToStardog("myDb"), "Santiago Santiago");
			//dbm.insertPersonFullName(dbm.connectToStardog("myDb"), "motroco@gmail.com", "Motroco Moraza");
			//
			//	dbm.insertPersonCompetences("motroco@gmail.com", comp);
			//dbm.insertPersonCompetences("motroco@gmail.com", comp2);
			//	dbm.insertPersonEmail(dbm.connectToStardog("myDb"), "babatunde@gmail.com");
			//dbm.insertPersonFullName(dbm.connectToStardog("myDb"), "Babatunde Moraza");
			//dbm.insertPersonFullName(dbm.connectToStardog("myDb"), "William Wilson");
			//dbm.insertPersonFullName(dbm.connectToStardog("myDb"), "Santiago Santiago");
			//	dbm.insertPersonFullName(dbm.connectToStardog("myDb"), "babatunde@gmail.com", "Babatunde Moraza");

			//		dbm.insertPersonCompetences("motroco@gmail.com", comp);
			//		dbm.insertPersonCompetences("motroco@gmail.com", comp2);
			//		dbm.insertPersonCompetences("babatunde@gmail.com", comp3);
			//		dbm.insertPersonCompetences("babatunde@gmail.com", comp2);
			//dbm.insertPersonCompetences("Santiago Santiago", comp);
			
			// dbm.insertPersonCompetences("Kevin Vegas", comp);
			//boolean pf = dbm.findPersonByEmail(dbm.connectToStardog("myDb"), "babatunde@gmail.com");
			//dbm.findPeopleByEachCompetence(dbm.connectToStardog("myDb"), search);
			// System.out.println(func.prepareStatementSelect(comp));
			// p = func.separateIntoLists(dbm.findPeopleByCompetences(
			// dbm.connectToStardog("myDb"), comp));
			rec = func.getEstablishedCompetencePunctuation(
					func.separateIntoLists(dbm.findPeopleByCompetences(
							dbm.connectToStardog("myDb"), search)), search);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
