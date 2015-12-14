package essentials;

import utils.UtilFunctions;
import utils.PrintFunctions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.openrdf.query.TupleQueryResult;

import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.SelectQuery;
import com.complexible.stardog.api.UpdateQuery;
import com.complexible.stardog.jena.SDJenaFactory;
import com.hp.hpl.jena.rdf.model.Model;

//¿HAY QUE PONER EL .TTL AL FINAL DE LOS URI PARA QUE ENTRE?
public class DatabaseManagement {
	
	public static PrintFunctions prints = new PrintFunctions();

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
				+ "?x jobSeeker:Full_Name " + "\"" + name
				+ "\"" + "\n" + "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		prints.printResults(aExec);

		// always close the execution
		aExec.close();
		aModel.close();

	}
	public String findPersonFullName(Connection aConn, String user)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		// start a transaction before adding the data. This is not required, but
		// it is faster to group the entire add into a single transaction rather
		// than rely on the auto commit of the underlying stardog connection.
		aModel.begin();

		// Query that we will run against the data we just loaded
		String aQueryString = "SELECT ?x WHERE {\n"
				+ "<http://people" + "/" + user + "> " + "jobSeeker:Full_Name "
				+ "?x" + "\n" + "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		if(aExec.hasNext()){
			String returnable = aExec.next().toString();
			aExec.close();
			aModel.close();
			return returnable;
		}

		// always close the execution
		aExec.close();
		aModel.close();
		return "";

	}
	public boolean findPersonByEmail(Connection aConn, String email)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		// start a transaction before adding the data. This is not required, but
		// it is faster to group the entire add into a single transaction rather
		// than rely on the auto commit of the underlying stardog connection.
		aModel.begin();

		// Query that we will run against the data we just loaded
		String aQueryString = "SELECT ?x WHERE {\n"
				+ "?x jobSeeker:email " + "\"" + email
				+ "\"" + "\n" + "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		if (!aExec.hasNext()){
			aExec.close();
			aModel.close();
			return false;
		}
		// always close the execution
		aExec.close();
		aModel.close();
		return true;
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
				+ "?y <C:/Users/aitor/Downloads/SEM/Ontology/rdf/SkillOntology#Name> "+ "\"" + comp + "\"" + ". " + "\n"
				+ "?y <C:/Users/aitor/Downloads/SEM/Ontology/rdf/JobSeekerOntology#email> "+ "?x" + ". " + "\n"
				+ "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		prints.printResults(aExec);

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
			/*String aQueryString = "SELECT ?x WHERE {\n"
					+ "?x <C:/Users/aitor/Downloads/SEM/Ontology/rdf/SkillOntology#Name> "
					+ "\"" + it.next().getComp() + "\"" + ". " + "\n" + "}";
					////////////////////////////////////////////////////////////////
			String aQueryString = "SELECT ?y WHERE {\n"
					+ "?y skill:Name "+ "\"" + it.next().getComp() + "\"" + "\n"
					+ "}";*/
			
			//Note: Si en vez de pedir el email, pedimos el recurso, tenemos acceso a todo el recurso entero (Nombre, competencias...)
			
			String aQueryString = "SELECT ?x WHERE {\n"
					+ "?y skill:Name "+ "\"" + it.next().getComp() + "\"" + ". " + "\n"
					+ "?y jobSeeker:email "+ "?x" + ". " + "\n"
					+ "}";
			
			SelectQuery aQuery = aConn.select(aQueryString);
			TupleQueryResult aExec = aQuery.execute();
			prints.printResults(aExec);
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
	public Vector<String> finalList (Connection aConn, Vector<Competence> vec) throws Exception{
		return recommenderIntersection(recListLikes(),recListNeeds(aConn, vec));
	}

	private Vector<String> recommenderIntersection(
			Vector<String> listLikes, Vector<String> listNeeds) {
		// TODO Auto-generated method stub
		
		return null;
	}

	private Vector<String> recListNeeds(Connection aConn,
			Vector<Competence> vec) throws Exception {
		// TODO Auto-generated method stub
		return findPeopleByCompetences(aConn, vec);
	}

	private Vector<String> recListLikes() {
		Vector <String> rec = new Vector <String>();
		return rec;
	}

	public void insertPersonFullName(Connection aConn, String user, String name)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://people"
				+ "/" + user + "> "
				+ "jobSeeker:Full_Name " + "\"" + name
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
	public void insertPersonPassword(Connection aConn, String user, String password)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://people"
				+ "/" + user + "> "
				+ "jobSeeker:password " + "\"" + password
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

	public void insertPersonCompetence(Connection aConn, String user,
			String competence) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		/*
		 * INSERT DATA { GRAPH <http://somewhere/bookStore> {
		 * <http://example/book1> ns:price 42 } }
		 * 
		 * String aQueryString = "insert data\n" + "{\n" + "<http://people"
				+ "/" + user.replaceAll("\\s", "") + "> "
				+ "<C:/Users/aitor/Downloads/SEM/Ontology/rdf/SkillOntology.ttl#Name> "
				+ "\"" + competence + "\"" + "\n" + "}";
		 */
		
		/*String aQueryString = "insert data\n" + "{\n" + "<http://people"
				+ "/" + email + "> "
				+ "<C:/Users/aitor/Downloads/SEM/Ontology/rdf/JobSeekerOntology#email> " + "\"" + email
				+ "\"" + "\n" + "}";*/

		String aQueryString = "insert data\n" + "{\n" + "<http://people"
				+ "/" + user + "> "
				+ "<C:/Users/aitor/Downloads/SEM/Ontology/rdf/SkillOntology#Name> "
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

	public void insertPersonCompetences(String user,
			Vector<Competence> competences) throws Exception {

		Iterator<Competence> it = competences.iterator();

		while (it.hasNext()) {
			insertPersonCompetence(connectToStardog("myDb"), user, it.next()
					.getComp());
		}
	}

	public void insertPersonEmail(Connection aConn, String email) throws Exception{
		// TODO Auto-generated method stub
		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://people"
				+ "/" + email + "> "
				+ "<C:/Users/aitor/Downloads/SEM/Ontology/rdf/JobSeekerOntology#email> " + "\"" + email
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

	//AÑADIR A LA ONTOLOGÍA EL ATRIBUTO "CONTRASEÑA" PARA PODER HACER EL MATCHING ENTRE USUARIO Y CONTRASEÑA
	//TODO: hacer pruebas
	public boolean findPersonPassword(Connection aConn, String user,
			String pass) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "SELECT ?y WHERE {\n"
				+ "?y " + "jobSeeker:password " + "\"" + pass + "\"" + ". " + "\n"
				+ "?y " + "jobSeeker:email " + "\"" + user + "\"" + ". " + "\n"
				+ "}";

		SelectQuery aQuery = aConn.select(aQueryString);
		TupleQueryResult aExec = aQuery.execute();
		
		if(aExec.hasNext()){
			aExec.close();
			aModel.close();
			return true;
		}
		
		aExec.close();
		aModel.close();
		return false;
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
