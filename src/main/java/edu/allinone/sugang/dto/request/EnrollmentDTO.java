package edu.allinone.sugang.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentDTO {
    private Integer studentId;
    private Integer lectureId;
}
