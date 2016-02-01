package mainPackage;

import java.util.HashMap;
import java.util.Vector;

import essentials.Competence;
import essentials.DatabaseManagement;
import essentials.Person;
import essentials.Recommended;
import utils.Constants;
import utils.UtilFunctions;
//import utils.Constants;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		DatabaseManagement dbm = new DatabaseManagement();
		UtilFunctions func = new UtilFunctions();
		
		Vector<Competence> comp = new Vector<Competence>();
		Vector<Competence> comp2 = new Vector<Competence>();
		Vector<Competence> comp3 = new Vector<Competence>();
		Vector<Competence> search = new Vector<Competence>();
		Vector<Competence> search2 = new Vector<Competence>();

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
		
		search2.add(new Competence("skill&Social_Skill", 5.0/100.0));
		search2.add(new Competence("skill&Technical_Skill", 20.0/100.0));
		search2.add(new Competence("skill&ICT_Skill", 85.0/100.0));
		search2.add(new Competence("skill&Organisational_Skill", 57.0/100.0));
		search2.add(new Competence("language&eng_English", 80.0/100.0));
		search2.add(new Competence("language&spa__Spanish_Castilian", 80.0/100.0));
		search2.add(new Competence("occupation&PROFESSIONALS__2", 20.0/100.0));
		search2.add(new Competence("occupation&TECHNICIANS_AND_ASSOCIATE_PROFESSIONALS__3", 85.0/100.0));
		search2.add(new Competence("education&Social_sciences_Business_and_Law__3", 57.0/100.0));
		search2.add(new Competence("education&Agriculture_and_Veterinary__6", 80.0/100.0));

		
		try {

//			dbm.insertPersonEmail(dbm.connectToStardog(Constants.database), "motroco@gmail.com");
//			dbm.insertPersonFullName(dbm.connectToStardog(Constants.database), "Babatunde Moraza");
//			dbm.insertPersonFullName(dbm.connectToStardog(Constants.database), "William Wilson");
//			dbm.insertPersonFullName(dbm.connectToStardog(Constants.database), "Santiago Santiago");
//			dbm.insertPersonFullName(dbm.connectToStardog(Constants.database), "motroco@gmail.com", "Motroco Moraza");
//			dbm.insertPersonFullName(dbm.connectToStardog(Constants.database), "Babatunde Moraza");
//			dbm.insertPersonFullName(dbm.connectToStardog(Constants.database), "William Wilson");
//			dbm.insertPersonFullName(dbm.connectToStardog(Constants.database), "Santiago Santiago");
//			dbm.insertPersonCompetences("pruebaPaleto@gmail.com", comp);
//			dbm.insertPersonCompetences("babatunde@gmail.com", comp2);
//			dbm.insertPersonCompetences("babatunde@gmail.com", comp3);

//			dbm.insertPersonCompetences("motroco@gmail.com", comp2);
//			dbm.insertPersonPassword(dbm.connectToStardog(Constants.database), "aimarpaleto@gmail.com", "aimarpaleto");
//			dbm.insertPersonCompetences("babatunde@gmail.com", comp2);
//			dbm.insertPersonCompetences("Santiago Santiago", comp);
//			dbm.deletePersonFullNameById(dbm.connectToStardog(Constants.database), "B6028E83407F3B5D628CDA83717BA5B1");
//			dbm.insertPersonFullNameById(dbm.connectToStardog(Constants.database), "B6028E83407F3B5D628CDA83717BA5B1", "Motroco M. Moraza");
			
			//dbm.insertPersonPassword(dbm.connectToStardog(Constants.database), "babatunde@gmail.com", "babatunde");
			
			//System.out.println(dbm.findPersonFullName(dbm.connectToStardog(Constants.database), "babatunde@gmail.com"));
			
			// dbm.insertPersonCompetences("Kevin Vegas", comp);
			//boolean pf = dbm.findPersonByEmail(dbm.connectToStardog(Constants.database), "babatunde@gmail.com");
			//dbm.findPeopleByEachCompetence(dbm.connectToStardog(Constants.database), search);
			// System.out.println(func.prepareStatementSelect(comp));
			// p = func.separateIntoLists(dbm.findPeopleByCompetences(
			// dbm.connectToStardog(Constants.database), comp));
			Vector<String> etiquetas = new Vector<String>();
			etiquetas.add("skill");
			etiquetas.add("language");
			System.out.println("///////////////COSENO////////////////");
			rec = func.getEstablishedCompetencePunctuationCoseno(
					func.separateIntoLists(dbm.findPeopleByCompetences(
							dbm.connectToStardog(Constants.database), search2, etiquetas)), search2);
			System.out.println("////////////////SUMATORIO////////////////");
			rec = func.getEstablishedCompetencePunctuationSuma(
					func.separateIntoLists(dbm.findPeopleByCompetences(
							dbm.connectToStardog(Constants.database), search2, etiquetas)), search2);
			System.out.println("////////////////JACCARD////////////////");
			rec = func.getEstablishedCompetencePunctuationJaccard(
					func.separateIntoLists(dbm.findPeopleByCompetences(
							dbm.connectToStardog(Constants.database), search2, etiquetas)), search2);
			

//			Vector <String> res = dbm.findPeopleByCompetences(
//					dbm.connectToStardog(Constants.database), search2, etiquetas);
//			Iterator <String> it = res.iterator();
//			while(it.hasNext()){
//				System.out.println(it.next());
//			}
			
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
