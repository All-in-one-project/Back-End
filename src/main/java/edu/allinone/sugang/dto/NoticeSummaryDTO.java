package edu.allinone.sugang.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeSummaryDTO {

    private Integer id;          // 공지사항 ID
    private String title;        // 공지사항 제목
    private LocalDateTime noticeTime; // 공지사항 작성 날짜

}

