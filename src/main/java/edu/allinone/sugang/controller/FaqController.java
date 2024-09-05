package edu.allinone.sugang.controller;

import edu.allinone.sugang.dto.FaqDetailDTO;
import edu.allinone.sugang.dto.FaqSummaryDTO;
import edu.allinone.sugang.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/faq")
public class FaqController {

    @Autowired
    private FaqService faqService;

    // 공지사항 목록 조회
    @GetMapping
    public List<FaqSummaryDTO> getAllFaqs() {
        return faqService.getAllFaqs();
    }

    // 특정 공지사항 조회
    @GetMapping("/{faqId}")
    public FaqDetailDTO getFaqById(@PathVariable Integer faqId) {
        return faqService.getFaqById(faqId);
    }

}