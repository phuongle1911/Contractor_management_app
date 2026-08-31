package com.contractormanagement.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "password_hash")
    private String password_hash;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;



}
