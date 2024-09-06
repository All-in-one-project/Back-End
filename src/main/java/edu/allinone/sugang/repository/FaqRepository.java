package edu.allinone.sugang.repository;

import edu.allinone.sugang.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer> {

    // 자주 묻는 질문 리스트를 최신순으로 조회
    List<Faq> findAllByOrderByIdDesc();

}

