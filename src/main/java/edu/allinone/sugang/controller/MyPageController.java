package edu.allinone.sugang.controller;

import edu.allinone.sugang.dto.LectureDTO;
import edu.allinone.sugang.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class MyPageController {
    @Autowired
    private LectureService lectureService;

    @GetMapping("/{subjectId}")
    public List<LectureDTO> getLecturesBySubjectIdAndTargetGrade(@PathVariable Integer subjectId, @PathVariable String targetGrade) {
        return lectureService.getLecturesBySubjectIdAndTargetGrade(subjectId, targetGrade);
    }
}
