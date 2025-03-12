package com.fronteers.controllers;

import com.fronteers.brightlife.model.Error;
import com.fronteers.models.constant.ErrorCode;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      org.springframework.security.core.AuthenticationException authException) throws IOException {
    logger.info("CustomAuthenticationEntryPoint invoked");
    Error errorResponse = new Error();
    errorResponse.setMessage("Unauthorized - Please login");
    errorResponse.setError(ErrorCode.AUTHENTICATION.name());
    errorResponse.setStatus(false);

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
  }
}
