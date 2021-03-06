package com.friendBook.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
@ControllerAdvice
public class GlobalExceptionCatcher {
	
	private static final String ERROR_MESSAGE = "Something went wrong please try again";
	@ExceptionHandler(value = { Exception.class, RuntimeException.class })
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("errorMessage", ERROR_MESSAGE);
		modelAndView.setViewName("error");
		return modelAndView;
	}
    @ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView notDefault(NoHandlerFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", ERROR_MESSAGE);
		modelAndView.setViewName("error");
		return modelAndView;
	}
}
