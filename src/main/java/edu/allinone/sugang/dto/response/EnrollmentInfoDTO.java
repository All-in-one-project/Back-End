package edu.allinone.sugang.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentInfoDTO {
    private Integer id;
    private Integer studentId;
    private Integer lectureId;
    private String lectureNumber;
    private String subjectDivision;
}
