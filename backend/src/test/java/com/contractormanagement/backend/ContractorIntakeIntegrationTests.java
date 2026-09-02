package com.contractormanagement.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import com.contractormanagement.backend.entity.Contractor;
import com.contractormanagement.backend.repository.ContractorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ContractorIntakeIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContractorRepository contractorRepository;

    @Test
    void completesContractorIntakeForThreeContractors() throws Exception {
        long initialContractorCount = contractorRepository.count();

        createContractor("Bright Spark Electrical", "bright-spark@example.com", "11111111111");
        createContractor("Reliable Plumbing Co", "reliable-plumbing@example.com", "22222222222");
        createContractor("Southern Build Group", "southern-build@example.com", "33333333333");

        Contractor contractor = contractorRepository.findAll().stream()
                .filter(candidate -> "bright-spark@example.com".equals(candidate.getEmail()))
                .findFirst()
                .orElseThrow();

        mockMvc.perform(put("/api/contractors/{id}/services", contractor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                ["electrical", "solar"]
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/contractors/{id}/documents", contractor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fileName": "electrical-licence.pdf",
                                  "versionNumber": 1
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/contractors/{id}/checklist", contractor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "insuranceVerified": true,
                                  "safetyInductionComplete": true
                                }
                                """))
                .andExpect(status().isOk());

        Contractor storedContractor = contractorRepository.findById(contractor.getId())
                .orElseThrow();

        assertEquals(initialContractorCount + 3, contractorRepository.count());
        assertEquals(Set.of("electrical", "solar"), storedContractor.getServices());
        assertEquals(true, storedContractor.getChecklist().get("insuranceVerified"));
        assertEquals(1, storedContractor.getDocuments().size());
        assertEquals(
                "electrical-licence.pdf",
                storedContractor.getDocuments().iterator().next().getFileName());
    }

    private void createContractor(String name, String email, String abn) throws Exception {
        mockMvc.perform(post("/api/contractors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "contractorName": "%s",
                                  "location": "Melbourne VIC",
                                  "email": "%s",
                                  "abn": "%s"
                                }
                                """.formatted(name, email, abn)))
                .andExpect(status().isCreated());
    }
}
