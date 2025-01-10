import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import {AuthService} from '../../../core/services/authService.service';
import {RouterModule, Router} from '@angular/router';
import {catchError, switchMap, tap} from 'rxjs/operators';
import {of} from 'rxjs';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    RouterModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  registerForm: FormGroup;
  registrationSuccess = false;
  registrationError = '';
  formSubmitted = false;
  emailErrorMessage: string | null = null;
  private authService = inject(AuthService);


  constructor(private fb: FormBuilder,private router: Router) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern('(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}'),
        ],
      ],
    });
  }

  onSubmit(): void {
    this.formSubmitted = true;
    this.registrationError = '';
    if (this.registerForm.valid) {
      const formData = this.registerForm.value;
      this.registerUser(formData);
    }
  }

  private registerUser(data: { username: string; email: string; password: string }) {
    this.authService.checkEmailUsernameExists(data.email, data.username).pipe(
      switchMap((exists: boolean) => {
        if (!exists) {
          return this.authService.registerUser(data).pipe(
            tap(() => {
              this.registrationSuccess = true;
              this.registrationError = '';
              this.router.navigate(['/login']);
            }),
            catchError((error: any) => {
              this.registrationError = error.message;
              return of(null);
            })
          );
        } else {
          this.registrationError = "Nom d'utilisateur ou email déjà existant";
          return of(null);
        }
      }),
      catchError((error: any) => {
        this.registrationError = error.message;
        return of(null);
      })
    ).subscribe();
  }
  goBack(): void {
    this.router.navigate(['']);
  }
}
