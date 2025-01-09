import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Article } from '../models/interfaces/Article';
import { Commentaire } from '../models/interfaces/Commentaire';
import { catchError, map } from 'rxjs/operators';
import { ArticleToSend } from '../models/interfaces/ArticleToSend';
import { Theme } from '../models/interfaces/Theme';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/article';
  private urlArticle = 'http://localhost:8080/api/commentaire';
  private urlTheme = 'http://localhost:8080/api/theme';

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  createArticle(article: ArticleToSend): Observable<ArticleToSend> {
    const headers = this.getAuthHeaders();
    const currentUserId = localStorage.getItem('authUserId');

    if (currentUserId) {
      article.authorId = parseInt(currentUserId, 10);
    } else {
      console.log('Aucun utilisateur trouvé');
    }

    return this.http.post<ArticleToSend>(this.apiUrl, article, { headers }).pipe(
      catchError((error) => {
        let errorMessage = 'Erreur inconnue';
        if (error.error && error.error.message) {
          errorMessage = error.error.message;
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  getAllArticles(): Observable<Article[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<Article[]>(this.apiUrl, { headers });
  }

  getArticlesByThemes(themeIds: number[]): Observable<Article[]> {
    return this.getAllArticles().pipe(
      map(articles => articles.filter(article => themeIds.includes(article.themeId)))
    );
  }

  getArticleById(id: number): Observable<Article> {
    const headers = this.getAuthHeaders();
    return this.http.get<Article>(`${this.apiUrl}/${id}`, { headers });
  }

  getCommentairesByArticleId(articleId: number): Observable<Commentaire[]> {
    const headers = this.getAuthHeaders();
    console.log('articleId', articleId);
    return this.http.get<Commentaire[]>(`${this.urlArticle}?articleId=${articleId}`, { headers });
  }

  getAllThemes(): Observable<Theme[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<Theme[]>(this.urlTheme, { headers });
  }
}
