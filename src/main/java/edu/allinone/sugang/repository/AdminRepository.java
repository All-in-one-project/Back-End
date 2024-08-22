package edu.allinone.sugang.repository;

import edu.allinone.sugang.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {

    // admin_name 필드를 기준으로 'Admin' 엔티티 조회하는 메서드
    Optional<Admin> findByAdminName(String admin_name);
}
