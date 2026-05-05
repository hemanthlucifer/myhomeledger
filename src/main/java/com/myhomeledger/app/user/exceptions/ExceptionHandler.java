package com.myhomeledger.app.user.exceptions;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(basePackages = "com.myhomeledger.app.user")
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserProcessException.class)
    public ProblemDetail handleUserProcessException(UserProcessException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(400);
        problemDetail.setTitle("User Process Error");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(404);
        problemDetail.setTitle("User Not Found");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

}
