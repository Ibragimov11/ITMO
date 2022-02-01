package ru.itmo.wp.lesson8.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.lesson8.domain.User;
import ru.itmo.wp.lesson8.form.UserCredentials;
import ru.itmo.wp.lesson8.form.UserStatusForm;
import ru.itmo.wp.lesson8.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(UserCredentials userCredentials) {
        User user = new User();
        user.setLogin(userCredentials.getLogin());
        user.setDisable(false);
        userRepository.save(user);
        userRepository.updatePassword(user.getId(), userCredentials.getLogin(), userCredentials.getPassword());
        return user;
    }

    @Transactional
    public boolean setStatus(UserStatusForm userStatusForm) {
        Optional<User> user = userRepository.findById(userStatusForm.getUserId());
        user.ifPresent(u -> {
            u.setDisable(userStatusForm.isUserStatus());
            userRepository.save(u);
        });
        return user.isPresent();
    }

    public boolean isLoginVacant(String login) {
        return userRepository.countByLogin(login) == 0;
    }

    public User findByLoginAndPassword(String login, String password) {
        return login == null || password == null ? null : userRepository.findByLoginAndPassword(login, password);
    }

    public User findById(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByIdDesc();
    }
}
