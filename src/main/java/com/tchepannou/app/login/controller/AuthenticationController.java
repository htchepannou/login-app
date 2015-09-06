package com.tchepannou.app.login.controller;

import com.tchepannou.app.login.client.v1.login.AppLoginRequest;
import com.tchepannou.app.login.client.v1.login.AppLoginResponse;
import com.tchepannou.app.login.exception.LoginException;
import com.tchepannou.app.login.service.login.LoginCommand;
import com.tchepannou.app.login.service.login.LogoutCommand;
import com.tchepannou.auth.client.v1.AuthConstants;
import com.tchepannou.core.client.v1.ErrorResponse;
import com.tchepannou.core.http.Http;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Api(basePath = "/v1/app/auth", value = "Authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/app/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController extends AbstractController {
    //-- Attributes
    @Autowired
    private LoginCommand loginCommand;

    @Autowired
    private LogoutCommand logoutCommand;


    //-- REST methods
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    @ApiOperation("Login")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 409, message = AuthConstants.ERROR_AUTH_FAILED),
    })
    public AppLoginResponse login(@Valid @RequestBody AppLoginRequest request) throws IOException {
        return loginCommand.execute(request,
                new CommandContextImpl()
        );
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    @ApiOperation("Logout")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
    })
    public void logout(@RequestHeader(Http.HEADER_ACCESS_TOKEN) String accessToken) throws IOException {
        logoutCommand.execute(null,
                new CommandContextImpl()
                        .withAccessTokenId(accessToken)
        );
    }

    //-- Error handler
    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ExceptionHandler(LoginException.class)
    public ErrorResponse loginError (LoginException exception, HttpServletRequest request){
        getLogger().error("Authentication failed", exception);

        return createErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage(), request);
    }
}
