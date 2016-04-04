package uk.gov.hscic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.exception.InvalidAgeException;
import uk.gov.hscic.exception.PersonNotFoundException;
import uk.gov.hscic.model.Person;
import uk.gov.hscic.repository.MockitoRepository;

@Service
public class MockitoService {
	@Autowired
	private MockitoRepository repository;
	
	public Person fetchPerson() {
		return repository.getPerson();
	}
	
	public int calculatePersonAgeInDays(Person person) throws InvalidAgeException {
		System.out.println("Person calculating age for is " + person.getName());
		if(person.getAge() == null || person.getAge() == 0) {
			throw new InvalidAgeException();
		}	
		
		return person.getAge() * 365;
	}
	
	public Person fetchPersonByName(String name) throws PersonNotFoundException{
		return repository.getPersonByName(name);
	}
}
