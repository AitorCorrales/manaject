package utils;


import essentials.DatabaseManagement;

public class ServerFunctions {

	// ///////////////////////////////////////////////////////////
	// //////////SIGN METHODS
	// ///////////////////////////////////////////////////////////
	public static DatabaseManagement DBM = new DatabaseManagement();

	public boolean fromFormToDbSignUp(String user, String pass) {
		try {
			if (DBM.findPersonByEmail(DBM.connectToStardog("myDb"), user))
				return false;
			
			DBM.insertPersonEmail(DBM.connectToStardog("myDb"), user);
			DBM.insertPersonPassword(DBM.connectToStardog("myDb"), pass);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean fromFormToDbSignIn(String user, String pass) {
		try {
			if (DBM.findPersonByEmail(DBM.connectToStardog("myDb"), user) && DBM.passOfUser(DBM.connectToStardog("myDb"), user, pass))
				return true;
			
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
