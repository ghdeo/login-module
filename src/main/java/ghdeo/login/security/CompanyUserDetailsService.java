package ghdeo.login.security;

import ghdeo.login.domain.CompanyUser;
import ghdeo.login.repository.CompanyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyUserDetailsService implements UserDetailsService {
    private final CompanyUserRepository companyUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CompanyUser companyUser = companyUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보 를 찾지 못했습니다."));
        return CompanyUserDetails.of(companyUser);
    }
}
