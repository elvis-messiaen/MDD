import { Injectable, inject } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import { Theme } from '../models/interfaces/Theme';
import { AuthService } from './authService.service';
import {catchError, switchMap, map} from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080/api/theme';

  getThemes(): Observable<Theme[]> {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Theme[]>(this.apiUrl, { headers });
  }

  getThemeById(themeId: number): Observable<Theme> {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Theme>(`${this.apiUrl}/${themeId}`, { headers }).pipe(
      catchError((error) => {
        let errorMessage = 'Erreur inconnue';
        if (error.error && error.error.message) {
          errorMessage = error.error.message;
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }


  subscribeTheme(themeId: number): Observable<any> {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.authService.getCurrentUserId().pipe(
      switchMap((utilisateurId: number) => {
        const payload = { utilisateurId };
        return this.http.post(`${this.apiUrl}/${themeId}/subscribe`, payload, { headers: headers, observe: 'response' }).pipe(
          map((response: HttpResponse<any>) => {
            if (response.status === 201 || response.status === 200) {
              return response.body.message;
            } else {
              throw new Error(`Unexpected response status: ${response.status}`);
            }
          }),
          catchError((error) => {
            let errorMessage = 'Erreur inconnue';
            if (error.error && error.error.message) {
              errorMessage = error.error.message;
            }
            return throwError(() => new Error(errorMessage));
          })
        );
      })
    );
  }

  unsubscribeTheme(themeId: number): Observable<any> {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.authService.getCurrentUserId().pipe(
      switchMap((utilisateurId: number) => {
        const payload = { utilisateurId };
        return this.http.post(`${this.apiUrl}/${themeId}/unsubscribe`, payload, { headers: headers, observe: 'response' }).pipe(
          map((response: HttpResponse<any>) => {
            if (response.status === 200) {
              return response.body.message;
            } else {
              throw new Error(`Unexpected response status: ${response.status}`);
            }
          }),
          catchError((error) => {
            let errorMessage = 'Erreur inconnue';
            if (error.error && error.error.message) {
              errorMessage = error.error.message;
            }
            return throwError(() => new Error(errorMessage));
          })
        );
      })
    );
  }

  isSubscribed(themeId: number): Observable<boolean> {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No auth token found');
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.authService.getCurrentUserId().pipe(
      switchMap((utilisateurId: number) => {
        const payload = { utilisateurId };
        return this.http.post(`${this.apiUrl}/${themeId}/isSubscribed`, payload, { headers: headers }).pipe(
          map((response: any) => {
            return response.subscribed;
          }),
          catchError((error) => {
            let errorMessage = 'Erreur inconnue';
            if (error.error && error.error.message) {
              errorMessage = error.error.message;
            }
            return throwError(() => new Error(errorMessage));
          })
        );
      })
    );
  }
}
