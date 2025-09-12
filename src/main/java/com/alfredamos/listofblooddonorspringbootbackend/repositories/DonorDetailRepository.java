package com.alfredamos.listofblooddonorspringbootbackend.repositories;

import com.alfredamos.listofblooddonorspringbootbackend.entities.DonorDetail;
import com.alfredamos.listofblooddonorspringbootbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DonorDetailRepository extends JpaRepository<DonorDetail, UUID> {
    List<DonorDetail> findDonorDetailByUser(User user);
    void deleteDonorDetailByUser(User user);
}