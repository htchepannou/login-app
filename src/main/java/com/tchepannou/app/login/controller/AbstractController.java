package com.tchepannou.app.login.controller;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.exception.AuthenticationException;
import com.tchepannou.app.login.exception.NotFoundException;
import com.tchepannou.core.client.v1.ErrorResponse;
import com.tchepannou.core.http.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.List;

public class AbstractController {
    //-- Exception Handler
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse validationError(final MethodArgumentNotValidException ex, final HttpServletRequest request) {
        getLogger().error("[] = Validation error", request.getRequestURI(), ex);

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), fieldErrors.get(0).getDefaultMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse serverError(final Exception exception, final HttpServletRequest request) {
        getLogger().error("{} - Unexpected error.", request.getRequestURI(), exception);

        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse authError(final AuthenticationException exception, final HttpServletRequest request) {
        getLogger().error("{} - Unable to authenticate the user", request.getRequestURI(), exception);

        return createErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse notFoundError(final NotFoundException exception, final HttpServletRequest request) {
        getLogger().error("{} - Not found", request.getRequestURI(), exception);

        return createErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ExceptionHandler(HTTPException.class)
    public ErrorResponse httpError(final HTTPException exception, final HttpServletRequest request) {
        getLogger().error("{} - Downstream error", request.getRequestURI(), exception);

        return createErrorResponse(HttpStatus.CONFLICT.value(), Constants.ERROR_IO, request);
    }

    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ExceptionHandler(IOException.class)
    public ErrorResponse ioError(final HTTPException exception, final HttpServletRequest request) {
        getLogger().error("{} - IO error", request.getRequestURI(), exception);

        return createErrorResponse(HttpStatus.CONFLICT.value(), Constants.ERROR_IO, request);
    }

    //-- Protected
    protected ErrorResponse createErrorResponse(int code, String text, HttpServletRequest request){
        return new ErrorResponse()
                .withCode(code)
                .withText(text)
                .withAccessTokenId(request.getHeader(Http.HEADER_ACCESS_TOKEN))
                .withTransactionId(request.getHeader(Http.HEADER_TRANSACTION_ID))
        ;
    }

    protected Logger getLogger (){
        return LoggerFactory.getLogger(getClass());
    }
}
