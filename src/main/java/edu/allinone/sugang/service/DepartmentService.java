package edu.allinone.sugang.service;

import edu.allinone.sugang.dto.DepartmentDTO;
import edu.allinone.sugang.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    // 전체 부서 목록을 반환하는 메서드
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream().map(department -> {
            DepartmentDTO dto = new DepartmentDTO();
            dto.setId(department.getId());
            dto.setDepartmentName(department.getDepartmentName());
            dto.setCollegeId(department.getCollege().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    // 특정 단과대학 ID에 해당하는 학과 목록을 반환하는 메서드
    public List<DepartmentDTO> getDepartmentsByCollegeId(Integer collegeId) {
        // Department 엔티티 리스트를 DepartmentDTO 리스트로 변환하여 반환
        return departmentRepository.findByCollegeId(collegeId).stream().map(department -> {
            // Department 엔티티를 DepartmentDTO로 변환
            DepartmentDTO dto = new DepartmentDTO();
            dto.setId(department.getId()); // 학과 ID 설정
            dto.setDepartmentName(department.getDepartmentName()); // 학과 이름 설정
            dto.setCollegeId(department.getCollege().getId()); // 단과대학 ID 설정
            return dto; // 변환된 DTO 반환
        }).collect(Collectors.toList()); // 변환된 DTO 리스트를 수집하여 반환
    }
}
