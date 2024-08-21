package edu.allinone.sugang.controller;

import edu.allinone.sugang.dto.global.ResponseDTO;
import edu.allinone.sugang.dto.request.EnrollmentDTO;
import edu.allinone.sugang.dto.response.EnrollmentInfoDTO;
import edu.allinone.sugang.dto.response.LectureSummaryDTO;
import edu.allinone.sugang.dto.response.LectureTimeDTO;
import edu.allinone.sugang.repository.LectureRepository;
import edu.allinone.sugang.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final LectureRepository lectureRepository;

    /* ================================================================= */
    //                              수강 신청                            //
    /* ================================================================= */

    /**
     * 학생id, 과목id를 입력 받고 수강 신청 하기
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED) // 상태를 201 Created로 설정
    public ResponseDTO<?> enroll(@RequestBody EnrollmentDTO requestDto) {
       try {
            enrollmentService.enroll(requestDto.getStudentId(), requestDto.getLectureId());
            return new ResponseDTO<>(HttpStatus.CREATED.value(), "신청 완료", requestDto);
        } catch (IllegalArgumentException e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
           return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
       }
    }

    /**
     * 학생id, 과목코드 입력 받고 수강 신청 하기
     */
    @PostMapping("/by-code")
    @ResponseStatus(HttpStatus.CREATED) // 상태를 201 Created로 설정
    public ResponseDTO<?> enrollByCode(@RequestParam Integer studentId, @RequestParam String lectureNumber) {
        EnrollmentDTO enrollmentDTO;
        try {
            Integer lectureId = lectureRepository.findByLectureNumber(lectureNumber)
                    .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다.")).getId();
            enrollmentService.enroll(studentId, lectureId);
            enrollmentDTO = new EnrollmentDTO(studentId, lectureId);
            return new ResponseDTO<>(HttpStatus.CREATED.value(), "신청 완료", enrollmentDTO);
        } catch (IllegalArgumentException e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * 수강 신청 취소 하기
     */
    @DeleteMapping("/{studentId}/{lectureId}")
    public ResponseDTO<?> cancel(@PathVariable Integer studentId, @PathVariable Integer lectureId) {
        try {
            enrollmentService.cancel(studentId, lectureId);
            EnrollmentDTO requestDto = new EnrollmentDTO(studentId, lectureId);
            return new ResponseDTO<>(HttpStatus.OK.value(), "취소 완료", requestDto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 수강 신청 내역 조회
     */
    @GetMapping("/{studentId}")
    public ResponseDTO<?> getEnrollments(@PathVariable Integer studentId) {
        try {
            List<EnrollmentInfoDTO> enrollments = enrollmentService.getEnrollmentDTO(studentId);
            if (enrollments.isEmpty()) {
                // 수강 신청 내역이 없으면
                return new ResponseDTO<>(HttpStatus.OK.value(), "수강신청 내역이 없습니다.");
            } else {
                return new ResponseDTO<>(HttpStatus.OK.value(), enrollments);
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /* ================================================================= */
    //                              강의 조회                            //
    /* ================================================================= */

    /**
     * 과목명으로 강의 조회
     */
    @GetMapping("/by-name")
    public ResponseDTO<?> getLecturesBySubjectName(@RequestParam String subjectName) {
        try {
            List<LectureSummaryDTO> lectures =  enrollmentService.getLecturesBySubjectName(subjectName);
            if (lectures.isEmpty()) {
                // 강의 목록이 없으면 메세지 전달
                return new ResponseDTO<>(HttpStatus.OK.value(), "강의 목록이 없습니다.");
            } else {
                return new ResponseDTO<>(HttpStatus.OK.value(), lectures);
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /* ================================================================= */
    //                              정보 갱신                            //
    /* ================================================================= */

    /**
     * 시간표 갱신
     */
    @GetMapping("/schedule")
    @ResponseStatus(HttpStatus.CREATED) // 상태를 201 Created로 설정
    public ResponseDTO<?> updateTimetable(@RequestParam Integer studentId) {
        try {
            List<LectureTimeDTO> lectures = enrollmentService.updateTimetable(studentId);
            return new ResponseDTO<>(HttpStatus.CREATED.value(), lectures);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 학점 갱신
     */
    @GetMapping("/credit")
    @ResponseStatus(HttpStatus.CREATED) // 상태를 201 Created로 설정
    public ResponseDTO<?> updateCredits(@RequestParam Integer studentId) {
        try {
            return new ResponseDTO<>(HttpStatus.CREATED.value(), enrollmentService.updateCredits(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
