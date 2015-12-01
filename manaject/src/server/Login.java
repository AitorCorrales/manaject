package server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String user = req.getParameter("correo");
		String pass = req.getParameter("pass1");
		if (user.equals("acorrales004@ikasle.ehu.es") && pass.equals("acorrales004")) {
			PrintWriter out = resp.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<a href='http://localhost:8080/manaject/SEM/html/Aptitudes.html'>" + "Pulse aqu&iacute; para continuar al perfil" + "</a>");
			out.println("</body>");
			out.println("</html>");
		} else {
			response(resp, "invalid login");
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