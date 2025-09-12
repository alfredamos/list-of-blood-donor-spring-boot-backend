package com.alfredamos.listofblooddonorspringbootbackend.repositories;

import com.alfredamos.listofblooddonorspringbootbackend.entities.BloodStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BloodStatRepository extends JpaRepository<BloodStat, UUID> {
}
