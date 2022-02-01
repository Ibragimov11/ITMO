package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;

    public PostPage(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/{id}")
    public String showPost(@PathVariable String id, Model model) {
        if (!id.matches("^[0-9]+$")) {
            return "PostPage";
        }

        Post post;
        try {
            post = postService.find(Long.parseLong(id));
        } catch (Exception e) {
            return "PostPage";
        }

        model.addAttribute("post", post);
        model.addAttribute("comment", new Comment());
        return "PostPage";
    }

    @PostMapping("/post/{id}")
    public String comment(@PathVariable String id,
                          Model model,
                          @Valid @ModelAttribute("comment") Comment comment,
                          BindingResult bindingResult,
                          HttpSession httpSession) {
        if (!id.matches("[0-9]+")) {
            return "PostPage";
        }

        Post post;
        try {
            post = postService.find(Long.parseLong(id));
        } catch (Exception e) {
            return "PostPage";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "PostPage";
        }

        if (post == null) {
            return "PostPage";
        }

        postService.comment(post.getId(), comment, getUser(httpSession));
        putMessage(httpSession, "Comment added");

        return "redirect:/post/" + id;
    }
}
