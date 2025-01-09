import { Component } from '@angular/core';
import {HeaderComponent} from '../../../layout/header/header.component';
import {Router, RouterOutlet} from '@angular/router';
import {ArticleComponent} from '../article/article.component';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [
    CommonModule,
    HeaderComponent,
    RouterOutlet
  ],
  templateUrl: './dashboard.component.html',
  standalone: true,
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

  constructor(private router: Router) {
  }

  ngOnInit(): void {
    this.router.navigate(['dashboard/articles']);
  }

}
