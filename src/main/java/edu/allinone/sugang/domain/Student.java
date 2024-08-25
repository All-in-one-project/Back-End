package edu.allinone.sugang.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class Student implements UserDetails {
    /* -------------------------------------------- */
    /* -------------- Default Column -------------- */
    /* -------------------------------------------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* -------------------------------------------- */
    /* ------------ Information Column ------------ */
    /* -------------------------------------------- */
    @Column(unique = true, name = "student_number", length = 30, nullable = false)
    private String studentNumber;

    @Column(name = "student_password", length = 100, nullable = false)
    private String studentPassword;

    @Column(name = "student_name", length = 20, nullable = false)
    private String studentName;

    @Column(name = "grade", length = 30)
    private String grade;

    @Column(name = "max_credits")
    private int maxCredits;

    /* -------------------------------------------- */
    /* -------------- Relation Column ------------- */
    /* -------------------------------------------- */
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Basket> baskets;

    /* -------------------------------------------- */
    /* ----------------- Overriding --------------- */
    /* -------------------------------------------- */

    @Override
    public String getUsername() {
        return this.studentNumber;
    }

    @Override
    public String getPassword() {
        return this.studentPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 학생의 역할을 "USER"로 설정
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
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