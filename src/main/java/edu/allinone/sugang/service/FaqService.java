package edu.allinone.sugang.service;

import edu.allinone.sugang.dto.FaqDetailDTO;
import edu.allinone.sugang.dto.FaqSummaryDTO;
import edu.allinone.sugang.repository.FaqRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaqService {

    private final FaqRepository faqRepository;

    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    // FAQ 목록 조회
    public List<FaqSummaryDTO> getAllFaqs() {
        return faqRepository.findAllByOrderByIdDesc().stream()
                .map(faq -> {
                    FaqSummaryDTO dto = new FaqSummaryDTO();
                    dto.setQuestion(faq.getQuestion());
                    return dto;
                }).collect(Collectors.toList());
    }

    // 특정 FAQ 조회
    public FaqDetailDTO getFaqById(Integer faqId) {
        return faqRepository.findById(faqId)
                .map(faq -> {
                    FaqDetailDTO dto = new FaqDetailDTO();
                    dto.setQuestion(faq.getQuestion());
                    dto.setAnswer(faq.getAnswer());
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("FAQ not found"));
    }

}
