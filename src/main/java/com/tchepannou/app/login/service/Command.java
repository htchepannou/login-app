package com.tchepannou.app.login.service;

import java.io.IOException;

public interface Command<I, O> {
    O execute(I request, CommandContext context) throws IOException;
}
