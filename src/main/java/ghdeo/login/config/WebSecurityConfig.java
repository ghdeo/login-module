package ghdeo.login.config;

import ghdeo.login.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // http 시큐리티 빌더
        httpSecurity.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정
                .and()
                // csrf는 현재 사용하지 않으므로 disable
                .csrf().disable()
                // token을 사용하므로 basic 인증 disable
                .httpBasic().disable()
                // session 기반이 아님을 선언
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // / 경로는 인증 안 해도 된다.
                .authorizeRequests().antMatchers("/", "/auth/**",
                        "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
                        "/api/v3/**", "/swagger-resources/**", "/webjars/**").permitAll()
                // /  이외의 모든 경로는 인증해야 된다.
                .anyRequest().authenticated()
                .and()
                // "ROLE_ANONYMOUS" 권한을 로그인하지 않은 사용자에게 자동으로 할당
                .anonymous()
                .and() // filter 등록
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class); // 매 요청마다 CorsFilter 실행한 후에 jwtAuthenticationFilter 실행한다.

        return httpSecurity.build();
    }

}
