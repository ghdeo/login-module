package ghdeo.login.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyUserDto {
    private Long corp_seq; // 기업 인덱스
    private String email; // 관리자 이메일
    private String corp_pw; // 패스워드
    private String corp_name; // 기업 명
    private String phone; // 관리자  연락처
    private String corp_code; // 기업 고유번호
    private String accessToken;
}
