package edu.allinone.sugang.service;

import edu.allinone.sugang.domain.Admin;
import edu.allinone.sugang.domain.Student;
import edu.allinone.sugang.repository.AdminRepository;
import edu.allinone.sugang.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> studentOptional = studentRepository.findByStudentNumber(username);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return new User(
                    student.getStudentNumber(),
                    student.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("USER"))
            );
        }

        Optional<Admin> adminOptional = adminRepository.findByAdminName(username);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return new User(
                    admin.getAdminName(),
                    admin.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ADMIN"))
            );
        }

        throw new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
    }
}

