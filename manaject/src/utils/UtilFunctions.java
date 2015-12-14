package utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import java.lang.Math;

import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import essentials.*;

public class UtilFunctions {

	// the punctuation reached by someone calculated with the matrix
	/*
	 * @SuppressWarnings("rawtypes") public Recommended
	 * puntuationCalc(HashMap<String, Competence> hash) { Set set =
	 * hash.keySet(); Iterator i = set.iterator(); Recommended recommended = new
	 * Recommended("", 0.0); double calc; while (i.hasNext()) { Map.Entry m =
	 * (Map.Entry) i.next(); calc = calculateSinglePerson(hash, (String)
	 * m.getKey()); if (recommended.getPunct() >= calc) recommended = new
	 * Recommended((String) m.getKey(), calc); } return recommended;
	 * 
	 * }
	 */

	// compares the competences to get if two competences refer to the same
	// concept

	private static PrintFunctions print = new PrintFunctions();

	public Recommended[] getRecommendedList(Vector<Person> people) {

		return null;
	}

	public void intoVectorResults(TupleQueryResult res, Vector<String> vec)
			throws QueryEvaluationException {
		try {
			while (res.hasNext()) {
				vec.add(res.next().toString());
			}
		} finally {
			res.close();
		}
	}

	public void resultIntoHashMap(Vector<String> vec,
			HashMap<String, String> hash) {

	}

	/*
	 * public double calculateSinglePerson(HashMap<String, Competence> hash,
	 * String person) { Set set = hash.entrySet(); Iterator i = set.iterator();
	 * double punct = 0.0; while (i.hasNext()) { Map.Entry m = (Map.Entry)
	 * i.next(); if (m.getKey() == person) punct += (double) m.getValue(); }
	 * return punct; }
	 */
	
	//La puntuacion de una persona se va a calcular mediante el angulo del coseno de los dos vectores
	public double calculateSinglePerson(Vector<Competence> comp,
			Vector<String> personC) {
		Iterator<Competence> it = comp.iterator();
		double dividendo = 0.0;
		double divisorPersona = 0.0;
		double divisorComp = 0.0;
		
		while (it.hasNext()) {
			Competence next = it.next();
			if (personC.contains(next.getComp())) {
				dividendo += next.getPunctuation();
				divisorPersona += 1;
			}
			divisorComp += Math.pow(next.getPunctuation(),2);
		}
		return (dividendo/(Math.sqrt(divisorPersona)*Math.sqrt(divisorComp)));
	}

	public String prepareStatementSelect(Vector<Competence> vec) {
		if (vec.isEmpty())
			return "";
		Iterator<Competence> it = vec.iterator();
		String statement = "SELECT ?x ?y WHERE {\n"
				+ "?j jobSeeker:email ?x .\n"
				+ "?j skill:Name " + "?y\n" + "FILTER (?y=" + "\"" + it.next().getComp() + "\""
				+ "^^xsd:string ";
		while (it.hasNext()) {
			statement = statement + "|| ?y=" + "\"" + it.next().getComp()
					+ "\"" + "^^xsd:string ";
		}
		statement = statement + ")\n" + "}";
		return statement;
	}

	public Vector<Person> separateIntoLists(Vector<String> vecString) {

		Iterator<String> it = vecString.iterator();
		Vector<Person> person = new Vector<Person>();
		String name = "";
		Vector<String> competenceV = new Vector<String>();
		int[] positions = new int[4];
		int pos;

		while (it.hasNext()) {
			pos = 0;
			String next = it.next();
			char[] chars = next.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == '\"') {
					positions[pos] = i;
					pos++;
				}
			}
			if (!name.equals(next.substring(positions[0] + 1, positions[1]))) {
				// no es la primera vuelta
				if (!competenceV.isEmpty()) {
					person.add(new Person(name, competenceV));
					competenceV = new Vector<String>();
				}
				name = next.substring(positions[0] + 1, positions[1]);
			}
			competenceV.add(next.substring(positions[2] + 1, positions[3]));
		}
		person.add(new Person(name, competenceV));
		print.printPeopleVector(person);

		return person;
	}

	public Vector<Recommended> getEstablishedCompetencePunctuation(
			Vector<Person> person, Vector<Competence> comp) {
		Iterator<Person> it = person.iterator();
		Vector<Recommended> rec = new Vector<Recommended>();
		while (it.hasNext()) {
			Person next = it.next();
			rec.add(new Recommended(next.getFullname(), calculateSinglePerson(
					comp, next.getCompetences())));
		}
		print.printRecommendedVector(inefficientOrdering(rec));
		return rec;
	}

	public Vector<Recommended> inefficientOrdering(Vector<Recommended> vec) {
		Recommended rec;
		for (int i = 0; i < vec.size(); i++) {
			for (int j = i + 1; j < vec.size(); j++) {
				if (vec.get(i).getPunct() < vec.get(j).getPunct()) {
					rec = vec.get(i);
					vec.set(i, vec.get(j));
					vec.set(j, rec);
				}
			}
		}
		return vec;
	}
	public double realPunctuationCalculation(Vector<Competence> comp,
			Vector<String> personC) {
		/*Iterator<Competence> it = comp.iterator();
		double punct = 0.0;
		while (it.hasNext()) {
			Competence next = it.next();
			if (personC.contains(next.getComp())) {
				punct += next.getPunctuation();
			}
		}
		return punct; */
		
		//Para calcular la similaridad entre dos vectores de objetos vamos a utilizar A Survey of Approaches to Designing Recommender Systems
		//Sistema de recomendación por gustos
		
		
		//Sistema de recomendación por necesidades
		
		
		return 0.0;
	}

}
