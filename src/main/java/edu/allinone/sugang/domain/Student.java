package edu.allinone.sugang.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class Student {
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

//    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Basket> baskets;

    /* -------------------------------------------- */
    /* ----------------- Functions ---------------- */
    /* -------------------------------------------- */
    // 신청 가능 학점 증가
    public void increaseMaxCredits(int credits) {
        this.maxCredits += credits;
    }

    // 신청 가능 학점 감소
    public void decreaseMaxCredits(int credits) {
        this.maxCredits -= credits;
    }
}