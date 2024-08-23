package edu.allinone.sugang.repository;

import edu.allinone.sugang.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    // student_number 필드를 기준으로 'Student' 엔티티 조회하는 메서드
    Optional<Student> findByStudentNumber(String student_number);
}