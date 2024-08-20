package edu.allinone.sugang.controller;

import edu.allinone.sugang.dto.DepartmentDTO;
import edu.allinone.sugang.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/{collegeId}")
    public List<DepartmentDTO> getDepartmentsByCollegeId(@PathVariable Integer collegeId) {
        return departmentService.getDepartmentsByCollegeId(collegeId);
    }

    @GetMapping //전체 부서 조회 API 추가
    public List<DepartmentDTO> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

}
