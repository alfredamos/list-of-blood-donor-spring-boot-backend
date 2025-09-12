package com.alfredamos.listofblooddonorspringbootbackend.repositories;

import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthRepository extends JpaRepository<User, UUID> {
//    @Transactional(readOnly = true)
    User findUserByEmail(String email);
}
