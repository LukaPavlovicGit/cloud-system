package com.example.usermanagement.repositories;

import com.example.usermanagement.data.entities.User;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepositoryImplementation<User, Long> {

    Optional<User> findByEmail(String email);
}
