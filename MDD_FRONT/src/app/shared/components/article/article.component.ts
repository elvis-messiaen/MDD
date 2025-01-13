import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ThemeService } from '../../../core/services/themeService';
import { AuthService } from '../../../core/services/authService.service';
import { Article } from '../../../core/models/interfaces/Article';
import { Theme } from '../../../core/models/interfaces/Theme';
import { forkJoin, map, Subscription } from 'rxjs';
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
export class ArticleComponent implements OnInit, OnDestroy {
  articles: Article[] = [];
  themes: Theme[] = [];
  subscribedThemes: number[] = [];
  private articleService = inject(ArticleService);
  private themeService = inject(ThemeService);
  private authService = inject(AuthService);
  private subscription: Subscription = new Subscription();

  ngOnInit(): void {
    this.loadSubscribedThemes();
  }

  private loadSubscribedThemes(): void {
    const themeSub = this.themeService.getThemes().subscribe({
      next: (themes: Theme[]) => {
        this.themes = themes;
        const subscriptionChecks = this.themes.map(theme =>
          this.themeService.isSubscribed(theme.id).pipe(
            map(isSubscribed => ({ theme, isSubscribed }))
          )
        );

        const forkJoinSub = forkJoin(subscriptionChecks).subscribe(results => {
          this.subscribedThemes = results.filter(result => result.isSubscribed).map(result => result.theme.id);
          this.loadArticles();
        });

        this.subscription.add(forkJoinSub);
      }
    });

    this.subscription.add(themeSub);
  }

  private loadArticles(): void {
    const articleSub = this.articleService.getArticlesByThemes(this.subscribedThemes).subscribe({
      next: (articles: Article[]) => {
        this.articles = articles;
      }
    });

    this.subscription.add(articleSub);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
