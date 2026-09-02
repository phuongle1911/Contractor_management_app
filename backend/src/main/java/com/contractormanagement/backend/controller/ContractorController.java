package com.contractormanagement.backend.controller;

import java.util.Map;
import java.util.Set;

import com.contractormanagement.backend.entity.Contractor;
import com.contractormanagement.backend.entity.ContractorDocument;
import com.contractormanagement.backend.repository.ContractorRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/contractors")
@Validated
@Transactional
public class ContractorController {

    private final ContractorRepository contractorRepository;

    public ContractorController(ContractorRepository contractorRepository) {
        this.contractorRepository = contractorRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contractor createContractor(@Valid @RequestBody Contractor contractor) {
        return contractorRepository.save(contractor);
    }

    @PutMapping("/{id}/services")
    public void replaceServices(
            @PathVariable Long id,
            @NotEmpty @RequestBody Set<@NotBlank @Size(max = 150) String> services) {
        Contractor contractor = findContractor(id);
        contractor.replaceServices(services);

        contractorRepository.save(contractor);
    }

    @PostMapping("/{id}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    public ContractorDocument addDocument(
            @PathVariable Long id,
            @Valid @RequestBody ContractorDocument document) {
        Contractor contractor = findContractor(id);
        contractor.addDocument(document);
        contractorRepository.save(contractor);

        return document;
    }

    @PutMapping("/{id}/checklist")
    public void replaceChecklist(
            @PathVariable Long id,
            @NotEmpty @RequestBody Map<String, Object> checklist) {
        Contractor contractor = findContractor(id);
        contractor.replaceChecklist(checklist);

        contractorRepository.save(contractor);
    }

    private Contractor findContractor(Long id) {
        return contractorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Contractor " + id + " was not found"));
    }
}
