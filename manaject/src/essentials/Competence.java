package essentials;


public class Competence {
	
	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public double getPunctuation() {
		return punctuation;
	}

	public void setPunctuation(int punctuation) {
		this.punctuation = punctuation;
	}

	private String comp;
	private double punctuation;

	public Competence(String comp, double punctuation){
		this.comp = comp;
		this.punctuation = punctuation;
	}
	
	
}
/*
 *Añadir competencias esperadas
 *Matchear las mismas competencias con diferentes nombres siempre que aparezcan nuevas
 */