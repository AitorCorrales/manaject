package utils;

import java.util.Iterator;
import java.util.Vector;

import essentials.Person;
import essentials.Recommended;

public class PrintFunctions {
	
	public void printVector(Vector<String> l) {
		Iterator<String> it = l.iterator();
		while (it.hasNext()) {
			System.out.println("     " + it.next());
		}
		System.out.println("---------------------");
	}

	public void printPeopleVector(Vector<Person> person) {

		Iterator<Person> it = person.iterator();
		while (it.hasNext()) {
			Person p = it.next();
			System.out.println(p.getFullname());
			printVector(p.getCompetences());

		}
	}

	public void printRecommendedVector(Vector<Recommended> rec) {
		Iterator<Recommended> it = rec.iterator();
		while (it.hasNext()) {
			Recommended p = it.next();
			System.out.print(p.getKey() + ": ");
			System.out.println(p.getPunct());
		}
	}

}
