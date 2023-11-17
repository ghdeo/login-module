package ghdeo.login.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long corp_seq; // 기업 인덱스
    private String email; // 관리자 이메일
    private String corp_pw; // 패스워드
    private String corp_name; // 기업 명
    private String phone; // 관리자  연락처
    private String corp_code; // 기업 고유번호
    private boolean corp_auth; // 인증 기업 여부
    private LocalDateTime created_at; // 생성일

}
