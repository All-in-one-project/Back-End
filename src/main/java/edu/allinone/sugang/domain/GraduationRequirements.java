package edu.allinone.sugang.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "graduation_requirements")
public class GraduationRequirements {

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
    @JoinColumn(name = "department_id")
    private Department department;

    /* -------------------------------------------- */
    /* ------------ Information Column ------------ */
    /* -------------------------------------------- */

    @Column(name = "required_credits")
    private Integer requiredCredits;

    @Column(name = "earned_credits")
    private Integer earnedCredits;

    @Column(name = "status")
    private String status;


}
