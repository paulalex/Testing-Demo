package uk.gov.hscic.repository;

import org.springframework.stereotype.Repository;

import uk.gov.hscic.exception.PersonNotFoundException;
import uk.gov.hscic.model.Person;

@Repository
public class MockitoRepository {
	
	public Person getPerson() {
		Person person = new Person();
		
		person.setName("John Smith");
		person.setAge(30);
		
		return person;
	}
	
	public Person getPersonByName(String name) throws PersonNotFoundException{
		if(name.equalsIgnoreCase("John") || name.equalsIgnoreCase("Mike")) {
			Person person = new Person();
			
			person.setName(name);
			person.setAge(30);
			
			return person;
		} else {
			throw new PersonNotFoundException();
		}
	}
}
