package com.fronteers.controllers;

import com.fronteers.brightlife.api.AuthenticationApi;
import com.fronteers.brightlife.model.JwtAuthenticationResponse;
import com.fronteers.brightlife.model.SignInRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fronteers.services.AuthenticationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class AuthenticationController implements AuthenticationApi {

  private final AuthenticationService authenticationService;

//  @PostMapping("/signup")
//  // public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
//  public ApiResponse signup(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {
//    InputValidator.validate(bindingResult, request.getEmail());
//    return authenticationService.signup(request);
//  }


  @Override
  public ResponseEntity<JwtAuthenticationResponse> login(SignInRequest request){
    return ResponseEntity.ok(authenticationService.signin(request));
  }
}
