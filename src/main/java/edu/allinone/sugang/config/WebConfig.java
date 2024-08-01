package edu.allinone.sugang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로에 대해 CORS 허용
        registry.addMapping("/**")
                .allowedOrigins("http://43.202.223.188")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);

        // 특정 경로에 대한 추가 설정: 필요시 사용
        registry.addMapping("/api/**")
                .allowedOrigins("http://43.202.223.188")
                .allowedMethods("GET", "POST")
                .allowCredentials(true);
    }
}
