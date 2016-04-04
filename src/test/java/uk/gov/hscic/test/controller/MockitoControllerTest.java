package uk.gov.hscic.test.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import uk.gov.hscic.controller.MockitoController;
import uk.gov.hscic.exception.InvalidAgeException;
import uk.gov.hscic.exception.PersonNotFoundException;
import uk.gov.hscic.model.Person;
import uk.gov.hscic.service.MockitoService;

@RunWith(SpringJUnit4ClassRunner.class)	
@ContextConfiguration(locations = {"classpath:root-context.xml", "classpath:servlet-context.xml"})
@WebAppConfiguration
public class MockitoControllerTest {
		     	   
	    @Mock
	    private MockitoService mockitoService;
	     
	    @InjectMocks		    
	    private MockitoController mockitoController;
	    
	    private MockMvc mockMvc;
	    
	    /**
	     * 
	     * Before each unit test is started reset the mocks and
	     * reinitialise the mock controller
	     * 
	     */
	    @Before
	    public void setUp() {
	        MockitoAnnotations.initMocks(this);
	        	        
	        //Set up the internal view resolver as we are using standalone set
	        //up and not passing in the reference to the web application context
	        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
	        viewResolver.setPrefix("/WEB-INF/views/");
	        viewResolver.setSuffix(".jsp");
	        
	        mockMvc = MockMvcBuilders.standaloneSetup(mockitoController).setViewResolvers(viewResolver).build();
	    }
	    
	    
	    /**
	     * Make a valid call to the url
	     * check the expected http result code
	     * check the model attribute exists
	     * check the view name the controller returns
	     * check the forwarded url that the view resolver returns
	     * Verify there are no more interactions with the services
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void testHomePageWithValidTest() throws Exception {	         
	    	Assert.assertNotNull(mockitoController);
	    	
	    	//Perform HTTP Get for the homepage
	    	mockMvc.perform(get("/").accept(MediaType.TEXT_HTML))	    	
	    	.andExpect(status().isOk())
	    	.andExpect(model().attributeExists("serverTime"))	    	
	    	.andExpect(view().name("home"))
	    	.andExpect(forwardedUrl("/WEB-INF/views/home.jsp"));	    		    		      
	    }	
	    
	    /**
	     * Call a valid url with an invalid HTTP method
	     * Expect that a method not allowed response code is returned
	     * Verify there are no more interactions with the services
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void testHomePageWithPost() throws Exception {	         
	    	Assert.assertNotNull(mockitoController);
	    	
	    	//Perform HTTP POST for the homepage
	    	mockMvc.perform(post("/"))	    	
	    	.andExpect(status().isMethodNotAllowed());	    	    	    		    		     
	    }	
	    	   	   
	    /**
	     * Call the retrieve persons controller method
	     * Mock the return of the service
	     * expect 200 response code
	     * expect person to be present in the model
	     * expect the person object to remain unaltered
	     * check the view name the controller returns
	     * check the forwarded url that the view resolver returns
	     * Verify there are no more interactions with the services
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void testPersonPageWithValidTest() throws Exception {	
	    	Assert.assertNotNull(mockitoController);
	    	
	    	//Create expected result
	    	Person expectedPerson = new Person();			
	    	expectedPerson.setName("John Smith");
	    	expectedPerson.setAge(30);
	    	when(mockitoService.fetchPerson()).thenReturn(expectedPerson);
	    		    	
	    	mockMvc.perform(get("/person").accept(MediaType.TEXT_HTML))	    	
	    	.andExpect(status().isOk())
	    	.andExpect(model().attributeExists("person"))
	    	.andExpect(model().attribute("person", expectedPerson))
	    	.andExpect(view().name("person"))
	    	.andExpect(forwardedUrl("/WEB-INF/views/person.jsp"));	    
	    	
	    	//Verify that the service method was only called one time
	    	verify(mockitoService, times(1)).fetchPerson();
	    }	
	    
	    /**
	     * Mock calls to fetchPerson and calculatePersonAgeInDays
	     * in the MockitoService
	     * expect 200 response code
	     * expect person age in days to be present in the model
	     * expect the person age variable to remain unaltered
	     * check the view name the controller returns
	     * check the forwarded url that the view resolver returns
	     * Verify there are no more interactions with the services
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void testPersonAgeInDays() throws Exception {	
	    	Assert.assertNotNull(mockitoController);
	    	
	    	//Create expected result
	    	Person expectedPerson = new Person();			
	    	expectedPerson.setName("John Smith");
	    	expectedPerson.setAge(30);
	    	when(mockitoService.fetchPerson()).thenReturn(expectedPerson);
	    	
	    	int expectedAgeInDays = 100;
	    	when(mockitoService.calculatePersonAgeInDays(any(Person.class))).thenReturn(100);
	    		    	
	    	mockMvc.perform(get("/person_age_in_days").accept(MediaType.TEXT_HTML))	    	
	    	.andExpect(status().isOk())
	    	.andExpect(model().attributeExists("age"))
	    	.andExpect(model().attribute("age", expectedAgeInDays))
	    	.andExpect(view().name("person_age_days"))
	    	.andExpect(forwardedUrl("/WEB-INF/views/person_age_days.jsp"));	    
	    	
	    	//Verify that the service methods were only called one time
	    	verify(mockitoService, times(1)).fetchPerson();
	    	verify(mockitoService, times(1)).calculatePersonAgeInDays(eq(expectedPerson));
	    }	
	    
	    /**
	     * Test when an InvalidAgeException
	     * is triggered in the controller
	     * 
	     * expect 400 response code
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void testInvalidAgeExceptionTriggeredInController() throws Exception {	
	    	Assert.assertNotNull(mockitoController);

	    	Person expectedToFail = new Person();
	    	expectedToFail.setName("Invalid Age");
	    	expectedToFail.setAge(0);
	    	
	    	when(mockitoService.fetchPerson()).thenReturn(expectedToFail);
	    	
	    	when(mockitoService.calculatePersonAgeInDays(eq(expectedToFail))).thenThrow(new InvalidAgeException());
	    	
	    	mockMvc.perform(get("/person_age_in_days").accept(MediaType.TEXT_HTML))
	    	.andExpect(status().isBadRequest());	    	
	    	   	    	
	    	//Verify that the service method was only called one time
	    	verify(mockitoService, times(1)).fetchPerson();
	    	verify(mockitoService, times(1)).calculatePersonAgeInDays(eq(expectedToFail));
	    }
	    
	    
	    /**
	     * Mock calls to fetchPersonByName
	     * expect 200 response code
	     * expect person to be present in the model
	     * expect the person object to remain unaltered
	     * check the view name the controller returns
	     * check the forwarded url that the view resolver returns
	     * Verify there are no more interactions with the services
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void testPersonByNameWithPathVariable() throws Exception {	
	    	Assert.assertNotNull(mockitoController);
	    	
	    	//Create expected result
	    	Person expectedPerson = new Person();			
	    	expectedPerson.setName("John Smith");
	    	expectedPerson.setAge(30);
	    	
	    	when(mockitoService.fetchPersonByName(anyString())).thenReturn(expectedPerson);	    		    	    	
	    		    	
	    	mockMvc.perform(get("/person/john").accept(MediaType.TEXT_HTML))	    	
	    	.andExpect(status().isOk())
	    	.andExpect(model().attributeExists("person"))
	    	.andExpect(model().attribute("person", expectedPerson))
	    	.andExpect(view().name("person_name"))
	    	.andExpect(forwardedUrl("/WEB-INF/views/person_name.jsp"));	    
	    	
	    	//Verify that the service method was only called one time
	    	verify(mockitoService, times(1)).fetchPersonByName(anyString());	    	
	    }
	    
	    /**
	     * Mock calls to fetchPersonByName
	     * expect 200 response code
	     * expect json response
	     * expect person to be returned as json
	     * expect the person object to remain unaltered
	     * Verify there are no more interactions with the services
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void testPersonByNameWithPathVariableReturnsJsonObject() throws Exception {	
	    	Assert.assertNotNull(mockitoController);
	    	
	    	//Create expected result
	    	Person expectedPerson = new Person();			
	    	expectedPerson.setName("John Smith");
	    	expectedPerson.setAge(30);
	    	
	    	when(mockitoService.fetchPersonByName(anyString())).thenReturn(expectedPerson);	    		    	    	
	    		    	
	    	mockMvc.perform(get("/person/john").accept(MediaType.APPLICATION_JSON))	    	
	    	.andExpect(status().isOk())
	    	.andExpect(jsonPath("$.name", equalTo("John Smith")))
	    	.andExpect(jsonPath("$.age", equalTo(30)));
	    	  	    	
	    	//Verify that the service method was only called one time
	    	verify(mockitoService, times(1)).fetchPersonByName(anyString());	    	
	    }
	    	    
	    /**
	     * Test when a person not found exception
	     * is triggered within the service layer
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void testPersonByNameThrowsPNFException() throws Exception {	
	    	Assert.assertNotNull(mockitoController);
	    		    		    
	    	when(mockitoService.fetchPersonByName(anyString())).thenThrow(new PersonNotFoundException());	    		    	    	
	    		    	
	    	mockMvc.perform(get("/person/john").accept(MediaType.TEXT_HTML))	    	
	    	.andExpect(status().isNotFound());
	    	   	    	
	    	//Verify that the service method was only called one time
	    	verify(mockitoService, times(1)).fetchPersonByName(anyString());	    	
	    }
	    
	    /**
	     * 
	     * General 404 test
	     * Will return tomcats error 404 page but in spring 3.x
	     * the dispatcher servlet does not throw exceptions
	     * this is handled by tomcat so not further testing of the response is
	     * possible
	     * 
	     */	    
	    @Test
	    public void testNonExistentUrl() throws Exception {	         
	    	Assert.assertNotNull(mockitoController);
	    	
	    	//Perform HTTP POST for the homepage
	    	mockMvc.perform(get("/test_for_404"))	    	
	    	.andExpect(status().isNotFound());	    	    	    	    		    		   
	    }	
}
