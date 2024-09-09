package edu.allinone.sugang.service;

import edu.allinone.sugang.dto.LectureDTO;
import edu.allinone.sugang.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class MyPageService {

    @Autowired
    private LectureRepository lectureRepository;

    // 특정 과목 ID와 대상 학년에 해당하는 강의 목록을 반환하는 메서드
    public List<LectureDTO> getLecturesBySubjectIdAndTargetGrade(Integer subjectId, String targetGrade) {
        // Lecture 엔티티 리스트를 LectureDTO 리스트로 변환하여 반환
        return lectureRepository.findBySubjectIdAndSubject_TargetGrade(subjectId, targetGrade).stream().map(lecture -> {
            // Lecture 엔티티를 LectureDTO로 변환
            LectureDTO dto = new LectureDTO();
            dto.setId(lecture.getId()); // 강의 ID 설정
            dto.setLectureNumber(lecture.getLectureNumber()); // 강의 번호 설정
            dto.setLectureRoom(lecture.getLectureRoom()); // 강의실 설정
            dto.setLectureHours(lecture.getLectureHours()); // 강의 시간 설정
            dto.setTotalCapacity(lecture.getTotalCapacity()); // 총 정원 설정
            dto.setLectureDescription(lecture.getLectureDescription()); // 강의 설명 설정
            dto.setSubjectId(lecture.getSubject().getId()); // 강의가 속한 과목 ID 설정
            dto.setProfessorId(lecture.getProfessor().getId()); // 강의를 담당하는 교수 ID 설정
            return dto; // 변환된 DTO 반환
        }).collect(Collectors.toList()); // 변환된 DTO 리스트를 수집하여 반환
    }
}
