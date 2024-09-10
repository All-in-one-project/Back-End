package edu.allinone.sugang.dto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SubjectDTO {
    private String subjectName;
    private String subjectDivision;
    private String subjectCode;
    private String professorName;
    private String lectureTime;
    private String lectureDescription;
    private Integer id;
    private String targetGrade;
    private Integer lectureId; // 강의 ID 추가

}
