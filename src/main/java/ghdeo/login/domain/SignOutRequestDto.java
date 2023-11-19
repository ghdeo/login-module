package ghdeo.login.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SignOutRequestDto {
    private String email; // 관리자 이메일
    private String access_token; // access token
}
