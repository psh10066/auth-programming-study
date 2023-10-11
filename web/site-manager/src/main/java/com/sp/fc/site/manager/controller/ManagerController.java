package com.sp.fc.site.manager.controller;

import com.sp.fc.user.service.SchoolService;
import com.sp.fc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final SchoolService schoolService;
    private final UserService userService;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("schoolCount", schoolService.count());
        model.addAttribute("teacherCount", userService.countTeacher());
        model.addAttribute("studentCount", userService.countStudent());
        model.addAttribute("auth",
            SecurityContextHolder.getContext().getAuthentication().toString()
                .replace(" [", "<br/>[").replace("(", "<br/>(").replace(",", ",<br/>").replace(" ", "")
        );

        return "/manager/index";
    }
}
