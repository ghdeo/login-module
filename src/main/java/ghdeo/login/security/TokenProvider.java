package ghdeo.login.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class TokenProvider {
    private static final String ACCESS_SECRET_KEY = "ACCESS_SECRET_KEY"; // Access Token Secret Key
    private static final String REFRESH_SECRET_KEY = "REFRESH_SECRET_KEY"; // Refresh Token Secret Key
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 3600; // Access Token 유효 시간 (1시간)
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 2592000; // Refresh Token 유효 시간 (30일)

    public String createAccessToken(UserDetails userDetails) {
        long accessTokenExpiryTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;

        return createJwtBuilder(accessTokenExpiryTime, "AccessToken")
                .setSubject(userDetails.getUsername())
                .compact();
    }

    public String createRefreshToken() {
        long refreshTokenExpiryTime = REFRESH_TOKEN_VALIDITY_SECONDS * 1000;

        // 보통 Refresh Token은 Access Token과 달리 Subject, Claim과 같은 회원 정보를 담고 있지 않는다.
        return createJwtBuilder(refreshTokenExpiryTime, "RefreshToken")
                .compact();
    }

    public JwtBuilder createJwtBuilder(long time, String secretKeyType) {
        String SECRET_KEY = "";
        if (secretKeyType.equals("AccessToken")) {
            SECRET_KEY = ACCESS_SECRET_KEY;
        }
        if (secretKeyType.equals("RefreshToken")) {
            SECRET_KEY = REFRESH_SECRET_KEY;
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + time); // 기한은 지금부터 토큰 유효 시간만큼 설정

        // JWT 생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SIGNATURE_ALGORITHM, SECRET_KEY)
                // payload에 들어갈 내용
                .setIssuer("ghdeo") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate); // exp
    }

    public boolean validateToken(String token, String secretKeyType) { // 토큰을 받아 유효성 검사를 실행
        try {
            String SECRET_KEY = "";
            if (secretKeyType.equals("AccessToken")) {
                SECRET_KEY = ACCESS_SECRET_KEY;
            }
            if (secretKeyType.equals("RefreshToken")) {
                SECRET_KEY = REFRESH_SECRET_KEY;
            }

            // SECRET_KEY를 이용하여 파싱을 수행
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);

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
}
