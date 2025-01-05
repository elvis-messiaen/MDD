package com.MDD_BACK.service.impl;

import com.MDD_BACK.entity.Commentaire;
import com.MDD_BACK.repository.CommentaireRepository;
import com.MDD_BACK.service.ICommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentaireServiceImpl implements ICommentaireService {

    @Autowired
    private final CommentaireRepository commentaireRepository;

    @Autowired
    public CommentaireServiceImpl(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }

    @Override
    public Commentaire create(Commentaire commentaire) {
        return commentaireRepository.save(commentaire);
    }

    @Override
    public Optional<Commentaire> findById(Long id) {
        return commentaireRepository.findById(id);
    }

    @Override
    public List<Commentaire> findAll() {
        return (List<Commentaire>) commentaireRepository.findAll();
    }

    @Override
    public Commentaire update(Long id, Commentaire commentaire) {
        if (commentaireRepository.existsById(id)) {
            commentaire.setId(id);
            return commentaireRepository.save(commentaire);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        commentaireRepository.deleteById(id);
    }
}