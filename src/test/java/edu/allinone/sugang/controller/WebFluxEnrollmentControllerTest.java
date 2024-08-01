package edu.allinone.sugang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.allinone.sugang.dto.global.EnqueueResponseDTO;
import edu.allinone.sugang.dto.global.ResponseDTO;
import edu.allinone.sugang.service.EnrollmentWebFluxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(controllers = WebFluxEnrollmentController.class) // WebFlux 테스트 설정
@AutoConfigureWebTestClient // WebTestClient 자동 구성
public class WebFluxEnrollmentControllerTest {

    @MockBean // Mocking된 서비스 주입
    private EnrollmentWebFluxService enrollmentWebFluxService;

    private WebTestClient webTestClient; // WebFlux의 테스트 클라이언트

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 데이터를 수동으로 변환하기 위한 ObjectMapper

    @BeforeEach // 각 테스트 실행 전 실행되는 메서드
    public void setUp() {
        webTestClient = WebTestClient.bindToController(new WebFluxEnrollmentController(enrollmentWebFluxService)).build(); // WebTestClient 초기화
    }

    @Test
    public void testEnqueue() {
        // Mocking된 서비스 메서드가 호출될 때 반환할 값을 설정
        EnqueueResponseDTO enqueueResponseDTO = new EnqueueResponseDTO(5, 50);
        given(enrollmentWebFluxService.enqueue(anyInt()))
                .willReturn(Mono.just(new ResponseDTO<>(201, "대기열에 추가되었습니다.", enqueueResponseDTO))); // 응답 수정

        // WebTestClient를 사용하여 POST 요청을 보내고 응답을 검증
        webTestClient.post().uri("/webflux/enrollment/enqueue?studentId=1")
                .accept(APPLICATION_JSON) // 요청의 Accept 헤더를 설정
                .exchange() // 요청을 실행
                .expectStatus().isCreated() // HTTP 상태가 201 Created인지 확인
                .expectBody(String.class) // 응답의 body를 String으로 변환
                .value(responseString -> {
                    try {
                        // JSON 데이터를 ResponseDTO<EnqueueResponseDTO>로 변환
                        ResponseDTO<EnqueueResponseDTO> response = objectMapper.readValue(responseString, objectMapper.getTypeFactory().constructParametricType(ResponseDTO.class, EnqueueResponseDTO.class));
                        assert response.getStatus() == 201; // 응답의 상태 코드가 201인지 확인
                        assert response.getMessage().equals("대기열에 추가되었습니다."); // 응답 메시지가 예상과 일치하는지 확인
                        EnqueueResponseDTO data = response.getData(); // 응답 데이터 변환
                        assert data.getPosition() == 5; // 대기열 위치가 예상과 일치하는지 확인
                        assert data.getWaitingTime() == 50; // 각 위치당 10초를 가정하여 대기 시간이 예상과 일치하는지 확인
                    } catch (Exception e) {
                        throw new RuntimeException(e); // JSON 변환 중 오류 발생 시 예외 발생
                    }
                });
    }

    @Test
    public void testGetWaitingTime() {
        // Mocking된 서비스 메서드가 호출될 때 반환할 값을 설정
        given(enrollmentWebFluxService.getWaitingTime(anyInt()))
                .willReturn(Mono.just(120L)); // 대기 시간을 120초로 설정

        // WebTestClient를 사용하여 GET 요청을 보내고 응답을 검증
        webTestClient.get().uri("/webflux/enrollment/waiting-time?studentId=1")
                .accept(APPLICATION_JSON) // 요청의 Accept 헤더를 설정
                .exchange() // 요청을 실행
                .expectStatus().isOk() // HTTP 상태가 200 OK인지 확인
                .expectBody(ResponseDTO.class) // 응답의 body를 ResponseDTO로 변환
                .value(response -> {
                    assert response.getStatus() == 200; // 응답의 상태 코드가 200인지 확인
                    assert response.getMessage().equals("예상 대기시간"); // 응답 메시지가 예상과 일치하는지 확인
                    assert ((Number) response.getData()).longValue() == 120L; // 응답 데이터가 120초인지 확인
                });
    }

    @Test
    public void testCanEnter() {
        // Mocking된 서비스 메서드가 호출될 때 반환할 값을 설정
        given(enrollmentWebFluxService.canEnter(anyInt()))
                .willReturn(Mono.just(true)); // 진입 가능 여부를 true로 설정

        // WebTestClient를 사용하여 GET 요청을 보내고 응답을 검증
        webTestClient.get().uri("/webflux/enrollment/can-enter/1")
                .accept(APPLICATION_JSON) // 요청의 Accept 헤더를 설정
                .exchange() // 요청을 실행
                .expectStatus().isOk() // HTTP 상태가 200 OK인지 확인
                .expectBody(ResponseDTO.class) // 응답의 body를 ResponseDTO로 변환
                .value(response -> {
                    assert response.getStatus() == 200; // 응답의 상태 코드가 200인지 확인
                    assert response.getMessage().equals("수강신청이 가능합니다."); // 응답 메시지가 예상과 일치하는지 확인
                    assert response.getData().equals(true); // 응답 데이터가 true인지 확인
                });
    }
}
