package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.UtilFunctions;
import essentials.DatabaseManagement;


public class ProjProfile extends HttpServlet {
	
	
	private static final long serialVersionUID = -6781398296066531034L;
	
	DatabaseManagement dbm = new DatabaseManagement();
	UtilFunctions func = new UtilFunctions();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			Vector <String> vec = func.showOntology(dbm.connectToStardog("myDb"), req.getParameter("classes"));
			/*dbm.updatePersonAddressById(dbm.connectToStardog("myDb"), req.getSession().getId(), req.getParameter("Telefono"));
			dbm.updatePersonPostalById(dbm.connectToStardog("myDb"), req.getSession().getId(), req.getParameter("Telefono"));*/
			response(resp, vec);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	private void response(HttpServletResponse resp, Vector <String> vec)
			throws IOException {
		PrintWriter out = resp.getWriter();
		Iterator<String> it = vec.iterator();
		out.println("<html>");
		out.println("<body>");
		while(it.hasNext()){
			String next = (String) it.next();
			out.println("<a href='" + next + "" + "'>" + next + "</a>");
		}
		out.println("</body>");
		out.println("</html>");
	}
	
	public void loadClasses(boolean dynamic){
		try{
			if(dynamic)
				dbm.showClasses(dbm.connectToStardog("myDb"), true);
			else 
				dbm.showClasses(dbm.connectToStardog("myDb"), false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String []args){
		System.out.println("just for building");
	}
}