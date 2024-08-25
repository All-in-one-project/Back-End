package edu.allinone.sugang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.allinone.sugang.domain.Department;
import edu.allinone.sugang.domain.Lecture;
import edu.allinone.sugang.domain.Professor;
import edu.allinone.sugang.domain.Subject;
import edu.allinone.sugang.dto.request.EnrollmentDTO;
import edu.allinone.sugang.dto.response.CreditDTO;
import edu.allinone.sugang.repository.LectureRepository;
import edu.allinone.sugang.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrollmentController.class) // Controller 레이어만 테스트 하고 싶다면 @WebMvcTest를 사용하는 것이 좋다.
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @MockBean
    private LectureRepository lectureRepository;

    @Autowired
    // 요청이 오면 Content-Type이 json인 것을 Object로 바꿔주고 처리 후 Object를 json으로 변경하여 request 합니다.
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /* ================================================================= */
    //                              수강 신청                            //
    /* ================================================================= */

    /**
     * 학생id, 과목id를 입력 받고 수강 신청 하기 테스트
     */
    @Test
    @DisplayName("학생id, 과목id 수강 신청 하기 테스트")
    public void enrollTest() throws Exception {
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1, 1);

        Mockito.doNothing().when(enrollmentService).enroll(anyInt(), anyInt());

        mockMvc.perform(post("/enrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("신청 완료"));
    }

    /**
     * 학생id, 과목코드 입력 받고 수강 신청 하기 테스트
     */
    @Test
    @DisplayName("학생id, 과목코드 수강 신청 하기 테스트")
    public void enrollByCodeTest() throws Exception {
        // Create dummy data for Department, Professor, and Subject
        Department department = Department.builder()
                .id(1)
                .departmentName("컴퓨터학부")
                .students(Collections.emptyList())
                .lectures(Collections.emptyList())
                .college(null) // Assuming college can be null or mocked accordingly
                .subjects(Collections.emptyList())
                .build();

        Professor professor = Professor.builder()
                .id(1)
                .professorName("김철수")
                .email("cs.kim@university.ac.kr")
                .lectures(Collections.emptyList())
                .build();

        Subject subject = Subject.builder()
                .id(1)
                .subjectName("컴퓨터 네트워크")
                .subjectDivision("전필")
                .targetGrade("3학년")
                .hoursPerWeek(3)
                .credit(3)
                .lectures(Collections.emptyList())
                .department(department)
                .build();

        // Create a Lecture object using the builder pattern
        Lecture lecture = Lecture.builder()
                .id(1)
                .lectureNumber("21032-001")
                .lectureRoom("A101")
                .lectureHours("3")
                .totalCapacity(50)
                .lectureDescription("컴퓨터 네트워크에 대한 기본 개념과 기술을 학습합니다.")
                .enrolledCount(0)
                .department(department)
                .professor(professor)
                .subject(subject)
                .enrollments(Collections.emptyList())
                .schedules(Collections.emptyList())
                .build();

        Mockito.when(lectureRepository.findByLectureNumber(anyString())).thenReturn(Optional.of(lecture));
        Mockito.doNothing().when(enrollmentService).enroll(anyInt(), anyInt());

        mockMvc.perform(post("/enrollment/by-code")
                        .param("studentId", "1")
                        .param("lectureNumber", "21032-001"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("신청 완료"));
    }


    /**
     * 수강 신청 취소 테스트
     */
    @Test
    @DisplayName("수강 신청 취소 테스트")
    public void cancelTest() throws Exception {
        Mockito.doNothing().when(enrollmentService).cancel(anyInt(), anyInt());

        mockMvc.perform(delete("/enrollment/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("취소 완료"));
    }

    /**
     * 수강 신청 내역 조회 테스트
     */
    @Test
    @DisplayName("수강 신청 내역 조회 테스트")
    public void testGetEnrollments() throws Exception {
        Mockito.when(enrollmentService.getEnrollments(anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/enrollment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("수강신청 내역이 없습니다."));
    }


    /* ================================================================= */
    //                              강의 조회                            //
    /* ================================================================= */

    /**
     * 과목명으로 강의 조회 테스트
     */
    @Test
    @DisplayName("과목명으로 강의 조회 테스트")
    public void testGetLecturesBySubjectName() throws Exception {
        Mockito.when(enrollmentService.getLecturesBySubjectName(any())).thenReturn(List.of());

        mockMvc.perform(get("/enrollment/by-name")
                        .param("subjectName", "Math"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("강의 목록이 없습니다."));
    }


    /* ================================================================= */
    //                              정보 갱신                            //
    /* ================================================================= */

    /**
     * 시간표 갱신 테스트
     */
    @Test
    @DisplayName("시간표 갱신 테스트")
    public void testUpdateTimetable() throws Exception {
        Mockito.when(enrollmentService.updateTimetable(anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/enrollment/schedule")
                        .param("studentId", "1"))
                .andExpect(status().isCreated());
    }

    /**
     * 학점 갱신 테스트
     */
    @Test
    @DisplayName("학점 갱신 테스트")
    public void testUpdateCredits() throws Exception {
        Mockito.when(enrollmentService.updateCredits(anyInt())).thenReturn((CreditDTO) List.of());

        mockMvc.perform(get("/enrollment/credit")
                        .param("studentId", "1"))
                .andExpect(status().isCreated());
    }
}
