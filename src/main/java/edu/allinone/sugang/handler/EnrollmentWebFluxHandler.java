package edu.allinone.sugang.handler;

import edu.allinone.sugang.dto.global.EnqueueResponseDTO;
import edu.allinone.sugang.dto.global.ResponseDTO;
import edu.allinone.sugang.dto.request.EnrollmentDTO;
import edu.allinone.sugang.service.EnrollmentWebFluxService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EnrollmentWebFluxHandler {

    @Autowired
    private final EnrollmentWebFluxService enrollmentService;

    public Mono<ServerResponse> enroll(ServerRequest request) {
        return request.bodyToMono(EnrollmentDTO.class) // 요청의 body를 EnrollmentDTO로 변환
                .flatMap(enrollmentDTO -> enrollmentService.enqueue(enrollmentDTO.getStudentId()) // 대기열에 추가
                        .flatMap(response -> {
                            if (response.getStatus() == HttpStatus.OK.value() && response.getData() instanceof EnqueueResponseDTO) {
                                EnqueueResponseDTO enqueueResponse = (EnqueueResponseDTO) response.getData(); // EnqueueResponseDTO로 변환
                                return ServerResponse.status(HttpStatus.OK) // HTTP 상태 설정
                                        .bodyValue(new ResponseDTO<>(HttpStatus.OK.value(), response.getMessage(), enqueueResponse)); // 응답 객체 설정
                            } else {
                                // 기존 ResponseDTO 객체 반환
                                return ServerResponse.status(HttpStatus.valueOf(response.getStatus())) // HTTP 상태 설정
                                        .bodyValue(response); // 응답 객체 설정
                            }
                        }));
    }

    public Mono<ServerResponse> getWaitingTime(ServerRequest request) {
        String studentId = request.queryParam("studentId").orElse(""); // query parameter로부터 studentId 가져오기
        return enrollmentService.getWaitingTime(Integer.valueOf(studentId)) // 대기 시간 조회
                .flatMap(waitingTime -> ServerResponse.ok() // 대기 시간 조회 성공 시 OK 상태로 응답
                        .bodyValue(new ResponseDTO<>(HttpStatus.OK.value(), "예상 대기시간", waitingTime)))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR) // 에러 발생 시 INTERNAL_SERVER_ERROR 상태로 응답
                        .bodyValue(new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())));
    }

    public Mono<ServerResponse> canEnter(ServerRequest request) {
        Integer studentId = Integer.valueOf(request.pathVariable("studentId")); // path variable로부터 studentId 가져오기
        return enrollmentService.canEnter(studentId) // 진입 가능 여부 조회
                .flatMap(canEnter -> {
                    if (canEnter) {
                        return ServerResponse.ok() // 진입 가능 시 OK 상태로 응답
                                .bodyValue(new ResponseDTO<>(HttpStatus.OK.value(), "수강신청이 가능합니다."));
                    } else {
                        return ServerResponse.ok() // 대기 중인 경우 OK 상태로 응답
                                .bodyValue(new ResponseDTO<>(HttpStatus.OK.value(), "아직 대기중입니다."));
                    }
                })
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR) // 에러 발생 시 INTERNAL_SERVER_ERROR 상태로 응답
                        .bodyValue(new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())));
    }
}
