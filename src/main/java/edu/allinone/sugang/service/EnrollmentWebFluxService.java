package edu.allinone.sugang.service;

import edu.allinone.sugang.dto.global.EnqueueResponseDTO;
import edu.allinone.sugang.dto.global.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 수강신청 WebFlux 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class EnrollmentWebFluxService {

    private final ReactiveStringRedisTemplate redisTemplate; // Redis 템플릿 주입

    private static final String WAITING_LIST_KEY = "waiting:list"; // 공통 대기열 키
    private static final int MAX_CONCURRENT_ENROLLMENTS = 1000; // 최대 동시 수강신청 처리 수

    /**
     * 수강신청 대기열 처리 메서드
     *
     * @param studentId 학생 ID
     * @return 응답 DTO를 포함한 Mono 객체
     */
    public Mono<ResponseDTO<EnqueueResponseDTO>> enqueue(Integer studentId) {
        long currentTime = System.currentTimeMillis(); // 현재 시간

        return redisTemplate.opsForZSet().rank(WAITING_LIST_KEY, studentId.toString()) // 학생이 이미 대기열에 있는지 확인
                .flatMap(existingRank ->
                        redisTemplate.opsForZSet().remove(WAITING_LIST_KEY, studentId.toString()) // 기존 대기열에서 제거
                                .then(redisTemplate.opsForZSet().add(WAITING_LIST_KEY, studentId.toString(), currentTime)) // 새로운 대기열에 추가
                                .then(redisTemplate.opsForZSet().rank(WAITING_LIST_KEY, studentId.toString())) // 새로운 대기열 순위 조회
                                .flatMap(newRank -> {
                                    long waitingTime = newRank * 10; // 예시로 각 위치당 10초를 가정
                                    EnqueueResponseDTO enqueueResponse = new EnqueueResponseDTO(newRank, waitingTime); // EnqueueResponseDTO 객체 생성
                                    return Mono.just(new ResponseDTO<>(HttpStatus.OK.value(), "대기열에 추가되었습니다.", enqueueResponse)); // 응답 객체 반환
                                })
                )
                .switchIfEmpty(
                        redisTemplate.opsForZSet().size(WAITING_LIST_KEY)  // 대기열 크기 확인
                                .flatMap(size -> {
                                    if (size >= MAX_CONCURRENT_ENROLLMENTS) {  // 최대 동시 수강신청 처리 수 초과 시
                                        return redisTemplate.opsForZSet().add(WAITING_LIST_KEY, studentId.toString(), currentTime) // 대기열에 추가
                                                .then(redisTemplate.opsForZSet().rank(WAITING_LIST_KEY, studentId.toString()))  // 대기열에서의 순위 조회
                                                .flatMap(rank -> {
                                                    long waitingTime = rank * 10; // 예시로 각 위치당 10초를 가정
                                                    EnqueueResponseDTO enqueueResponse = new EnqueueResponseDTO(rank, waitingTime); // EnqueueResponseDTO 객체 생성
                                                    return Mono.just(new ResponseDTO<>(HttpStatus.OK.value(), "대기열에 추가되었습니다.", enqueueResponse)); // 응답 객체 반환
                                                });
                                    } else {
                                        EnqueueResponseDTO enqueueResponse = new EnqueueResponseDTO(0, 0); // EnqueueResponseDTO 객체 생성
                                        return Mono.just(new ResponseDTO<>(HttpStatus.CREATED.value(), "바로 신청 가능", enqueueResponse)); // 응답 객체 반환
                                    }
                                })
                );
    }


    /**
     * 대기 시간 조회 메서드
     *
     * @param studentId 학생 ID
     * @return 대기 시간을 포함한 Mono 객체 (초 단위)
     */
    public Mono<Long> getWaitingTime(Integer studentId) {
        return redisTemplate.opsForZSet().rank(WAITING_LIST_KEY, studentId.toString()) // 대기열에서의 순위 조회
                .flatMap(rank -> Mono.just(rank * 10L)); // 대기시간을 10초씩 계산 (예시)
    }

    /**
     * 진입 가능 여부 조회 메서드
     *
     * @param studentId 학생 ID
     * @return 진입 가능 여부를 포함한 Mono 객체
     */
    public Mono<Boolean> canEnter(Integer studentId) {
        return redisTemplate.opsForZSet().rank(WAITING_LIST_KEY, studentId.toString()) // 대기열에서의 순위 조회
                .flatMap(rank -> Mono.just(rank < MAX_CONCURRENT_ENROLLMENTS)); // 현재 대기열 크기와 비교하여 진입 가능 여부 판단
    }
}
