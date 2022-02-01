package ru.itmo.wp.lesson8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.lesson8.domain.User;
import ru.itmo.wp.lesson8.service.UserService;

@Controller
public class UserPage extends Page {
    private final UserService userService;

    public UserPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public String index(@PathVariable String id, Model model) {
        if (!id.matches("[0-9]+")) {
            return "UserPage";
        }

        User user;
        try {
            user = userService.findById(Long.parseLong(id));
        } catch (Exception e) {
            return "UserPage";
        }

        model.addAttribute("user", user);
        return "UserPage";
    }
}
