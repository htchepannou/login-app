package com.tchepannou.app.login.service.blog;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.service.CommandContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class GetTeamPostsCommand extends AbstractSearchPostCommand {
    //-- AbstractSecuredCommand overrides
    @Override
    protected String getMetricName() {
        return Constants.METRIC_MY_DASHBOARD;
    }

    @Override
    protected List<Long> getTeamIds (CommandContext context) throws IOException {
        return Collections.singletonList(context.getId());

    }
}
