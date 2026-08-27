import { useEffect, useState } from "react";
import { SafeAreaView } from "react-native-safe-area-context";
import { ActivityIndicator, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import { useTranslation } from "react-i18next";
import { COLORS } from "@/constants/colors.ts";
import { AuthStackParamList } from "@/navigation/types.ts";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { activateUser, resendCode, USER_ERROR_CODES } from "@/api/userApi.ts";
import axios from "axios";

type VerifyScreenRouteProp = NativeStackScreenProps<AuthStackParamList, 'Verify'>;

const codeLength = 6;
const resendCooldownSeconds = 60;

function VerifyScreen({ route, navigation }: VerifyScreenRouteProp) {
  const { t } = useTranslation();
  const { email, userId } = route.params;

  const [code, setCode] = useState('');
  const [codeError, setCodeError] = useState<string | null>(null);
  const [verifyError, setVerifyError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [resendCooldown, setResendCooldown] = useState(resendCooldownSeconds);

  useEffect(() => {
    const interval = setInterval(() => {
      setResendCooldown((prev) => (prev > 0 ? prev - 1 : 0));
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  async function handleVerify() {
    if (isSubmitting) return;

    if (!code.trim()) {
      setCodeError(t('auth.verify.emptyCode'));
      return;
    }

    if (code.length !== codeLength) {
      setCodeError(t('auth.verify.invalidCodeLength', { length: codeLength }));
      return;
    }

    setIsSubmitting(true);
    setCodeError(null);
    setVerifyError(null);

    try {
      await activateUser({ code });
      navigation.navigate('Login');
    } catch (err) {
      handleVerifyError(err);
    } finally {
      setIsSubmitting(false);
    }
  }

  function handleVerifyError(err: unknown) {
    if (!axios.isAxiosError(err)) {
      setVerifyError(t('common.genericError'))
      return;
    }

    if (!err.response) {
      setVerifyError(t('common.networkError'));
      return;
    }

    const { status, data } = err.response;

    if (status === 429) {
      setVerifyError(t('common.rateLimited'))
      return;
    }

    switch (data?.code) {
      case USER_ERROR_CODES.ACTIVATION_CODE_NOT_FOUND: {
        setCodeError(t('auth.verify.errors.codeNotFound'));
        break;
      }
      case USER_ERROR_CODES.ACTIVATION_CODE_EXPIRED: {
        setCodeError(t('auth.verify.errors.codeExpired'));
        break;
      }
      case USER_ERROR_CODES.ALREADY_ACTIVATED: {
        setVerifyError(t('auth.verify.errors.alreadyActivated'));
        break;
      }
      default:
        setVerifyError(t('auth.verify.errors.generic'));
    }
  }

  async function handleCodeResend() {
    if (isSubmitting || resendCooldown > 0) return;

    setIsSubmitting(true);
    setVerifyError(null);

    try {
      await resendCode({ userId });
      setResendCooldown(resendCooldownSeconds);
    } catch (err) {
      handleResendError(err);
    } finally {
      setIsSubmitting(false);
    }
  }

  function handleResendError(err: unknown) {
    if (!axios.isAxiosError(err)) {
      setVerifyError(t('common.genericError'))
      return;
    }

    if (!err.response) {
      setVerifyError(t('common.networkError'));
      return;
    }

    const { status, data } = err.response;

    if (status === 429) {
      setVerifyError(t('common.rateLimited'))
      return;
    }

    switch (data?.code) {
      case USER_ERROR_CODES.ALREADY_ACTIVATED: {
        setVerifyError(t('auth.verify.errors.alreadyActivated'));
        break;
      }
      default:
        setVerifyError(t('auth.verify.errors.generic'));
    }
  }

  return (
    <SafeAreaView style={styles.safe} edges={['top', 'bottom']}>
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.title}>{t('auth.verify.title')}</Text>

        <Text style={styles.description}>{t('auth.verify.description', { length: codeLength, email })}</Text>

        <Text style={styles.label}>{t('auth.verify.codeLabel')}</Text>
        <View style={styles.inputWrapper}>
          <TextInput
            style={styles.input}
            value={code}
            onChangeText={setCode}
            keyboardType="number-pad"
          />
        </View>
        {codeError && <Text style={styles.errorText}>{codeError}</Text>}

        <TouchableOpacity
          style={[styles.button, isSubmitting && styles.buttonDisabled]}
          activeOpacity={0.85}
          onPress={() => handleVerify()}
          disabled={isSubmitting}
        >
          {isSubmitting
            ? <ActivityIndicator color="#fff" />
            : <Text style={styles.buttonText}>{t('auth.verify.submit')}</Text>}
        </TouchableOpacity>
        {verifyError && <Text style={styles.errorText}>{verifyError}</Text>}

        <Text style={styles.resendText}>
          {t('auth.verify.noCode')}{' '}
          {resendCooldown > 0
            ? <Text style={styles.resendLinkDisabled}>{t('auth.verify.resendCountdown', { seconds: resendCooldown })}</Text>
            : <Text style={styles.resendLink} onPress={() => handleCodeResend()}>{t('auth.verify.resend')}</Text>}
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
  title: {
    fontSize: 26,
    fontWeight: '800',
    color: COLORS.text,
    marginBottom: 6,
  },
  description: {
    fontSize: 14,
    fontWeight: '800',
    color: COLORS.gray,
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
  resendText: {
    fontSize: 14,
    color: COLORS.gray,
    marginTop: 24,
  },
  resendLink: {
    color: COLORS.primary,
    fontWeight: '700',
  },
  resendLinkDisabled: {
    color: COLORS.gray,
    fontWeight: '700',
  },
});

export default VerifyScreen;