import {Commentaire} from './Commentaire';

export interface ArticleToSend {
  title: string;
  description: string;
  authorId: number;
  date: string;
  themeId: number;
  commentaires: Commentaire[];
}
