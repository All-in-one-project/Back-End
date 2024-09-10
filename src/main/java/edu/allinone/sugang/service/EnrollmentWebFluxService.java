package edu.allinone.sugang.service;

import edu.allinone.sugang.dto.global.EnqueueResponseDTO;
import edu.allinone.sugang.dto.global.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.r2dbc.core.DatabaseClient;

/**
 * 수강신청 WebFlux 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class EnrollmentWebFluxService {

    private final ReactiveStringRedisTemplate redisTemplate; // Redis 템플릿 주입
    private final DatabaseClient databaseClient; // R2DBC를 사용하여 DB 조회

    private static final String WAITING_LIST_KEY = "waiting:list"; // 대기열 키
    private static final int MAX_CONCURRENT_ENROLLMENTS = 1000; // 대기열에서 대기 가능 인원

    /**
     * 수강신청 대기열 처리 메서드
     *
     * @param studentId 학생 ID
     * @param lectureId 과목 ID
     * @return 응답 DTO를 포함한 Mono 객체
     */
    public Mono<ResponseDTO<EnqueueResponseDTO>> enqueue(Integer studentId, Integer lectureId) {
        long currentTime = System.currentTimeMillis(); // 현재 시간

        // 과목의 총 정원을 DB에서 조회
        return getTotalCapacityForLecture(lectureId)
                .flatMap(totalCapacity -> {
                    // 해당 과목의 수강신청 인원이 총 정원을 넘는지 확인
                    return redisTemplate.opsForSet().size("enrolled:students:" + lectureId) // 이미 수강신청한 학생 수 확인
                            .flatMap(enrolledCount -> {
                                if (enrolledCount >= totalCapacity) {
                                    // 정원이 다 찬 경우
                                    return Mono.just(new ResponseDTO<>(
                                            HttpStatus.BAD_REQUEST.value(), "수강신청 정원이 다 찼습니다. 다른 과목을 선택해주세요.", null));
                                }

                                // 대기열에 학생을 추가하고 순번을 확인
                                return redisTemplate.opsForZSet().add(WAITING_LIST_KEY + ":" + lectureId, studentId.toString(), currentTime)
                                        .then(redisTemplate.opsForZSet().rank(WAITING_LIST_KEY + ":" + lectureId, studentId.toString()))
                                        .flatMap(rank -> {
                                            if (rank < MAX_CONCURRENT_ENROLLMENTS) {
                                                // 1000명 이하이면 바로 신청 가능
                                                return Mono.just(new ResponseDTO<>(
                                                        HttpStatus.CREATED.value(), "바로 신청 가능합니다.", new EnqueueResponseDTO(0, 0)));
                                            } else {
                                                // 1000명을 초과하면 대기열에 남음
                                                long waitingTime = rank * 10; // 예시로 각 순서당 10초 대기 시간
                                                EnqueueResponseDTO response = new EnqueueResponseDTO(rank, waitingTime);
                                                return Mono.just(new ResponseDTO<>(
                                                        HttpStatus.OK.value(), "대기열에 추가되었습니다.", response));
                                            }
                                        });
                            });
                });
    }

    /**
     * 대기열에서 처리 완료된 학생을 삭제하는 메서드
     *
     * @param studentId 학생 ID
     * @param lectureId 과목 ID
     * @return 처리 완료 후 응답 Mono 객체
     */
    public Mono<ResponseDTO<String>> removeFromQueue(Integer studentId, Integer lectureId) {
        return redisTemplate.opsForZSet().remove(WAITING_LIST_KEY + ":" + lectureId, studentId.toString()) // 대기열에서 학생 삭제
                .then(Mono.just(new ResponseDTO<>(HttpStatus.OK.value(), "수강신청 완료 및 대기열에서 삭제되었습니다.", null)));
    }

    /**
     * 기존 대기열에 있는 학생을 맨 뒤로 보내는 메서드 (새로고침 또는 재접속 시)
     *
     * @param studentId 학생 ID
     * @param lectureId 과목 ID
     * @return 대기열 순번이 맨 뒤로 밀린 후 응답 Mono 객체
     */
    public Mono<ResponseDTO<EnqueueResponseDTO>> moveToEndOfQueue(Integer studentId, Integer lectureId) {
        long currentTime = System.currentTimeMillis(); // 현재 시간

        // 대기열에서 학생 삭제 후, 다시 맨 뒤에 추가
        return redisTemplate.opsForZSet().remove(WAITING_LIST_KEY + ":" + lectureId, studentId.toString()) // 기존 대기열에서 삭제
                .then(redisTemplate.opsForZSet().add(WAITING_LIST_KEY + ":" + lectureId, studentId.toString(), currentTime)) // 맨 뒤에 추가
                .then(redisTemplate.opsForZSet().rank(WAITING_LIST_KEY + ":" + lectureId, studentId.toString())) // 순위 조회
                .flatMap(rank -> {
                    long waitingTime = rank * 10; // 각 순서당 10초 대기 시간
                    EnqueueResponseDTO response = new EnqueueResponseDTO(rank, waitingTime);
                    return Mono.just(new ResponseDTO<>(HttpStatus.OK.value(), "대기열에서 순번이 맨 뒤로 이동했습니다.", response));
                });
    }

    /**
     * 대기 시간 조회 메서드
     *
     * @param studentId 학생 ID
     * @param lectureId 과목 ID
     * @return 대기 시간을 포함한 Mono 객체 (초 단위)
     */
    public Mono<Long> getWaitingTime(Integer studentId, Integer lectureId) {
        return redisTemplate.opsForZSet().rank(WAITING_LIST_KEY + ":" + lectureId, studentId.toString())
                .map(rank -> rank * 10L); // 10초씩 대기 시간 계산
    }

    /**
     * 진입 가능 여부 조회 메서드
     *
     * @param studentId 학생 ID
     * @param lectureId 과목 ID
     * @return 진입 가능 여부를 포함한 Mono 객체
     */
    public Mono<Boolean> canEnter(Integer studentId, Integer lectureId) {
        return redisTemplate.opsForZSet().rank(WAITING_LIST_KEY + ":" + lectureId, studentId.toString())
                .map(rank -> rank < MAX_CONCURRENT_ENROLLMENTS);
    }

    /**
     * 과목의 총 수강 인원 (total_capacity)를 DB에서 조회하는 메서드
     *
     * @param lectureId 과목 ID
     * @return 해당 과목의 수강 정원 (total_capacity)를 반환하는 Mono 객체
     */
    private Mono<Integer> getTotalCapacityForLecture(Integer lectureId) {
        // 인덱스 기반 파라미터 바인딩으로 변경
        return databaseClient.sql("SELECT total_capacity FROM lecture WHERE id = ?")
                .bind(0, lectureId)  // 첫 번째 파라미터에 lectureId를 바인딩
                .map(row -> row.get("total_capacity", Integer.class))
                .one();
    }

}
