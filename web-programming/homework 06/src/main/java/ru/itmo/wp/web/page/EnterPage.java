package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.EventService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class EnterPage extends Page {
    private final UserService userService = new UserService();
    private final EventService eventService = new EventService();

    private void enter(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        String loginOrEmail = request.getParameter("loginOrEmail");
        String password = request.getParameter("password");

        userService.validateEnter(loginOrEmail, password);
        User user;
        if (loginOrEmail.contains("@")) {
            user = userService.findByEmailAndPassword(loginOrEmail, password);
        } else {
            user = userService.findByLoginAndPassword(loginOrEmail, password);
        }

        Event event = new Event();
        event.setUserId(user.getId());
        event.setType(Event.Type.ENTER);

        setUser(request, user);
        eventService.addEvent(event);
        setMessage(request, "Hello, " + user.getLogin());
        throw new RedirectException("/index");
    }
}
