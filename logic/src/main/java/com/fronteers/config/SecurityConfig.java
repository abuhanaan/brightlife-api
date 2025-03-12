package com.fronteers.config;

import com.fronteers.controllers.CustomAccessDeniedHandler;
import com.fronteers.controllers.CustomAuthenticationEntryPoint;
import com.fronteers.filters.JwtAuthenticationFilter;
import com.fronteers.services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;
  private final CustomAccessDeniedHandler accessDeniedHandler;

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService.userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  // @Bean
  // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
  // Exception {
  // http
  // .csrf(csrf -> csrf.disable())
  // .sessionManagement(session -> session
  // .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
  // .authorizeHttpRequests(authorize -> authorize
  // .requestMatchers(HttpMethod.POST, "/api/v1/signup",
  // "/api/v1/signin")
  // .permitAll()
  // .requestMatchers(HttpMethod.GET, "/api/v1/test/**").permitAll()
  // .anyRequest().authenticated())
  // .authenticationProvider(authenticationProvider())
  // .addFilterBefore(jwtAuthenticationFilter,
  // UsernamePasswordAuthenticationFilter.class);

  // return http.build();
  // }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, "/api/v1/signup",
                "/api/v1/signin")
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/test/**", "/v3/api-docs/**", "/v3/api-docs",
                "/swagger-ui/**", "/swagger-ui/index.html", "/swagger-ui.html", "/brightlife_api_contract.yaml")
            .permitAll()
            .anyRequest().authenticated())
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
