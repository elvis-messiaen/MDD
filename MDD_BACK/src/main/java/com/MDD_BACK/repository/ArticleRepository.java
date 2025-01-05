package com.MDD_BACK.repository;

import com.MDD_BACK.entity.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    boolean existsByTitle(String title);
}
