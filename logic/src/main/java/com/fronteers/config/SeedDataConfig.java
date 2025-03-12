package com.fronteers.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fronteers.models.constant.Role;
import com.fronteers.models.entity.User;
import com.fronteers.repositories.UserRepository;
import com.fronteers.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Override
  public void run(String... args) throws Exception {

    if (userRepository.count() == 0) {

      User admin = User
          .builder()
          .firstName("admin")
          .lastName("admin")
          .email("admin@admin.com")
          .password(passwordEncoder.encode("password"))
          .role(Role.ROLE_ADMIN)
          .build();

      User user = User
          .builder()
          .firstName("user")
          .lastName("user")
          .email("user@email.com")
          .password(passwordEncoder.encode("password"))
          .role(Role.ROLE_USER)
          .build();

      User user2 = User
          .builder()
          .firstName("user")
          .lastName("Mustopha")
          .email("adewunmi@gmail.com")
          .password(passwordEncoder.encode("password"))
          .role(Role.ROLE_USER)
          .build();

      userService.save(admin);
      userService.save(user);
      userService.save(user2);
      log.debug("created ADMIN user - {}", admin);
      log.debug("created NORMAL user - {}", user);
      log.debug("created 2nd NORMAL user - {}", user2);
    }
  }
}
