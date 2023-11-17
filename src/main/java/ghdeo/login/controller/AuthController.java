package ghdeo.login.controller;

import ghdeo.login.domain.CompanyUserDto;
import ghdeo.login.security.CompanyUserDetails;
import ghdeo.login.security.CompanyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final CompanyUserDetailsService companyUserDetailsService;
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody CompanyUserDto companyUserDto) {
        return ResponseEntity.ok().body(companyUserDto);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody CompanyUserDto companyUserDto) {
        return ResponseEntity.ok().body(companyUserDto);
    }
    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal CompanyUserDetails companyUserDetails) {
        return ResponseEntity.ok().body(companyUserDetails);
    }

}
