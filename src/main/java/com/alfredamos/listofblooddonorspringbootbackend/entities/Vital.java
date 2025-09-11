package com.alfredamos.listofblooddonorspringbootbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Vitals")
public class Vital {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Double pressureUp;

    @Column(nullable = false)
    private Double pressureLow;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = true)
    private Double bmi;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
