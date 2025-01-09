import { Routes } from '@angular/router';
import {HomeComponent} from './features/home/home/home.component';
import {LoginComponent} from './features/auth/login/login.component';
import {RegisterComponent} from './features/auth/register/register.component';
import {DashboardComponent} from './shared/components/dashboard/dashboard.component';
import {ThemeComponent} from './shared/components/theme/theme.component';
import {ArticleComponent} from './shared/components/article/article.component';
import {ProfilComponent} from './shared/components/profil/profil.component';
import {DetailArticleComponent} from './shared/components/articles/detail-article/detail-article.component';
import {CreateArticleComponent} from './shared/components/create-article/create-article.component';

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'dashboard', component: DashboardComponent, children: [
      {path: 'themes', component: ThemeComponent},
      {path: 'articles', component: ArticleComponent},
      {path: 'detail-article/:id', component: DetailArticleComponent },
      {path: 'create-article', component: CreateArticleComponent },
      {path: 'profil', component: ProfilComponent},
    ]}
];
