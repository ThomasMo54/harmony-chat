import * as Keychain from 'react-native-keychain';

const SERVICE_ACCESS_TOKEN = 'com.motompro.harmony.accessToken';
const SERVICE_REFRESH_TOKEN = 'com.motompro.harmony.refreshToken';

export const secureStorage = {
  async saveTokens(accessToken: string, refreshToken: string) {
    await Keychain.setGenericPassword('accessToken', accessToken, {
      service: SERVICE_ACCESS_TOKEN,
    });
    await Keychain.setGenericPassword('refreshToken', refreshToken, {
      service: SERVICE_REFRESH_TOKEN,
    });
  },

  async getAccessToken(): Promise<string | null> {
    const credentials = await Keychain.getGenericPassword({
      service: SERVICE_ACCESS_TOKEN,
    });
    return credentials ? credentials.password : null;
  },

  async getRefreshToken(): Promise<string | null> {
    const credentials = await Keychain.getGenericPassword({
      service: SERVICE_REFRESH_TOKEN,
    });
    return credentials ? credentials.password : null;
  },

  async clearTokens() {
    await Keychain.resetGenericPassword({ service: SERVICE_ACCESS_TOKEN });
    await Keychain.resetGenericPassword({ service: SERVICE_REFRESH_TOKEN });
  },
};