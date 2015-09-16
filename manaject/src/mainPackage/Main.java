package mainPackage;

import essentials.DatabaseManagement;

public class Main {

	public static void main (String [] args){
		DatabaseManagement dbm = new DatabaseManagement();
		try {
			//dbm.findPerson(dbm.connectToStardog("myDb"));
			dbm.insertPersonFullName(dbm.connectToStardog("myDb"), "Rodrigo Aimardez");
			dbm.findPersonByFullName(dbm.connectToStardog("myDb"), "Rodrigo Aimardez");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
