export type Book = {
  id: string;
  title: string;
  author: string;
  slug: string;
  description: string | null;
  language: string;
  publicDomain: boolean;
  source: string | null;
  coverUrl: string | null;
};

export type UserBook = {
  id: string;
  status: string;
  addedAt: string;
  book: Book;
};
