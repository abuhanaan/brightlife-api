package com.fronteers.services;

import com.fronteers.brightlife.model.JwtAuthenticationResponse;
import com.fronteers.brightlife.model.SignInRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fronteers.exceptions.AuthenticationException;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.models.constant.Role;
import com.fronteers.models.entity.User;
import com.fronteers.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

//  public JwtAuthenticationResponse signup( request) {
//    var user = User
//        .builder()
//        .firstName(request.getFirstName())
//        .lastName(request.getLastName())
//        .email(request.getEmail())
//        .password(passwordEncoder.encode(request.getPassword()))
//        .role(Role.ROLE_USER)
//        .build();
//
//    user = userService.save(user);
//    var jwt = jwtService.generateToken(user);
//    JwtAuthenticationResponse response = new JwtAuthenticationResponse();
//    response.setToken(jwt);
//    return response;
//  }

  public JwtAuthenticationResponse signin(SignInRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new AuthenticationException(String.format("User with email %s does not exist",
            request.getEmail())));

    if (!(passwordEncoder.matches(request.getPassword(), user.getPassword()))) {
      System.out.println("Throwing Password Exception");
      throw new BadRequestException("Invalid Password");
    }
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    var jwt = jwtService.generateToken(user);
    JwtAuthenticationResponse response = new JwtAuthenticationResponse();
    response.setToken(jwt);
    return response;
  }
}
