package com.MDD_BACK.repository;

import com.MDD_BACK.entity.Theme;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ThemeRepository extends CrudRepository<Theme, Long> {
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Theme t WHERE t.title = ?1")
    boolean existsByTitle(String title);
}
