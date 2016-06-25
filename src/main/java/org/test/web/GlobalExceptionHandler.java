package org.test.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * One point to handle all errors in the application.
 *
 * @author Myroslav Rudnytskyi
 * @version 26.06.2016
 */
@ControllerAdvice("org.test")
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public String handleBadRequest(HttpServletRequest req, Exception ex) {
		return ex.getLocalizedMessage();
	}
}
