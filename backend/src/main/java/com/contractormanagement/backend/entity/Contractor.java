package com.contractormanagement.backend.entity;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @NotNull
    @ElementCollection
    @CollectionTable(
            name = "contractor_services",
            joinColumns = @JoinColumn(name = "contractor_id", nullable = false))
    @Column(name = "service", nullable = false, length = 150)
    @Setter(AccessLevel.NONE)
    private Set<@NotBlank @Size(max = 150) String> services = new LinkedHashSet<>();

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

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "checklist", nullable = false, columnDefinition = "jsonb")
    @Setter(AccessLevel.NONE)
    private Map<String, Object> checklist = new LinkedHashMap<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contractor_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Set<ContractorDocument> documents = new LinkedHashSet<>();

    public Contractor(String contractorName, String location) {
        this.contractorName = contractorName;
        this.location = location;
    }

    public void replaceServices(Set<String> services) {
        this.services.clear();
        this.services.addAll(services);
    }

    public void replaceChecklist(Map<String, Object> checklist) {
        this.checklist.clear();
        this.checklist.putAll(checklist);
    }

    public void addDocument(ContractorDocument document) {
        this.documents.add(document);
    }
}
