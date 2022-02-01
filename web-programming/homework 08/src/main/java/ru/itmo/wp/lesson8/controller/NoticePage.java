package ru.itmo.wp.lesson8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.lesson8.form.NoticeForm;
import ru.itmo.wp.lesson8.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class NoticePage extends Page {
    private final NoticeService noticeService;

    public NoticePage(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/notice")
    public String noticeGet(Model model) {
        model.addAttribute("noticeForm", new NoticeForm());
        return "NoticePage";
    }

    @PostMapping("/notice")
    public String noticePost(@Valid @ModelAttribute("noticeForm") NoticeForm noticeForm,
                             BindingResult bindingResult,
                             HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            return "NoticePage";
        }

        if (getUser(httpSession) == null) {
            setMessage(httpSession, "You should be enter");
            return "NoticePage";
        }

        noticeService.addNotice(noticeForm);
        setMessage(httpSession, "Notice successfully added");

        return "NoticePage";
    }

}
