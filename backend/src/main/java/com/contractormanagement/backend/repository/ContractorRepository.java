package com.contractormanagement.backend.repository;

import com.contractormanagement.backend.entity.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorRepository extends JpaRepository<Contractor, Long> {
}
