package edu.allinone.sugang.repository;

import edu.allinone.sugang.domain.CourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Integer> {
    Optional<CourseHistory> findBySemester(Integer semester);
}
