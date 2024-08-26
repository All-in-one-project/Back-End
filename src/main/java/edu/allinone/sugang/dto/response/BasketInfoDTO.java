package edu.allinone.sugang.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketInfoDTO {
    private Integer id;

    private Integer studentId;

    private Integer lectureId;
    private String lectureNumber;
    private String lectureRoom;

    private String subjectName;
    private String subjectDivision;

    private String professorName;

    private List<LectureTimeDTO> lectureTimes;
}
