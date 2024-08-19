//package edu.allinone.sugang.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration
//@EnableWebSecurity // Spring Security의 웹 보안 지원을 활성화
//@Slf4j // 로깅 위한 Log 객체 자동 생성
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
//
//    @Bean
//    // 어떤 URL 경로를 보안해야 하고 어떤 경로를 보안하지 않아야 하는지 정의
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(request -> request
//                                .anyRequest().permitAll() // 모든 요청에 대해 인증 없이 접근 가능
//                        // 위에서 명시한 경로를 제외한 모든 요청에 대해 인증된 사용자만 접근할 수 있도록 설정
//
//                )
//                .formLogin(login -> login
//                        .loginPage("/login") // 커스텀 로그인 페이지 설정
//                        .defaultSuccessUrl("/", true)
//                        // 성공 시 이동 경로 (임의로 작성, 나중에 구현 시 수정)
//                        .usernameParameter("studentNumber") // 로그인 폼에서 사용자 이름 필드 이름 설정
//                        .passwordParameter("studentPassword") // 로그인 폼에서 비밀번호 필드 이름 설정
//                        .permitAll() // 로그인 폼에 모든 사용자 접근 가능
//                )
//                .logout(Customizer.withDefaults()); // 기본 값
//
//        return http.build();
//    }
//
//}
