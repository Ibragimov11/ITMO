package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user, String passwordSha);
    User find(long id);
    User findByLogin(String login);
    User findByEmail(String login);
    User findByLoginAndPasswordSha(String login, String passwordSha);
    User findByEmailAndPasswordSha(String email, String passwordSha);
    List<User> findAll();
    long findCount();
}
