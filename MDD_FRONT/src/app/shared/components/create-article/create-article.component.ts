import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Theme } from '../../../core/models/interfaces/Theme';
import { ArticleService } from '../../../core/services/article.service';
import { ThemeService } from '../../../core/services/themeService';
import { lastValueFrom } from 'rxjs';
import { AuthService } from '../../../core/services/authService.service';
import { CommonModule } from '@angular/common';
import { ArticleToSend } from '../../../core/models/interfaces/ArticleToSend';
import {MatIcon} from '@angular/material/icon';
@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  standalone: true,
  styleUrls: ['./create-article.component.scss'],
  imports: [ReactiveFormsModule, CommonModule, MatIcon],
})
export class CreateArticleComponent implements OnInit {
  selectedThemeIndex: number = 1;
  articleForm: FormGroup;
  themes: Theme[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private articleService: ArticleService,
    private themeService: ThemeService,
    private authService: AuthService,
    private router: Router
  ) {
    this.articleForm = new FormGroup({
      title: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      themeId: new FormControl('', Validators.required),
    });
  }
  ngOnInit(): void {
    this.loadThemes();
  }

  async loadThemes(): Promise<void> {
    this.isLoading = true;
    try {
      this.themes = await lastValueFrom(this.themeService.getThemes());
    } catch (error) {
      this.errorMessage = 'Erreur lors du chargement des thèmes';
    } finally {
      this.isLoading = false;
    }
  }
  onThemeChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.selectedThemeIndex = target.selectedIndex - 1;

    if (this.selectedThemeIndex < 0 || this.selectedThemeIndex >= this.themes.length) {
      this.errorMessage = 'Veuillez sélectionner un thème valide';
      return;
    }
    this.errorMessage = '';
  }

  async onSubmit(): Promise<void> {
    if (this.articleForm.invalid) {
      this.errorMessage = 'Tous les champs sont obligatoires';
      return;
    }

    if (this.selectedThemeIndex < 0 || this.selectedThemeIndex >= this.themes.length) {
      this.errorMessage = 'Veuillez sélectionner un thème valide';
      return;
    }

    const selectedTheme = this.themes[this.selectedThemeIndex];
    const currentDate = new Date().toISOString().split('T')[0];

    try {
      const authorId = await lastValueFrom(this.authService.getCurrentUserId());
      const articleToSend: ArticleToSend = {
        title: this.articleForm.value.title,
        description: this.articleForm.value.description,
        authorId: authorId,
        date: currentDate,
        themeId: selectedTheme.id,
        commentaires: [],
      };

      await lastValueFrom(this.articleService.createArticle(articleToSend));
      this.successMessage = 'Article créé avec succès';
      this.router.navigate(['/dashboard/articles']);
    } catch (error) {
      this.errorMessage = "Erreur lors de la création de l'article";
    }
  }

  getErrorMessage(control: string): string {
    if (this.articleForm.get(control)?.hasError('required')) {
      return 'Ce champ est obligatoire';
    }
    return '';
  }

  goBack(): void {
    this.router.navigate(['/dashboard/articles']);
  }
}
