package com.alfredamos.listofblooddonorspringbootbackend.repositories;

import com.alfredamos.listofblooddonorspringbootbackend.entities.BloodStat;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BloodStatRepository extends JpaRepository<BloodStat, UUID> {
    List<BloodStat> findBloodStatByUser(User user);
    List<BloodStat> deleteBloodStatsByUser(User user);

    //void deleteAll();

}
