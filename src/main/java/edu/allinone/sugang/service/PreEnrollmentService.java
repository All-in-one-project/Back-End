package edu.allinone.sugang.service;

import edu.allinone.sugang.domain.Basket;
import edu.allinone.sugang.domain.Enrollment;
import edu.allinone.sugang.domain.Lecture;
import edu.allinone.sugang.domain.Student;
import edu.allinone.sugang.repository.BasketRepository;
import edu.allinone.sugang.repository.LectureRepository;
import edu.allinone.sugang.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PreEnrollmentService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final BasketRepository basketRepository;

    /* ================================================================= */
    //                           예비 수강 신청                          //
    /* ================================================================= */
    /**
     * 장바구니 담기
     */
    @Transactional
    public void basket(Integer studentId, Integer lectureId) {
        // 1. 강의 정보, 학생 정보 가져오기
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생이 존재하지 않습니다."));

        // 2. 신청 가능 학점 확인
        if (student.getBaskets().get - lecture.getSubject().getCredit() < 0) {
            throw new IllegalArgumentException("신청 가능 학점을 초과했습니다.");
        }

        // 3. 예비 수강 신청
        basketRepository.save(Basket.builder()
                .student(student)
                .lecture(lecture)
                .build()
        );

        // 5. 신청 인원 증가
        lecture.incrementEnrolledCount();

        // 6. 신청 가능 학점 감소
        student.decreaseMaxCredits(lecture.getSubject().getCredit());
    }
}
