package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import essentials.DatabaseManagement;

import utils.Constants;

public class PersonalAptitudes extends HttpServlet {

	private static final long serialVersionUID = -6781398296066531034L;

	DatabaseManagement dbm = new DatabaseManagement();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// BORRAR LA SESIÓN ACTUAL
			// req.getSession(false).invalidate();

			String ident = req.getSession().getId();

			try {
				String[] skillValue = req.getParameterValues("skills");
				if (skillValue.length > 0) {
					for (int i = 0; i < skillValue.length; i++) {
						dbm.insertPersonAnyById(dbm.connectToStardog(Constants.database),
								ident, skillValue[i], "skill");
					}
				}
			} catch (Exception e) {
			}

			try {
				String[] educationValue = req.getParameterValues("education");

				if (educationValue.length > 0) {
					for (int i = 0; i < educationValue.length; i++) {
						dbm.insertPersonAnyById(dbm.connectToStardog(Constants.database),
								ident, educationValue[i], "education");
					}
				}
			} catch (Exception e) {
			}
			
			try {
				String[] professionalValue = req.getParameterValues("professional");

				if (professionalValue.length > 0) {
					for (int i = 0; i < professionalValue.length; i++) {
						dbm.insertPersonAnyById(dbm.connectToStardog(Constants.database),
								ident, professionalValue[i], "occupation");
					}
				}
			} catch (Exception e) {
			}
			
			try {
				String[] languageValue = req.getParameterValues("languages");

				if (languageValue.length > 0) {
					for (int i = 0; i < languageValue.length; i++) {
						dbm.insertPersonAnyById(dbm.connectToStardog(Constants.database),
								ident, languageValue[i], "language");
					}
				}
			} catch (Exception e) {
			}
			
			try {
				String[] languageValue = req.getParameterValues("language");

				if (languageValue.length > 0) {
					for (int i = 0; i < languageValue.length; i++) {
						dbm.insertPersonMotherTongueById(dbm.connectToStardog(Constants.database),
								ident, languageValue[i]);
					}
				}
			} catch (Exception e) {
			}

			/*
			 * dbm.updatePersonAddressById(dbm.connectToStardog(Constants.database),
			 * req.getSession().getId(), req.getParameter("Telefono"));
			 * dbm.updatePersonPostalById(dbm.connectToStardog(Constants.database),
			 * req.getSession().getId(), req.getParameter("Telefono"));
			 */

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
		out.println("<a href='projGlobal.html'>"
				+ "Pulse aqu&iacute; para volver a la página principal"
				+ "</a>");
		out.println("</body>");
		out.println("</html>");
	}

	public void loadClasses(boolean dynamic) {
		try {
			if (dynamic)
				dbm.showClasses(dbm.connectToStardog(Constants.database), true);
			else
				dbm.showClasses(dbm.connectToStardog(Constants.database), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("just for building");
	}
}