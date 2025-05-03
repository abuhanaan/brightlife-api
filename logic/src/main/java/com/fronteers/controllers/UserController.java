package com.fronteers.controllers;

import com.fronteers.brightlife.api.UserApi;
import com.fronteers.brightlife.model.PasswordUpdate;
import com.fronteers.brightlife.model.Success;
import com.fronteers.brightlife.model.UserDto;
import com.fronteers.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  public ResponseEntity<Success> updatePassword(Long userId, PasswordUpdate request){
    log.info("Updating User Password");
    Success response = userService.changePassword(userId, request);
    log.info("Password update response: {}", response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<UserDto> profile(Long userId){
    log.info("Fetching User Profile Details");
    UserDto response = userService.fetchUser(userId);
    log.info("User Profile Retrieved Successfully");
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> updateProfile(Long userId, UserDto request){
    log.info("Updating User Profile");
    Success response = userService.updateProfile(userId, request);
    log.info("User Profile Updated Successfully");
    return ResponseEntity.ok(response);
  }
}
