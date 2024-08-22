package edu.allinone.sugang.controller;

import edu.allinone.sugang.dto.SignInDTO;
import edu.allinone.sugang.dto.response.AdminDTO;
import edu.allinone.sugang.dto.response.StudentDTO;
import edu.allinone.sugang.service.AdminLoginService;
import edu.allinone.sugang.service.StudentLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final StudentLoginService studentLoginService;
    private final AdminLoginService adminLoginService;

    @PostMapping("/login")
    public StudentDTO signIn(@RequestBody SignInDTO signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        StudentDTO studentDTO = studentLoginService.signIn(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", studentDTO.getAccessToken(), studentDTO.getRefreshToken());
        return studentDTO;
    }

    @PostMapping("/admin/login")
    public AdminDTO adminSignIn(@RequestBody SignInDTO signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        AdminDTO adminDTO = adminLoginService.adminSignIn(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", adminDTO.getAccessToken(), adminDTO.getRefreshToken());
        return adminDTO;
    }

}
