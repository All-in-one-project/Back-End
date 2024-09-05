package edu.allinone.sugang.service;

import edu.allinone.sugang.domain.GraduationRequirements;
import edu.allinone.sugang.dto.CourseDTO;
import edu.allinone.sugang.dto.StudentStatusDTO;
import edu.allinone.sugang.repository.AcademicStatusRepository;
import edu.allinone.sugang.repository.CourseHistoryRepository;
import edu.allinone.sugang.repository.GraduationRequirementsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class MyPageService {

    private final AcademicStatusRepository academicStatusRepository;
    private final CourseHistoryRepository courseHistoryRepository;
    private final GraduationRequirementsRepository graduationRequirementsRepository;

    public MyPageService(GraduationRequirementsRepository graduationRequirementsRepository, CourseHistoryRepository courseHistoryRepository, AcademicStatusRepository academicStatusRepository) {
        this.academicStatusRepository = academicStatusRepository;
        this.courseHistoryRepository = courseHistoryRepository;
        this.graduationRequirementsRepository = graduationRequirementsRepository;
    }

    // 현재 재학 상태, 졸업까지 남은 학기, 전체 학점 평점, 전공 학점 평점, 졸업 요건 충족 여부
    public List<StudentStatusDTO> getStudentStatusByStudentId(Integer studentId, Integer departmentId) {
        List<GraduationRequirements> requirementsList = graduationRequirementsRepository.findByDepartmentId(departmentId);

        if (requirementsList.isEmpty()) {
            return List.of();
        }

        GraduationRequirements requirements = requirementsList.get(0);

        return academicStatusRepository.findByStudentId(studentId).stream().map(academicStatus -> {
            StudentStatusDTO dto = new StudentStatusDTO();

            // 재학 상태
            String graduationStatus = academicStatus.getGraduationStatus();

            // 졸업 학점 조건 확인
            if (requirements.getEarnedCredits() >= requirements.getRequiredCredits()) {
                // 졸업 요건 충족 시 재학 상태 변경
                graduationStatus = "졸업";
            }

            dto.setGraduationStatus(graduationStatus); // 재학 상태 설정
            dto.setCurrentSemester(academicStatus.getCurrentSemester()); // 현재 학기 설정
            dto.setTotalSemesters(academicStatus.getTotalSemesters()); // 전체 학기 설정

            // 남은 학기 계산
            int remainingSemesters = academicStatus.getTotalSemesters() - academicStatus.getCurrentSemester();
            dto.setRemainingSemesters(remainingSemesters); // 남은 학기 설정

            dto.setGpa(academicStatus.getGpa()); // 전체 학점 평점 설정
            dto.setMajorGpa(academicStatus.getMajorGpa()); // 전공 학점 평점 설정

            // 졸업에 필요한 학점 및 획득한 학점 설정
            dto.setStatus(requirements.getStatus()); // 졸업 요건 충족 상태 설정

            return dto;

        }).collect(Collectors.toList());
    }

    // 과목을 수강한 학기, 과목 이름, 과목 구분 반환
    public List<CourseDTO> getCourseBySemester(Integer semester) {
        return courseHistoryRepository.findBySemester(semester)
                .map(courseHistory -> {
                    CourseDTO dto = new CourseDTO();

                    dto.setCourseId(courseHistory.getLecture().getId());
                    dto.setSemester(courseHistory.getSemester()); // 과목을 수강한 학기 반환

                    return dto;
                })
                .stream()
                .collect(Collectors.toList());
    }

}

