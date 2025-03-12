package com.fronteers.controllers;

import com.fronteers.brightlife.model.Error;
import com.fronteers.models.constant.ErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    Error errorResponse = new Error();
    errorResponse.setStatus(false);
    errorResponse.setError(ErrorCode.FORBIDDEN.name());
    errorResponse.setMessage("Forbidden - Insufficient permissions to access this resource");

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
  }
}
