package uk.gov.hscic.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Supplied Age Parameter is Invalid")
public class InvalidAgeException extends Exception {

}
