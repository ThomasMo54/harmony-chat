import apiClient from './client';

export const AUTH_ERROR_CODES = {
  INVALID_CREDENTIALS: 'error.auth.invalid_credentials',
  USER_NOT_ENABLED: 'error.auth.user_not_enabled',
} as const;

export class LoginResponseDto {
  accessToken!: string;
  refreshToken!: string;
}

export async function login(email: string, password: string) {
  const { data } = await apiClient.post('/auth/login', { email, password });
  return data as LoginResponseDto;
}