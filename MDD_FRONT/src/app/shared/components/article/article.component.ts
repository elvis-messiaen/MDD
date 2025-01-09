import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ThemeService } from '../../../core/services/themeService';
import { AuthService } from '../../../core/services/authService.service';
import { Article } from '../../../core/models/interfaces/Article';
import { Theme } from '../../../core/models/interfaces/Theme';
import { forkJoin, map } from 'rxjs';
import { ArticleService } from '../../../core/services/article.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-article',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    RouterModule
  ],
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit {
  articles: Article[] = [];
  themes: Theme[] = [];
  subscribedThemes: number[] = [];
  private articleService = inject(ArticleService);
  private themeService = inject(ThemeService);
  private authService = inject(AuthService);

  ngOnInit(): void {
    this.loadSubscribedThemes();
  }

  private loadSubscribedThemes(): void {
    this.themeService.getThemes().subscribe({
      next: (themes: Theme[]) => {
        this.themes = themes;
        const subscriptionChecks = this.themes.map(theme =>
          this.themeService.isSubscribed(theme.id).pipe(
            map(isSubscribed => ({ theme, isSubscribed }))
          )
        );

        forkJoin(subscriptionChecks).subscribe(results => {
          this.subscribedThemes = results.filter(result => result.isSubscribed).map(result => result.theme.id);
          this.loadArticles();
        });
      }
    });
  }

  private loadArticles(): void {
    const subscribedThemeIds = this.subscribedThemes;
    this.articleService.getArticlesByThemes(subscribedThemeIds).subscribe({
      next: (articles: Article[]) => {
        this.articles = articles;
      }
    });
  }
}
