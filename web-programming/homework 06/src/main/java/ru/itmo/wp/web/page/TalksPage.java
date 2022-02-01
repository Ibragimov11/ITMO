package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.TalkService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TalksPage extends Page {
    UserService userService = new UserService();
    TalkService talkService = new TalkService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        if (getUser(request) != null) {
            view.put("talks", talkService.findAll());
            view.put("users", userService.findAll());
        } else {
            setMessage(request, "You should enter");
            throw new RedirectException("/index");
        }
    }

    private void submit(HttpServletRequest request, Map<String, Object> view) {
        if (getUser(request) == null) {
            setMessage(request, "You need to be authorized to talk");
            throw new RedirectException("/index");
        }

        Talk talk = new Talk();
        String text = request.getParameter("text");
        if (text == null || text.isBlank()) {
            setMessage(request, "Message should be is not empty");
            throw new RedirectException("/talks");
        }

        talk.setText(text);
        talk.setTargetUserId(Long.parseLong(request.getParameter("targetUserId")));
        talk.setSourceUserId(getUser(request).getId());

        talkService.addTalk(talk);

        setMessage(request, "The message was successfully sent");
        throw new RedirectException("/talks");
    }
}
