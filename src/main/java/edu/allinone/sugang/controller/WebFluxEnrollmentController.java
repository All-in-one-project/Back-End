package edu.allinone.sugang.controller;

import edu.allinone.sugang.dto.global.EnqueueResponseDTO;
import edu.allinone.sugang.dto.global.ResponseDTO;
import edu.allinone.sugang.service.EnrollmentWebFluxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/webflux/enrollment")
@RequiredArgsConstructor
public class WebFluxEnrollmentController {

    private final EnrollmentWebFluxService enrollmentWebFluxService;

    /**
     * 학생id를 입력 받고 수강 신청 대기열에 추가
     *
     * @param studentId 학생 ID
     * @return Mono<ResponseDTO<?>> 대기열 추가 결과
     */
    @PostMapping("/enqueue")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDTO<EnqueueResponseDTO>> enqueue(@RequestParam Integer studentId) {
        return enrollmentWebFluxService.enqueue(studentId) // 대기열에 추가하는 서비스 메서드 호출
                .onErrorResume(e -> Mono.just(new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "대기열 추가 중 오류가 발생했습니다."))); // 에러 발생 시 응답
    }

    /**
     * 대기 시간 조회
     *
     * @param studentId 학생 ID
     * @return Mono<ResponseDTO<Long>> 대기 시간 정보
     */
    @GetMapping("/waiting-time")
    public Mono<ResponseDTO<Long>> getWaitingTime(@RequestParam Integer studentId) {
        return enrollmentWebFluxService.getWaitingTime(studentId) // 대기 시간 조회 서비스 메서드 호출
                .map(waitingTime -> new ResponseDTO<>(HttpStatus.OK.value(), "예상 대기시간", waitingTime)) // 대기 시간 응답
                .onErrorResume(e -> Mono.just(new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "대기시간 조회 중 오류가 발생했습니다."))); // 에러 발생 시 응답
    }

    /**
     * 진입 가능 여부 조회
     *
     * @param studentId 학생 ID
     * @return Mono<ResponseDTO<Boolean>> 진입 가능 여부 응답
     */
    @GetMapping("/can-enter/{studentId}")
    public Mono<ResponseDTO<Boolean>> canEnter(@PathVariable Integer studentId) {
        return enrollmentWebFluxService.canEnter(studentId) // 진입 가능 여부 조회 서비스 메서드 호출
                .flatMap(canEnter -> Mono.just(new ResponseDTO<>(HttpStatus.OK.value(), canEnter ? "수강신청이 가능합니다." : "아직 대기중입니다.", canEnter))) // 진입 가능 여부 응답
                .onErrorResume(e -> Mono.just(new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "진입 가능 여부 조회 중 오류가 발생했습니다."))); // 에러 발생 시 응답
    }
}
