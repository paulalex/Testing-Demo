package uk.gov.hscic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Person Not Found")
public class PersonNotFoundException extends Exception {

}
