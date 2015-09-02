package utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import essentials.*;
import lists.NewList;


public class Functions {

	    //the punctuation reached by someone calculated with the matrix
		@SuppressWarnings("rawtypes")
		public Recommended puntuationCalc (HashMap<String, Competence> hash)
		{
			Set set = hash.keySet();
			Iterator i = set.iterator();
			Recommended recommended = new Recommended("", 0.0);
			double calc;
		    while(i.hasNext()){
		    	Map.Entry m = (Map.Entry)i.next();
		    	calc = calculateSinglePerson(hash, (String)m.getKey());
		    	if(recommended.getPunct() >= calc)
		    		recommended = new Recommended((String)m.getKey(), calc);
		    }
			return recommended;			

		}
		//compares the competences to get if two competences refer to the same concept
		public boolean semanticComp ()
		{
			return false;
		}
		public void fillTheNewList (NewList newList)
		{
			
		}
		public void fillTheHash (HashMap<String, Competence> hash)
		{
			
		}
		@SuppressWarnings("rawtypes")
		public double calculateSinglePerson (HashMap<String, Competence> hash, String person)
		{
			Set set = hash.entrySet();
			Iterator i = set.iterator();
			double punct = 0.0;
		    while(i.hasNext()){
		    	Map.Entry m = (Map.Entry)i.next();
		    	if(m.getKey() == person )
		    		punct += (double)m.getValue();
		    }
			return punct;
		}

}
//la matriz para calcular las puntuaciones de las personas se van a generar para cada proyecto en concreto 