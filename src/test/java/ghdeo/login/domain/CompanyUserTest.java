package ghdeo.login.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CompanyUserTest {
    @Test
    public void 기업_회원_생성자_테스트() throws Exception {
        // given
        // when
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

        // then
        assertNotNull(companyUser.getCorp_seq());
        assertEquals("admin@example.com", companyUser.getEmail());
        assertEquals("password", companyUser.getCorp_pw());
        assertEquals("Test Company", companyUser.getCorp_name());
        assertEquals("123-456-7890", companyUser.getPhone());
        assertEquals("ABC123", companyUser.getCorp_code());
        assertEquals(true, companyUser.isCorp_auth());
        assertNotNull(companyUser.getCreated_at());
    }
}