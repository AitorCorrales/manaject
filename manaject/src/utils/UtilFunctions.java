package utils;

import java.util.Iterator;
import java.util.Vector;

import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.SelectQuery;
import com.complexible.stardog.api.UpdateQuery;
import com.complexible.stardog.jena.SDJenaFactory;
import com.hp.hpl.jena.rdf.model.Model;

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
	

	/*
	 * public double calculateSinglePerson(HashMap<String, Competence> hash,
	 * String person) { Set set = hash.entrySet(); Iterator i = set.iterator();
	 * double punct = 0.0; while (i.hasNext()) { Map.Entry m = (Map.Entry)
	 * i.next(); if (m.getKey() == person) punct += (double) m.getValue(); }
	 * return punct; }
	 */

	// La puntuacion de una persona se va a calcular mediante el angulo del
	// coseno de los dos vectores
	public double calculateSinglePersonCoseno(Vector<Competence> comp,
			Vector<String> personC) {
		Iterator<Competence> it = comp.iterator();
		double dividendo = 0.0;
		double divisorPersona = 0.0;
		double divisorComp = 0.0;

		while (it.hasNext()) {
			Competence next = it.next();
			if (personC.contains(getSecondPart(next.getComp()))) {
				dividendo += next.getPunctuation();
				divisorPersona += 1;
			}
			divisorComp += Math.pow(next.getPunctuation(), 2);
		}
		return (dividendo / (Math.sqrt(divisorPersona) * Math.sqrt(divisorComp)));
	}
	
	public double calculateSinglePersonSuma(Vector<Competence> comp,
			Vector<String> personC) {
		Iterator<Competence> it = comp.iterator();
		double fin = 0.0;

		while (it.hasNext()) {
			Competence next = it.next();
			if (personC.contains(getSecondPart(next.getComp()))) {
				fin += next.getPunctuation();
			}
		}
		return fin;
	}
	
	//La puntuación de la persona se va a calcular mediante el índice Jaccard
	public double calculateSinglePersonJaccard(Vector<Competence> comp,
			Vector<String> personC) {
		Iterator<Competence> it = comp.iterator();
		Iterator<String> it2 = personC.iterator();
		double M11 = 0.0;
		double M01 = 0.0;
		double M10 = 0.0;

		while (it.hasNext()) {
			Competence next = it.next();
			if (personC.contains(getSecondPart(next.getComp()))) {
				M11 += next.getPunctuation();
			} else 
				M10 += 1;
		}
		//mejorable mediante intersección de vectores
		while (it2.hasNext()){
			String next = it2.next();
			if(comp.contains(returnCompetenceByString(comp, next)))
				M01 += 1;
		}
		return M11/(M01 + M10 - M11);
	}
//	public double calculateSinglePersonJaccard(Vector<Competence> comp,
//			Vector<String> personC) {
//		Iterator<Competence> it = comp.iterator();
//		Iterator<String> it2 = personC.iterator();
//		double M11 = 0.0;
//		double M01 = 0.0;
//		double M10 = 0.0;
//
//		while (it.hasNext()) {
//			Competence next = it.next();
//			if (personC.contains(getSecondPart(next.getComp()))) {
//				M11 += Math.pow(next.getPunctuation(), 2);
//			}
//			M10 += Math.pow(next.getPunctuation(), 2);
//		}
//		while (it2.hasNext()){
//			String next = it2.next();
//			M01 += 1;
//		}
//		M11 = Math.sqrt(M11);
//		M10 = Math.sqrt(M10);
//		M01 = Math.sqrt(M01);
//		return M11/(M01 + M10 - M11);
//	}
	
	private Competence returnCompetenceByString(Vector<Competence> comp, String compName){
		Iterator<Competence> it = comp.iterator();
		while(it.hasNext()){
			Competence next = it.next();
			if(compName.equals(getSecondPart(next.getComp())))
				return next;
		}
		return null;
	}

	/*public String prepareStatementSelect(Vector<Competence> vec, String etiqueta) {
		if (vec.isEmpty())
			return "";
		Iterator<Competence> it = vec.iterator();
		String statement = "SELECT ?x ?y WHERE {\n"
				+ "?j jobSeeker:email ?x .\n" + "?j " + etiqueta + ":Name " + "?y\n"
				+ "FILTER (?y=" + "\"" + it.next().getComp() + "\""
				+ "^^xsd:string ";
		while (it.hasNext()) {
			statement = statement + "|| ?y=" + "\"" + it.next().getComp()
					+ "\"" + "^^xsd:string ";
		}
		statement = statement + ")\n" + "}";
		return statement;
		}
		*/
	public String prepareStatementSelect(Vector<Competence> vec, Vector<String> etiquetas) {
		if (vec.isEmpty())
			return "";
		Iterator<Competence> it = vec.iterator();
		Iterator<String> etiqueta = etiquetas.iterator();
		Competence nextG = null;
		String nextEt = etiqueta.next();
		String statement = "SELECT distinct * WHERE\n" + "{\n" + "{\n";
		statement += "SELECT distinct ?x ?y WHERE {\n"
				+ "?j jobSeeker:email ?x .\n" + "?j " + nextEt + ":Name " + "?y\n"
				+ "FILTER (?y=" + "\"" + getSecondPart(it.next().getComp()) + "\""
				+ "^^xsd:string ";
		while (it.hasNext()) {
			Competence next = it.next();
			nextG = next;
			if(getFirstPart(next.getComp()).equals(nextEt)){
				statement = statement + "|| ?y=" + "\"" + getSecondPart(next.getComp())
				+ "\"" + "^^xsd:string ";
			} else {
				break;
			}
		}
		statement = statement + ")\n" + "}\n" + "}\n";
		while (etiqueta.hasNext()){
			nextEt = etiqueta.next();
			statement += "UNION\n" + "{\n";
			statement += "SELECT distinct ?x ?y WHERE {\n"
					+ "?j jobSeeker:email ?x .\n" + "?j " + nextEt + ":Name " + "?y\n"
					+ "FILTER (?y=" + "\"" + getSecondPart(nextG.getComp()) + "\""
					+ "^^xsd:string ";
			while (it.hasNext()) {
				Competence next = it.next();
				nextG = next;
				if(getFirstPart(next.getComp()).equals(nextEt)){
				statement = statement + "|| ?y=" + "\"" + getSecondPart(next.getComp())
						+ "\"" + "^^xsd:string ";
				} else {
					break;
				}
			}
			statement = statement + ")\n" + "}\n" + "}\n";
		}
		statement += "}\n";
//		System.out.println(statement);
		return statement;
	}
	public String prepareStatementSelectParaPruebas(Vector<Competence> vec, Vector<String> etiquetas) {
		if (vec.isEmpty())
			return "";
		Iterator<Competence> it = vec.iterator();
		Iterator<String> etiqueta = etiquetas.iterator();
		Competence nextG = null;
		String nextEt = etiqueta.next();
		String statement = "SELECT * WHERE\n" + "{\n" + "{\n";
		statement += "SELECT ?x ?y WHERE {\n"
				+ "?j jobSeeker:email ?x .\n" + "?j " + nextEt + ":Name " + "?y\n"
				+ "FILTER (?y=" + "\"" + it.next().getComp() + "\""
				+ "^^xsd:string ";
		while (it.hasNext()) {
			Competence next = it.next();
			nextG = next;
			if(getFirstPart(next.getComp()).equals(nextEt)){
				statement = statement + "|| ?y=" + "\"" + next.getComp()
				+ "\"" + "^^xsd:string ";
			} else {
				break;
			}
		}
		statement = statement + ")\n" + "}\n" + "}\n";
		
		while (etiqueta.hasNext()){
			nextEt = etiqueta.next();
			statement += "UNION\n" + "{\n";
			statement += "SELECT ?x ?y WHERE {\n"
					+ "?j jobSeeker:email ?x .\n" + "?j " + nextEt + ":Name " + "?y\n"
					+ "FILTER (?y=" + "\"" + nextG.getComp() + "\""
					+ "^^xsd:string ";
			while (it.hasNext()) {
				Competence next = it.next();
				if(getFirstPart(next.getComp()).equals(nextEt)){
				statement = statement + "|| ?y=" + "\"" + next.getComp()
						+ "\"" + "^^xsd:string ";
				} else {
					break;
				}
			}
			statement = statement + ")\n" + "}\n" + "}\n";
		}
		statement += "}\n";		
		return statement;
	}
	
	public String prepareStatementSelect2(Vector<Competence> vec, String session) {
		if (vec.isEmpty())
			return "";
		String statement = "SELECT * WHERE\n" + "{\n" + "{\n";
		statement += "SELECT ?y WHERE {\n" + "?j "
				+ "jobOffer:id_session " + "\"" + session + "\"" + ". " + "\n"
				+ "?j " + "jobOffer:has_education " + "?y" + ". " + "\n"
				+ "}\n" + "UNION\n" + "{\n";
		
		statement += "SELECT ?y WHERE {\n" + "?j "
				+ "jobOffer:id_session " + "\""  + session + "\"" + ". " + "\n"
				+ "?j " + "language:Name " + "?y" + ". " + "\n"
				+ "}" + "UNION\n" + "{\n";
		
		statement += "SELECT ?y WHERE {\n" + "?j "
				+ "jobOffer:id_session " + "\"" + session + "\"" + ". " + "\n"
				+ "?j " + "jobOffer:requires_professional_affiliation " + "?y" + ". " + "\n"
				+ "}" + "UNION\n" + "{\n";
		
		statement += "SELECT ?y WHERE {\n" + "?j "
				+ "jobOffer:id_session " + "\"" + session + "\"" + ". " + "\n"
				+ "?j " + "skill:Name " + "?y" + ". " + "\n"
				+ "}";
		
		statement += "}\n";		
		return statement;
	}
	
	/*
	 * SELECT Distinct * WHERE
{ 
{
    SELECT ?x ?y WHERE 
    {
         ?j jobSeeker:email ?x.
         ?j skill:Name ?y
         FILTER(?y="Social_Skill" || ?y="ICT_Skill")
    }
}
UNION
{
    SELECT ?x ?y WHERE 
    {
         ?j jobSeeker:email ?x.
         ?j occupation:Name ?y
         FILTER(?y="ELEMENTARY_OCCUPATIONS__9" || ?y="ARMED_FORCES__0")
    }
}
}
	 */

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
		//print.printPeopleVector(person);

		return person;
	}
	
	public String getFirstPart(String comp){
		int part1 = 0;
		int part2 = 0;
		char [] compArray = comp.toCharArray();
		for (int i = 0; i < compArray.length; i++) {
			if(compArray[i]=='&')
				part2 = i;
		}
		return comp.substring(part1, part2);
	}
	
	public String getSecondPart(String comp){
		int part1 = 0;
		int part2 = comp.length();
		char [] compArray = comp.toCharArray();
		for (int i = 0; i < compArray.length; i++) {
			if(compArray[i]=='&')
				part1 = i;
		}
		return comp.substring(part1 + 1, part2);
	}

	public String clearString(String email){
		char[] chars = email.toCharArray();
		int [] positions = new int [2];
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '\"') {
				positions[pos] = i;
				pos++;
			}
		}
		return email.substring(positions[0] + 1, positions[1]);
	}
	
//	public Vector<Recommended> getEstablishedCompetencePunctuation(
//			Vector<Person> person, Vector<Competence> comp) {
//		Iterator<Person> it = person.iterator();
//		Vector<Recommended> rec = new Vector<Recommended>();
//		while (it.hasNext()) {
//			Person next = it.next();
//			rec.add(new Recommended(next.getFullname(), calculateSinglePerson(
//					comp, next.getCompetences())));
//		}
//		print.printRecommendedVector(inefficientOrdering(rec));
//		return rec;
//	}
//	
//	public Vector<Recommended> getEstablishedCompetencePunctuationForHTML(
//			Vector<Person> person, Vector<Competence> comp) {
//		Iterator<Person> it = person.iterator();
//		Vector<Recommended> rec = new Vector<Recommended>();
//		while (it.hasNext()) {
//			Person next = it.next();
//			rec.add(new Recommended(next.getFullname(), calculateSinglePerson(
//					comp, next.getCompetences())));
//		}
//		return rec;
//	}
	public Vector<Recommended> lastAdd(Vector <Recommended> vec){
		Vector <Recommended> last = new Vector<Recommended>();
		Iterator <Recommended> it = vec.iterator();
		Iterator <Recommended> it2;
		String piv = "";
		double pivPunct = 0.0;
		int position = 0;
		int position2 = 0;
		int removable = 0;
		Vector<Integer> listInt = new Vector<Integer>();
		while(it.hasNext()){
			Recommended next = it.next();	
			piv = next.getKey();
			pivPunct = next.getPunct();
			it2 = vec.listIterator(position + 1);
			while(it2.hasNext()){
				Recommended next2 = it2.next();
				position2++;
				if(next2.getKey().equals(piv)){
					pivPunct += next2.getPunct();
					listInt.add(position2);
				  //  it2.remove();
				}
			}
			position++;
			position2 = position;
			last.add(new Recommended(piv, pivPunct));
		}
		for (Integer integer : listInt) {
			last.remove(integer - removable);
			removable++;
		}
		return last;
	}
	public Vector<Recommended> getEstablishedCompetencePunctuationCoseno(
			Vector<Person> person, Vector<Competence> comp) {
		Iterator<Person> it = person.iterator();
		Vector<Recommended> rec = new Vector<Recommended>();
		while (it.hasNext()) {
			Person next = it.next();
			rec.add(new Recommended(next.getFullname(), calculateSinglePersonCoseno(
					comp, next.getCompetences())));
		}
		print.printRecommendedVector(inefficientOrdering(lastAdd(rec)));
		return rec;
	}
	
	public Vector<Recommended> getEstablishedCompetencePunctuationSuma(
			Vector<Person> person, Vector<Competence> comp) {
		Iterator<Person> it = person.iterator();
		Vector<Recommended> rec = new Vector<Recommended>();
		while (it.hasNext()) {
			Person next = it.next();
			rec.add(new Recommended(next.getFullname(), calculateSinglePersonSuma(
					comp, next.getCompetences())));
		}
		print.printRecommendedVector(inefficientOrdering(lastAdd(rec)));
		return rec;
	}

	public Vector<Recommended> getEstablishedCompetencePunctuationJaccard(
			Vector<Person> person, Vector<Competence> comp) {
		Iterator<Person> it = person.iterator();
		Vector<Recommended> rec = new Vector<Recommended>();
		while (it.hasNext()) {
			Person next = it.next();
			rec.add(new Recommended(next.getFullname(), calculateSinglePersonJaccard(
					comp, next.getCompetences())));
		}
		print.printRecommendedVector(inefficientOrdering(lastAdd(rec)));
		return rec;
	}
	
	public Vector<Recommended> getEstablishedCompetencePunctuationForHTMLJaccard(
			Vector<Person> person, Vector<Competence> comp) {
		Iterator<Person> it = person.iterator();
		Vector<Recommended> rec = new Vector<Recommended>();
		while (it.hasNext()) {
			Person next = it.next();
			rec.add(new Recommended(next.getFullname(), calculateSinglePersonJaccard(
					comp, next.getCompetences())));
		}
		return rec;
	}
	
	public Vector<Recommended> getEstablishedCompetencePunctuationForHTMLCoseno(
			Vector<Person> person, Vector<Competence> comp) {
		Iterator<Person> it = person.iterator();
		Vector<Recommended> rec = new Vector<Recommended>();
		while (it.hasNext()) {
			Person next = it.next();
			rec.add(new Recommended(next.getFullname(), calculateSinglePersonCoseno(
					comp, next.getCompetences())));
		}
		return rec;
	}
	
	public Vector<Recommended> getEstablishedCompetencePunctuationForHTMLSuma(
			Vector<Person> person, Vector<Competence> comp) {
		Iterator<Person> it = person.iterator();
		Vector<Recommended> rec = new Vector<Recommended>();
		while (it.hasNext()) {
			Person next = it.next();
			rec.add(new Recommended(next.getFullname(), calculateSinglePersonSuma(
					comp, next.getCompetences())));
		}
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

	public void update(Connection aConn, String user, String paramToChange,
			String paramValue) throws StardogException {

		Model aModel = SDJenaFactory.createModel(aConn);
		UpdateQuery aQuery;

		// DELETE
		aModel.begin();

		String aQueryDelete = "delete where \n" + "{\n" + "<http://people"
				+ "/" + user + "> " + paramToChange + " ?y" + "\n" + "}";

		aQuery = aConn.update(aQueryDelete);

		// ... and run it
		if (aQuery.execute())
			System.out.println("triple borrado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");

		aConn.commit();
		aModel.close();

		// INSERT

		aModel.begin();

		String aQueryInsert = "insert data\n" + "{\n" + "<http://people" + "/"
				+ user + "> " + paramToChange + "\" " + paramValue + "\""
				+ "\n" + "}";

		aQuery = aConn.update(aQueryInsert);

		// ... and run it
		if (aQuery.execute())
			System.out.println("triple borrado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");

		aConn.commit();
		aModel.close();
	}

//	public Vector<Recommended> recommenderFunctionsByLike(
//			Vector<Person> person, Vector<Competence> comp) {
//		/*
//		 * :has_job_category: OccOnt:Occupation ,
//		 * OccOnt:Computing_professionals__213
//		 * 
//		 * El sistema de recomendación por gustos para el usuario se va a llevar
//		 * a cabo teniendo en cuenta las categorías de los proyectos en los que
//		 * ya ha trabajado y el proyecto ofertado en este instante.
//		 */
//		Iterator<Person> it = person.iterator();
//		Vector<Recommended> rec = new Vector<Recommended>();
//		while (it.hasNext()) {
//			Person next = it.next();
//			rec.add(new Recommended(next.getFullname(), calculateSinglePerson(
//					comp, next.getCompetences())));
//		}
//		print.printRecommendedVector(inefficientOrdering(rec));
//		return rec;
//	}

	public Vector<String> showOntology(Connection aConn, String className)
			throws StardogException, QueryEvaluationException {

		Vector<String> ret = new Vector<String>();

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String statement = "SELECT ?x WHERE {\n" + "?x rdfs:subClassOf "
				+ "skill:" + className + " \n" + "}";
		SelectQuery aQuery = aConn.select(statement);
		TupleQueryResult aExec = aQuery.execute();

		while (aExec.hasNext())
			ret.add(aExec.next().toString());

		aExec.close();
		aModel.close();
		return ret;

	}

}
