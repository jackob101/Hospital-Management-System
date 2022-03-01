package com.jackob101.hms.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @Min(value = 0, groups = OnUpdate.class)
    @NotNull(message = "ID cannot be null", groups = OnUpdate.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "user_credentials_id", unique = true)
    private String userCredentialsId;

    @Size(max = 200)
    @NotBlank
    @Column(name = "pesel", unique = true)
    private String pesel;

    @Size(max = 200)
    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 200)
    @NotBlank
    @Column(name = "second_name")
    private String secondName;

    @Size(max = 200)
    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 200)
    @NotBlank
    @Column(name = "phone_number")
    private String phoneNumber;

}
