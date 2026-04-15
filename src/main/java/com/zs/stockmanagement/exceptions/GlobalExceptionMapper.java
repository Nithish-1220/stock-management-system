package com.zs.stockmanagement.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<AppException> {

    @Override
    public Response toResponse(AppException ex) {
        return Response.status(ex.getStatusCode())
                .entity(ex.getMessage())
                .build();
    }

}