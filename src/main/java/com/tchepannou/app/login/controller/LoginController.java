package com.tchepannou.app.login.controller;

import com.tchepannou.app.login.client.v1.login.AppLoginRequest;
import com.tchepannou.app.login.client.v1.login.AppLoginResponse;
import com.tchepannou.app.login.exception.LoginException;
import com.tchepannou.app.login.service.login.LoginCommand;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.core.client.v1.ErrorResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
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
@Api(basePath = "/v1/app/login", value = "Greeting", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/app/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController extends AbstractController {
    //-- Attributes
    @Autowired
    private LoginCommand loginCommand;


    //-- REST methods
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Authenticate a user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 409, message = AuthConstants.ERROR_AUTH_FAILED),
    })
    public AppLoginResponse login(@Valid @RequestBody AppLoginRequest request) throws IOException {
        return loginCommand.execute(request,
                new CommandContextImpl()
        );
    }


    //-- Error handler
    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginException.class)
    public ErrorResponse authFailed (LoginException exception, HttpServletRequest request){
        getLogger().error("Authentication failed", exception);

        return createErrorResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), request);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse failure(final Exception exception, final HttpServletRequest request) {
        getLogger().error("Unexpected error", exception);

        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), request);
    }
}
