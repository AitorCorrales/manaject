package essentials;

import utils.UtilFunctions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.SelectQuery;
import com.complexible.stardog.api.UpdateQuery;
import com.complexible.stardog.jena.SDJenaFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class DatabaseManagement {

	public Connection connectToStardog(String dbname) throws Exception {

		/*
		 * @SuppressWarnings("unused") Server aServer = Stardog.buildServer()
		 * .bind(new InetSocketAddress("10.0.0.1", 5820)).start();
		 * 
		 * AdminConnection aAdminConnection = AdminConnectionConfiguration
		 * .toServer("http://localhost:5820").credentials("admin",
		 * "admin").connect();
		 */

		Connection aConn = ConnectionConfiguration.to(dbname) // the name of the
																// db to connect

				.server("http://localhost:5820")
				// to
				.credentials("admin", "admin") // the credentials with which to
												// connect
				.connect();

		return aConn;
	}

	public void findPersonByFullName(Connection aConn, String name)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		// start a transaction before adding the data. This is not required, but
		// it is faster to group the entire add into a single transaction rather
		// than rely on the auto commit of the underlying stardog connection.
		aModel.begin();

		// Query that we will run against the data we just loaded
		String aQueryString = "SELECT ?x WHERE {\n"
				+ "?x <http://www.w3.org/2001/vcard-rdf/3.0#FN> " + "\"" + name
				+ "\"" + "\n" + "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		printResults(aExec);

		// always close the execution
		aExec.close();
		aModel.close();

	}

	public void findPersonByCompetence(Connection aConn, String comp)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		// start a transaction before adding the data. This is not required, but
		// it is faster to group the entire add into a single transaction rather
		// than rely on the auto commit of the underlying stardog connection.
		aModel.begin();

		// Query that we will run against the data we just loaded
		String aQueryString = "SELECT ?x WHERE {\n"
				+ "?x <C:/Users/aitor/Downloads/ttl/SkillOntology.ttl#Name> "
				+ "\"" + comp + "\"" + ". " + "\n" + "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		printResults(aExec);

		// always close the execution
		aExec.close();
		aModel.close();

	}

	// Por cada competencia, encuentra a las personas que la poseen
	public void findPeopleByEachCompetence(Connection aConn,
			Vector<Competence> comp) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		Iterator<Competence> it = comp.iterator();

		while (it.hasNext()) {
			String aQueryString = "SELECT ?x WHERE {\n"
					+ "?x <C:/Users/aitor/Downloads/ttl/SkillOntology.ttl#Name> "
					+ "\"" + it.next().getComp() + "\"" + ". " + "\n" + "}";
			SelectQuery aQuery = aConn.select(aQueryString);
			TupleQueryResult aExec = aQuery.execute();
			printResults(aExec);
			aExec.close();
			System.out.println("------------------------------");
		}

		aModel.close();

	}

	public Vector<String> findPeopleByCompetences(Connection aConn,
			Vector<Competence> vec) throws Exception {

		UtilFunctions func = new UtilFunctions();
		Vector<String> vecO = new Vector<String>();
		HashMap<String, String> hash = new HashMap<String, String>();

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		SelectQuery aQuery = aConn.select(func.prepareStatementSelect(vec));
		TupleQueryResult aExec = aQuery.execute();
		// printResults(aExec);
		func.intoVectorResults(aExec, vecO);
		func.resultIntoHashMap(vecO, hash);
		// func.printVector(vecO);

		return vecO;
	}

	public void insertPersonFullName(Connection aConn, String name)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://somewhere"
				+ "/" + name.replaceAll("\\s", "") + "> "
				+ "<http://www.w3.org/2001/vcard-rdf/3.0#FN> " + "\"" + name
				+ "\"" + "\n" + "}";

		// Create a query...
		UpdateQuery aQuery = aConn.update(aQueryString);

		// ... and run it
		if (aQuery.execute())
			System.out.println("triple insertado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");

		aConn.commit();
		aModel.close();
	}

	public void insertPersonCompetence(Connection aConn, String name,
			String competence) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		/*
		 * INSERT DATA { GRAPH <http://somewhere/bookStore> {
		 * <http://example/book1> ns:price 42 } }
		 */

		String aQueryString = "insert data\n" + "{\n" + "<http://somewhere"
				+ "/" + name.replaceAll("\\s", "") + "> "
				+ "<C:/Users/aitor/Downloads/ttl/SkillOntology.ttl#Name> "
				+ "\"" + competence + "\"" + "\n" + "}";

		// Create a query...
		UpdateQuery aQuery = aConn.update(aQueryString);

		// ... and run it
		if (aQuery.execute())
			System.out.println("triple insertado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");

		aConn.commit();

		aModel.close();
	}

	public void insertPersonCompetences(String name,
			Vector<Competence> competences) throws Exception {

		Iterator<Competence> it = competences.iterator();

		while (it.hasNext()) {
			insertPersonCompetence(connectToStardog("myDb"), name, it.next()
					.getComp());
		}
	}

	public void printResults(TupleQueryResult res)
			throws QueryEvaluationException {
		try {
			while (res.hasNext()) {
				System.out.println(res.next().toString());
			}
		} finally {
			res.close();
		}
	}

	/*
	 * SELECT ?x ?dumbassValue ?uglyValue WHERE { { ?x :hasCompetence "Dumbass"
	 * . bind( if(exists(?x),1,0) as ?dumbassValue) } UNION { ?x :hasCompetence
	 * "Ugly" bind( if(exists(?x),2,0) as ?uglyValue) }
	 * 
	 * //La competencia es un string
	 * 
	 * SELECT ?x WHERE { ?x :hasCompetence ?y FILTER (?y="Paleto"^^xsd:string ||
	 * ?y="Feo"^^xsd:string) }
	 * 
	 * //La competencia es un recurso (que tendra el nombre en algun sitio)
	 * 
	 * SELECT ?x WHERE { ?x :hasCompetence ?j ?j :name ?y FILTER
	 * (?y="Paleto"^^xsd:string || ?y="Feo"^^xsd:string) }
	 */

}
