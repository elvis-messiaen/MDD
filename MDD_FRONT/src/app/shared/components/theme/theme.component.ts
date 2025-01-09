import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ThemeService} from '../../../core/services/themeService';
import {Theme} from '../../../core/models/interfaces/Theme';

@Component({
  selector: 'app-theme',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.scss']
})
export class ThemeComponent implements OnInit {
  themes: Theme[] = [];
  subscribedThemes: number[] = [];
  private themeService = inject(ThemeService);

  ngOnInit(): void {
    this.loadThemes();
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
          }
        }
      });
    });
  }

  subscribeTheme(id: number): void {
    if (this.isSubscribed(id)) {
      this.themeService.unsubscribeTheme(id).subscribe({
        next: () => {
          this.subscribedThemes = this.subscribedThemes.filter(themeId => themeId !== id);
        }
      });
    } else {
      this.themeService.subscribeTheme(id).subscribe({
        next: () => {
          this.subscribedThemes.push(id);
        }
      });
    }
  }

  isSubscribed(themeId: number): boolean {
    return this.subscribedThemes.includes(themeId);
  }
}
