package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignUp extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6858159168102108335L;
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//TODO: comprobar que no está el correo ya registrado en la base de datos
		//String user = req.getParameter("correo");
		//String pass = req.getParameter("pass1");
		String statement = "de momento";
		//statement = "SELECT * FROM users WHERE " + user + "=correo";
		if (!statement.equals("")) {
			PrintWriter out = resp.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<a href='http://localhost:8080/manaject/SEM/html/index.html'>" + "Pulse aqu&iacute; para volver a la p&aacute;gina de inicio" + "</a>");
			out.println("</body>");
			out.println("</html>");
		} else {
			response(resp, "Ese email ya est&aacute; registrado");
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
	public static void main(String []args){
		System.out.println("just for building");
	}
}