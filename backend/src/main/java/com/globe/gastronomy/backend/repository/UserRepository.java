package com.globe.gastronomy.backend.repository;

import com.globe.gastronomy.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findUserByFirstNameAndLastName(String firstName,String lastName);

    Optional<User> findUserByFirstNameAndLastNameAndEmail(String firstName, String lastName,String email);

}
