package essentials;

import java.util.Vector;

public class Project {
	
	private String name;
	private Vector <Competence> comp;
	
	public Project(String name, Vector <Competence> comp){
		this.setName(name);
		this.setComp(comp);
	}

	public Vector <Competence> getComp() {
		return comp;
	}

	public void setComp(Vector <Competence> comp) {
		this.comp = comp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
