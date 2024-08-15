package edu.allinone.sugang.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lecture")
public class Lecture {
    /* -------------------------------------------- */
    /* -------------- Default Column -------------- */
    /* -------------------------------------------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* -------------------------------------------- */
    /* ------------ Information Column ------------ */
    /* -------------------------------------------- */
    @Column(name = "lecture_number", length = 20, nullable = false)
    private String lectureNumber;

    @Column(name = "lecture_room", length = 20)
    private String lectureRoom;

    @Column(name = "lecture_hours", length = 10, nullable = false)
    private String lectureHours;

    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;

    @Column(name = "lecture_description", length = 200)
    private String lectureDescription; // 강의 설명 추가

    @Column(name = "enrolled_count", nullable = false)
    private int enrolledCount;

    /* -------------------------------------------- */
    /* -------------- Relation Column ------------- */
    /* -------------------------------------------- */
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Basket> baskets;

    /* -------------------------------------------- */
    /* ----------------- Functions ---------------- */
    /* -------------------------------------------- */
    // 신청 인원 감소
    public void decrementEnrolledCount() {
        this.enrolledCount--;
    }

    // 신청 인원 증가
    public void incrementEnrolledCount() {
        this.enrolledCount++;
    }
}
