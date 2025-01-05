package com.MDD_BACK.service;

import com.MDD_BACK.entity.Article;
import java.util.List;
import java.util.Optional;

public interface IArticleService {

    Optional<Article> findById(Long id);

    List<Article> findAll();

    Article create(Article article);

    void deleteById(Long id);

    Article update(Long id, Article article);
}
