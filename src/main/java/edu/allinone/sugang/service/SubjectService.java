package edu.allinone.sugang.service;

import edu.allinone.sugang.domain.Lecture;
import edu.allinone.sugang.domain.Professor;
import edu.allinone.sugang.domain.Schedule;
import edu.allinone.sugang.domain.Subject;
import edu.allinone.sugang.dto.SubjectDTO;
import edu.allinone.sugang.repository.LectureRepository;
import edu.allinone.sugang.repository.ProfessorRepository;
import edu.allinone.sugang.repository.ScheduleRepository;
import edu.allinone.sugang.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;



    // 특정 학부 ID에 해당하는 과목 목록을 반환하는 메서드
    public List<SubjectDTO> getSubjectsByDepartmentId(Integer departmentId) {
        // Department 엔티티 리스트를 SubjectDTO 리스트로 변환하여 반환
        List<Subject> subjects = subjectRepository.findByDepartmentId(departmentId);
        return subjects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 강의명으로 과목을 조회하는 메서드
    public List<SubjectDTO> getSubjectsByLectureName(String lectureName) {
        // Subject 엔티티 리스트를 SubjectDTO 리스트로 변환하여 반환
        List<Subject> subjects = subjectRepository.findBySubjectNameContaining(lectureName);
        return subjects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Subject 엔티티를 SubjectDTO로 변환하는 메서드
    private SubjectDTO convertToDTO(Subject subject) {
        SubjectDTO dto = new SubjectDTO(); // 새로운 SubjectDTO 객체 생성
        dto.setSubjectName(subject.getSubjectName()); // 과목 이름 설정
        dto.setSubjectDivision(subject.getSubjectDivision()); // 과목 구분 설정

        // Lecture 정보를 통해 과목 코드, 교수 이름, 강의 시간 설정
        Lecture lecture = lectureRepository.findBySubjectId(subject.getId()).stream().findFirst().orElse(null);
        if (lecture != null) {
            dto.setId(subject.getId()); // 과목 ID 설정
            dto.setSubjectCode(lecture.getLectureNumber()); //과목코드 설정
            dto.setLectureDescription(lecture.getLectureDescription()); //강의계획서 설정
            dto.setTargetGrade(subject.getTargetGrade());  // 대상 학년 설정

            // 교수 이름 설정
            Professor professor = lecture.getProfessor();
            dto.setProfessorName("교수 " + professor.getProfessorName());

            // 강의 시간 설정
            List<Schedule> schedules = scheduleRepository.findByLectureId(lecture.getId());
            String lectureTime = schedules.stream()
                    .map(schedule -> schedule.getDayOfWeek() + " " + convertTimeToPeriod(schedule.getFirstTime(), schedule.getLastTime()))
                    .collect(Collectors.joining(", "));
            dto.setLectureTime(lectureTime);
        }

        return dto;
    }

    // 시작 시간과 끝나는 시간을 "1-3교시" 형식으로 변환하는 메서드
    private String convertTimeToPeriod(LocalTime firstTime, LocalTime lastTime) {
        int startPeriod = convertToPeriod(firstTime);
        int endPeriod = convertToPeriod(lastTime.plusMinutes(-1)); // 마지막 교시를 올바르게 계산하도록 수정
        return startPeriod + "-" + endPeriod + "교시";
    }

    // 시간을 교시로 변환하는 메서드 (예: 09:00 -> 1교시, 10:00 -> 2교시)
    private int convertToPeriod(LocalTime time) {
        int hour = time.getHour();
        if (hour >= 9 && hour < 12) {
            return hour - 8;  // 9시 시작 -> 1교시, 10시 시작 -> 2교시, 11시 시작 -> 3교시
        } else if (hour >= 13 && hour < 16) {
            return hour - 12 + 3;  // 13시 시작 -> 4교시, 14시 시작 -> 5교시, 15시 시작 -> 6교시
        }
        return -1;  // 예외 처리
    }
}