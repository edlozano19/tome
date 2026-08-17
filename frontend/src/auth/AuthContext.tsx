import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import * as authApi from './api';
import { clearTokens, loadTokens, saveTokens } from './storage';
import type { Account, LoginRequest, RegisterRequest } from './types';

type AuthContextValue = {
  user: Account | null;
  accessToken: string | null;
  isLoading: boolean;
  login: (data: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<Account | null>(null);
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function restoreSession() {
      const { accessToken: storedAccess, refreshToken: storedRefresh } =
        loadTokens();

      if (!storedAccess) {
        setIsLoading(false);
        return;
      }

      try {
        const account = await authApi.fetchMe(storedAccess);
        setAccessToken(storedAccess);
        setUser(account);
      } catch {
        if (!storedRefresh) {
          clearTokens();
          setIsLoading(false);
          return;
        }
        try {
          const tokens = await authApi.refresh({ refreshToken: storedRefresh });
          saveTokens(tokens.accessToken, tokens.refreshToken);
          setAccessToken(tokens.accessToken);
          setUser(tokens.account);
        } catch {
          clearTokens();
        }
      } finally {
        setIsLoading(false);
      }
    }

    void restoreSession();
  }, []);

  const applyTokenResponse = useCallback(
    (tokens: {
      accessToken: string;
      refreshToken: string;
      account: Account;
    }) => {
      saveTokens(tokens.accessToken, tokens.refreshToken);
      setAccessToken(tokens.accessToken);
      setUser(tokens.account);
    },
    []
  );

  const login = useCallback(
    async (data: LoginRequest) => {
      const tokens = await authApi.login(data);
      applyTokenResponse(tokens);
    },
    [applyTokenResponse]
  );

  const register = useCallback(
    async (data: RegisterRequest) => {
      const tokens = await authApi.register(data);
      applyTokenResponse(tokens);
    },
    [applyTokenResponse]
  );

  const logout = useCallback(() => {
    clearTokens();
    setAccessToken(null);
    setUser(null);
  }, []);

  const value = useMemo(
    () => ({ user, accessToken, isLoading, login, register, logout }),
    [user, accessToken, isLoading, login, register, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('useAuth must be used inside AuthProvider');
  }
  return ctx;
}
