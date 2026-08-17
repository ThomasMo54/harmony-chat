import React, { createContext, useContext, useState, useEffect } from 'react';
import { secureStorage } from "../utils/secureStorage.ts";
import { login as loginApi } from '../api/authApi';
import { authEvents } from "../utils/authEvents.ts";

type AuthContextType = {
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function checkAuth() {
      const accessToken = await secureStorage.getAccessToken();
      setIsAuthenticated(!!accessToken);
      setIsLoading(false);
    }
    checkAuth();
  }, []);

  useEffect(() => {
    authEvents.onForceLogout(() => {
      setIsAuthenticated(false);
    });
  }, []);

  async function login(email: string, password: string) {
    const { accessToken, refreshToken } = await loginApi(email, password);
    await secureStorage.saveTokens(accessToken, refreshToken);
    setIsAuthenticated(true);
  }

  async function logout() {
    await secureStorage.clearTokens();
    setIsAuthenticated(false);
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, isLoading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used in AuthProvider');
  }
  return context;
}