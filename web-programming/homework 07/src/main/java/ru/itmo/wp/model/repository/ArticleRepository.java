package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;

import java.util.List;

public interface ArticleRepository {
    Article find(long id);
    Article findByTitle(String title);
    List<Article> findAll();
    void save(Article article);
}
