package ru.itmo.wp.lesson8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itmo.wp.lesson8.domain.Notice;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexPage extends Page {
    @GetMapping({"", "/"})
    public String index(/*Model model*/) {
        return "IndexPage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        unsetUser(httpSession);
        setMessage(httpSession, "Good bye!");
        return "redirect:";
    }
}
