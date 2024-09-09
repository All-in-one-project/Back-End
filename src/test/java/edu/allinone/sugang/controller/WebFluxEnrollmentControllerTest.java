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

@WebFluxTest(controllers = WebFluxEnrollmentController.class)
@AutoConfigureWebTestClient
public class WebFluxEnrollmentControllerTest {

    @MockBean
    private EnrollmentWebFluxService enrollmentWebFluxService;

    private WebTestClient webTestClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToController(new WebFluxEnrollmentController(enrollmentWebFluxService)).build();
    }

    @Test
    public void testEnqueue() {
        EnqueueResponseDTO enqueueResponseDTO = new EnqueueResponseDTO(5, 50);
        // Mocked 서비스가 studentId와 lectureId를 받도록 수정
        given(enrollmentWebFluxService.enqueue(anyInt(), anyInt()))
                .willReturn(Mono.just(new ResponseDTO<>(201, "대기열에 추가되었습니다.", enqueueResponseDTO)));

        // URI에 studentId와 lectureId 둘 다 전달
        webTestClient.post().uri("/webflux/enrollment/enqueue?studentId=1&lectureId=101")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .value(responseString -> {
                    try {
                        ResponseDTO<EnqueueResponseDTO> response = objectMapper.readValue(responseString, objectMapper.getTypeFactory().constructParametricType(ResponseDTO.class, EnqueueResponseDTO.class));
                        assert response.getStatus() == 201;
                        assert response.getMessage().equals("대기열에 추가되었습니다.");
                        EnqueueResponseDTO data = response.getData();
                        assert data.getPosition() == 5;
                        assert data.getWaitingTime() == 50;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    public void testGetWaitingTime() {
        given(enrollmentWebFluxService.getWaitingTime(anyInt(), anyInt()))
                .willReturn(Mono.just(120L));

        webTestClient.get().uri("/webflux/enrollment/waiting-time?studentId=1&lectureId=101")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDTO.class)
                .value(response -> {
                    assert response.getStatus() == 200;
                    assert response.getMessage().equals("예상 대기시간");
                    assert ((Number) response.getData()).longValue() == 120L;
                });
    }

    @Test
    public void testCanEnter() {
        given(enrollmentWebFluxService.canEnter(anyInt(), anyInt()))
                .willReturn(Mono.just(true));

        webTestClient.get().uri("/webflux/enrollment/can-enter/1/101")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDTO.class)
                .value(response -> {
                    assert response.getStatus() == 200;
                    assert response.getMessage().equals("수강신청이 가능합니다.");
                    assert response.getData().equals(true);
                });

    }
}
