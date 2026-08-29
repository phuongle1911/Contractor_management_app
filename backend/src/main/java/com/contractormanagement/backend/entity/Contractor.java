package com.contractormanagement.backend.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contractors")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(name = "contractor_name", nullable = false, length = 200)
    private String contractorName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @NotBlank
    @Size(max = 150)
    @Column(name = "service", nullable = false, length = 150)
    private String service;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    @Digits(integer = 1, fraction = 2)
    @Column(name = "external_review_rating", precision = 3, scale = 2)
    private BigDecimal externalReviewRating;

    @Size(max = 2_000)
    @Column(name = "external_review_summary", length = 2_000)
    private String externalReviewSummary;

    @Email
    @Size(max = 320)
    @Column(name = "email", unique = true, length = 320)
    private String email;

    @Size(max = 32)
    @Column(name = "phone_number", length = 32)
    private String phoneNumber;

    @Size(max = 2_048)
    @Column(name = "website_url", length = 2_048)
    private String websiteUrl;

    @Pattern(regexp = "\\d{11}", message = "ABN must contain exactly 11 digits")
    @Column(name = "abn", unique = true, length = 11)
    private String abn;

    public Contractor(String contractorName, String location, String service) {
        this.contractorName = contractorName;
        this.location = location;
        this.service = service;
    }
}
