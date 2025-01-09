package com.MDD_BACK.service.impl;

import com.MDD_BACK.entity.Commentaire;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.repository.CommentaireRepository;
import com.MDD_BACK.repository.UtilisateurRepository;
import com.MDD_BACK.service.ICommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CommentaireServiceImpl implements ICommentaireService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CommentaireServiceImpl.class);

    @Autowired
    private final CommentaireRepository commentaireRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    public CommentaireServiceImpl(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }

    @Override
    public Commentaire create(Commentaire commentaire, String authorUsername) {
        Utilisateur author = utilisateurRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur not found"));
        commentaire.setAuthor(author);
        commentaire.setDate(LocalDate.now());
        return commentaireRepository.save(commentaire);
    }


    @Override
    public List<Commentaire> findByArticleId(Long id) {
        List<Commentaire> commentaires = commentaireRepository.findByArticleId(id);
        return commentaireRepository.findByArticleId(id);
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