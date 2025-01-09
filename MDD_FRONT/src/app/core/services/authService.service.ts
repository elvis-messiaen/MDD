import { Injectable, inject } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {Utilisateur} from '../models/interfaces/Utilisateur';
import {tap} from 'rxjs/operators';
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
    return this.http.get<Utilisateur>(this.profil, { headers });
  }

  updateUtilisateurProfile(data: Utilisateur): Observable<Utilisateur> {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put<Utilisateur>(this.profil, data, { headers });
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
      this.http.post(`${this.logoutUrl}`, {}, { headers }).subscribe({
        next: () => {
          localStorage.removeItem('authToken');
          this.router.navigate(['']);
        }
      });
    }
  }



}
