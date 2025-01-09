import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ArticleService } from '../../../../core/services/article.service';
import { CommentaireService } from '../../../../core/services/commentaire.service';
import { Article } from '../../../../core/models/interfaces/Article';
import { Commentaire } from '../../../../core/models/interfaces/Commentaire';
import { ThemeService } from '../../../../core/services/themeService';
import {AuthService} from '../../../../core/services/authService.service';
import {catchError, switchMap, tap} from 'rxjs/operators';
import {of} from 'rxjs';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-detail-article',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    FormsModule
  ],
  templateUrl: './detail-article.component.html'
})
export class DetailArticleComponent implements OnInit {
  article: Article | undefined = undefined;
  commentaires: Commentaire[] = [];
  themeTitle: string | undefined = undefined;
  newComment: string = '';

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private articleService = inject(ArticleService);
  private themeService = inject(ThemeService);
  private commentaireService = inject(CommentaireService);
  private authService = inject(AuthService);

  ngOnInit(): void {
    const articleId = this.route.snapshot.paramMap.get('id');
    if (articleId) {
      this.articleService.getArticleById(+articleId).subscribe((article) => {
        this.article = article;
        console.log("Article récupéré dans ngOnInit DetailArticleComponent :", article);
        this.loadCommentaires(+articleId);
        this.loadThemeTitle(article.themeId);
      });
    }
  }

  private loadThemeTitle(themeId: number): void {
    this.themeService.getThemeById(themeId).subscribe(
      (theme) => {
        this.themeTitle = theme.title;
      }
    );
  }

  private loadCommentaires(articleId: number): void {
    this.articleService.getCommentairesByArticleId(articleId).subscribe((commentaires) => {
      this.commentaires = commentaires;
      console.log(" idde l'author", this.commentaires[0].authorUsername);
      console.log("Commentaires récupérés pour l'article dans loadCommentaires :", commentaires);
    });
  }

  addComment(): void {

    if (this.newComment.trim() && this.article) {
      this.authService.getUtilisateurProfile().pipe(
        tap((utilisateur) => {
          console.log("User profile fetched:", utilisateur);
        }),
        switchMap((utilisateur) => {
          const commentaire: Commentaire = {
            description: this.newComment,
            articleId: this.article!.id,
            authorUsername: utilisateur.username,
            date: new Date().toISOString().split('T')[0]
          };

          return this.commentaireService.addCommentaire(commentaire, utilisateur.username);
        }),
        tap(() => {
          this.loadCommentaires(this.article!.id);
          this.newComment = '';
        }),
        catchError((error) => {
          return of(null);
        })
      ).subscribe();
    }
  }
  goBack(): void {
    this.router.navigate(['dashboard/articles']);
  }
}