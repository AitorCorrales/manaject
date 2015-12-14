package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import essentials.DatabaseManagement;

public class SignUp extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6858159168102108335L;

	DatabaseManagement dbm = new DatabaseManagement();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO: comprobar que no est� el correo ya registrado en la base de
		// datos
		// String user = req.getParameter("correo");
		// String pass = req.getParameter("pass1");
		// statement = "SELECT * FROM users WHERE " + user + "=correo";
		try {
			if (req.getParameter("pass1").equals(req.getParameter("pass2"))) {
				if (!dbm.findPersonByEmail(dbm.connectToStardog("myDb"),
						req.getParameter("correo"))/*
													 * req.getParameter("correo")
													 * .equals("gb")
													 */) {

					dbm.insertPersonEmail(dbm.connectToStardog("myDb"),
							req.getParameter("correo"));
					dbm.insertPersonPassword(dbm.connectToStardog("myDb"), req.getParameter("correo"),
							req.getParameter("pass1"));

					PrintWriter out = resp.getWriter();
					out.println("<html>");
					out.println("<body>");
					out.println("<p>Registrado correctamente</p>");
					out.println("<a href='http://localhost:8080/manaject/SEM/html/index.html'>"
							+ "Pulse aqu&iacute; para volver a la p&aacute;gina de inicio"
							+ "</a>");
					out.println("</body>");
					out.println("</html>");
				} else {
					response(resp, "Ese email ya est&aacute; registrado");
				}
			} else {
				response(resp, "las contrase�as no coinciden");
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
		out.println("<t1>" + msg + "</t1>");
		out.println("</body>");
		out.println("</html>");
	}

	public static void main(String[] args) {
		System.out.println("just for building");
	}
}