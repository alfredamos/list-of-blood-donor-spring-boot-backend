package com.alfredamos.listofblooddonorspringbootbackend.repositories;

import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByEmail(String email);
}
