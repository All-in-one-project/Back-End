package edu.allinone.sugang.controller;

import edu.allinone.sugang.dto.global.ResponseDTO;
import edu.allinone.sugang.dto.request.EnrollmentDTO;
import edu.allinone.sugang.dto.response.BasketInfoDTO;
import edu.allinone.sugang.dto.response.LectureTimeDTO;
import edu.allinone.sugang.service.PreEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class PreEnrollmentController {

    private final PreEnrollmentService preEnrollmentService;

    /* ================================================================= */
    //                             예비 수강 신청                           //
    /* ================================================================= */

    /**
     * 학생id, 과목id를 입력 받고 장바구니 담기
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED) // 상태를 201 Created로 설정
    public ResponseDTO<?> enroll(@RequestBody EnrollmentDTO requestDto) {
        try {
            preEnrollmentService.basket(requestDto.getStudentId(), requestDto.getLectureId());
            return new ResponseDTO<>(HttpStatus.CREATED.value(), "신청 완료", requestDto);
        } catch (IllegalArgumentException e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * 장바구니 삭제
     */
    @DeleteMapping("/{student_id}/{lecture_id}")
    public ResponseDTO<?> delete(@PathVariable Integer student_id, @PathVariable Integer lecture_id) {
        try {
            preEnrollmentService.deleteBasket(student_id, lecture_id);
            return new ResponseDTO<>(HttpStatus.OK.value(), "삭제 완료");
        } catch (IllegalArgumentException e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /* ================================================================= */
    //                               정보 조회                             //
    /* ================================================================= */

    /**
     * 학생id로 장바구니 내역 조회
     */
    @GetMapping("/{student_id}")
    public ResponseDTO<?> getBasket(@PathVariable Integer student_id) {
        try {
            List<BasketInfoDTO> baskets = preEnrollmentService.getBasket(student_id);
            if (baskets.isEmpty()) {
                // 장바구니 내역이 없으면
                return new ResponseDTO<>(HttpStatus.OK.value(), "장바구니 내역이 없습니다.");
            } else {
                return new ResponseDTO<>(HttpStatus.OK.value(), baskets);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /* ================================================================= */
    //                               정보 갱신                             //
    /* ================================================================= */

    /**
     * 장바구니 시간표 갱신
     */
    @GetMapping("/schedule")
    @ResponseStatus(HttpStatus.CREATED) // 상태를 201 Created로 설정
    public ResponseDTO<?> updateTimetable(@RequestParam Integer studentId) {
        try {
            List<LectureTimeDTO> lectures = preEnrollmentService.updateTimetable(studentId);
            return new ResponseDTO<>(HttpStatus.CREATED.value(), lectures);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
