package com.jackob101.hms.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonIgnoreProperties("hibernateLazyInitializer")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class UserDetails implements IEntity {

    @NotNull(message = "ID cannot be null", groups = {OnUpdate.class})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
