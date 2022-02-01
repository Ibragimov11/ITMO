package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class ArticleService {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void validateCreate(Article article) throws ValidationException {
        if (article.getTitle() == null || article.getTitle().isBlank()) {
            throw new ValidationException("Title is required");
        }
        if (article.getText() == null || article.getText().isBlank()) {
            throw new ValidationException("Text is required");
        }
        if (articleRepository.findByTitle(article.getTitle()) != null) {
            throw new ValidationException("Title is already in use");
        }
    }

    public List<ArticleView> findViews() {
        List<Article> articles = articleRepository.findAll();
        ArrayList<ArticleView> views = new ArrayList<>();
        for (Article article : articles) {
            User user = userRepository.find(article.getUserId());
            views.add(new ArticleView(article, user));
        }

        return views;
    }

    public void create(Article article) {
        articleRepository.save(article);
    }

    public static class ArticleView {
        private final Article article;
        private final User user;

        public ArticleView(Article article, User user) {
            this.article = article;
            this.user = user;
        }

        public Article getArticle() {
            return article;
        }

        public User getUser() {
            return user;
        }
    }
}
