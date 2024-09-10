package edu.allinone.sugang.controller;

import edu.allinone.sugang.dto.SubjectDTO;
import edu.allinone.sugang.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// SubjectController에 대한 단위 테스트를 수행하기 위해 WebMvcTest를 사용
@WebMvcTest(SubjectController.class)
public class SubjectControllerTest {

    // MockMvc를 사용하여 HTTP 요청을 모의
    @Autowired
    private MockMvc mockMvc;

    // SubjectService를 모의(Mock)하여 테스트에서 사용할 수 있도록 설정
    @MockBean
    private SubjectService subjectService;

    // 테스트에서 사용할 과목(Subject) 목록을 저장할 변수
    private List<SubjectDTO> subjectList;

    // 각 테스트가 실행되기 전에 실행되는 설정 메서드
    @BeforeEach
    public void setUp() {
        // Mockito의 애노테이션을 초기화
        MockitoAnnotations.openMocks(this);

        // 테스트에서 사용할 과목 DTO 객체 생성
        SubjectDTO subject1 = new SubjectDTO();
        subject1.setId(1); // 과목 객체의 ID 설정
        subject1.setSubjectName("자료구조"); // 과목 이름 설정
        subject1.setSubjectDivision("전공"); // 과목 구분 설정
        subject1.setTargetGrade("2학년"); // 과목 대상 학년 설정
        //subject1.setHoursPerWeek(3); // 주당 강의 시간 설정
        //subject1.setCredit(3); // 학점 설정
        //subject1.setDepartmentId(1); // 과목이 속한 학부 ID 설정

        SubjectDTO subject2 = new SubjectDTO();
        subject2.setId(2); // 과목 객체의 ID 설정
        subject2.setSubjectName("운영체제"); // 과목 이름 설정
        subject2.setSubjectDivision("전공"); // 과목 구분 설정
        subject2.setTargetGrade("3학년"); // 과목 대상 학년 설정
        //subject2.setHoursPerWeek(3); // 주당 강의 시간 설정
        //subject2.setCredit(3); // 학점 설정
        //subject2.setDepartmentId(1); // 과목이 속한 학부 ID 설정

        // 과목 목록을 리스트에 추가
        subjectList = Arrays.asList(subject1, subject2);
    }

    // 특정 학부 ID에 해당하는 과목 목록을 반환하는 메서드를 테스트
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // 인증된 사용자 모의
    public void getSubjectsByDepartmentId_ShouldReturnSubjects() throws Exception {
        Integer departmentId = 1; // 테스트할 학부 ID 설정

        // 모의 서비스 메서드가 호출될 때 반환할 값을 설정
        when(subjectService.getSubjectsByDepartmentId(departmentId)).thenReturn(subjectList);

        // GET 요청을 보내고 응답이 예상대로 나오는지 검증
        mockMvc.perform(get("/api/subjects/{departmentId}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON)) // 요청의 Content-Type을 JSON으로 설정
                .andExpect(status().isOk()) // HTTP 상태가 200 OK인지 확인
                .andExpect(content().json("[{'id': 1, 'subjectName': '자료구조', 'subjectDivision': '전공', 'targetGrade': '2학년', 'hoursPerWeek': 3, 'credit': 3, 'departmentId': 1}, {'id': 2, 'subjectName': '운영체제', 'subjectDivision': '전공', 'targetGrade': '3학년', 'hoursPerWeek': 3, 'credit': 3, 'departmentId': 1}]")); // 응답 JSON이 예상과 일치하는지 확인
    }

    // 강의명으로 과목을 조회하는 메서드를 테스트
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // 인증된 사용자 모의
    public void getSubjectsByLectureName_ShouldReturnSubjects() throws Exception {
        String lectureName = "자료구조"; // 테스트할 강의명 설정

        // 모의 서비스 메서드가 호출될 때 반환할 값을 설정
        when(subjectService.getSubjectsByLectureName(lectureName)).thenReturn(subjectList);

        // GET 요청을 보내고 응답이 예상대로 나오는지 검증
        mockMvc.perform(get("/api/subjects/search")
                        .param("lectureName", lectureName) // 요청 파라미터로 강의명 설정
                        .contentType(MediaType.APPLICATION_JSON)) // 요청의 Content-Type을 JSON으로 설정
                .andExpect(status().isOk()) // HTTP 상태가 200 OK인지 확인
                .andExpect(content().json("[{'id': 1, 'subjectName': '자료구조', 'subjectDivision': '전공', 'targetGrade': '2학년', 'hoursPerWeek': 3, 'credit': 3, 'departmentId': 1}, {'id': 2, 'subjectName': '운영체제', 'subjectDivision': '전공', 'targetGrade': '3학년', 'hoursPerWeek': 3, 'credit': 3, 'departmentId': 1}]")); // 응답 JSON이 예상과 일치하는지 확인
    }
}