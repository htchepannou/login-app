package com.tchepannou.app.login.controller;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.client.v1.profile.AppProfileResponse;
import com.tchepannou.app.login.service.profile.GetProfileCommand;
import com.tchepannou.core.http.Http;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Api(basePath = "/v1/app/profile", value = "Manage User Profile", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/app/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController extends AbstractController {
    @Autowired
    private GetProfileCommand getProfileCommand;

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("Returns user profile")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = Constants.ERROR_NOT_FOUND),
            @ApiResponse(code = 401, message = Constants.ERROR_AUTH_FAILED),
    })
    public AppProfileResponse me(
            @RequestHeader(value= Http.HEADER_ACCESS_TOKEN) String accessToken
    ) throws IOException {
        return getProfileCommand.execute(null,
                new CommandContextImpl().withAccessTokenId(accessToken)
        );
    }
}
