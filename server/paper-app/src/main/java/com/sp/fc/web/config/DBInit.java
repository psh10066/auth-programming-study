package com.sp.fc.web.config;

import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.School;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.SchoolService;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final SchoolService schoolService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        School defaultSchool = schoolService.findSchool(1L).orElseGet(() ->
            schoolService.save(School.builder()
                .name("테스트 학교")
                .city("서울")
                .build())
        );

        userService.findByEmail("admin@test.com").or(() -> {
            User admin = User.builder()
                .name("admin")
                .email("admin@test.com")
                .password(passwordEncoder.encode("1234"))
                .enabled(true)
                .school(defaultSchool)
                .build();
            admin = userService.save(admin);
            admin.setAuthorities(Set.of(
                new Authority(admin.getUserId(), Authority.ROLE_ADMIN)
            ));
            return Optional.of(userService.save(admin));
        });
        User teacherData = userService.findByEmail("teacher@test.com").orElseGet(() -> {
            User teacher = User.builder()
                .name("teacher")
                .email("teacher@test.com")
                .password(passwordEncoder.encode("1234"))
                .enabled(true)
                .school(defaultSchool)
                .build();
            teacher = userService.save(teacher);
            teacher.setAuthorities(Set.of(
                new Authority(teacher.getUserId(), Authority.ROLE_TEACHER)
            ));
            return userService.save(teacher);
        });
        userService.findByEmail("student@test.com").or(() -> {
            User student = User.builder()
                .name("student")
                .email("student@test.com")
                .password(passwordEncoder.encode("1234"))
                .enabled(true)
                .teacher(teacherData)
                .school(defaultSchool)
                .build();
            student = userService.save(student);
            student.setAuthorities(Set.of(
                new Authority(student.getUserId(), Authority.ROLE_STUDENT)
            ));
            return Optional.of(userService.save(student));
        });
    }


}
