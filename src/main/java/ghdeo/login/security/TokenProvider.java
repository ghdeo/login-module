package ghdeo.login.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private static final String ACCESS_SECRET_KEY = "ACCESS_SECRET_KEY"; // Access Token Secret Key
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 3600; // Access Token 유효 시간 (1시간)

    public String createToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000); // 기한은 지금부터 토큰 유효 시간만큼 설정

        return Jwts.builder()
                .setSubject(email)
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SIGNATURE_ALGORITHM, ACCESS_SECRET_KEY)
                // payload에 들어갈 내용
                .setIssuer("ghdeo") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate)
                .compact();
    }

    public boolean validateToken(String accessToken) { // 토큰을 받아 유효성 검사를 실행
        try {
            Jwts.parser().setSigningKey(ACCESS_SECRET_KEY).parseClaimsJws(accessToken);

            return true;
        }
        catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        }
        catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        }
        catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        }
        catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String getEmail(String accessToken) {
        // parseClaimsJws 메서드가 Base64로 디코딩 및 파싱
        // 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날린다.
        // 그 중 우리는 email이 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser()
                .setSigningKey(ACCESS_SECRET_KEY)
                .parseClaimsJws(accessToken)
                .getBody();

        return claims.getSubject();
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parser()
                .setSigningKey(ACCESS_SECRET_KEY)
                .parseClaimsJws(accessToken)
                .getBody().getExpiration();

        // 현재 시간
        long time = new Date().getTime();

        return (expiration.getTime() - time);
    }

}
