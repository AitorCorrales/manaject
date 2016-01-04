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
			// BORRAR LA SESIÓN ACTUAL
			// req.getSession(false).invalidate();

			try {
				String[] skillValue = req.getParameterValues("skills");
				String[] skillValueNumber = req.getParameterValues("skillsValue");
				if (skillValue.length > 0) {
					for (int i = 0; i < skillValue.length; i++) {
						competences.add(new Competence(skillValue[i], Double.parseDouble(skillValueNumber[i])/100));
					}
				}
			} catch (Exception e) {
			}

			try {
				String[] educationValueNumber = req.getParameterValues("educationValue");
				String[] educationValue = req.getParameterValues("education");
				if (educationValue.length > 0) {
					for (int i = 0; i < educationValue.length; i++) {
						competences.add(new Competence(educationValue[i], Double.parseDouble(educationValueNumber[i])/100));
					}
				}
			} catch (Exception e) {
			}
			
			try {
				String[] professionalValue = req.getParameterValues("professional");
				String[] professionalValueNumber = req.getParameterValues("professionalValue");
				if (professionalValue.length > 0) {
					for (int i = 0; i < professionalValue.length; i++) {
						competences.add(new Competence(professionalValue[i], Double.parseDouble(professionalValueNumber[i])/100));
					}
				}
			} catch (Exception e) {
			}
			
			try {
				String[] languageValue = req.getParameterValues("languages");
				String[] languageValueNumber = req.getParameterValues("languagesValue");
				if (languageValue.length > 0) {
					for (int i = 0; i < languageValue.length; i++) {
						competences.add(new Competence(languageValue[i], Double.parseDouble(languageValueNumber[i])/100));
					}
				}
			} catch (Exception e) {
			}
			
			try {
				String[] languageValue = req.getParameterValues("language");
				int length = languageValue.length;
				if (length > 0) {
					for (int i = 0; i < length; i++) {
						competences.add(new Competence(languageValue[i], 0.8));
						//VALOR DE LA LENGUA MATERNA
					}
				}
			} catch (Exception e) {
			}

			/*
			 * dbm.updatePersonAddressById(dbm.connectToStardog("myDb"),
			 * req.getSession().getId(), req.getParameter("Telefono"));
			 * dbm.updatePersonPostalById(dbm.connectToStardog("myDb"),
			 * req.getSession().getId(), req.getParameter("Telefono"));
			 */
			 
			response(resp, func.getEstablishedCompetencePunctuationForHTML(
					func.separateIntoLists(dbm.findPeopleByCompetences(
							dbm.connectToStardog("myDb"), competences)), competences));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}

	private void response(HttpServletResponse resp, Vector <Recommended> vec)
			throws IOException {
		PrintWriter out = resp.getWriter();
		Iterator<Recommended> it = vec.iterator();
		out.println("<html>");
		out.println("<body>");
		while(it.hasNext()){
			Recommended next = it.next();
			out.println("<p>" + next.getKey() + ": " + next.getPunct() + "</p>");
		}
		out.println("</body>");
		out.println("</html>");
	}
	
//	public void loadClasses(boolean dynamic){
//		try{
//			if(dynamic)
//				dbm.showClasses(dbm.connectToStardog("myDb"), true);
//			else 
//				dbm.showClasses(dbm.connectToStardog("myDb"), false);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String []args){
		System.out.println("just for building");
	}
}