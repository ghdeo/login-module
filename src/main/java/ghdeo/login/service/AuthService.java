package ghdeo.login.service;

import ghdeo.login.domain.CompanyUser;
import ghdeo.login.domain.CompanyUserDto;
import ghdeo.login.repository.CompanyUserRepository;
import ghdeo.login.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenProvider tokenProvider;
    private final CompanyUserRepository companyUserRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public Long signUp(final CompanyUserDto companyUserDto) {
        CompanyUser companyUser = CompanyUser.builder()
                .corp_seq(companyUserDto.getCorp_seq())
                .email(companyUserDto.getEmail())
                .corp_pw(passwordEncoder.encode(companyUserDto.getCorp_pw()))
                .corp_name(companyUserDto.getCorp_name())
                .phone(companyUserDto.getPhone())
                .corp_code(companyUserDto.getCorp_code())
                .corp_auth(false)
                .created_at(LocalDateTime.now())
                .build();

        CompanyUser save = companyUserRepository.save(companyUser);

        return save.getCorp_seq();
    }

    public CompanyUserDto signIn(final CompanyUserDto companyUserDto) {
        CompanyUser companyUser = companyUserRepository
                .findByEmail(companyUserDto.getEmail())
                .orElseThrow();

        if (!passwordEncoder.matches(companyUser.getCorp_pw(), companyUserDto.getCorp_pw())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

    return CompanyUserDto.builder()
            .corp_seq(companyUserDto.getCorp_seq())
            .email(companyUserDto.getEmail())
            .corp_pw(companyUserDto.getCorp_pw())
            .corp_name(companyUserDto.getCorp_name())
            .phone(companyUserDto.getPhone())
            .corp_code(companyUserDto.getCorp_code())
            .accessToken(tokenProvider.createToken(companyUserDto.getEmail()))
            .build();
    }

    public Long signOut(final CompanyUserDto companyUserDto) {
        if (!tokenProvider.validateToken(companyUserDto.getAccessToken())) {
            System.out.println("잘못된 요쳥");
        }

        // 해당 Access Token 유효 시간을 가지고 와서 BlackList 로 저장하기
        Long expiration = tokenProvider.getExpiration(companyUserDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(companyUserDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return companyUserDto.getCorp_seq();
    }
}
