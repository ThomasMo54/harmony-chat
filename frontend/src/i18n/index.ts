import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import { NativeModules, Platform } from "react-native";
import en from "./locales/en.json";

function getDeviceLanguage(): string {
  try {
    const locale = Platform.OS === 'ios'
      ? NativeModules.SettingsManager?.settings?.AppleLocale
        ?? NativeModules.SettingsManager?.settings?.AppleLanguages?.[0]
      : NativeModules.I18nManager?.localeIdentifier;

    return locale ? locale.split(/[-_]/)[0] : 'en';
  } catch {
    return 'en';
  }
}

i18n
  .use(initReactI18next)
  .init({
    compatibilityJSON: 'v4',
    resources: {
      en: { translation: en },
    },
    lng: getDeviceLanguage(),
    fallbackLng: 'en',
    interpolation: {
      escapeValue: false,
    },
  });

export default i18n;
