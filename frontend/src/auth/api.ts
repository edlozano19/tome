import { apiRequest } from '../api/client';
import type {
  LoginRequest,
  RefreshRequest,
  RegisterRequest,
  TokenResponse,
  Account,
} from './types';

export function register(data: RegisterRequest): Promise<TokenResponse> {
  return apiRequest<TokenResponse>('/api/auth/register', {
    method: 'POST',
    body: data,
  });
}

export function login(data: LoginRequest): Promise<TokenResponse> {
  return apiRequest<TokenResponse>('/api/auth/login', {
    method: 'POST',
    body: data,
  });
}

export function refresh(data: RefreshRequest): Promise<TokenResponse> {
  return apiRequest<TokenResponse>('/api/auth/refresh', {
    method: 'POST',
    body: data,
  });
}

export function fetchMe(accessToken: string): Promise<Account> {
  return apiRequest<Account>('/api/me', {
    accessToken,
  });
}
