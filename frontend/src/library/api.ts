import { apiRequest, ApiError } from '../api/client';
import type { Book, UserBook } from './types';

const API_BASE = 'http://localhost:8080';

export function fetchCatalog(accessToken: string): Promise<Book[]> {
  return apiRequest<Book[]>('/api/catalog/books', { accessToken });
}

export function fetchLibrary(accessToken: string): Promise<UserBook[]> {
  return apiRequest<UserBook[]>('/api/library/books', { accessToken });
}

export function addToLibrary(
  accessToken: string,
  bookId: string
): Promise<UserBook> {
  return apiRequest<UserBook>('/api/library/books', {
    method: 'POST',
    accessToken,
    body: { bookId },
  });
}

export async function uploadEpub(
  accessToken: string,
  file: File
): Promise<UserBook> {
  const formData = new FormData();
  formData.append('file', file);

  const response = await fetch(`${API_BASE}/api/library/books/upload`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
    body: formData,
  });

  if (!response.ok) {
    let message = `HTTP ${response.status}`;
    try {
      const data = (await response.json()) as {
        message?: string;
        error?: string;
      };
      message = data.message ?? data.error ?? message;
    } catch {
      /* empty */
    }
    throw new ApiError(response.status, message);
  }
  return (await response.json()) as UserBook;
}
