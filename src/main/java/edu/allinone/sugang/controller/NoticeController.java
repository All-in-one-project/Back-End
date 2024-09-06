package edu.allinone.sugang.controller;

import edu.allinone.sugang.dto.NoticeDetailDTO;
import edu.allinone.sugang.dto.NoticeSummaryDTO;
import edu.allinone.sugang.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 공지사항 목록 조회
    @GetMapping
    public List<NoticeSummaryDTO> getAllNotices() {
        return noticeService.getAllNotices();
    }

    // 특정 공지사항 조회
    @GetMapping("/{noticeId}")
    public NoticeDetailDTO getNoticeById(@PathVariable Integer noticeId) {
        return noticeService.getNoticeById(noticeId);
    }

}
