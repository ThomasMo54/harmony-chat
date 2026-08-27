import { SafeAreaView } from "react-native-safe-area-context";
import { ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import { useState } from "react";
import { COLORS } from "@/constants/colors.ts";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { AuthStackParamList } from "@/navigation/types.ts";
import { useNavigation } from "@react-navigation/native";
import axios from "axios";
import { useTranslation } from "react-i18next";
import { createUser, USER_ERROR_CODES } from "@/api/userApi.ts";

type RegisterScreenNavigationProp = NativeStackNavigationProp<AuthStackParamList, 'Register'>;

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const minUsernameSize = 3;
const maxUsernameSize = 30;

const minPasswordSize = 8;
const maxPasswordSize = 200;

function RegisterScreen() {
  const { t } = useTranslation();
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [emailError, setEmailError] = useState<string | null>(null);
  const [usernameError, setUsernameError] = useState<string | null>(null);
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [registerError, setRegisterError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigation = useNavigation<RegisterScreenNavigationProp>();

  async function handleRegister() {
    if (isSubmitting) return;
    let hasError = false;

    if (!email.trim() || !emailRegex.test(email)) {
      setEmailError(t('auth.register.invalidEmail'));
      hasError = true;
    }

    if (username.length < minUsernameSize) {
      setUsernameError(t('auth.register.usernameTooShort', { size: minUsernameSize }));
      hasError = true;
    } else if (username.length > maxUsernameSize) {
      setUsernameError(t('auth.register.usernameTooLong', { size: maxUsernameSize }));
      hasError = true;
    }

    if (password.length < minPasswordSize) {
      setPasswordError(t('auth.register.passwordTooShort', { size: minPasswordSize }));
      hasError = true;
    } else if (password.length > maxPasswordSize) {
      setPasswordError(t('auth.register.passwordTooLong', { size: maxPasswordSize }));
      hasError = true;
    } else if (password !== confirmPassword) {
      setPasswordError(t('auth.register.passwordsDoNotMatch'));
      hasError = true;
    }

    if (hasError) return;

    setIsSubmitting(true);
    setEmailError(null);
    setUsernameError(null);
    setPasswordError(null);
    setRegisterError(null);

    try {
      const user = await createUser({
        email,
        password,
        name: username,
      });
      navigation.navigate('Verify', { email, userId: user.id });
    } catch (err) {
      handleRegisterError(err);
    } finally {
      setIsSubmitting(false);
    }
  }

  function navigateToLogIn() {
    navigation.navigate('Login');
  }

  function handleRegisterError(err: unknown) {
    if (!axios.isAxiosError(err)) {
      setRegisterError(t('common.genericError'))
      return;
    }

    if (!err.response) {
      setRegisterError(t('common.networkError'));
      return;
    }

    const { status, data } = err.response;

    if (status === 429) {
      setRegisterError(t('common.rateLimited'))
      return;
    }

    switch (data?.code) {
      case USER_ERROR_CODES.EMAIL_ALREADY_EXISTS: {
        setEmailError(t('auth.register.errors.emailAlreadyExists'));
        break;
      }
      case USER_ERROR_CODES.NAME_ALREADY_EXISTS: {
        setUsernameError(t('auth.register.errors.nameAlreadyExists'));
        break;
      }
      default:
        setRegisterError(t('auth.register.errors.generic'));
    }
  }

  return (
    <SafeAreaView style={styles.safe} edges={['top', 'bottom']}>
      <ScrollView contentContainerStyle={styles.container}>
        <View style={styles.iconWrapper}>
          <Text style={styles.iconText}>💬</Text>
        </View>

        <Text style={styles.title}>{t('auth.register.title')}</Text>

        <Text style={styles.label}>{t('auth.register.emailLabel')}</Text>
        <View style={styles.inputWrapper}>
          <TextInput
            style={styles.input}
            value={email}
            onChangeText={setEmail}
            keyboardType="email-address"
            autoCapitalize="none"
          />
        </View>
        {emailError && <Text style={styles.errorText}>{emailError}</Text>}

        <Text style={styles.label}>{t('auth.register.usernameLabel')}</Text>
        <View style={styles.inputWrapper}>
          <Text style={styles.atSign}>@</Text>
          <TextInput
            style={[styles.input, styles.inputWithIcon]}
            value={username}
            onChangeText={setUsername}
            autoCapitalize="none"
          />
        </View>
        {usernameError && <Text style={styles.errorText}>{usernameError}</Text>}

        <Text style={styles.label}>{t('auth.register.passwordLabel')}</Text>
        <View style={styles.inputWrapper}>
          <TextInput
            style={styles.input}
            value={password}
            onChangeText={setPassword}
            secureTextEntry
          />
        </View>
        {passwordError && <Text style={styles.errorText}>{passwordError}</Text>}

        <Text style={styles.label}>{t('auth.register.confirmPasswordLabel')}</Text>
        <View style={styles.inputWrapper}>
          <TextInput
            style={styles.input}
            value={confirmPassword}
            onChangeText={setConfirmPassword}
            secureTextEntry
          />
        </View>

        <TouchableOpacity style={styles.button} activeOpacity={0.85} onPress={() => handleRegister()}>
          <Text style={styles.buttonText}>{t('auth.register.submit')}</Text>
        </TouchableOpacity>
        {registerError && <Text style={styles.errorText}>{registerError}</Text>}

        <View style={styles.divider} />

        <Text style={styles.loginText}>
          {t('auth.register.haveAccount')}{' '}
          <Text style={styles.loginLink} onPress={() => navigateToLogIn()}>{t('auth.register.logIn')}</Text>
        </Text>
      </ScrollView>
    </SafeAreaView>
  )
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
  inputWithIcon: {
    marginLeft: 2,
  },
  errorText: {
    width: '100%',
    color: COLORS.error,
    fontSize: 13,
    fontWeight: '600',
    marginTop: 16,
  },
  atSign: {
    fontSize: 16,
    color: COLORS.gray,
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
  loginText: {
    fontSize: 14,
    color: COLORS.gray,
  },
  loginLink: {
    color: COLORS.primary,
    fontWeight: '700',
  },
});

export default RegisterScreen;