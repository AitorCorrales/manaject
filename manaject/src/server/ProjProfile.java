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
import utils.Constants;

import essentials.DatabaseManagement;
import essentials.Competence;
import essentials.Recommended;

public class ProjProfile extends HttpServlet {

	private static final long serialVersionUID = -6781398296066531034L;

	DatabaseManagement dbm = new DatabaseManagement();
	UtilFunctions func = new UtilFunctions();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			Vector<Competence> competences = new Vector<Competence>();
			Vector<String> etiquetas = new Vector<String>();
			// BORRAR LA SESIÓN ACTUAL
			// req.getSession(false).invalidate();
			String[] Value;
			String[] ValueNumber;
			String nombre = req.getParameter("nombre").replaceAll("\\s","");
			String numParticipantes = req.getParameter("numero");
			
			if(nombre != null && numParticipantes != null){				
			try {
				Value = req.getParameterValues("skills");
				ValueNumber = req.getParameterValues("skillsValue");
				if (Value.length > 0) {
					String eti = "skill";
					etiquetas.add(eti);
					for (int i = 0; i < Value.length; i++) {
						competences.add(new Competence(eti + "&"+ Value[i], Double
								.parseDouble(ValueNumber[i]) / 100));
						dbm.insertProjectCompetenceSkills(dbm
								.connectToStardog(Constants.database), func.clearString(dbm.findEmailById(
								dbm.connectToStardog(Constants.database), req.getSession()
										.getId())), nombre, Value[i]);
					}
				}

			} catch (Exception e) {
			}

			try {
				ValueNumber = req.getParameterValues("educationValue");
				Value = req.getParameterValues("education");
				if (Value.length > 0) {
					String eti = "education";
					etiquetas.add(eti);
					for (int i = 0; i < Value.length; i++) {
						competences.add(new Competence(eti + "&" + Value[i], Double
								.parseDouble(ValueNumber[i]) / 100));
						dbm.insertProjectCompetenceEducation(dbm
								.connectToStardog(Constants.database), func.clearString(dbm.findEmailById(
								dbm.connectToStardog(Constants.database), req.getSession()
										.getId())), nombre, Value[i]);
					}
				}
			} catch (Exception e) {
			}

			try {
				Value = req.getParameterValues("professional");
				ValueNumber = req.getParameterValues("professionalValue");
				if (Value.length > 0) {
					String eti = "occupation";
					etiquetas.add(eti);
					for (int i = 0; i < Value.length; i++) {
						competences.add(new Competence(eti + "&" + Value[i], Double
								.parseDouble(ValueNumber[i]) / 100));
						dbm.insertProjectCompetenceOccupation(dbm
								.connectToStardog(Constants.database), func.clearString(dbm.findEmailById(
								dbm.connectToStardog(Constants.database), req.getSession()
										.getId())), nombre, Value[i]);
					}
				}
			} catch (Exception e) {
			}

			try {
				Value = req.getParameterValues("languages");
				ValueNumber = req.getParameterValues("languagesValue");
				if (Value.length > 0) {
					String eti = "language";
					etiquetas.add(eti);
					for (int i = 0; i < Value.length; i++) {
						competences.add(new Competence(eti + "&" + Value[i], Double
								.parseDouble(ValueNumber[i]) / 100));
						dbm.insertProjectCompetenceLanguage(dbm
								.connectToStardog(Constants.database), func.clearString(dbm.findEmailById(
								dbm.connectToStardog(Constants.database), req.getSession()
										.getId())), nombre, Value[i]);
					}
				}
			} catch (Exception e) {
			}

			try {
				Value = req.getParameterValues("language");
				int length = Value.length;
				if (length > 0) {
					for (int i = 0; i < length; i++) {
						competences.add(new Competence(Value[i], 0.8));
						// VALOR DE LA LENGUA MATERNA
						dbm.insertProjectCompetenceMotherTongue(dbm
								.connectToStardog(Constants.database), func.clearString(dbm.findEmailById(
								dbm.connectToStardog(Constants.database), req.getSession()
										.getId())), nombre, Value[i]);
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
			dbm.insertProjectHead(dbm.connectToStardog(Constants.database), func.clearString(dbm.findEmailById(
					dbm.connectToStardog(Constants.database), req.getSession()
					.getId())), nombre);

			response(resp, func.inefficientOrdering(func.getEstablishedCompetencePunctuationForHTMLJaccard(
					func.separateIntoLists(dbm.findPeopleByCompetences(
							dbm.connectToStardog(Constants.database), competences, etiquetas)),
					competences)), Integer.parseInt(numParticipantes));
				} else
					responseError(resp, "Debe ponerle un nombre al proyecto");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void response(HttpServletResponse resp, Vector<Recommended> vec, int num)
			throws IOException {
		PrintWriter out = resp.getWriter();
		Iterator<Recommended> it = vec.iterator();
		int itNum = 0;
		out.println("<html>");
		out.println("<body>");
		out.println("<h1> Personas recomendadas </h1>");
		while (it.hasNext() && itNum < num) {
			Recommended next = it.next();
			out.println("<p>" + next.getKey() + ": " + next.getPunct() + "</p>");
//			out.println("<p>" + next.getKey() + "</p>");
			itNum++;
		}
		out.println("</body>");
		out.println("</html>");
	}
	private void responseError(HttpServletResponse resp, String error)
			throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<p>"+ error + "</p>");
		out.println("</body>");
		out.println("</html>");
	}

	// public void loadClasses(boolean dynamic){
	// try{
	// if(dynamic)
	// dbm.showClasses(dbm.connectToStardog(Constants.database), true);
	// else
	// dbm.showClasses(dbm.connectToStardog(Constants.database), false);
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// }

	public static void main(String[] args) {
		System.out.println("just for building");
	}
}