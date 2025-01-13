import { Injectable, inject } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {map, Observable, of, throwError} from 'rxjs';
import {Utilisateur} from '../models/interfaces/Utilisateur';
import {catchError, tap} from 'rxjs/operators';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private registerUrl = 'http://localhost:8080/auth/register';
  private loginUrl = 'http://localhost:8080/auth/login';
  private profil = 'http://localhost:8080/auth/me';
  private logoutUrl = 'http://localhost:8080/auth/logout';
  private checkUserNameEmailUrl = 'http://localhost:8080/auth/exists/';

  private currentUserId: number | null = null;

  registerUser(data: { username: string; email: string; password: string }): Observable<any> {
    return this.http.post(this.registerUrl, data);
  }

  loginUser(data: { identifier: string; password: string }): Observable<any> {
    return this.http.post<{ token: string }>(this.loginUrl, data).pipe(
      tap(response => {
        localStorage.setItem('authToken', response.token);
        this.getUtilisateurProfile().subscribe(user => {
          localStorage.setItem('username', user.username);
          localStorage.setItem('authUserId', user.id.toString());
        });
      })
    );
  }

  getUtilisateurProfile(): Observable<Utilisateur> {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Utilisateur>(this.profil, {headers});
  }

  updateUtilisateurProfile(data: Utilisateur): Observable<{ token: string }> {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put<{ token: string }>(this.profil, data, { headers }).pipe(
      tap(response => {
        localStorage.setItem('authToken', response.token);
      })
    );
  }

  getCurrentUserId(): Observable<number> {
    return this.getUtilisateurProfile().pipe(
      map((user: Utilisateur) => user.id)
    );
  }

  logout(): void {
    const token = localStorage.getItem('authToken');
    if (token) {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http.post(`${this.logoutUrl}`, {}, {headers}).subscribe({
        next: () => {
          localStorage.removeItem('authToken');
          this.router.navigate(['']);
        }
      });
    }
  }

  checkEmailUsernameExists(email: string, username: string): Observable<boolean> {
    const url = `${this.checkUserNameEmailUrl}${email}/${username}`;
    return this.http.get<{ message: string }>(url, {observe: 'response'}).pipe(
      map(response => {
        const message = response.body?.message;
        if (response.status === 200 && message === "Disponible") {
          return false;
        } else if (response.status === 409 && message === "Le nom d'utilisateur ou l'email est déjà pris") {
          return true;
        } else {
          throw new Error('Unexpected response status: ' + response.status);
        }
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 409) {
          return throwError(() => new Error("Le nom d'utilisateur ou l'email est déjà pris"));
        } else {
          return throwError(() => new Error("Erreur inconnue avec le statut: " + error.status));
        }
      })
    );
  }
}
