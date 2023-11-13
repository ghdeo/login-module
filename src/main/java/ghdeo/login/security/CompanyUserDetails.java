package ghdeo.login.security;

import ghdeo.login.entity.CompanyUser;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Builder
public class CompanyUserDetails implements UserDetails {
    private Long corp_seq; // 기업 인덱스
    private String email; // 관리자 이메일
    private String corp_pw; // 패스워드
    private String corp_name; // 기업 명
    private String phone; // 관리자  연락처
    private String corp_code; // 기업 고유번호

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return corp_pw;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static CompanyUserDetails of(CompanyUser companyUser) {
        return CompanyUserDetails.builder()
                .corp_seq(companyUser.getCorp_seq())
                .email(companyUser.getEmail())
                .corp_pw(companyUser.getCorp_pw())
                .corp_name(companyUser.getCorp_name())
                .phone(companyUser.getPhone())
                .corp_name(companyUser.getCorp_code())
                .build();
    }
}
