package com.tchepannou.app.login.controller;

import com.tchepannou.app.login.client.v1.AppLoginRequest;
import com.tchepannou.app.login.client.v1.AppLoginResponse;
import com.tchepannou.app.login.exception.LoginException;
import com.tchepannou.app.login.service.impl.LoginCommand;
import com.tchepannou.core.client.v1.ErrorResponse;
import com.tchepannou.core.http.Http;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Api(basePath = "/v1/login", value = "Greeting", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/app/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    //-- Attributes
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginCommand loginCommand;


    //-- REST methods
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Authenticate a user")
    public AppLoginResponse login(@Valid @RequestBody AppLoginRequest request) throws IOException {
        return loginCommand.execute(request);
    }


    //-- Error handler
    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginException.class)
    public ErrorResponse authFailed (LoginException exception, HttpServletRequest request){
        LOG.error("Authentication failed", exception);

        return createErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse failure(final Exception exception, final HttpServletRequest request) {
        LOG.error("Unexpected error", exception);

        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request);
    }


    protected ErrorResponse createErrorResponse(int code, String text, HttpServletRequest request){
        return new ErrorResponse()
                .withCode(code)
                .withText(text)
                .withAccessTokenId(request.getHeader(Http.HEADER_ACCESS_TOKEN))
                .withTransactionId(request.getHeader(Http.HEADER_TRANSACTION_ID));
    }
}
