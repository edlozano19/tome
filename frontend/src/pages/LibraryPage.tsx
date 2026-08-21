import { useRef, useState, type SubmitEvent } from 'react';
import { Link } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { ApiError } from '../api/client';
import { useAuth } from '../auth/AuthContext';
import {
  addToLibrary,
  fetchCatalog,
  fetchLibrary,
  uploadEpub,
} from '../library/api';

export function LibraryPage() {
  const { user, accessToken } = useAuth();
  const queryClient = useQueryClient();
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [actionError, setActionError] = useState<string | null>(null);

  const libraryQuery = useQuery({
    queryKey: ['library'],
    queryFn: () => fetchLibrary(accessToken!),
    enabled: !!accessToken,
  });

  const catalogQuery = useQuery({
    queryKey: ['catalog'],
    queryFn: () => fetchCatalog(accessToken!),
    enabled: !!accessToken,
  });

  const addMutation = useMutation({
    mutationFn: (bookId: string) => addToLibrary(accessToken!, bookId),
    onSuccess: async () => {
      setActionError(null);
      await queryClient.invalidateQueries({ queryKey: ['library'] });
    },
    onError: (err: unknown) => {
      setActionError(
        err instanceof ApiError ? err.message : 'Could not add book'
      );
    },
  });

  const uploadMutation = useMutation({
    mutationFn: (file: File) => uploadEpub(accessToken!, file),
    onSuccess: async () => {
      setActionError(null);
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
      await queryClient.invalidateQueries({ queryKey: ['library'] });
      await queryClient.invalidateQueries({ queryKey: ['catalog'] });
    },
    onError: (err: unknown) => {
      setActionError(err instanceof ApiError ? err.message : 'Upload failed');
    },
  });

  function handleUpload(e: SubmitEvent<HTMLFormElement>) {
    e.preventDefault();
    const file = fileInputRef.current?.files?.[0];
    if (!file) {
      setActionError('Choose an EPUB file first');
      return;
    }
    uploadMutation.mutate(file);
  }

  const libraryIds = new Set(
    (libraryQuery.data ?? []).map((item) => item.book.id)
  );

  return (
    <main>
      <p>
        <Link to="/">Home</Link>
      </p>
      <h1>Library</h1>
      <p>Signed in as {user?.username}</p>

      {actionError ? <p>{actionError}</p> : null}

      <section>
        <h2>Your books</h2>
        {libraryQuery.isLoading ? <p>Loading library...</p> : null}
        {libraryQuery.isError ? <p>Could not load library.</p> : null}
        {libraryQuery.data?.length === 0 ? <p>No books yet.</p> : null}
        <ul>
          {(libraryQuery.data ?? []).map((item) => (
            <li key={item.id}>
              <strong>{item.book.title}</strong> - {item.book.author} (
              {item.status})
            </li>
          ))}
        </ul>
      </section>

      <section>
        <h2>Catalog</h2>
        {catalogQuery.isLoading ? <p>Loading catalog...</p> : null}
        {catalogQuery.isError ? <p>Could not load catalog.</p> : null}
        <ul>
          {(catalogQuery.data ?? []).map((book) => {
            const alreadyInLibrary = libraryIds.has(book.id);
            return (
              <li key={book.id}>
                <strong>{book.title}</strong> - {book.author}{' '}
                <button
                  type="button"
                  disabled={alreadyInLibrary || addMutation.isPending}
                  onClick={() => addMutation.mutate(book.id)}
                >
                  {alreadyInLibrary ? 'In Library' : 'Add'}
                </button>
              </li>
            );
          })}
        </ul>
      </section>

      <section>
        <h2>Upload EPUB</h2>
        <form onSubmit={handleUpload}>
          <input
            ref={fileInputRef}
            type="file"
            accept=".epub,application/epub+zip"
          />
          <button type="submit" disabled={uploadMutation.isPending}>
            {uploadMutation.isPending ? 'Uploading...' : 'Upload'}
          </button>
        </form>
      </section>
    </main>
  );
}
