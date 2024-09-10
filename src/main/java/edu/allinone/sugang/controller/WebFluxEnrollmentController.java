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
     * 학생ID와 과목ID를 입력받고 수강 신청 대기열에 추가
     *
     * @param studentId 학생 ID
     * @return Mono<ResponseDTO<?>> 대기열 추가 결과
     */
    @PostMapping("/enqueue")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDTO<EnqueueResponseDTO>> enqueue(@RequestParam Integer studentId) {
        return enrollmentWebFluxService.enqueue(studentId)
                .onErrorResume(e -> Mono.just(new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "대기열 추가 중 오류가 발생했습니다.")));
    }

    /**
     * 대기 시간 조회
     *
     * @param studentId 학생 ID
     * @return Mono<ResponseDTO<Long>> 대기 시간 정보
     */
    @GetMapping("/waiting-time")
    public Mono<ResponseDTO<Long>> getWaitingTime(@RequestParam Integer studentId) {
        return enrollmentWebFluxService.getWaitingTime(studentId) // studentId 전달
                .map(waitingTime -> new ResponseDTO<>(HttpStatus.OK.value(), "예상 대기시간", waitingTime))
                .onErrorResume(e -> Mono.just(new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "대기시간 조회 중 오류가 발생했습니다.")));
    }

    /**
     * 진입 가능 여부 조회
     *
     * @param studentId 학생 ID
     * @return Mono<ResponseDTO<Boolean>> 진입 가능 여부 응답
     */
    @GetMapping("/can-enter/{studentId}/{lectureId}")
    public Mono<ResponseDTO<Boolean>> canEnter(@PathVariable Integer studentId) {
        return enrollmentWebFluxService.canEnter(studentId) // 두 개의 파라미터 전달
                .flatMap(canEnter -> Mono.just(new ResponseDTO<>(HttpStatus.OK.value(), canEnter ? "수강신청이 가능합니다." : "아직 대기중입니다.", canEnter)))
                .onErrorResume(e -> Mono.just(new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "진입 가능 여부 조회 중 오류가 발생했습니다.")));
    }
}
