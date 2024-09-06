package edu.allinone.sugang.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "academic_status")
public class AcademicStatus {

    /* -------------------------------------------- */
    /* -------------- Default Column -------------- */
    /* -------------------------------------------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* -------------------------------------------- */
    /* -------------- Relation Column ------------- */
    /* -------------------------------------------- */

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    /* -------------------------------------------- */
    /* ------------ Information Column ------------ */
    /* -------------------------------------------- */

    @Column(name = "current_semester")
    private Integer currentSemester;

    @Column(name = "total_semesters")
    private Integer totalSemesters;

    @Column(name = "graduation_status")
    private String graduationStatus;

    @Column(name = "gpa")
    private Float gpa;

    @Column(name = "major_gpa")
    private Float majorGpa;

}