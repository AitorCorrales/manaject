package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import essentials.DatabaseManagement;


public class Login extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6781398296066531034L;
	
	DatabaseManagement dbm = new DatabaseManagement();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pass = req.getParameter("pass1");
		try {
			if (dbm.findPersonByEmail(dbm.connectToStardog("myDb"), req.getParameter("correo")) && pass.equals("acorrales004")) {
				PrintWriter out = resp.getWriter();
				out.println("<html>");
				out.println("<body>");
				out.println("<a href='projGlobal.html'>" + "Pulse aqu&iacute; para continuar al inicio" + "</a>");
				out.println("</body>");
				out.println("</html>");
			} else {
				response(resp, "invalid login");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void response(HttpServletResponse resp, String msg)
			throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		//out.println("<t1>" + msg + "</t1>");
		out.println("<a href='SignIn.html'>" + "Pulse aqu&iacute; para volver a iniciar sesi&oacute;n" + "</a>");
		out.println("</body>");
		out.println("</html>");
	}
	public static void main(String []args){
		System.out.println("just for building");
	}
}