package com.spotlight.platform.userprofile.api.web.exceptionmappers;

import com.spotlight.platform.userprofile.api.core.exceptions.InvalidCommandException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidCommandExceptionMapper implements ExceptionMapper<InvalidCommandException> {
    @Override
    public Response toResponse(InvalidCommandException exception) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}

