package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.EventRepository;
import ru.itmo.wp.model.repository.impl.EventRepositoryImpl;
import ru.itmo.wp.model.service.EventService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class LogoutPage extends Page {
    private final EventService eventService = new EventService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        if (getUser(request) != null) {
            User user = getUser(request);
            request.getSession().removeAttribute("user");
            Event event = new Event();
            event.setUserId(user.getId());
            event.setType(Event.Type.LOGOUT);

            eventService.addEvent(event);
            setMessage(request, "Good bye. Hope to see you soon!");
        }
        throw new RedirectException("/index");
    }
}
