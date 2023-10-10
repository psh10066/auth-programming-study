package com.sp.fc.site.student.controller;

import com.sp.fc.site.student.controller.vo.StudentSignUpForm;
import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.SchoolService;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class StudentSignupController {

    private final SchoolService schoolService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup/student")
    public String signUp(Model model) {
        model.addAttribute("site", "student");
        model.addAttribute("cityList", schoolService.cities());
        return "/student/signup";
    }

    @PostMapping(value = "/signUp/student", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String singup(StudentSignUpForm form, Model model) {
        final User student = User.builder()
            .name(form.getName())
            .email(form.getEmail())
            .password(passwordEncoder.encode(form.getPassword()))
            .grade(form.getGrade())
            .enabled(true)
            .build();
        schoolService.findSchool(form.getSchoolId()).ifPresent(student::setSchool);
        userService.findUser(form.getTeacherId()).ifPresent(student::setTeacher);

        User saved = userService.save(student);
        userService.addAuthority(saved.getUserId(), Authority.ROLE_STUDENT);
        model.addAttribute("site", "student");
        return "redirect:/login?site=student";
    }
}
