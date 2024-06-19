package com.lec.spring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class handle {

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleException(HttpServletRequest request, Throwable ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        return mav;
    }
}