export type Role = 'USER' | 'ADMIN';

export type Account = {
  id: string;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  role: Role;
};

export type TokenResponse = {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  account: Account;
};

export type RegisterRequest = {
  email: string;
  password: string;
  username: string;
  firstName: string;
  lastName: string;
};

export type LoginRequest = {
  email: string;
  password: string;
};

export type RefreshRequest = {
  refreshToken: string;
};
