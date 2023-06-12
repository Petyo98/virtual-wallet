package com.example.virtualwallet.configuration;

import com.example.virtualwallet.controllers.UserController;
import com.example.virtualwallet.exceptions.InvalidCardInputException;
import com.example.virtualwallet.exceptions.InvalidUserInputException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackageClasses = {UserController.class})
@Order(1)
public class ExceptionHandlerConfig {

    @ExceptionHandler({InvalidUserInputException.class, InvalidCardInputException.class})
    public ModelAndView handleEntityNotFoundException(Exception e) {
        ModelAndView model = new ModelAndView();
        model.addObject("errorMessage", e.getMessage());
        model.setViewName("error");
        return model;
    }
}
