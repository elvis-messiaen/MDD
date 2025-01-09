package com.MDD_BACK.repository;

import com.MDD_BACK.entity.Commentaire;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentaireRepository extends CrudRepository<Commentaire, Long> {

    List<Commentaire> findByArticleId(Long articleId);

}
