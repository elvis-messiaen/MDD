import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import {AuthService} from '../../../core/services/authService.service';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  styleUrls: ['./login.component.scss'],
  imports: [CommonModule, ReactiveFormsModule, MatIcon]
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      identifier: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm && this.loginForm.valid) {
      this.authService.loginUser(this.loginForm.value).pipe(
        tap(response => {
          this.router.navigate(['/dashboard/articles']);
        }),
        catchError(error => {
          return of(error);
        })
      ).subscribe();
    }
  }

  goBack(): void {
    this.router.navigate(['']);
  }
}
