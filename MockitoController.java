package uk.gov.hscic.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.gov.hscic.exception.InvalidAgeException;
import uk.gov.hscic.exception.PersonNotFoundException;
import uk.gov.hscic.model.Person;
import uk.gov.hscic.service.MockitoService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MockitoController {
	
	private static final Logger logger = LoggerFactory.getLogger(MockitoController.class);
	
	@Autowired
	private MockitoService mockitoService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", 
			method = RequestMethod.GET, 
			produces = MediaType.TEXT_HTML_VALUE)
	public String home(Locale locale, Model model) {				
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		System.out.println("calling home controller");
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate);
		
		return "home";
	}
	
	@RequestMapping(value = "/person", 
			method = RequestMethod.GET,
			produces = MediaType.TEXT_HTML_VALUE)
	public String person(Model model) {				
		Person person = mockitoService.fetchPerson();
		
		model.addAttribute("person", person);
		
		return "person";
	}
	
	@RequestMapping(value = "/person_age_in_days", 
			method = RequestMethod.GET,
			produces = MediaType.TEXT_HTML_VALUE)
	public String personAgeInDays(Model model) throws InvalidAgeException {				
		Person person = mockitoService.fetchPerson();
		
		System.out.println("Person: " + person.getName() + " : " + person.getAge());
		
		int age = mockitoService.calculatePersonAgeInDays(person);
		System.out.println("Age is: " + age);
		model.addAttribute("age", age);
		System.out.println("CALLING THE PERSON AGE IN DAYS CONTROLLER METHOD");
		return "person_age_days";
	}
	
	@RequestMapping(value = "/person/{name}", 
			method = RequestMethod.GET,
			produces = MediaType.TEXT_HTML_VALUE)
	public String getPersonByName(Model model, @PathVariable String name) throws PersonNotFoundException {				
		Person person = mockitoService.fetchPersonByName(name);
		
		model.addAttribute("person", person);
		
		return "person_name";
	}
	
	@RequestMapping(value = "/person/{name}", 
			method = RequestMethod.GET,			
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Person getPersonAsJson(@PathVariable String name) throws PersonNotFoundException {				
		Person person = mockitoService.fetchPersonByName(name);
						
		return person;
	}
}
