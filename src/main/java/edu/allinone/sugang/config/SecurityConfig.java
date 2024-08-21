package edu.allinone.sugang.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity // Spring Security의 웹 보안 지원을 활성화
@Slf4j // 로깅 위한 Log 객체 자동 생성
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                                .anyRequest().permitAll()
                        // 위에서 명시한 경로를 제외한 모든 요청에 대해 인증된 사용자만 접근할 수 있도록 설정

                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("studentNumber")
                        .passwordParameter("studentPassword")
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .cors(Customizer.withDefaults()) // CORS 설정 추가
                .csrf(csrf -> csrf.disable()); // CSRF 비활성화

        return http.build();
    }

    // CORS 설정을 Spring Security와 함께 사용하기 위해 추가
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://43.202.223.188")
                        .allowedOrigins("http://localhost:3000")  // React 애플리케이션이 실행되는 도메인
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
            }
        };
    }
}