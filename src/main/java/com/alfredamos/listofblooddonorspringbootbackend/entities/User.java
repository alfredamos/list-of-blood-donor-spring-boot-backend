package com.alfredamos.listofblooddonorspringbootbackend.entities;

import com.alfredamos.listofblooddonorspringbootbackend.validations.ValueOfEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Address address;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @ValueOfEnum(enumClass = Gender.class)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dateOfBirth;

    @Column(nullable = true)
    private Integer age;

    @ValueOfEnum(enumClass = Role.class)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Vital> vitals;

    @OneToMany(mappedBy = "user")
    private List<DonorDetail> donorDetails;

}
