package edu.allinone.sugang.repository;

import edu.allinone.sugang.domain.AcademicStatus;
import edu.allinone.sugang.domain.GraduationRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraduationRequirementsRepository extends JpaRepository<GraduationRequirements, Integer> {

    List<GraduationRequirements> findByDepartmentId(Integer departmentId);

}