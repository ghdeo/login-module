package ghdeo.login.controller;

import ghdeo.login.domain.CompanyUserDto;
import ghdeo.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody CompanyUserDto companyUserDto) {
        Long corp_seq = authService.signUp(companyUserDto);
        return ResponseEntity.ok().body(corp_seq);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody CompanyUserDto companyUserDto) {
        return ResponseEntity.ok().body(authService.signIn(companyUserDto));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@RequestBody CompanyUserDto companyUserDto) {
        return ResponseEntity.ok().body(authService.signOut(companyUserDto));
    }

}
