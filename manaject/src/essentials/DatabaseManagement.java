package essentials;

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

		/*@SuppressWarnings("unused")
		Server aServer = Stardog.buildServer()
				.bind(new InetSocketAddress("10.0.0.1", 5820)).start();
		
		AdminConnection aAdminConnection = AdminConnectionConfiguration
				.toServer("http://localhost:5820").credentials("admin", "admin").connect();*/

		Connection aConn = ConnectionConfiguration.to(dbname) // the name of the db to connect

				.server("http://localhost:5820")												
																// to
				.credentials("admin", "admin") // the credentials with which to
												// connect
				.connect();

		return aConn;
	}

	public void findPersonByFullName (Connection aConn, String name) throws Exception{
		

		Model aModel = SDJenaFactory.createModel(aConn);
		/*AdminConnection aAdminConnection = AdminConnectionConfiguration
				.toEmbeddedServer().credentials("admin", "admin").connect();

		aAdminConnection.createMemory(dbname);*/


		// start a transaction before adding the data. This is not required, but
		// it is faster to group the entire add into a single transaction rather
		// than rely on the auto commit of the underlying stardog connection.
		aModel.begin();


		// read data into the model. note, this will add statement at a time.
		// Bulk loading needs to be performed directly with the
		// BulkUpdateHandler provided
		// by the underlying graph, or read in files in RDF/XML format, which
		// uses the bulk loader natively. Alternatively, you can load data into
		// the stardog
		// database using it's native API via the command line client.
		
		
		/*aModel.getReader("N3").read(aModel,
				new FileInputStream("data/sp2b_10k.n3"), "");*/
		
		//aModel.getReader("N3").read(aModel,
			//	new FileInputStream("C:/Users/aitor/Example/file.rdf"), "");

		// Query that we will run against the data we just loaded
		String aQueryString = "SELECT ?x WHERE {\n" +
				 "?x <http://www.w3.org/2001/vcard-rdf/3.0#FN> " + "\"" + name + "\"" + "\n" +
				 "}";
		
		// Create a query...
		SelectQuery aQuery = aConn.select(aQueryString);
		
		// ... and run it
		TupleQueryResult aExec = aQuery.execute();
		printResults(aExec);

		// now print the results
//		ResultSetFormatter.out(aExec.execSelect(), aModel);

		// always close the execution
		aExec.close();
		aModel.close();

		// you must always log out of the dbms.
		//aAdminConnection.close();
	}
	
public void insertPersonFullName (Connection aConn, String name) throws Exception{
		

		Model aModel = SDJenaFactory.createModel(aConn);

		aModel.begin();

		String aQueryString = "insert data\n" +
				 "{\n" + "<http://somewhere" + "/" + name.replaceAll("\\s","") + "> " + "<http://www.w3.org/2001/vcard-rdf/3.0#FN> " + "\"" + name + "\"" + "\n" +
				 "}";
		
		// Create a query...
		UpdateQuery aQuery = aConn.update(aQueryString);
		
		// ... and run it
		if ( aQuery.execute())
			System.out.println("triple insertado correctamente");
		else
			System.out.println("error al insertar triple en base de datos");
		
		aConn.commit();

		aModel.close();

		// you must always log out of the dbms.
		//aAdminConnection.close();
	}
	
	public void printResults (TupleQueryResult res) throws QueryEvaluationException {
		try {
			int count = 0;
			while (res.hasNext()) {
				count++;
				System.out.println(res.next().toString());
			}
			System.out.println("Numero total de resultados: " + count);
		}
		finally {
			res.close();
		}
	}
}
