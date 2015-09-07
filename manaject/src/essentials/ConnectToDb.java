package essentials;


import java.io.FileInputStream;

import com.complexible.common.protocols.server.Server;
import com.complexible.stardog.Stardog;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import com.complexible.stardog.protocols.snarl.SNARLProtocolConstants;
import com.complexible.stardog.jena.SDJenaFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class ConnectToDb {
	
	public void connectToStardog() throws Exception {
		Server aServer = Stardog
            .buildServer()
            .bind(SNARLProtocolConstants.EMBEDDED_ADDRESS)
            .start();
	
	AdminConnection aAdminConnection = AdminConnectionConfiguration.toEmbeddedServer().credentials("admin", "admin").connect();
	/**if (aAdminConnection.list().contains("testJena")) {
		aAdminConnection.drop("testJena");
	}*/
	aAdminConnection.createMemory("myDB");
	aAdminConnection.close();

	Connection aConn = ConnectionConfiguration
		                   .to("myDB")		  // the name of the db to connect to
		                   .credentials("admin", "admin") // the credentials with which to connect
		                   .connect();
	
	// obtain a jena for the specified stardog database.  Just creating an in-memory database
			// this is roughly equivalent to ModelFactory.createDefaultModel.
			Model aModel = SDJenaFactory.createModel(aConn);

			// start a transaction before adding the data.  This is not required, but it is faster to group the entire add into a single transaction rather
			// than rely on the auto commit of the underlying stardog connection.
			aModel.begin();

			// read data into the model.  note, this will add statement at a time.  Bulk loading needs to be performed directly with the BulkUpdateHandler provided
			// by the underlying graph, or read in files in RDF/XML format, which uses the bulk loader natively.  Alternatively, you can load data into the stardog
			// database using it's native API via the command line client.
			aModel.getReader("N3").read(aModel, new FileInputStream("data/sp2b_10k.n3"), "");

			// done!
			aModel.commit();

			// Query that we will run against the data we just loaded
			String aQueryString = "select * where { ?s ?p ?o. filter(?s = <http://localhost/publications/articles/Journal1/1940/Article1>).}";

			// Create a query...
			Query aQuery = QueryFactory.create(aQueryString);
			// ... and run it
			QueryExecution aExec = QueryExecutionFactory.create(aQuery, aModel);

			// now print the results
			ResultSetFormatter.out(aExec.execSelect(), aModel);

			// always close the execution
			aExec.close();

			// close the model to free up the connection to the stardog database
			aModel.close();

			aServer.stop();
	}
}
