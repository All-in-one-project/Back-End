package edu.allinone.sugang.dto.global;

/**
 * 대기열 응답 DTO 클래스
 */
public class EnqueueResponseDTO {
    private long position; // 대기열 위치
    private long waitingTime; // 예상 대기 시간

    /**
     * 기본 생성자
     */
    public EnqueueResponseDTO() {}

    /**
     * 매개변수를 받는 생성자
     *
     * @param position 대기열 위치
     * @param waitingTime 예상 대기 시간
     */
    public EnqueueResponseDTO(long position, long waitingTime) {
        this.position = position;
        this.waitingTime = waitingTime;
    }

    /**
     * 대기열 위치를 반환
     *
     * @return 대기열 위치
     */
    public long getPosition() {
        return position;
    }

    /**
     * 대기열 위치를 설정
     *
     * @param position 대기열 위치
     */
    public void setPosition(long position) {
        this.position = position;
    }

    /**
     * 예상 대기 시간을 반환
     *
     * @return 예상 대기 시간
     */
    public long getWaitingTime() {
        return waitingTime;
    }

    /**
     * 예상 대기 시간을 설정
     *
     * @param waitingTime 예상 대기 시간
     */
    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }
}
