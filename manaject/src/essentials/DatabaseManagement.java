package essentials;

import utils.UtilFunctions;
import utils.PrintFunctions;
import utils.Constants;

import java.util.Iterator;
import java.util.Vector;

import org.openrdf.query.TupleQueryResult;

import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.SelectQuery;
import com.complexible.stardog.api.UpdateQuery;
import com.complexible.stardog.jena.SDJenaFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class DatabaseManagement {

	public static PrintFunctions prints = new PrintFunctions();

	// IMPORTANTE: ANTES DENTRO DE UNA FUNCION
	public static UtilFunctions func = new UtilFunctions();

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
		String aQueryString = "SELECT ?x WHERE {\n" + "?x jobSeeker:Full_Name "
				+ "\"" + name + "\"" + "\n" + "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		prints.printResults(aExec);

		// always close the execution
		aExec.close();
		aModel.close();

	}

	public String findEmailById(Connection aConn, String id) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		// start a transaction before adding the data. This is not required, but
		// it is faster to group the entire add into a single transaction rather
		// than rely on the auto commit of the underlying stardog connection.
		aModel.begin();

		// Query that we will run against the data we just loaded
		String aQueryString = "SELECT ?y WHERE {\n"
				+ "?x jobSeeker:id_session " + "\"" + id + "\"" + ". "
				+ "?x jobSeeker:email " + "?y" + ". " + "\n" + "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		if (aExec.hasNext()) {
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

	public String findPersonFullName(Connection aConn, String user)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		// start a transaction before adding the data. This is not required, but
		// it is faster to group the entire add into a single transaction rather
		// than rely on the auto commit of the underlying stardog connection.
		aModel.begin();

		// Query that we will run against the data we just loaded
		String aQueryString = "SELECT ?x WHERE {\n" + "<http://people" + "/"
				+ user + "> " + "jobSeeker:Full_Name " + "?x" + "\n" + "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		if (aExec.hasNext()) {
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
		String aQueryString = "SELECT ?x WHERE {\n" + "?x jobSeeker:email "
				+ "\"" + email + "\"" + "\n" + "}";

		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);

		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		if (!aExec.hasNext()) {
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
		String aQueryString = "SELECT ?x WHERE {\n" + "?y skill:Name " + "\""
				+ comp + "\"" + ". " + "\n" + "?y jobSeeker:email " + "?x"
				+ ". " + "\n" + "}";

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

			String aQueryString = "SELECT ?x WHERE {\n" + "?y skill:Name "
					+ "\"" + it.next().getComp() + "\"" + ". " + "\n"
					+ "?y jobSeeker:email " + "?x" + ". " + "\n" + "}";

			SelectQuery aQuery = aConn.select(aQueryString);
			TupleQueryResult aExec = aQuery.execute();
			prints.printResults(aExec);
			aExec.close();
			System.out.println("------------------------------");
		}

		aModel.close();

	}

	public Vector<String> findPeopleByCompetences(Connection aConn,
			Vector<Competence> vec, Vector<String> etiquetas) throws Exception {

		Vector<String> vecO = new Vector<String>();

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

			SelectQuery aQuery = aConn.select(func.prepareStatementSelect(vec,
					etiquetas));
			TupleQueryResult aExec = aQuery.execute();
			// printResults(aExec);
			func.intoVectorResults(aExec, vecO);
		// func.printVector(vecO);

		return vecO;
	}
	
	public Vector<String> findPeopleByCompetences2(Connection aConn,
			Vector<Competence> vec, String id) throws Exception {

		Vector<String> vecO = new Vector<String>();

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

			SelectQuery aQuery = aConn.select(func.prepareStatementSelect2(vec,
					id));
			TupleQueryResult aExec = aQuery.execute();
			// printResults(aExec);
			func.intoVectorResults(aExec, vecO);
		// func.printVector(vecO);

		return vecO;
	}

	public void insertPersonFullName(Connection aConn, String user, String name)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://people" + "/"
				+ user + "> " + "jobSeeker:Full_Name " + "\"" + name + "\""
				+ "\n" + "}";

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

	/*
	 * INSERT { ?y jobSeeker:Full_Name "Adam Pierf Jongh" } WHERE { ?y
	 * jobSeeker:id_session "C1CB0FA7BE8507B15658768FC02B772D" }
	 */

	public void insertPersonPassword(Connection aConn, String user,
			String password) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://people" + "/"
				+ user + "> " + "jobSeeker:password " + "\"" + password + "\""
				+ "\n" + "}";

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

		String aQueryString = "insert data\n" + "{\n" + "<http://people" + "/"
				+ user + "> " + "skill:Name " + "\"" + competence + "\"" + "\n"
				+ "}";

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

	public void insertPersonCompetenceById(Connection aConn, String id,
			String competence) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert\n" + "{ ?y skill:Name " + "\""
				+ competence + "\"" + " }\n" + "where\n"
				+ "{ ?y jobSeeker:id_session " + "\"" + id + "\"" + " }";

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

	public void insertPersonAnyById(Connection aConn, String id,
			String competence, String field) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert\n" + "{ ?y " + field + ":Name " + "\""
				+ competence + "\"" + " }\n" + "where\n"
				+ "{ ?y jobSeeker:id_session " + "\"" + id + "\"" + " }";

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

	public void insertPersonMotherTongueById(Connection aConn, String id,
			String competence) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert\n" + "{ ?y language:Mother_tongue "
				+ "\"" + competence + "\"" + " }\n" + "where\n"
				+ "{ ?y jobSeeker:id_session " + "\"" + id + "\"" + " }";

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
			insertPersonCompetence(connectToStardog(Constants.database), user,
					it.next().getComp());
		}
	}

	public void insertPersonEmail(Connection aConn, String email)
			throws Exception {
		// TODO Auto-generated method stub
		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://people" + "/"
				+ email + "> " + "jobSeeker:email " + "\"" + email + "\""
				+ "\n" + "}";

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

	public void insertProjectCompetenceSkills(Connection aConn, String user,
			String name, String competence) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert data\n" + "{ " + "<http://project" + "/"
				+ user + "_" + name + "> " + "skill:Name " + "\"" + competence
				+ "\"" + " " + "}";

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

	public void insertProjectHead(Connection aConn, String user, String name)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert data\n" + "{ " + "<http://project" + "/"
				+ user + "_" + name + "> " + "jobOffer:email " + "\"" + user
				+ "\"" + " " + "}";

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

	public void insertProjectCompetenceEducation(Connection aConn, String user,
			String name, String competence) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://project" + "/"
				+ user + "_" + name + "> " + "jobOffer:has_education " + "\""
				+ competence + "\"" + "\n" + "}";

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

	public void insertProjectCompetenceOccupation(Connection aConn,
			String user, String name, String competence) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://project" + "/"
				+ user + "_" + name + "> "
				+ "jobOffer:requires_professional_affiliation " + "\""
				+ competence + "\"" + "\n" + "}";

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

	public void insertProjectCompetenceLanguage(Connection aConn, String user,
			String name, String competence) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://project" + "/"
				+ user + "_" + name + "> " + "language:Name " + "\""
				+ competence + "\"" + "\n" + "}";

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

	public void insertProjectCompetenceMotherTongue(Connection aConn,
			String user, String name, String competence) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://project" + "/"
				+ user + "_" + name + "> " + "language:Mother_tongue " + "\""
				+ competence + "\"" + "\n" + "}";

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

	// public void insertProjectID(Connection aConn, String user) throws
	// Exception {
	//
	// Model aModel = SDJenaFactory.createModel(aConn);
	//
	// aModel.begin();
	//
	// String aQueryString = "insert data\n"
	// + "{\n"
	// + "<http://project"
	// + "/"
	// + user
	// + "> "
	// + "skill:Name "
	// + "\"" + competence + "\"" + "\n" + "}";
	//
	// // Create a query...
	// UpdateQuery aQuery = aConn.update(aQueryString);
	//
	// // ... and run it
	// if (aQuery.execute())
	// System.out.println("triple insertado correctamente");
	// else
	// System.out.println("error al insertar triple en base de datos");
	//
	// aConn.commit();
	//
	// aModel.close();
	// }

	public void insertProjectCompetences(String user,
			Vector<Competence> competences) throws Exception {

		Iterator<Competence> it = competences.iterator();

		while (it.hasNext()) {
			insertPersonCompetence(connectToStardog(Constants.database), user,
					it.next().getComp());
		}
	}

	// A�ADIR A LA ONTOLOG�A EL ATRIBUTO "CONTRASE�A" PARA PODER HACER EL
	// MATCHING ENTRE USUARIO Y CONTRASE�A
	// TODO: hacer pruebas
	public boolean findPersonPassword(Connection aConn, String user, String pass)
			throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "SELECT ?y WHERE {\n" + "?y "
				+ "jobSeeker:password " + "\"" + pass + "\"" + ". " + "\n"
				+ "?y " + "jobSeeker:email " + "\"" + user + "\"" + ". " + "\n"
				+ "}";

		SelectQuery aQuery = aConn.select(aQueryString);
		TupleQueryResult aExec = aQuery.execute();

		if (aExec.hasNext()) {
			aExec.close();
			aModel.close();
			return true;
		}

		aExec.close();
		aModel.close();
		return false;
	}
	
	public String findProjectSkills(Connection aConn, String session)
			throws Exception {


		return "SELECT ?y WHERE {\n" + "?j "
				+ "jobOffer:id_session " + "\"" + session + "\"" + ". " + "\n"
				+ "?j " + "skill:Name " + "?y" + ". " + "\n"
				+ "}";

	}
	
	public String findProjectOccupations(Connection aConn, String session)
			throws Exception {

		return "SELECT ?y WHERE {\n" + "?j "
				+ "jobOffer:id_session " + "\"" + session + "\"" + ". " + "\n"
				+ "?j " + "jobOffer:requires_professional_affiliation " + "?y" + ". " + "\n"
				+ "}";

		}
	
	public String findProjectLanguages(Connection aConn, String session)
			throws Exception {


		return "SELECT ?y WHERE {\n" + "?j "
				+ "jobOffer:id_session " + "\""  + session + "\"" + ". " + "\n"
				+ "?j " + "language:Name " + "?y" + ". " + "\n"
				+ "}";

	}
	
	public String findProjectEducation(Connection aConn, String session)
			throws Exception {


		return "SELECT ?y WHERE {\n" + "?j "
				+ "jobOffer:id_session " + "\"" + session + "\"" + ". " + "\n"
				+ "?j " + "jobOffer:has_education " + "?y" + ". " + "\n"
				+ "}";

	}

	public void assignNewSessionToken(Connection aConn, String user,
			String session) throws StardogException {
		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "insert data\n" + "{\n" + "<http://people" + "/"
				+ user + "> " + "jobSeeker:id_session " + "\"" + session + "\""
				+ "\n" + "}";

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

	public void deleteSessionToken(Connection aConn, String user)
			throws StardogException {
		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "delete where \n" + "{\n" + "<http://people"
				+ "/" + user + "> " + "jobSeeker:id_session " + "?y" + "\n"
				+ "}";

		/*
		 * DELETE { ?person ?property ?value } WHERE { ?person ?property ?value
		 * ; jobSeeker:email 'babatunde@gmail.com' }
		 */

		// Create a query...
		UpdateQuery aQuery = aConn.update(aQueryString);

		// ... and run it
		if (aQuery.execute())
			System.out.println("triple borrado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");

		aConn.commit();
		aModel.close();

	}

	// TODO: finish
	public boolean hasSessionToken(Connection aConn, String sessionId)
			throws StardogException {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "SELECT ?y WHERE {\n" + "?y "
				+ "jobSeeker:id_session " + "\"" + sessionId + "\"" + "\n"
				+ "}";

		/*
		 * DELETE { ?person ?property ?value } WHERE { ?person ?property ?value
		 * ; jobSeeker:email 'babatunde@gmail.com' }
		 */

		// Create a query...
		UpdateQuery aQuery = aConn.update(aQueryString);

		// ... and run it
		if (aQuery.execute())
			System.out.println("triple borrado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");

		aConn.commit();
		aModel.close();

		return true;

	}

	public void deletePersonFullNameById(Connection aConn, String id)
			throws Exception {

		// DELETE
		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryDelete = "delete\n" + "{ ?y jobSeeker:Full_Name " + "?x "
				+ "}\n" + "WHERE\n" + "{ ?y jobSeeker:id_session " + "\"" + id
				+ "\"" + ". \n" + "?y jobSeeker:Full_Name " + "?x" + ". \n"
				+ "}";

		// Create a query...
		UpdateQuery aQuery = aConn.update(aQueryDelete);

		// ... and run it
		if (aQuery.execute())
			System.out.println("triple insertado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");

		aConn.commit();
		aModel.close();
	}

	public void insertPersonFullNameById(Connection aConn, String id,
			String name) throws Exception {

		// INSERT
		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryInsert = "insert\n" + "{ ?y jobSeeker:Full_Name " + "\""
				+ name + "\"" + " }\n" + "where\n"
				+ "{ ?y jobSeeker:id_session " + "\"" + id + "\"" + " }";

		// Create a query...
		UpdateQuery aQuery = aConn.update(aQueryInsert);

		// ... and run it
		if (aQuery.execute())
			System.out.println("triple insertado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");

		aConn.commit();
		aModel.close();
	}

	public void deletePersonTelephoneById(Connection aConn, String id)
			throws Exception {

		// DELETE
		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryDelete = "delete\n" + "{ ?y jobSeeker:Telephone " + "?x "
				+ "}\n" + "where\n" + "{ ?y jobSeeker:id_session " + "\"" + id
				+ "\"" + ". \n" + "?y jobSeeker:Telephone " + "?x" + ". \n"
				+ "}";

		// Create a query...
		UpdateQuery aQuery = aConn.update(aQueryDelete);

		// ... and run it
		if (aQuery.execute())
			System.out.println("triple insertado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");

		aConn.commit();
		aModel.close();
	}

	public void insertPersonTelephoneById(Connection aConn, String id,
			String tlf) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "insert\n" + "{ ?y jobSeeker:Telephone " + "\""
				+ tlf + "\"" + "}\n" + "where\n" + "{ ?y jobSeeker:id_session "
				+ "\"" + id + "\"" + " }";

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

	public void updatePersonAddressById(Connection aConn, String id,
			String address) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "INSERT\n" + "{ ?y jobSeeker:Address " + "\""
				+ address + "\"" + "}\n" + "WHERE\n"
				+ "{ ?y jobSeeker:id_session " + "\"" + id + "\"" + "}";

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

	public void updatePersonPostalById(Connection aConn, String id,
			String postal) throws Exception {

		Model aModel = SDJenaFactory.createModel(aConn);
		aModel.begin();

		String aQueryString = "INSERT\n" + "{ ?y jobSeeker:Postal_Code " + "\""
				+ postal + "\"" + "}\n" + "WHERE\n"
				+ "{ ?y jobSeeker:id_session " + "\"" + id + "\"" + "}";

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

	public void showClasses(Connection aConn, boolean bool) {
		// TODO Auto-generated method stub
		if (bool) {

		} else {

		}
	}

	public void insertProjectId(Connection aConn,
			String user, String name, String id) throws StardogException {
		// TODO Auto-generated method stub
		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert data\n" + "{ " + "<http://project" + "/"
				+ user + "_" + name + "> " + "jobOffer:id_session " + "\"" + id
				+ "\"" + " " + "}";

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
