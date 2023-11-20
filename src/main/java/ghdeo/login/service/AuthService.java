package ghdeo.login.service;

import ghdeo.login.domain.*;
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

    public SignInResponseDto signIn(final SignInRequestDto signInRequestDto) {
        CompanyUser companyUser = companyUserRepository
                .findByEmail(signInRequestDto.getEmail())
                .orElseThrow();

        if (!passwordEncoder.matches(signInRequestDto.getCorp_pw(), companyUser.getCorp_pw())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return SignInResponseDto.builder()
                .email(signInRequestDto.getEmail())
                .access_token(tokenProvider.createToken(signInRequestDto.getEmail()))
                .build();
    }

    public SignOutResponseDto signOut(final SignOutRequestDto signOutRequestDto) {
        if (!tokenProvider.validateToken(signOutRequestDto.getAccess_token())) {
            System.out.println("잘못된 요쳥");
        }

        // 해당 Access Token 유효 시간을 가지고 와서 BlackList 로 저장하기
        Long expiration = tokenProvider.getExpiration(signOutRequestDto.getAccess_token());
        redisTemplate.opsForValue()
                .set(signOutRequestDto.getAccess_token(), "logout", expiration, TimeUnit.MILLISECONDS);

        return SignOutResponseDto.builder()
                .email(signOutRequestDto.getEmail())
                .build();
    }
}
