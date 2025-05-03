package com.fronteers.services;

import com.fronteers.brightlife.model.PasswordUpdate;
import com.fronteers.brightlife.model.Success;
import com.fronteers.brightlife.model.UserDto;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.User;
import com.fronteers.repositories.UserRepository;
import com.fronteers.utils.CopyBeanUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {

      @Override
      public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
            .orElseThrow(() -> new BadRequestException(
                String.format("User with email %s not found", username)));
      }
    };
  }

  public User save(User newUser) {
    if (newUser.getId() == null) {
      newUser.setCreatedAt(LocalDateTime.now());
    }
    newUser.setUpdatedAt(LocalDateTime.now());
    return userRepository.save(newUser);
  }

  public Success changePassword(Long userId, PasswordUpdate request) {
    User user = fetchUserById(userId);
    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new BadRequestException("Old password is incorrect");
    }
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new BadRequestException("New password and confirm password do not match");
    }
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
    return new Success(true, "Password Update Successful", "User Password Has Been Changed Successfully");
  }

  public UserDto fetchUser(Long userId) {
    User user = fetchUserById(userId);
    UserDto dto = new UserDto();
    dto.setEmail(user.getEmail());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.setMiddleName(user.getMiddleName());
    return dto;
  }

  public Success updateProfile(Long userId, UserDto request) {
    User existingUser = fetchUserById(userId);
    if (request.getEmail() != null) existingUser.setEmail(request.getEmail());
    if (request.getFirstName() != null) existingUser.setFirstName(request.getFirstName());
    if (request.getLastName() != null) existingUser.setLastName(request.getLastName());
    if (request.getMiddleName() != null) existingUser.setMiddleName(request.getMiddleName());
    existingUser = userRepository.save(existingUser);
    return new Success(true, "Profile Update Successful", "User Profile Has Been Updated Successfully");
  }

  private User fetchUserById(Long userId){
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }
}
