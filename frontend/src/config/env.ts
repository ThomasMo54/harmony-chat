import Config from 'react-native-config';

export const ENV = {
  API_BASE_URL: Config.API_BASE_URL as string,
  API_TIMEOUT: Number(Config.API_TIMEOUT) || 10000,
};