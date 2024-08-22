package edu.allinone.sugang.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AdminDTO {
    private String adminName;
    private Integer adminId;
    private String accessToken;
    private String refreshToken;
}
