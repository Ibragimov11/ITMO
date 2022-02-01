package ru.itmo.wp.web.page;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Page {
    private final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    public void before(HttpServletRequest request, Map<String, Object> view) {
        putUser(request, view);
        putMessage(request, view);
    }

    public void after(HttpServletRequest request, Map<String, Object> view) {
        view.put("userCount", userService.findCount());
    }

    private void putUser(HttpServletRequest request, Map<String, Object> view) {
        User user = getUser(request);
        if (user != null) {
            view.put("user", user);
        }
    }

    private void putMessage(HttpServletRequest request, Map<String, Object> view) {
        String message = (String) request.getSession().getAttribute("message");
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            request.getSession().removeAttribute("message");
        }
    }

    protected void setMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute("message", message);
    }

    protected void setUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute("user", user);
    }

    protected User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }
}
