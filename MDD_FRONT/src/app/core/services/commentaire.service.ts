import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Commentaire } from '../models/interfaces/Commentaire';

@Injectable({
  providedIn: 'root'
})
export class CommentaireService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/commentaire';

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  addCommentaire(commentaire: Commentaire, authorUsername: string): Observable<Commentaire> {
    const headers = this.getAuthHeaders();
    return this.http.post<Commentaire>(`${this.apiUrl}?authorUsername=${authorUsername}`, commentaire, { headers });
  }
}
