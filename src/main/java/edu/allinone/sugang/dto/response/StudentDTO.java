package edu.allinone.sugang.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StudentDTO {
    private String studentNumber;
    private Integer studentId;
    private Integer departmentId;
    private String studentName;
    private String grade;
    private int maxCredits;
    private String accessToken;
    private String refreshToken;
}
