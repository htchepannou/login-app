package com.tchepannou.app.login.controller;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.client.v1.blog.AppPostCollectionResponse;
import com.tchepannou.app.login.service.blog.GetTeamPostsCommand;
import com.tchepannou.app.login.service.blog.GetMyPostsCommand;
import com.tchepannou.core.http.Http;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Api(basePath = "/v1/app/blog", value = "Blog", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value="/v1/app/blog", produces = MediaType.APPLICATION_JSON_VALUE)
public class BlogController extends AbstractController {
    @Autowired
    private GetMyPostsCommand getMyPostsCommand;

    @Autowired
    private GetTeamPostsCommand getTeamPostsCommand;

    //-- REST methods
    @RequestMapping(method = RequestMethod.GET, value="/my-posts")
    @ApiOperation("Returns user posts")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = Constants.ERROR_AUTH_FAILED),
    })
    public AppPostCollectionResponse me(
            @RequestHeader(value= Http.HEADER_ACCESS_TOKEN) String accessToken,
            @RequestParam(defaultValue = "30") int limit,
            @RequestHeader(defaultValue = "0") int offset
    ) throws IOException {
        return getMyPostsCommand.execute(null,
                new CommandContextImpl()
                        .withAccessTokenId(accessToken)
                        .withLimit(limit)
                        .withOffset(offset)
        );
    }

    @RequestMapping(method = RequestMethod.GET, value="/{teamId}/posts")
    @ApiOperation("Returns team posts")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = Constants.ERROR_AUTH_FAILED),
    })
    public AppPostCollectionResponse team(
            @RequestHeader(value= Http.HEADER_ACCESS_TOKEN) String accessToken,
            @PathVariable long teamId,
            @RequestParam(defaultValue = "30") int limit,
            @RequestHeader(defaultValue = "0") int offset
    ) throws IOException {
        return getTeamPostsCommand.execute(null,
                new CommandContextImpl()
                        .withAccessTokenId(accessToken)
                        .withId(teamId)
                        .withLimit(limit)
                        .withOffset(offset)
        );
    }

}
