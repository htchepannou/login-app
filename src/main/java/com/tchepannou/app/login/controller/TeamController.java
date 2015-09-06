package com.tchepannou.app.login.controller;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.client.v1.team.AppTeamCollectionResponse;
import com.tchepannou.app.login.service.team.MyTeamsCommand;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Api(basePath = "/v1/app/team", value = "Manage User Team", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/app/team", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamController extends AbstractController {
    @Autowired
    private MyTeamsCommand myTeamsCommand;

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("Returns user profile")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = Constants.ERROR_AUTH_FAILED),
    })
    public AppTeamCollectionResponse list(
            @RequestHeader(value= Http.HEADER_ACCESS_TOKEN) String accessToken,
            @RequestParam(value="limit", defaultValue="20") int limit,
            @RequestParam(value="offset", defaultValue="0") int offset
    ) throws IOException {
        return myTeamsCommand.execute(null,
                new CommandContextImpl()
                        .withAccessTokenId(accessToken)
                        .withLimit(limit)
                        .withOffset(offset)
        );
    }
}
