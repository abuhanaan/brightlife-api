package com.fronteers.filters;

import java.io.IOException;

import com.fronteers.models.constant.ErrorCode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fronteers.services.JwtService;
import com.fronteers.services.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserService userService;

  // @Override
  // protected void doFilterInternal(HttpServletRequest request,
  // HttpServletResponse response,
  // FilterChain filterChain)
  // throws ServletException, IOException {
  // final String authHeader = request.getHeader("Authorization");
  // final String jwt;
  // final String userEmail;
  // if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader,
  // "Bearer ")) {
  // filterChain.doFilter(request, response);
  // return;
  // }
  // jwt = authHeader.substring(7);
  // log.debug("JWT - {}", jwt.toString());
  // userEmail = jwtService.extractUserName(jwt);
  // if (StringUtils.isNotEmpty(userEmail) &&
  // SecurityContextHolder.getContext().getAuthentication() == null) {
  // UserDetails userDetails =
  // userService.userDetailsService().loadUserByUsername(userEmail);
  // if (jwtService.isTokenValid(jwt, userDetails)) {
  // log.debug("User - {}", userDetails);
  // SecurityContext context = SecurityContextHolder.createEmptyContext();
  // UsernamePasswordAuthenticationToken authToken = new
  // UsernamePasswordAuthenticationToken(
  // userDetails, null, userDetails.getAuthorities());
  // authToken.setDetails(new
  // WebAuthenticationDetailsSource().buildDetails(request));
  // context.setAuthentication(authToken);
  // SecurityContextHolder.setContext(context);
  // }
  // }
  // filterChain.doFilter(request, response);
  // }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    log.debug("JWT - {}", jwt);

    try {
      userEmail = jwtService.extractUserName(jwt);
    } catch (ExpiredJwtException e) {
      log.error("Token has expired: {}", e.getMessage());
      handleException(response, ErrorCode.UNAUTHORIZED, "Token has expired, please login again",
          HttpServletResponse.SC_UNAUTHORIZED);
      return;
    } catch (Exception e) {
      log.error("Invalid token: {}", e.getMessage());
      handleException(response, ErrorCode.UNAUTHORIZED, "Invalid token", HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
      if (jwtService.isTokenValid(jwt, userDetails)) {
        log.debug("User - {}", userDetails);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
      }
    }

    filterChain.doFilter(request, response);
  }

  private void handleException(HttpServletResponse response, ErrorCode errorCode, String message, int status)
      throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.getWriter().write(
        String.format("{\"status\": false, \"error\": \"%s\", \"message\": \"%s\"}", errorCode, message));
  }
}
