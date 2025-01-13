import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../../core/services/themeService';
import { Theme } from '../../../core/models/interfaces/Theme';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-theme',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit, OnDestroy {
  themes: Theme[] = [];
  subscribedThemes: number[] = [];
  private themeService = inject(ThemeService);
  private subscription: Subscription = new Subscription();

  ngOnInit(): void {
    this.loadThemes();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
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
          }
        }
      });

      this.subscription.add(subCheckSub);
    });
  }

  subscribeTheme(id: number): void {
    if (this.isSubscribed(id)) {
      const unsubscribeSub = this.themeService.unsubscribeTheme(id).subscribe({
        next: () => {
          this.subscribedThemes = this.subscribedThemes.filter(themeId => themeId !== id);
        }
      });

      this.subscription.add(unsubscribeSub);
    } else {
      const subscribeSub = this.themeService.subscribeTheme(id).subscribe({
        next: () => {
          this.subscribedThemes.push(id);
        }
      });

      this.subscription.add(subscribeSub);
    }
  }

  isSubscribed(themeId: number): boolean {
    return this.subscribedThemes.includes(themeId);
  }
}
