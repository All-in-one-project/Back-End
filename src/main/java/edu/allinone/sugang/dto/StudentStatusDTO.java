package edu.allinone.sugang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudentStatusDTO {

    // 재학 상태
    private String graduationStatus;
    private Integer currentSemester;
    private Integer totalSemesters;
    private Integer remainingSemesters; // 남은 학기

    // 학점 평점
    private Float gpa;
    private Float majorGpa;

    // 졸업 요건
    private String status; // 졸업 요건 충족 상태

}
