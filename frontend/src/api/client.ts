import axios from 'axios';
import { secureStorage } from "@/utils/secureStorage.ts";
import { ENV } from "@/config/env.ts";
import { authEvents } from "@/utils/authEvents.ts";

const apiClient = axios.create({
  baseURL: ENV.API_BASE_URL,
  timeout: ENV.API_TIMEOUT,
});

apiClient.interceptors.request.use(async (config) => {
  const accessToken = await secureStorage.getAccessToken();
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  return config;
});

let isRefreshing = false;
let refreshQueue: Array<(token: string) => void> = [];

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      if (isRefreshing) {
        return new Promise((resolve) => {
          refreshQueue.push((newToken) => {
            originalRequest.headers.Authorization = `Bearer ${newToken}`;
            resolve(apiClient(originalRequest));
          });
        });
      }

      isRefreshing = true;

      try {
        const refreshToken = await secureStorage.getRefreshToken();
        if (!refreshToken) throw new Error('Pas de refresh token');

        const { data } = await apiClient.post('/auth/refresh', {
          refreshToken,
        });

        await secureStorage.saveTokens(data.accessToken, data.refreshToken);

        refreshQueue.forEach((callback) => callback(data.accessToken));
        refreshQueue = [];

        originalRequest.headers.Authorization = `Bearer ${data.accessToken}`;
        return apiClient(originalRequest);
      } catch {
        await secureStorage.clearTokens();
        authEvents.emitForceLogout();
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;