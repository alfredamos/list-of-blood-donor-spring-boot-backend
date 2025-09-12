package com.alfredamos.listofblooddonorspringbootbackend.repositories;

import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import com.alfredamos.listofblooddonorspringbootbackend.entities.Vital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VitalRepository extends JpaRepository<Vital, UUID> {
    List<Vital> findVitalByUser(User user);

    void deleteVitalByUser(User user);
}
