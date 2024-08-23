package edu.allinone.sugang.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
public class Admin implements UserDetails {
    /* -------------------------------------------- */
    /* -------------- Default Column -------------- */
    /* -------------------------------------------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* -------------------------------------------- */
    /* ------------ Information Column ------------ */
    /* -------------------------------------------- */
    @Column(unique = true, name = "admin_name", length = 20, nullable = false)
    private String adminName;

    @Column(name = "admin_password")
    private String adminPassword;

    /* -------------------------------------------- */
    /* ----------------- Overriding --------------- */
    /* -------------------------------------------- */

    @Override
    public String getUsername() {
        return this.adminName;
    }

    @Override
    public String getPassword() {return this.adminPassword; };

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 관리자의 역할을 "ADMIN"으로 설정
        return Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}