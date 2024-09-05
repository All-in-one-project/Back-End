package edu.allinone.sugang.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeDetailDTO {

    private String title;        // 공지사항 제목
    private String content;      // 공지사항 내용
    private LocalDateTime noticeTime; // 공지사항 작성 날짜

}

