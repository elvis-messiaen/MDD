import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [
    FormsModule,
    MatIcon,
    NgIf,
    ReactiveFormsModule,
    RouterModule
  ],
  templateUrl: './header.component.html',
  standalone: true,
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  isMenuOpen = false;

  constructor(private router: Router) { }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  navigateTo(route: string) {
    this.router.navigate([route]).then(() => {
      this.isMenuOpen = false;
    });
  }
}
