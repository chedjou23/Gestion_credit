package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
  Optional<User> findByEmail(String email);
  User findByRole(Role role);
}
