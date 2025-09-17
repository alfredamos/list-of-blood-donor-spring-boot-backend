package com.alfredamos.listofblooddonorspringbootbackend.entities;

import com.alfredamos.listofblooddonorspringbootbackend.validations.ValueOfEnum;
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

    @ValueOfEnum(enumClass = Category.class)
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
