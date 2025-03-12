package com.fronteers.repositories;

import com.fronteers.models.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  // User findByEmail(String email);

  User findOneByEmail(String email);
}
