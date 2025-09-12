package com.alfredamos.listofblooddonorspringbootbackend.controllers;

import com.alfredamos.listofblooddonorspringbootbackend.dto.DonorDetailCreate;
import com.alfredamos.listofblooddonorspringbootbackend.dto.DonorDetailDto;
import com.alfredamos.listofblooddonorspringbootbackend.dto.DonorDetailUpdate;
import com.alfredamos.listofblooddonorspringbootbackend.services.DonorDetailService;
import com.alfredamos.listofblooddonorspringbootbackend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/donor-details")
public class DonorDetailController {
    private final DonorDetailService donorDetailService;

    @PostMapping
    public ResponseEntity<DonorDetailDto> createDonorDetail(DonorDetailCreate donorDetailRequest) {
        var donorDetailDto = donorDetailService.createDonorDetail(donorDetailRequest);

        return ResponseEntity.ok(donorDetailDto);
    }

    @GetMapping
    public ResponseEntity<List<DonorDetailDto>> getAllDonorDetails() {
        var allDonorDetails = donorDetailService.findAllDonorDetails();

        return ResponseEntity.ok(allDonorDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonorDetailDto> getDonorDetailById(@PathVariable(value = "id") UUID id) {
        var donorDetailDto = donorDetailService.findDonorDetailById(id);

        return ResponseEntity.ok(donorDetailDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteDonorDetailById(@PathVariable(value = "id") UUID id) {
        var responseMessage = donorDetailService.deleteDonorDetail(id);

        return ResponseEntity.ok(responseMessage);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DonorDetailDto> editDonorDetailById(@PathVariable(value = "id") UUID id, DonorDetailUpdate donorDetailRequest) {
        var donorDetail = donorDetailService.editDonorDetailById(id, donorDetailRequest);

        return ResponseEntity.ok(donorDetail);
    }

    @DeleteMapping("/delete-by-user-id/{userId}")
    public ResponseEntity<ResponseMessage> deleteDonorDetailByUserId(@PathVariable(value = "userId") UUID id) {
        var response = donorDetailService.deleteDonorDetailByUserId(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/all/delete-all")
    public ResponseEntity<ResponseMessage> deleteAllDonorDetails() {
        var response = donorDetailService.deleteAllDonorDetails();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-user-id/{userId}")
    public ResponseEntity<List<DonorDetailDto>> getDonorDetailByUserId(@PathVariable(value = "userId") UUID userId) {
        var donorDetailsDto = donorDetailService.findDonorDetailByUserId(userId);

        return ResponseEntity.ok(donorDetailsDto);
    }
}


