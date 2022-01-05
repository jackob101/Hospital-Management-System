package com.jackob101.hms.model.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userDetails")
    private Employee employee;

    @OneToOne(mappedBy = "userDetails")
    private Patient patient;

    @Column(name = "user_credentials_id", unique = true)
    private String userCredentialsId;

    @Column(name = "pesel", unique = true)
    private String pesel;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

}
