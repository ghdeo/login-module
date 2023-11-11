package ghdeo.login.repository;

import ghdeo.login.entity.CompanyUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompanyUserRepositoryTest {
    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Test
    public void 기업_회원_저장_테스트() {
        // given
        CompanyUser companyUser = new CompanyUser(
                1L,
                "admin@example.com",
                "password",
                "Test Company",
                "123-456-7890",
                "ABC123",
                true,
                LocalDateTime.now()
        );

        // when
        CompanyUser savedCompanyUser = companyUserRepository.save(companyUser);

        // Then
        assertNotNull(savedCompanyUser.getCorp_seq());
        Optional<CompanyUser> retrievedCompanyUser = companyUserRepository.findById(savedCompanyUser.getCorp_seq());
        assertTrue(retrievedCompanyUser.isPresent());
        assertEquals("admin@example.com", retrievedCompanyUser.get().getEmail());
    }
}