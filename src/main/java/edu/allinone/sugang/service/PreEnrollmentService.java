package edu.allinone.sugang.service;

import edu.allinone.sugang.domain.Basket;
import edu.allinone.sugang.domain.Enrollment;
import edu.allinone.sugang.domain.Lecture;
import edu.allinone.sugang.domain.Student;
import edu.allinone.sugang.dto.response.BasketInfoDTO;
import edu.allinone.sugang.dto.response.LectureTimeDTO;
import edu.allinone.sugang.repository.BasketRepository;
import edu.allinone.sugang.repository.LectureRepository;
import edu.allinone.sugang.repository.ScheduleRepository;
import edu.allinone.sugang.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreEnrollmentService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final BasketRepository basketRepository;
    private final ScheduleRepository scheduleRepository;

    /* ================================================================= */
    //                             예비 수강 신청                           //
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

        // 2. 신청 가능 학점 확인 (30학점 초과 시 예외 발생)
        int totalBasketCredits = basketRepository.findByStudentId(studentId).stream()
                .mapToInt(basket -> basket.getLecture().getSubject().getCredit())
                .sum();

        int remainingCredits = 30 - totalBasketCredits;

        if (remainingCredits - lecture.getSubject().getCredit() < 0) {
            throw new IllegalArgumentException("신청 가능 학점을 초과했습니다.");
        }

        // 3. 이미 신청한 과목인지 확인
        if (basketRepository.findByStudentIdAndLectureId(studentId, lectureId).isPresent()) {
            throw new IllegalArgumentException("이미 신청한 강의입니다.");
        }

        // 4. 예비 수강 신청
        basketRepository.save(Basket.builder()
                .student(student)
                .lecture(lecture)
                .build()
        );
    }

    /**
     * 장바구니 삭제
     */
    @Transactional
    public void deleteBasket(Integer studentId, Integer lectureId) {
        // 1. 장바구니 정보 가져오기
        Basket basket = basketRepository.findByStudentIdAndLectureId(studentId, lectureId)
                .orElseThrow(() -> new IllegalArgumentException("예비 수강 신청하지 않은 강의입니다."));

        // 2. 장바구니 삭제
        basketRepository.delete(basket);
    }

    /* ================================================================= */
    //                               정보 조회                             //
    /* ================================================================= */

    /**
     * 장바구니 내역 조회
     */
    @Transactional
    public List<BasketInfoDTO> getBasket(Integer studentId) {
        // 1. 학생 정보 가져오기
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생이 존재하지 않습니다."));

        // 2. 예비 수강 신청 내역 조회
        List<Basket> baskets = basketRepository.findByStudentId(studentId);

        return baskets.stream()
                .map(basket -> BasketInfoDTO.builder()
                        .id(basket.getId())
                        .studentId(basket.getStudent().getId())
                        .lectureId(basket.getLecture().getId())
                        .lectureNumber(basket.getLecture().getLectureNumber())
                        .lectureRoom(basket.getLecture().getLectureRoom())
                        .subjectName(basket.getLecture().getSubject().getSubjectName())
                        .subjectDivision(basket.getLecture().getSubject().getSubjectDivision())
                        .professorName(basket.getLecture().getProfessor().getProfessorName())
                        .lectureTimes(scheduleRepository.findByLectureId(basket.getLecture().getId()).stream()
                                .map(schedule -> LectureTimeDTO.builder()
                                        .dayOfWeek(schedule.getDayOfWeek())
                                        .firstTime(schedule.getFirstTime())
                                        .lastTime(schedule.getLastTime())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Basket 조회
     */
    @Transactional
    public List<Basket> getBaskets(Integer studentId) {
        // 1. 학생 정보 가져오기
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생이 존재하지 않습니다."));

        // 2. 예비 수강 신청 내역 조회
        return basketRepository.findByStudentId(studentId);
    }

    /* ================================================================= */
    //                               정보 갱신                             //
    /* ================================================================= */

    /**
     * 예비 수강 신청 시간표 갱신
     */
    @Transactional
    public List<LectureTimeDTO> updateTimetable(Integer studentId) {
        // 1. 예비 수강 신청 내역 가져오기
        List<Basket> baskets = getBaskets(studentId);

        // 2. 예비 수강 신청 내역에서 lectureId만 추출
        List<Integer> lectureIds = baskets.stream()
                .map(basket -> basket.getLecture().getId())
                .toList();

        // 3. LectureTimeDTO 리스트 생성 후 반환
        return lectureIds.stream()
                .flatMap(lectureId -> scheduleRepository.findByLectureId(lectureId).stream())
                .map(schedule -> LectureTimeDTO.builder()
                        .dayOfWeek(schedule.getDayOfWeek())
                        .firstTime(schedule.getFirstTime())
                        .lastTime(schedule.getLastTime())
                        .build())
                .collect(Collectors.toList());
    }
}
