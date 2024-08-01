package edu.allinone.sugang.router;

import edu.allinone.sugang.handler.EnrollmentWebFluxHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebFluxRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeEnrollment(EnrollmentWebFluxHandler handler) {
        return route(POST("/webflux/enrollment").and(accept(MediaType.APPLICATION_JSON)), handler::enroll)  // 수강신청 대기열 엔드포인트
                .andRoute(GET("/webflux/enrollment/waiting-time").and(accept(MediaType.APPLICATION_JSON)), handler::getWaitingTime)  // 대기 시간 조회 엔드포인트
                .andRoute(GET("/webflux/enrollment/can-enter/{studentId}").and(accept(MediaType.APPLICATION_JSON)), handler::canEnter);  // 진입 가능 여부 조회 엔드포인트
    }
}
