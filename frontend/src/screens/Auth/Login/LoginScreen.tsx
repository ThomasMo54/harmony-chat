import { SafeAreaView } from "react-native-safe-area-context";
import { ActivityIndicator, ScrollView, StatusBar, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import { useState } from "react";
import axios from "axios";
import { useTranslation } from "react-i18next";
import { useAuth } from "@/context/AuthContext.tsx";
import { COLORS } from "@/constants/colors.ts";
import { useNavigation } from "@react-navigation/native";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { AuthStackParamList } from "@/navigation/types.ts";
import { AUTH_ERROR_CODES } from "@/api/authApi.ts";

type LoginScreenNavigationProp = NativeStackNavigationProp<AuthStackParamList, 'Login'>;

function LoginScreen() {
  const { t } = useTranslation();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { login, logout } = useAuth();
  const navigation = useNavigation<LoginScreenNavigationProp>();

  async function handleLogin() {
    if (isSubmitting) return;

    if (!email.trim() || !password) {
      setError(t('auth.login.missingFields'));
      return;
    }

    setIsSubmitting(true);
    setError(null);

    try {
      await login(email.trim(), password);
    } catch (err) {
      setError(getLoginErrorMessage(err));
    } finally {
      setIsSubmitting(false);
    }
  }

  function navigateToRegister() {
    navigation.navigate('Register');
  }

  function getLoginErrorMessage(err: unknown): string {
    if (!axios.isAxiosError(err)) {
      return t('common.genericError');
    }

    if (!err.response) {
      return t('common.networkError');
    }

    const { status, data } = err.response;

    if (status === 429) {
      return t('common.rateLimited');
    }

    switch (data?.code) {
      case AUTH_ERROR_CODES.INVALID_CREDENTIALS:
        return t('auth.login.errors.invalidCredentials');
      case AUTH_ERROR_CODES.USER_NOT_ENABLED:
        return t('auth.login.errors.userNotEnabled');
      default:
        return data?.message ?? t('auth.login.errors.generic');
    }
  }

  return (
    <SafeAreaView style={styles.safe} edges={['top', 'bottom']}>
      <StatusBar barStyle="dark-content" />
      <ScrollView contentContainerStyle={styles.container}>
        <View style={styles.iconWrapper}>
          <Text style={styles.iconText}>💬</Text>
        </View>

        <Text style={styles.title}>{t('auth.login.title')}</Text>

        <Text style={styles.label}>{t('auth.login.emailLabel')}</Text>
        <View style={styles.inputWrapper}>
          <TextInput
            style={styles.input}
            value={email}
            onChangeText={setEmail}
            keyboardType="email-address"
            autoCapitalize="none"
          />
        </View>

        <Text style={styles.label}>{t('auth.login.passwordLabel')}</Text>
        <View style={styles.inputWrapper}>
          <TextInput
            style={styles.input}
            value={password}
            onChangeText={setPassword}
            secureTextEntry={!showPassword}
          />
          <TouchableOpacity onPress={() => setShowPassword(!showPassword)}>
            <Text style={styles.showText}>
              {showPassword ? t('auth.login.hide') : t('auth.login.show')}
            </Text>
          </TouchableOpacity>
        </View>

        {error && <Text style={styles.errorText}>{error}</Text>}

        <TouchableOpacity
          style={[styles.button, isSubmitting && styles.buttonDisabled]}
          activeOpacity={0.85}
          onPress={() => handleLogin()}
          disabled={isSubmitting}
        >
          {isSubmitting ? (
            <ActivityIndicator color="#fff" />
          ) : (
            <Text style={styles.buttonText}>{t('auth.login.submit')}</Text>
          )}
        </TouchableOpacity>

        <View style={styles.divider} />

        <Text style={styles.registerText}>
          {t('auth.login.noAccount')}{' '}
          <Text
            style={styles.registerLink}
            onPress={() => navigateToRegister()}
          >
            {t('auth.login.createAccount')}
          </Text>
        </Text>

        <TouchableOpacity onPress={logout}>
          <Text>{t('auth.login.disconnect')}</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: {
    flex: 1,
    backgroundColor: '#eee'
  },
  container: {
    padding: 24,
    alignItems: 'center',
  },
  iconWrapper: {
    width: 64,
    height: 64,
    borderRadius: 16,
    backgroundColor: COLORS.primary,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 16,
    marginBottom: 20,
  },
  iconText: { fontSize: 28 },
  title: {
    fontSize: 26,
    fontWeight: '800',
    color: COLORS.text,
    marginBottom: 6,
  },
  label: {
    alignSelf: 'flex-start',
    fontSize: 12,
    fontWeight: '600',
    color: COLORS.gray,
    letterSpacing: 0.5,
    marginBottom: 6,
    marginTop: 14,
  },
  inputWrapper: {
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.bg,
    borderWidth: 1,
    borderColor: COLORS.border,
    borderRadius: 12,
    paddingHorizontal: 16,
    height: 52,
  },
  input: {
    flex: 1,
    fontSize: 16,
    color: COLORS.text,
  },
  showText: {
    fontSize: 14,
    color: COLORS.gray,
    fontWeight: '500',
  },
  errorText: {
    width: '100%',
    color: COLORS.error,
    fontSize: 13,
    fontWeight: '600',
    marginTop: 16,
  },
  button: {
    width: '100%',
    height: 52,
    backgroundColor: COLORS.primary,
    borderRadius: 14,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 24,
  },
  buttonDisabled: {
    opacity: 0.7,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '700',
  },
  divider: {
    width: '100%',
    height: 1,
    backgroundColor: COLORS.border,
    marginVertical: 20,
  },
  registerText: {
    fontSize: 14,
    color: COLORS.gray,
  },
  registerLink: {
    color: COLORS.primary,
    fontWeight: '700',
  },
});

export default LoginScreen;