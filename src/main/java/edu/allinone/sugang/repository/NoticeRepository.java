package edu.allinone.sugang.repository;

import edu.allinone.sugang.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    // 특정 조건에 맞는 공지사항을 조회하는 커스텀 메서드를 정의할 수 있습니다.
    // 예를 들어, 최근 작성된 공지사항을 날짜순으로 조회
    List<Notice> findAllByOrderByNoticeTimeDesc();

}
