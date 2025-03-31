package com.example.userauthenticationservice.repos;

import com.example.userauthenticationservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByEmailAndPassword(String email, String password);

    Optional<User> findById(Long id);

}
