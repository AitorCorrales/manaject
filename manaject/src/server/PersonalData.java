package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.UtilFunctions;
import utils.Constants;

import essentials.DatabaseManagement;


public class PersonalData extends HttpServlet {
	
	
	private static final long serialVersionUID = -6781398296066531034L;
	
	static DatabaseManagement dbm = new DatabaseManagement();
	static UtilFunctions func = new UtilFunctions();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			dbm.deletePersonFullNameById(dbm.connectToStardog(Constants.database), req.getSession().getId());
			dbm.insertPersonFullNameById(dbm.connectToStardog(Constants.database), req.getSession().getId(), req.getParameter("nombre") + " " + req.getParameter("ape1") + " " + req.getParameter("ape2"));
			dbm.deletePersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId());
			dbm.insertPersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId(), req.getParameter("Telefono"));
			dbm.deletePersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId());
			dbm.insertPersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId(), req.getParameter("Telefono"));
			dbm.deletePersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId());
			dbm.insertPersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId(), req.getParameter("Telefono"));
			dbm.deletePersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId());
			dbm.insertPersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId(), req.getParameter("Telefono"));
			dbm.deletePersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId());
			dbm.insertPersonTelephoneById(dbm.connectToStardog(Constants.database), req.getSession().getId(), req.getParameter("Telefono"));
			
			//BORRAR LA SESIÓN ACTUAL
			req.getSession(false).invalidate();

			/*dbm.updatePersonAddressById(dbm.connectToStardog(Constants.database), req.getSession().getId(), req.getParameter("Telefono"));
			dbm.updatePersonPostalById(dbm.connectToStardog(Constants.database), req.getSession().getId(), req.getParameter("Telefono"));*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response(resp, "Insertado correctamente");
		}

	private void response(HttpServletResponse resp, String msg)
			throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<t1>" + msg + "</t1>");
		out.println("<a href='PersonalProfile.html'>" + "Pulse aqu&iacute; para volver al apartado de aptitudes personales" + "</a>");
		out.println("</body>");
		out.println("</html>");
	}
	public static void main(String []args){
		System.out.println("just for building");
		try {
			System.out.println(func.clearString(dbm.findEmailById(
									dbm.connectToStardog(Constants.database), "DBF8FD2233D8588D2EA5D3A733EB69E8")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}