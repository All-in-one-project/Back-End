package edu.allinone.sugang.service;

import edu.allinone.sugang.dto.NoticeDetailDTO;
import edu.allinone.sugang.dto.NoticeSummaryDTO;
import edu.allinone.sugang.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // 전체 공지사항 목록 조회 (id, 제목, 날짜 반환)
    public List<NoticeSummaryDTO> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(notice -> {
                    NoticeSummaryDTO dto = new NoticeSummaryDTO();
                    dto.setId(notice.getId());
                    dto.setTitle(notice.getTitle());
                    dto.setNoticeTime(notice.getNoticeTime());
                    return dto;
                }).collect(Collectors.toList());
    }

    // 특정 공지사항 조회 (제목, 내용, 날짜 반환)
    public NoticeDetailDTO getNoticeById(Integer id) {
        return noticeRepository.findById(id)
                .map(notice -> {
                    NoticeDetailDTO dto = new NoticeDetailDTO();
                    dto.setTitle(notice.getTitle());
                    dto.setContent(notice.getContent());
                    dto.setNoticeTime(notice.getNoticeTime());
                    return dto;
                }).orElseThrow(() -> new RuntimeException("Notice not found"));
    }
}
