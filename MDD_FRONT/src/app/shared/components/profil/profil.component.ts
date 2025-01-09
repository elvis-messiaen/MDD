import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/authService.service';
import { Utilisateur } from '../../../core/models/interfaces/Utilisateur';
import { Theme } from '../../../core/models/interfaces/Theme';
import { ThemeService } from '../../../core/services/themeService';
import { Location } from '@angular/common';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    RouterModule
  ],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']
})
export class ProfilComponent implements OnInit {
  profileForm: FormGroup;
  updateSuccess = false;
  updateError = '';
  formSubmitted = false;
  themes: Theme[] = [];
  subscribedThemes: number[] = [];
  private themeService = inject(ThemeService);

  private authService = inject(AuthService);
  private router = inject(Router);

  constructor(private fb: FormBuilder,private location: Location) {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
    });
  }

  ngOnInit(): void {
    this.loadUserProfile();
    this.loadThemes();
  }

  isSubscribed(themeId: number): boolean {
    return this.subscribedThemes.includes(themeId);
  }

  unsubscribeTheme(id: number): void {
    this.themeService.unsubscribeTheme(id).subscribe({
      next: () => {
        this.subscribedThemes = this.subscribedThemes.filter(themeId => themeId !== id);
        this.themes = this.themes.filter(theme => theme.id !== id);
      }
    });
  }

  private loadThemes(): void {
    this.themeService.getThemes().subscribe({
      next: (themes: Theme[]) => {
        this.themes = themes;
        this.checkSubscriptions();
      }
    });
  }

  private checkSubscriptions(): void {
    this.themes.forEach(theme => {
      this.themeService.isSubscribed(theme.id).subscribe({
        next: (isSubscribed: boolean) => {
          if (isSubscribed) {
            this.subscribedThemes.push(theme.id);
          } else {
            this.themes = this.themes.filter(t => t.id !== theme.id);
          }
        }
      });
    });
  }

  private loadUserProfile(): void {
    this.authService.getUtilisateurProfile().subscribe({
      next: (utilisateur: Utilisateur) => {
        this.profileForm.patchValue({
          username: utilisateur.username,
          email: utilisateur.email,
        });
      }
    });
  }

  onSubmit(): void {
    this.formSubmitted = true;
    if (this.profileForm.valid) {
      const formData = this.profileForm.value;
      this.updateUserProfile(formData);
    }
  }

  private updateUserProfile(data: Utilisateur): void {
    this.authService.updateUtilisateurProfile(data).subscribe({
      next: (response) => {
        this.updateSuccess = true;
        this.updateError = '';
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.updateSuccess = false;
        this.updateError = error.message;
      },
    });
  }

  disconnected(): void {
    localStorage.removeItem('authToken');
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goBack(): void {
    this.location.back();
  }

}
