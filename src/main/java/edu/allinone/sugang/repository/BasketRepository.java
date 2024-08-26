package edu.allinone.sugang.repository;

import edu.allinone.sugang.domain.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Integer> {
    // 학생id와 강의id로 수강 신청 정보 가져오기
    Optional<Basket> findByStudentIdAndLectureId(Integer studentId, Integer lectureId);

    // 학생id로 수강 신청 내역 리스트 가져오기
    List<Basket> findByStudentId(Integer studentId);
}