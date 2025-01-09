import {Commentaire} from './Commentaire';

export interface Article {
  id: number;
  title: string;
  description: string;
  authorUsername: string;
  date: string;
  themeId: number ;
  commentaires: Commentaire[];
}
