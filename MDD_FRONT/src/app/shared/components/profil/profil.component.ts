import { Component, OnInit, OnDestroy, inject } from '@angular/core';
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
import { Subscription } from 'rxjs';

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
export class ProfilComponent implements OnInit, OnDestroy {
  profileForm: FormGroup;
  updateSuccess = false;
  updateError = '';
  formSubmitted = false;
  themes: Theme[] = [];
  subscribedThemes: number[] = [];
  private themeService = inject(ThemeService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private subscription: Subscription = new Subscription();

  constructor(private fb: FormBuilder, private location: Location) {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
    });
  }

  ngOnInit(): void {
    this.loadUserProfile();
    this.loadThemes();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  isSubscribed(themeId: number): boolean {
    return this.subscribedThemes.includes(themeId);
  }

  unsubscribeTheme(id: number): void {
    const unsubscribeSub = this.themeService.unsubscribeTheme(id).subscribe({
      next: () => {
        this.subscribedThemes = this.subscribedThemes.filter(themeId => themeId !== id);
        this.themes = this.themes.filter(theme => theme.id !== id);
      }
    });

    this.subscription.add(unsubscribeSub);
  }

  private loadThemes(): void {
    const themesSub = this.themeService.getThemes().subscribe({
      next: (themes: Theme[]) => {
        this.themes = themes;
        this.checkSubscriptions();
      }
    });

    this.subscription.add(themesSub);
  }

  private checkSubscriptions(): void {
    this.themes.forEach(theme => {
      const subCheckSub = this.themeService.isSubscribed(theme.id).subscribe({
        next: (isSubscribed: boolean) => {
          if (isSubscribed) {
            this.subscribedThemes.push(theme.id);
          } else {
            this.themes = this.themes.filter(t => t.id !== theme.id);
          }
        }
      });

      this.subscription.add(subCheckSub);
    });
  }

  private loadUserProfile(): void {
    const userProfileSub = this.authService.getUtilisateurProfile().subscribe({
      next: (utilisateur: Utilisateur) => {
        this.profileForm.patchValue({
          username: utilisateur.username,
          email: utilisateur.email,
        });
      }
    });

    this.subscription.add(userProfileSub);
  }

  onSubmit(): void {
    this.formSubmitted = true;
    if (this.profileForm.valid) {
      const formData = this.profileForm.value;
      this.updateUserProfile(formData);
    }
  }

  private updateUserProfile(data: Utilisateur): void {
    const updateProfileSub = this.authService.updateUtilisateurProfile(data).subscribe({
      next: () => {
        this.updateSuccess = true;
        this.updateError = '';
        this.loadUserProfile();
      },
      error: (error) => {
        this.updateSuccess = false;
        this.updateError = error.message;
      },
    });

    this.subscription.add(updateProfileSub);
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
