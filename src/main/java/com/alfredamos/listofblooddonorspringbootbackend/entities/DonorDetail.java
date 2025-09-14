package com.alfredamos.listofblooddonorspringbootbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DonorDetails")
public class DonorDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Double volumePerDonation;

    @Column(nullable = false)
    private Integer numberOfDonations;

    @Column(nullable = false)
    private DonoType Type;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
