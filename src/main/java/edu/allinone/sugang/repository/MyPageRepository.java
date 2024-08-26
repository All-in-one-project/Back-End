package edu.allinone.sugang.repository;

import edu.allinone.sugang.domain.Lecture;
import edu.allinone.sugang.domain.Student;

import java.util.List;
import java.util.Optional;

public interface MyPageRepository {

    List<Student> findBySubjectIdAndSubject_TargetGrade(Integer subjectId, String targetGrade);
    List<Lecture> findBySubject_SubjectNameContaining(String subjectName);
    Optional<Lecture> findByLectureNumber(String lectureNumber); // lectureNumber로 강의 조회
}
